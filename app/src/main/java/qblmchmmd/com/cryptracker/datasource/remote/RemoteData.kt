package qblmchmmd.com.cryptracker.datasource.remote

import kotlinx.coroutines.experimental.Deferred

interface RemoteData<T> {
    suspend fun fetchData() : Deferred<T>
}