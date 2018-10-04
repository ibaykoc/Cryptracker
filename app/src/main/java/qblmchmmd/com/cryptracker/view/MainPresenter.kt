package qblmchmmd.com.cryptracker.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.crypto_list_item.view.*
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainPresenter(private val viewModel: MainViewModel, private val mainView: MainActivity) : MainViewDelegate {
    override val data: MutableList<CryptoUiModel> = mutableListOf()

    init {
        viewModel.loadingState.observe(mainView, Observer { loading ->
            loading?.let {
                Log.d("DEBUK", "MainPresenter::loadingState: $it")
                mainView.main_refresh.isRefreshing = it
            }
        })

        viewModel.errorState.observe(mainView, Observer { error ->
            error?.let {
                Log.d("DEBUK", "MainPresenter::errorState: ${it.localizedMessage}")
            }
        })

        viewModel.dataState.observe(mainView, Observer { data ->
            data?.let {
                Log.d("DEBUK", "MainPresenter::dataState.size: ${it.count()}")
                mainView.cryptoListAdapter.submitList(it)
            }
        })
    }

    override fun onRefresh() {
        Log.d("DEBUK", "MainPresenter::onRefresh()")
        viewModel.update()
    }

    @SuppressLint("SetTextI18n")
    override fun onCryptoListBindItem(itemView: View, itemData: CryptoUiModel?, position: Int) {
        itemData?.let {
            itemView.crypto_list_item_name.text = it.name
            itemView.crypto_list_item_price.text = "$${it.price}"
        } ?: run {
            itemView.crypto_list_item_name.text = "$position"
            itemView.crypto_list_item_price.text = "Not Available"
        }
    }
}