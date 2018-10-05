package qblmchmmd.com.cryptracker.model.repository

import qblmchmmd.com.cryptracker.model.entity.CryptoListResponse
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel

class CryptoModelValidator : ModelTransformer<CryptoListResponse, List<CryptoUiModel>> {
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
