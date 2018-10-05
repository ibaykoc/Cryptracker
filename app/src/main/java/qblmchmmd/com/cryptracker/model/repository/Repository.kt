package qblmchmmd.com.cryptracker.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import qblmchmmd.com.cryptracker.model.datasource.local.CryptoDao
import qblmchmmd.com.cryptracker.model.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel


interface Repository<V, R> {
    val loading: MutableLiveData<Boolean>
    val error: MutableLiveData<Exception>
    val cryptoRemoteDataSource: CryptoRemoteDatasource
    val localData: CryptoDao
    val modelTransformer: ModelTransformer<R, V>
    fun getState(): RepositoryState<CryptoUiModel>
    fun getLatestCrypto(start: Int, limit: Int)
}