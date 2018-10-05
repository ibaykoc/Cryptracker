package qblmchmmd.com.cryptracker.model.datasource.remote

import com.google.gson.Gson
import kotlinx.coroutines.experimental.*
import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class CoinMarketCapAPI : CryptoRemoteDatasource {
    override suspend fun getLatestCrypto(start: Int, limit: Int) : Deferred<CryptoListResponse> {
        return GlobalScope.async{
            val url = URL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=$start&limit=$limit&convert=USD")
            with(url.openConnection() as HttpURLConnection) {
                setRequestProperty("X-CMC_PRO_API_KEY", "94d26260-8b2a-48a4-b889-57e2a85657a9")
                requestMethod = "GET"

                try {
                    BufferedReader(inputStream.reader()).use {
                        val response = it.readText()
                        Gson().fromJson(response, CryptoListResponse::class.java)
                    }
                } catch (err: Error) {
                    throw err
                }
            }
        }
    }
}