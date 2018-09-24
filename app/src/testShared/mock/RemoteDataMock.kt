package mock

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData


class RemoteDataMock<T>(private val fetchDataReturn : T) : RemoteData<T> {
    var fetchDataCalled = false

    override suspend fun fetchData(): Deferred<T> {
        fetchDataCalled = true
        return GlobalScope.async { fetchDataReturn }
    }
}