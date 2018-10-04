package qblmchmmd.com.cryptracker.view

import android.arch.lifecycle.Observer
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.crypto_list_item.view.*
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainPresenter(private val viewModel:MainViewModel, private val mainView:MainActivity): MainViewDelegate {
    override val data: MutableList<CryptoListResponse.Data> = mutableListOf()

    init {
        viewModel.loadingState.observe(mainView, Observer { loading ->
            loading?.let {
                Log.d("DEBUK", "loadingState: $it")
                mainView.main_refresh.isRefreshing = it
            }
        })

        viewModel.errorState.observe(mainView, Observer { error ->
            error?.let {
                Log.d("DEBUK", "errorState: ${it.localizedMessage}")
            }
        })

        viewModel.dataState.observe(mainView, Observer { data ->
            data?.let {
                Log.d("DEBUK", "dataState size: ${it.data?.count()}")
                this.data.clear()
                this.data.addAll(it.data as List<CryptoListResponse.Data>)
                mainView.cryptoListAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onRefresh() {
        Log.d("DEBUK", "MainPresenter::onRefresh()")
        viewModel.update()
    }

    override fun cryptoListItemSize(): Int {
        return data.size
    }

    override fun onCryptoListBindItem(itemView: View, position: Int) {
        itemView.crypto_list_item_name.text = data[position].name
        itemView.crypto_list_item_price.text = "$${data[position].quote?.uSD?.price.toString()}"
    }
}