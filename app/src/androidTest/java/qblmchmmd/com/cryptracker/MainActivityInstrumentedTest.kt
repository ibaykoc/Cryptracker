package qblmchmmd.com.cryptracker

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.agoda.kakao.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import mock.RepositoryMock
import mock.mockData
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
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

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    private val viewModelMockModule = module {
        viewModel(override = true) {
            MainViewModel(RepositoryMock(getDataReturn = mockData),
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
    fun whenDataReceived_ShowData() {
        runBlocking {
            delay(1000)
        }
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
    fun whenDataShown_swipeRefreshNotRefreshing() {
        runBlocking {
            delay(1000)
        }
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
