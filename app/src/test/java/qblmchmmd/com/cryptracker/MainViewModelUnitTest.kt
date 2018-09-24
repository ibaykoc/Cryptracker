@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package qblmchmmd.com.cryptracker

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.koc.countrinfo.util.LiveDataTestUtil
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import mock.RepositoryMock
import mock.mockDataNull
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import java.io.IOException

class MainViewModelUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val repository = RepositoryMock(mockDataNull)

    val viewModel = MainViewModel(
            repository = repository,
            mainThread = Dispatchers.Unconfined,
            bgThread = Dispatchers.Unconfined)

    @Test
    fun whenUpdate_isLoadingIsTrue() {
        viewModel.update()

        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdate_repositoryCalled() {
        val repository = RepositoryMock(mockDataNull)
        val viewModel = MainViewModel(
                repository = repository,
                mainThread = Dispatchers.Unconfined,
                bgThread = Dispatchers.Unconfined)
        viewModel.update()

        assertEquals(true, repository.getDataCalled)
    }

    @Test
    fun whenUpdate_repositoryComplete_isLoadingIsFalse() {
        val repository = RepositoryMock(mockDataNull)
        val viewModel = MainViewModel(
                repository = repository,
                mainThread = Dispatchers.Unconfined,
                bgThread = Dispatchers.Unconfined)

        runBlocking {
            viewModel.update()
            delay(1000)
        }

        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdate_repositoryError_isLoadingIsFalse() {
        val repository = RepositoryMock(
                getDataReturn = mockDataNull,
                throwException = IOException("error"))
        val viewModel = MainViewModel(
                repository = repository,
                mainThread = Dispatchers.Unconfined,
                bgThread = Dispatchers.Unconfined)

        runBlocking {
            viewModel.update()
            delay(1000)
        }
        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdateRepositoryComplete_dataUpdated() {
        val expectedData = mockDataNull

        val repository = RepositoryMock(getDataReturn = expectedData)
        val viewModel = MainViewModel(
                repository = repository,
                mainThread = Dispatchers.Unconfined,
                bgThread = Dispatchers.Unconfined)

        runBlocking {
            viewModel.update()
            delay(1000)
        }
        assertEquals(expectedData, LiveDataTestUtil.getValue(viewModel.uiData))
    }

    @Test
    fun whenUpdateRepositoryError_isErrorIsTrue() {
        val repository = RepositoryMock(getDataReturn = mockDataNull, throwException = IOException("error"))
        val viewModel = MainViewModel(
                repository = repository,
                mainThread = Dispatchers.Unconfined,
                bgThread = Dispatchers.Unconfined)

        runBlocking {
            viewModel.update()
            delay(1000)
        }
        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isError))
    }
}
