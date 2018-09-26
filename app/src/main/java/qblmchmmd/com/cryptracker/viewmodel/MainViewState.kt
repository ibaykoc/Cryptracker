package qblmchmmd.com.cryptracker.viewmodel

import qblmchmmd.com.cryptracker.model.CryptoListResponse


sealed class MainViewState {
    data class Loading(val isLoading: Boolean) : MainViewState()
    data class Error(val errorMessage: String) : MainViewState()
    data class Data(val data: CryptoListResponse) : MainViewState()
}