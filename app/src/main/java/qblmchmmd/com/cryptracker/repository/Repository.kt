package qblmchmmd.com.cryptracker.repository

import kotlinx.coroutines.experimental.Deferred
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData

interface Repository<T> {
    val remoteData : RemoteData<T>
    val localData : LocalData<T>
    suspend fun getData() : T?
}