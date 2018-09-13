@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package qblmchmmd.com.cryptracker

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.koc.countrinfo.util.LiveDataTestUtil
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainViewModelUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Test
    fun whenUpdate_isLoadingIsTrue() {
        val repository = RepositoryMock<String>()
        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdate_repositoryCalled() {
        val repository = RepositoryMock<String>()
        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(true, repository.getDataCalled)
    }

    @Test
    fun whenUpdate_repositoryComplete_isLoadingIsFalse() {
        val repository = RepositoryMock<String>(onCompleteMockData = "a")

        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdate_repositoryError_isLoadingIsFalse() {
        val repository = RepositoryMock<String>(doOnError = true)

        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdateRepositoryComplete_dataUpdated() {
        val mockData = "Testetsdtetstetdst"

        val repository = RepositoryMock(onCompleteMockData = mockData)
        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(mockData, LiveDataTestUtil.getValue(viewModel.uiData))
    }

    @Test
    fun whenUpdateRepositoryError_isErrorIsTrue() {
        val repository = RepositoryMock<String>(doOnError = true)

        val viewModel = MainViewModel(repository)

        viewModel.update()

        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isError))
    }
}
