package qblmchmmd.com.cryptracker.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import qblmchmmd.com.cryptracker.R
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val tag = this::class.java.simpleName

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.uiData.observe(this, Observer {
            tv.text = it ?: "null"
        })

        viewModel.isLoading.observe(this, Observer {
            Log.d(tag, "isLoading $it")
        })

        viewModel.isError.observe(this, Observer {
            Log.d(tag, "isError $it")
        })

        viewModel.update()
    }
}
