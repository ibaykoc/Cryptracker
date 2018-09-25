@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package qblmchmmd.com.cryptracker

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.koc.countrinfo.util.LiveDataTestUtil
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.experimental.*
import mock.RepositoryMock
import mock.mockData
import mock.mockDataNull
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
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
    fun whenUpdate_isLoadingIsTrue() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            delay(100)
            mockData
        }

        // When
        viewModel.update()

        // Then
        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isLoading))
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
    fun whenUpdate_repositoryComplete_isLoadingIsFalse() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            delay(10)
            mockData
        }

        // When
        runBlocking {
            viewModel.update()
            delay(11)
        }

        //Then
        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdate_repositoryError_isLoadingIsFalse() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            throw IOException()
        }

        // When
        runBlocking {
            viewModel.update()
            delay(1)
        }

        // Then
        assertEquals(false, LiveDataTestUtil.getValue(viewModel.isLoading))
    }

    @Test
    fun whenUpdateRepositoryComplete_dataUpdated() {
        // Given
        val expectedData = mockDataNull
        coEvery { repository.getData() } returns GlobalScope.async {
            expectedData
        }

        runBlocking {
            viewModel.update()
            delay(1)
        }

        assertEquals(expectedData, LiveDataTestUtil.getValue(viewModel.uiData))
    }

    @Test
    fun whenUpdateRepositoryError_isErrorIsTrue() {
        // Given
        coEvery { repository.getData() } returns GlobalScope.async {
            throw IOException()
        }

        // When
        runBlocking {
            viewModel.update()
            delay(1)
        }

        // Then
        assertEquals(true, LiveDataTestUtil.getValue(viewModel.isError))
    }
}
