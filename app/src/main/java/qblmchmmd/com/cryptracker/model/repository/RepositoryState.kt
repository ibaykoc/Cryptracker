package qblmchmmd.com.cryptracker.model.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel


data class RepositoryState<V>(
        val loading: LiveData<Boolean>,
        val error: LiveData<Exception>,
        val liveDataPagedList: LiveData<PagedList<CryptoUiModel>>
)