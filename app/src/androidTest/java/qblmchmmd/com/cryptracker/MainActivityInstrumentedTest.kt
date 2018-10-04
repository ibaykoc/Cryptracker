package qblmchmmd.com.cryptracker

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
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
import qblmchmmd.com.cryptracker.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.repository.CryptoListRepository
import qblmchmmd.com.cryptracker.repository.ModelTransformer
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

class CryptoRemoteDatasourceMock : CryptoRemoteDatasource {
    override suspend fun getLatestCrypto(start: Int, limit: Int): Deferred<CryptoListResponse> {
        return GlobalScope.async {
            delay(1000)
            mockData
        }
    }
}

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    private val viewModelMockModule = module {

        val context = InstrumentationRegistry.getTargetContext()
        val repositoryMock = CryptoListRepository(
                CryptoRemoteDatasourceMock(),
                Room.inMemoryDatabaseBuilder(context, CryptoDB::class.java).build().cryptoDao(),
                object : ModelTransformer<CryptoListResponse, List<CryptoUiModel>> {
                    override fun transform(input: CryptoListResponse): List<CryptoUiModel> {
                        input.data?.let { list ->
                            return list.asSequence()
                                    .filter { it != null }
                                    .map { data ->
                                        var cryptoUiModel: CryptoUiModel? = null
                                        data!!.id?.let { cryptId ->
                                            data.name?.let { cryptName ->
                                                data.quote?.uSD?.price?.let { cryptPrice ->
                                                    cryptoUiModel = CryptoUiModel(cryptId, cryptName, cryptPrice)
                                                }
                                            }
                                            cryptoUiModel
                                        }
                                    }
                                    .filter { transformedData -> transformedData != null }
                                    .map { validData -> validData!! }.toList()

                        }
                        return emptyList()
                    }

                })

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
                    cryptoPrice { hasText("$8000.0") }
                }
                childAt<MainScreen.CryptoListItem>(position = 1) {
                    isVisible()
                    cryptoName { hasText("Bitcoca") }
                    cryptoPrice { hasText("$9000.0") }
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
                    cryptoName { hasText("Bitcoca") }
                    swipeRefresh { isNotRefreshing() }
                }
            }
        }
    }
}
