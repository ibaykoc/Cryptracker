package qblmchmmd.com.cryptracker

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import qblmchmmd.com.cryptracker.view.MainEventHandler
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainEventHandlerUnitTest {

    private val mainViewModel = mockk<MainViewModel>(relaxed = true)
    private val mainEventHandler = MainEventHandler(mainViewModel)


    @Test
    fun whenOnCreateShouldCallViewModelUpdate() {
        // When
        mainEventHandler.onCreate()

        // Then
        verify(exactly = 1) { mainViewModel.update() }
    }

    @Test
    fun whenOnRefreshShouldCallViewModelUpdate() {
        // When
        mainEventHandler.onRefresh()

        // Then
        verify(exactly = 1) {mainViewModel.update()}
    }
}