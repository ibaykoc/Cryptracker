package qblmchmmd.com.cryptracker.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.crypto_list_item.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import qblmchmmd.com.cryptracker.R
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import qblmchmmd.com.cryptracker.viewmodel.MainViewState

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    lateinit var eventHandler : MainEventHandler

    lateinit var presenter: MainPresenter

    val cryptoListAdapter = CryptoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventHandler = MainEventHandler(viewModel)
        presenter = MainPresenter(viewModel, this)

        main_refresh.setOnRefreshListener { eventHandler.onRefresh() }

        eventHandler.onCreate()

        crypto_list.adapter = cryptoListAdapter
        crypto_list.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false)
    }

    inner class CryptoListAdapter : RecyclerView.Adapter<CryptoListAdapter.ViewHolder>() {

        val data = mutableListOf<CryptoListResponse.Data>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item, parent, false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.crypto_list_item_name.text = data[position].name
            holder.itemView.crypto_list_item_price.text = "$${data[position].quote?.uSD?.price.toString()}"
        }

        fun updateData(newData: List<CryptoListResponse.Data>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
