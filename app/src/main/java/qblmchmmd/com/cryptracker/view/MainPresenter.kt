package qblmchmmd.com.cryptracker.view

import android.arch.lifecycle.Observer
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainPresenter(viewModel:MainViewModel, private val mainView:MainActivity) {

    init {
        viewModel.loadingState.observe(mainView, Observer { loading ->
            loading?.let {
                mainView.main_refresh.isRefreshing = loading
            }
        })

        viewModel.errorState.observe(mainView, Observer { error ->
            error?.let {
                Log.d(this::class.java.simpleName, error.localizedMessage)
            }
        })

        viewModel.dataState.observe(mainView, Observer { data ->
            data?.let {
                mainView.cryptoListAdapter.updateData(it.data as List<CryptoListResponse.Data>)
            }
        })
    }

}