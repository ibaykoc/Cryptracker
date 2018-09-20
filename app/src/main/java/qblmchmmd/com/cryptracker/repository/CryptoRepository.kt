package qblmchmmd.com.cryptracker.repository

import android.util.Log
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData
import qblmchmmd.com.cryptracker.model.CryptoListResponse

class CryptoRepository(override val remoteData: RemoteData<CryptoListResponse>, override val localData: LocalData<CryptoListResponse>) : Repository<CryptoListResponse> {
    override suspend fun getData(): CryptoListResponse? {
        try {
            return remoteData.fetchData().await()
        } catch (err : Exception) {
            Log.d(this::class.java.simpleName, err.localizedMessage)
            throw err
        }
    }
}