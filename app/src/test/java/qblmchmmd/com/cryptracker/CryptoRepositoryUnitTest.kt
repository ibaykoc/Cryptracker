package qblmchmmd.com.cryptracker

import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import qblmchmmd.com.cryptracker.mock.LocalDataMock
import qblmchmmd.com.cryptracker.mock.RemoteDataMock
import qblmchmmd.com.cryptracker.mock.mockDataNull
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.CryptoRepository

class CryptoRepositoryUnitTest {

    @Test
    fun whenGetData_remoteFetchDataCalled() {

        val remoteData = RemoteDataMock(mockDataNull)
        val localData = LocalDataMock<CryptoListResponse>()
        val cryptoRepo = CryptoRepository(remoteData = remoteData, localData = localData)

        runBlocking {
            cryptoRepo.getData()
            delay(1000)
        }

        Assert.assertEquals(remoteData.fetchDataCalled, true)
    }
}