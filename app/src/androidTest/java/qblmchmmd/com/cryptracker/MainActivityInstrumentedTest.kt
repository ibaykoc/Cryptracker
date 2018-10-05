package qblmchmmd.com.cryptracker

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.agoda.kakao.*
import kotlinx.coroutines.experimental.*
import mock.MockDataFactory
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import qblmchmmd.com.cryptracker.model.datasource.local.CryptoDB
import qblmchmmd.com.cryptracker.model.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import qblmchmmd.com.cryptracker.model.repository.CryptoListRepository
import qblmchmmd.com.cryptracker.model.repository.CryptoModelValidator
import qblmchmmd.com.cryptracker.view.MainActivity
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    class MainScreen : Screen<MainScreen>() {
        val swipeRefresh = KSwipeRefreshLayout { withId(R.id.main_refresh) }
        val cryptoList = KRecyclerView({
            withId(R.id.crypto_list)
        }, itemTypeBuilder = {
            itemType(::CryptoListItem)
        })

        class CryptoListItem(parent: Matcher<View>) : KRecyclerItem<CryptoListItem>(parent) {
            val cryptoName = KTextView(parent) { withId(R.id.crypto_list_item_name) }
            val cryptoPrice = KTextView(parent) { withId(R.id.crypto_list_item_price) }
        }
    }

    class CryptoRemoteDataSourceMock : CryptoRemoteDatasource {
        override suspend fun getLatestCrypto(start: Int, limit: Int): Deferred<CryptoListResponse> {
            return GlobalScope.async {
                delay(1000)
                MockDataFactory.createCryptoListResponse(101,200)
            }
        }
    }

    private val viewModelMockModule = module {

        val context = InstrumentationRegistry.getTargetContext()
        val repositoryMock = CryptoListRepository(
                CryptoRemoteDataSourceMock(),
                Room.inMemoryDatabaseBuilder(context, CryptoDB::class.java).build().cryptoDao(),
                CryptoModelValidator())
        repositoryMock.localData.insertAll(MockDataFactory.createCryptoUiModel(100))
        viewModel(override = true) {
            MainViewModel(repositoryMock)
        }
    }

    @JvmField
    @Rule
    val testRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loadKoinModules(listOf(viewModelMockModule))
        }
    }

    private val mainScreen = MainScreen()

    @Test
    fun showDataOnDatabase() {
        mainScreen {
            cryptoList {
                firstChild<MainScreen.CryptoListItem> {
                    isVisible()
                    cryptoName { hasText("crypto100") }
                    cryptoPrice { hasText("$100.0") }
                }
                childAt<MainScreen.CryptoListItem>(position = 1) {
                    isVisible()
                    cryptoName { hasText("crypto99") }
                    cryptoPrice { hasText("$99.0") }
                }
            }
        }
    }

    @Test
    fun whenGetData_showUpdatedData() {
        mainScreen {
            swipeRefresh.swipeDown()
            runBlocking { delay(2000) }
            cryptoList {
                firstChild<MainScreen.CryptoListItem> {
                    isVisible()
                    cryptoName { hasText("crypto200") }
                    cryptoPrice { hasText("$200.0") }
                }
                childAt<MainScreen.CryptoListItem>(position = 1) {
                    isVisible()
                    cryptoName { hasText("crypto199") }
                    cryptoPrice { hasText("$199.0") }
                }
            }
        }
    }
}
