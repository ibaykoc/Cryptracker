@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package qblmchmmd.com.cryptracker

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import io.mockk.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import mock.mockValidData
import org.junit.Rule
import org.junit.Test
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.repository.CryptoListRepository
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.repository.RepositoryState
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import java.io.IOException

class MainViewModelUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    val repository = mockk<Repository<List<CryptoUiModel>, CryptoListResponse>>()

    val viewModel = MainViewModel(
            repository = repository,
            mainThread = Dispatchers.Unconfined,
            bgThread = Dispatchers.Unconfined)

    @Test
    fun whenUpdate_loadingStateTrue() {
        // Given
        every { repository.getData() } returns
            RepositoryState(
                    loading = MutableLiveData<Boolean>().also { it.value = true },
                    error = MutableLiveData<Exception>().also { it.value = null },
                    pagedListBuilder = LivePagedListBuilder(mockk(), 10)
            )

        val obs = spyk(Observer<Boolean> {})
        viewModel.loadingState.observeForever(obs)

        // When
        viewModel.update()

        // Then
        verify(exactly = 1) { obs.onChanged(true) }
    }

    @Test
    fun whenUpdate_repositoryCalled() {
        // Given
        every { repository.getData() } returns
                RepositoryState(
                        MutableLiveData<Boolean>().also { it.value = true },
                        MutableLiveData<Exception>().also { it.value = null },
                        LivePagedListBuilder(mockk(), 10)
                )

        // When
        viewModel.update()

        // Then
        coVerify { repository.getData() }
    }

    @Test
    fun whenUpdate_repositoryError_loadingStateFalse() {
        // Given
        every { repository.getData() } returns
                RepositoryState(
                        MutableLiveData<Boolean>().also { it.value = true },
                        MutableLiveData<Exception>().also { it.value = null },
                        LivePagedListBuilder(mockk(), 10)
                )

        val obs = spyk(Observer<Boolean> {})

        viewModel.loadingState.observeForever(obs)

        // When
        runBlocking {
            viewModel.update()
            delay(5)
        }

        // Then
        verify(exactly = 1) { obs.onChanged(false) }
    }

    @Test
    fun whenUpdateRepositoryComplete_dataUpdated() {
        // Given
        val expectedData = mockValidData as PagedList<CryptoUiModel>
        every { repository.getData() } returns
                RepositoryState(
                        MutableLiveData<Boolean>().also { it.value = true },
                        MutableLiveData<Exception>().also { it.value = null },
                        LivePagedListBuilder(mockk(), 10)
                )

        val obs = spyk(Observer<PagedList<CryptoUiModel>> {})
        viewModel.dataState.observeForever(obs)

        runBlocking {
            viewModel.update()
            delay(5)
        }

        verify(exactly = 1) { obs.onChanged(expectedData) }
    }

    @Test
    fun whenUpdateRepositoryError_errorStateIsNotNull() {
        // Given
        val exception = IOException("Error Test")
        every { repository.getData() } returns
                RepositoryState(
                        MutableLiveData<Boolean>().also { it.value = true },
                        MutableLiveData<Exception>().also { it.value = null },
                        LivePagedListBuilder(mockk(), 10)
                )
        val obs = spyk(Observer<Exception> {})
        viewModel.errorState.observeForever(obs)

        // When
        runBlocking {
            viewModel.update()
            delay(5)
        }

        // Then
        verify(exactly = 1) { obs.onChanged(exception) }
    }
}
