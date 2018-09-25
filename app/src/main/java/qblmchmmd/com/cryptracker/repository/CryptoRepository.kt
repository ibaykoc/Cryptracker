package qblmchmmd.com.cryptracker.repository

import android.util.Log
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData
import qblmchmmd.com.cryptracker.model.CryptoListResponse

class CryptoRepository(override val remoteData: RemoteData<CryptoListResponse>, override val localData: LocalData<CryptoListResponse>) : Repository<CryptoListResponse> {
    override suspend fun getData(): Deferred<CryptoListResponse> = GlobalScope.async {
        try {
            remoteData.fetchData().await()
        } catch (err : Exception) {
//            Log.d(this::class.java.simpleName, err.localizedMessage)
            throw err
        }
    }
}