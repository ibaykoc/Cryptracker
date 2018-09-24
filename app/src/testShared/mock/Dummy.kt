package mock

import qblmchmmd.com.cryptracker.model.CryptoListResponse

val mockDataNull = CryptoListResponse(status = null, data = null)
val mockData = CryptoListResponse(status = null, data = listOf(
        CryptoListResponse.Data(
                null,
                "Bitcoco",
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
                                8000.0,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                )
        ),
        CryptoListResponse.Data(
                null,
                "Bitcoca",
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
                                9000.0,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                )
        ))
)