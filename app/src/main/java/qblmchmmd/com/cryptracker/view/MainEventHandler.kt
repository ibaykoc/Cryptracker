package qblmchmmd.com.cryptracker.view

import qblmchmmd.com.cryptracker.viewmodel.MainViewModel

class MainEventHandler(private val viewModel: MainViewModel) {

    fun onCreate() {
//        viewModel.update()
    }

    fun onRefresh() {
        viewModel.update()
    }
}
