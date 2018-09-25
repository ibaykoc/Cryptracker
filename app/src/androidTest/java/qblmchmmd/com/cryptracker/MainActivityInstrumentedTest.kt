package qblmchmmd.com.cryptracker

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.agoda.kakao.*
import kotlinx.coroutines.experimental.*
import mock.mockData
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import qblmchmmd.com.cryptracker.datasource.local.CryptoDB
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.CryptoRepository
import qblmchmmd.com.cryptracker.view.MainActivity
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

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

class RemoteDataMock : RemoteData<CryptoListResponse> {
    override suspend fun fetchData(): Deferred<CryptoListResponse> {
        return GlobalScope.async {
            delay(1000)
            mockData
        }
    }

}

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    private val viewModelMockModule = module {

        val repositoryMock = CryptoRepository(RemoteDataMock(), CryptoDB())

        viewModel(override = true) {
            MainViewModel(repositoryMock,
                    mainThread = get(name = "mainThread"),
                    bgThread = get(name = "bgThread"))
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
    fun whenStart_showLoading() {
        runBlocking { 10 }
        mainScreen {
            swipeRefresh { isRefreshing() }
        }
    }

    @Test
    fun whenDataReceived_ShowData() {
        runBlocking { delay(1001) }
        mainScreen {
            cryptoList {
                firstChild<MainScreen.CryptoListItem> {
                    isVisible()
                    cryptoName { hasText("Bitcoco") }
                    cryptoPrice {hasText( "$8000.0")}
                }
                childAt<MainScreen.CryptoListItem>(position = 1) {
                    isVisible()
                    cryptoName {hasText("Bitcoca")}
                    cryptoPrice {hasText( "$9000.0")}
                }
            }
        }
    }

    @Test
    fun whenDataShown_hideLoading() {
        runBlocking { delay(1001) }
        mainScreen {
            cryptoList {
                firstChild<MainScreen.CryptoListItem> {
                    isVisible()
                    cryptoName { hasText("Bitcoco") }
                }
                childAt<MainScreen.CryptoListItem>(position = 1) {
                    isVisible()
                    cryptoName {hasText("Bitcoca")}
                    swipeRefresh { isNotRefreshing() }
                }
            }
        }
    }
}
