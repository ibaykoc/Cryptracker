package qblmchmmd.com.cryptracker.di

import android.arch.persistence.room.Room
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.android.Main
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import qblmchmmd.com.cryptracker.model.datasource.local.CryptoDB
import qblmchmmd.com.cryptracker.model.datasource.remote.CoinMarketCapAPI
import qblmchmmd.com.cryptracker.model.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel
import qblmchmmd.com.cryptracker.model.repository.CryptoListRepository
import qblmchmmd.com.cryptracker.model.repository.CryptoModelValidator
import qblmchmmd.com.cryptracker.model.repository.ModelTransformer
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import kotlin.coroutines.experimental.CoroutineContext

val cryptrackerRepositoryModule = module {
    single<CryptoRemoteDatasource> {

        CoinMarketCapAPI()
    }

    single(name = "CryptoDao") {
//        Room.databaseBuilder(androidApplication(),
//                CryptoDB::class.java, "crypto.db")
//                .build().cryptoDao()

        Room.inMemoryDatabaseBuilder(androidApplication(),
                CryptoDB::class.java)
                .build().cryptoDao()
    }

    single<CoroutineContext>(name = "mainThread") {
        Dispatchers.Main
    }

    single<CoroutineContext>(name = "bgThread") {
        Dispatchers.IO
    }

    single<ModelTransformer<CryptoListResponse, List<CryptoUiModel>>> { _ ->
        CryptoModelValidator()
    }

    viewModel {
        MainViewModel(repository = CryptoListRepository(cryptoRemoteDataSource = get(),
                localData = get(name = "CryptoDao"), modelTransformer = get()))
    }
}