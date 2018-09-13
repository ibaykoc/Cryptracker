package qblmchmmd.com.cryptracker.model

import com.google.gson.annotations.SerializedName


data class CryptoListingResponse(
        @SerializedName("status") val status: Status?,
        @SerializedName("data") val data: List<Data?>?
) {

    data class Data(
            @SerializedName("id") val id: Int?, // 2010
            @SerializedName("name") val name: String?, // Cardano
            @SerializedName("symbol") val symbol: String?, // ADA
            @SerializedName("slug") val slug: String?, // cardano
            @SerializedName("circulating_supply") val circulatingSupply: Long?, // 25927070538
            @SerializedName("total_supply") val totalSupply: Long?, // 31112483745
            @SerializedName("max_supply") val maxSupply: Long?, // 45000000000
            @SerializedName("date_added") val dateAdded: String?, // 2017-10-01T00:00:00.000Z
            @SerializedName("num_market_pairs") val numMarketPairs: Int?, // 47
            @SerializedName("cmc_rank") val cmcRank: Int?, // 10
            @SerializedName("last_updated") val lastUpdated: String?, // 2018-09-12T10:00:38.000Z
            @SerializedName("quote") val quote: Quote?
    ) {

        data class Quote(
                @SerializedName("USD") val uSD: USD?
        ) {

            data class USD(
                    @SerializedName("price") val price: Double?, // 0.0623136172508
                    @SerializedName("volume_24h") val volume24h: Double?, // 69179737.19109
                    @SerializedName("percent_change_1h") val percentChange1h: Double?, // -0.85225
                    @SerializedName("percent_change_24h") val percentChange24h: Double?, // -13.9318
                    @SerializedName("percent_change_7d") val percentChange7d: Double?, // -37.8877
                    @SerializedName("market_cap") val marketCap: Double?, // 1615609549.9394252
                    @SerializedName("last_updated") val lastUpdated: String? // 2018-09-12T10:00:38.000Z
            )
        }
    }


    data class Status(
            @SerializedName("timestamp") val timestamp: String?, // 2018-09-12T10:01:35.719Z
            @SerializedName("error_code") val errorCode: Int?, // 0
            @SerializedName("error_message") val errorMessage: Any?, // null
            @SerializedName("elapsed") val elapsed: Int?, // 8
            @SerializedName("credit_count") val creditCount: Int? // 1
    )
}