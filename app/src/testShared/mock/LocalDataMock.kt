package mock

import qblmchmmd.com.cryptracker.datasource.local.LocalData

class LocalDataMock<T> : LocalData<T> {
    var localDataCalled = false
    var localDataUpdated = false

    override suspend fun fetchLocalData(): Deferred<T> {
        localDataCalled = true
        return GlobalScope.async { localDataReturn }
    }

    override suspend fun updateLocalData(): Deferred<T> {
        localDataUpdated = true
        return GlobalScope.async { localDataReturn }
    }
}