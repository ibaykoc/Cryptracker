@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package qblmchmmd.com.cryptracker

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import io.mockk.*
import kotlinx.coroutines.experimental.*
import mock.mockData
import mock.mockDataNull
import org.junit.Rule
import org.junit.Test
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import qblmchmmd.com.cryptracker.viewmodel.MainViewState
import java.io.IOException

class MainViewModelUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val repository = mockk<Repository<CryptoListResponse>>()

    val viewModel = MainViewModel(
            repository = repository,
            mainThread = Dispatchers.Unconfined,
            bgThread = Dispatchers.Unconfined)

    @Test
    fun whenUpdate_viewStateLoadingTrue() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            delay(100)
            mockData
        }
        val obs = spyk(Observer<MainViewState> {})
        viewModel.viewState.observeForever(obs)

        // When
        viewModel.update()

        // Then
        verify(exactly = 1) { obs.onChanged(MainViewState.Loading(true)) }
    }

    @Test
    fun whenUpdate_repositoryCalled() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            mockData
        }

        // When
        viewModel.update()

        // Then
        coVerify { repository.getData() }
    }

    @Test
    fun whenUpdate_repositoryError_viewStateLoadingFalse() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            throw IOException("Error test")
        }

        val obs = spyk(Observer<MainViewState> {})

        viewModel.viewState.observeForever(obs)

        // When
        runBlocking {
            viewModel.update()
            delay(5)
        }

        // Then
        verify(exactly = 1) { obs.onChanged(MainViewState.Loading(false)) }
    }

    @Test
    fun whenUpdateRepositoryComplete_dataUpdated() {
        // Given
        val expectedData = mockDataNull
        coEvery { repository.getData() } returns GlobalScope.async {
            expectedData
        }
        val obs = spyk(Observer<MainViewState> {})
        viewModel.viewState.observeForever(obs)

        runBlocking {
            viewModel.update()
            delay(5)
        }

        verify(exactly = 1) { obs.onChanged(MainViewState.Data(expectedData)) }
    }

    @Test
    fun whenUpdateRepositoryError_isErrorIsTrue() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            throw IOException("Error Test")
        }
        val obs = spyk(Observer<MainViewState> {})
        viewModel.viewState.observeForever(obs)

        // When
        runBlocking {
            viewModel.update()
            delay(5)
        }

        // Then
        verify(exactly = 1) { obs.onChanged(MainViewState.Error("Error Test")) }
    }
}
