package qblmchmmd.com.cryptracker.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.Repository
import kotlin.coroutines.experimental.CoroutineContext

class MainViewModel(private val repository: Repository<CryptoListResponse>,
                    private val mainThread: CoroutineContext,
                    private val bgThread: CoroutineContext) : ViewModel() {

    private val state = MutableLiveData<MainViewState>()
    val viewState: LiveData<MainViewState> = Transformations.map(state) {
        state.value
    }

    fun update() {
        GlobalScope.launch(mainThread) {
            state.value = MainViewState.Loading(true)
            state.value = try {
                MainViewState.Data(
                        withContext(bgThread) { repository.getData().await() }
                )
            } catch (err: Exception) {
                MainViewState.Error(err.localizedMessage)
            }
            state.value = MainViewState.Loading(false)
        }
    }

}