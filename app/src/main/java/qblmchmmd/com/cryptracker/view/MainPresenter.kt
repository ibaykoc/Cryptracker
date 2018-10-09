package qblmchmmd.com.cryptracker.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.crypto_list_item.view.*
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainPresenter(private val viewModel: MainViewModel,
                    private val mainView: MainActivity) : MainViewDelegate {

    override val data: MutableList<CryptoUiModel> = mutableListOf()

    init {

        viewModel.loadingState.observe(mainView, Observer { loading ->
            loading?.let {
                mainView.main_refresh.isRefreshing = it
            }
        })

        viewModel.errorState.observe(mainView, Observer { error ->
            error?.let {
            }
        })

        viewModel.dataState.observe(mainView, Observer { data ->
            data?.let {
                mainView.cryptoListAdapter.submitList(it)
            }
        })

        mainView.cryptoListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
//                mainView.crypto_list.scrollToPosition(positionStart)
                //TODO: Fix weird scroll when reload on enditemloaded
                Log.d("DEBUK", "onItemRangeInserted(positionStart: $positionStart, itemCount: $itemCount)")
            }

            override fun onChanged() {
                super.onChanged()
                Log.d("DEBUK", "onChanged()")
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                Log.d("DEBUK", "onItemRangeRemoved(positionStart: $positionStart, itemCount: $itemCount)")
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                Log.d("DEBUK", "onItemRangeMoved(fromPosition: $fromPosition, toPosition: $toPosition, itemCount: $itemCount)")
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                Log.d("DEBUK", "onItemRangeChanged(positionStart: $positionStart, itemCount: $itemCount)")
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                Log.d("DEBUK", "onItemRangeChanged(positionStart: $positionStart, itemCount: $itemCount, payload: $payload)")
            }
        })
    }

    override fun onRefresh() {
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