package qblmchmmd.com.cryptracker.di

import android.arch.persistence.room.Room
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.android.Main
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import qblmchmmd.com.cryptracker.datasource.local.CryptoDB
import qblmchmmd.com.cryptracker.datasource.local.CryptoDao
import qblmchmmd.com.cryptracker.datasource.remote.CoinMarketCapAPI
import qblmchmmd.com.cryptracker.datasource.remote.CryptoRemoteDatasource
import qblmchmmd.com.cryptracker.model.CryptoListResponse
import qblmchmmd.com.cryptracker.model.CryptoUiModel
import qblmchmmd.com.cryptracker.repository.CryptoListRepository
import qblmchmmd.com.cryptracker.repository.ModelTransformer
import qblmchmmd.com.cryptracker.viewmodel.MainViewModel
import kotlin.coroutines.experimental.CoroutineContext

val cryptrackerRepositoryModule = module {
    single<CryptoRemoteDatasource> {

        CoinMarketCapAPI()
    }

    single<CryptoDao>(name = "CryptoDao") {
        Room.databaseBuilder(androidApplication(),
                CryptoDB::class.java, "crypto.db")
                .build().cryptoDao()

//        Room.inMemoryDatabaseBuilder(androidApplication(),
//                CryptoDB::class.java)
//                .build().cryptoDao()
    }

    single<CoroutineContext>(name = "mainThread") {
        Dispatchers.Main
    }

    single<CoroutineContext>(name = "bgThread") {
        Dispatchers.IO
    }

    single<ModelTransformer<CryptoListResponse, List<CryptoUiModel>>> { _ ->
        object : ModelTransformer<CryptoListResponse, List<CryptoUiModel>> {
            override fun transform(input: CryptoListResponse): List<CryptoUiModel> {
                input.data?.let { list ->
                    return list.asSequence()
                            .filter { it != null }
                            .map { data ->
                                var cryptoUiModel: CryptoUiModel? = null
                                data!!.id?.let { cryptId ->
                                    data.name?.let { cryptName ->
                                        data.quote?.uSD?.price?.let { cryptPrice ->
                                            cryptoUiModel = CryptoUiModel(cryptId, cryptName, cryptPrice)
                                        }
                                    }
                                    cryptoUiModel
                                }
                            }
                            .filter { transformedData -> transformedData != null }
                            .map { validData -> validData!! }.toList()

                }
                return emptyList()
            }

        }
    }

    viewModel {
        MainViewModel(repository = CryptoListRepository(cryptoRemoteDataSource = get(),
                localData = get(name = "CryptoDao"), modelTransformer = get()),
                mainThread = get(name = "mainThread"),
                bgThread = get(name = "bgThread"))
    }
}