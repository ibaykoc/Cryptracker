package qblmchmmd.com.cryptracker

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import mock.LocalDataMock
import mock.RemoteDataMock
import mock.mockData
import mock.mockDataNull
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.CryptoRepository

class CryptoRepositoryUnitTest {

    @Test
    fun whenGetData_remoteFetchDataCalled() {

        // Given
        val remoteData = mockk<RemoteData<CryptoListResponse>>()
        val localData = mockk<LocalData<CryptoListResponse>>()
        coEvery { remoteData.fetchData() } returns GlobalScope.async { mockData }
        val cryptoRepo = CryptoRepository(remoteData = remoteData, localData = localData)

        // When
        runBlocking {
            cryptoRepo.getData()
        }

        // Then
        coVerify { remoteData.fetchData() }
    }
}