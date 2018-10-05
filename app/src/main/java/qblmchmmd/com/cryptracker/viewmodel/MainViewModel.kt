package qblmchmmd.com.cryptracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel
import qblmchmmd.com.cryptracker.model.repository.Repository

class MainViewModel(val repository: Repository<List<CryptoUiModel>, CryptoListResponse>)
    : ViewModel() {

    val errorState : LiveData<Exception>

    val loadingState : LiveData<Boolean>

    val dataState : LiveData<PagedList<CryptoUiModel>>

    init {
        // Do observe repository
        val repoState = repository.getState()
        loadingState = repoState.loading
        errorState = repoState.error
        dataState = repoState.liveDataPagedList
    }

    fun update() {
        Log.d("DEBUK", "MainViewModel::repository.getLatestCrypto(1, ${dataState.value?.size ?: 0})")
        repository.getLatestCrypto(1, dataState.value?.size ?: 0)
    }
}