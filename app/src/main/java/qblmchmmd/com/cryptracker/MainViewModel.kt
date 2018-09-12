package qblmchmmd.com.cryptracker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel

class MainViewModel(private val repository: Repository<String>):ViewModel() {

    private val data = MutableLiveData<String>()
    val uiData : LiveData<String> = Transformations.map(data) {
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
        loading.value = true
        repository.getData({
            data.value = it
            loading.value = false
        },{
            error.value = true
            loading.value = false
        })
    }

}