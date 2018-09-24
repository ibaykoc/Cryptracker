package qblmchmmd.com.cryptracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import qblmchmmd.com.cryptracker.repository.Repository
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import java.io.IOException
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel(private val repository: Repository<CryptoListResponse>,
                    private val mainThread : CoroutineContext,
                    private val bgThread : CoroutineContext):ViewModel() {

    private val data = MutableLiveData<CryptoListResponse>()
    val uiData : LiveData<CryptoListResponse> = Transformations.map(data) {
        it
    }

    private val loading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = Transformations.map(loading) {
        it
    }

    private val error = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = Transformations.map(error) {
        it
    }

    fun update() {
        GlobalScope.launch(mainThread){
            loading.value = true
            try {
                data.value = withContext(bgThread) { repository.getData().await() }
            } catch (err : Exception) {
//                Log.d(this::class.java.simpleName, err.localizedMessage)
                error.value = true
            }
            loading.value = false
        }
    }

}