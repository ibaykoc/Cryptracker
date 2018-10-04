package qblmchmmd.com.cryptracker.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import qblmchmmd.com.cryptracker.datasource.local.CryptoDao
import qblmchmmd.com.cryptracker.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.CryptoUiModel

data class RepositoryState<V>(
        val loading: LiveData<Boolean>,
        val error: LiveData<Exception>,
        val pagedListBuilder: LivePagedListBuilder<Int, V>
)

interface Repository<V, R> {
    val loading: MutableLiveData<Boolean>
    val error: MutableLiveData<Exception>
    val cryptoRemoteDataSource: CryptoRemoteDatasource
    val localData: CryptoDao
    val modelTransformer: ModelTransformer<R, V>
    fun getData(): RepositoryState<CryptoUiModel>
    fun getLatestCrypto(start: Int, limit: Int)
}