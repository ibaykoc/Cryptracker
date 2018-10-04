package qblmchmmd.com.cryptracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.Repository
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel(val repository: Repository<CryptoListResponse>,
                    val mainThread: CoroutineContext,
                    val bgThread: CoroutineContext) : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    val loadingState = loading as LiveData<Boolean>

    private val error = MutableLiveData<Exception>()
    val errorState = error as LiveData<Exception>

    private val data = MutableLiveData<CryptoListResponse>()
    val dataState = data as LiveData<CryptoListResponse>

    init {
        update()
    }

    fun update() {
        GlobalScope.launch(mainThread) {
            loading.value = true
            try {
                data.value = withContext(bgThread) { repository.getData().await() }
            } catch (err: Exception) {
                error.value = err
            }
            loading.value = false
        }
    }

}