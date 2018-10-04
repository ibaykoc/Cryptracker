package qblmchmmd.com.cryptracker.viewmodel

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.repository.RepositoryState
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel(val repository: Repository<List<CryptoUiModel>, CryptoListResponse>,
                    val mainThread: CoroutineContext,
                    val bgThread: CoroutineContext) : ViewModel() {


    val errorState = MediatorLiveData<Exception>()

    val dataState =  MediatorLiveData<PagedList<CryptoUiModel>>()

    val loadingState = MediatorLiveData<Boolean>()


    var lastRequestedPage = 1

    init {
        val repoState = repository.getData()
        loadingState.addSource(repoState.loading) {
            Log.d("DEBUK","MainViewModel::loadingState:$it")
            loadingState.value = it
        }
        errorState.addSource(repoState.error) {
            Log.d("DEBUK","MainViewModel::errorState:$it")
            errorState.value = it
        }
        dataState.addSource(repoState.pagedListBuilder.setBoundaryCallback(CryptoListBoundaryCallback()).build()) {
            Log.d("DEBUK","MainViewModel::dataState:$it")
            if(it != null && it.size >= 10) {
                lastRequestedPage += 1
            }
            dataState.value = it
        }
    }

    fun update() {
        repository.getLatestCrypto(1, dataState.value?.size ?: 0)
    }

    inner class CryptoListBoundaryCallback : PagedList.BoundaryCallback<CryptoUiModel> (){

        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()
            Log.d("DEBUK","CryptoBoundaryCallback::onZeroItemsLoaded()")
            repository.getLatestCrypto(1,10)
        }

        override fun onItemAtEndLoaded(itemAtEnd: CryptoUiModel) {
            super.onItemAtEndLoaded(itemAtEnd)
            Log.d("DEBUK","CryptoBoundaryCallback::onItemAtEndLoaded(itemAtEnd: $itemAtEnd)")
            repository.getLatestCrypto(lastRequestedPage * 10, 10)
        }

        override fun onItemAtFrontLoaded(itemAtFront: CryptoUiModel) {
            super.onItemAtFrontLoaded(itemAtFront)
            Log.d("DEBUK","CryptoBoundaryCallback::onItemAtFrontLoaded(itemAtFront: $itemAtFront)")
        }
    }
}