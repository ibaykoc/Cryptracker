package mock

import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel

class MockDataFactory {
    companion object {
        fun createCryptoUiModel(total: Int): List<CryptoUiModel> {
            val list = mutableListOf<CryptoUiModel>()
            for (i in 0..total) {
                list.add(CryptoUiModel(i, "crypto$i", i.toDouble()))
            }
            return list
        }

        fun createCryptoListResponse(fromPrice: Int, to: Int): CryptoListResponse {
            val list = mutableListOf<CryptoListResponse.Data>()
            for (i in fromPrice..to) {
                list.add(CryptoListResponse.Data(
                        i,
                        "crypto$i",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        CryptoListResponse.Data.Quote(
                                CryptoListResponse.Data.Quote.USD(
                                        i.toDouble(),
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                        )
                ))
            }
            return CryptoListResponse(status = null, data = list)
        }
    }
}