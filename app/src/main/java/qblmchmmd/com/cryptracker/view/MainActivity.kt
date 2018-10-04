package qblmchmmd.com.cryptracker.view

import android.arch.paging.PagedListAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import qblmchmmd.com.cryptracker.R
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    lateinit var delegate: MainViewDelegate

    val cryptoListAdapter = CryptoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delegate = MainPresenter(viewModel, this)

        main_refresh.setOnRefreshListener { delegate.onRefresh() }

        crypto_list.adapter = cryptoListAdapter
        crypto_list.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false)
    }

    inner class CryptoListAdapter : PagedListAdapter<CryptoUiModel, CryptoListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<CryptoUiModel>() {
        override fun areItemsTheSame(oldItem: CryptoUiModel?, newItem: CryptoUiModel?): Boolean {
            return oldItem?.id == newItem?.id
        }

        override fun areContentsTheSame(oldItem: CryptoUiModel?, newItem: CryptoUiModel?): Boolean {
            return oldItem?.name == newItem?.name && oldItem?.price == newItem?.price
        }

    }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            delegate.onCryptoListBindItem(holder.itemView,getItem(position), position)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
