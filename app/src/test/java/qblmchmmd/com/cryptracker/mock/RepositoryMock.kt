package qblmchmmd.com.cryptracker.mock

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData

class RepositoryMock<T>(private val getDataReturn: T,
                        private val throwException : Exception? = null,
                        override val remoteData: RemoteData<T> = RemoteDataMock(getDataReturn),
                        override val localData: LocalData<T> = LocalDataMock())
    : Repository<T> {

    var getDataCalled = false

    override suspend fun getData(): Deferred<T> {
        getDataCalled = true
        delay(100)
        throwException?.let {
            throw  it
        }
        return GlobalScope.async { getDataReturn }
    }
}