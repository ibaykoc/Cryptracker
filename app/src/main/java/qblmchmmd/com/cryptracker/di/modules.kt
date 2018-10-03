package qblmchmmd.com.cryptracker.di

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.android.Main
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import qblmchmmd.com.cryptracker.datasource.local.LocalData
import qblmchmmd.com.cryptracker.datasource.local.CryptoDB
import qblmchmmd.com.cryptracker.datasource.remote.CoinMarketCapAPI
import qblmchmmd.com.cryptracker.datasource.remote.RemoteData
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.repository.CryptoRepository
import qblmchmmd.com.cryptracker.view.MainEventHandler
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import kotlin.coroutines.experimental.CoroutineContext

val cryptrackerRepositoryModule = module {
    single<RemoteData<CryptoListResponse>> {

        CoinMarketCapAPI()
    }

    single<LocalData<CryptoListResponse>> {
        CryptoDB()
    }

    single<CoroutineContext> (name = "mainThread") {
        Dispatchers.Main
    }

    single<CoroutineContext> (name = "bgThread") {
        Dispatchers.IO
    }

    viewModel {
        MainViewModel(repository = CryptoRepository(remoteData = get(), localData = get()),
                mainThread = get(name = "mainThread"),
                bgThread = get(name = "bgThread"))
    }
}