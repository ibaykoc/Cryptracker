package qblmchmmd.com.cryptracker

class RepositoryMock<T>(val onCompleteMockData: T? = null,
                        val doOnError:Boolean = false) : Repository<T> {

    var getDataCalled = false

    override fun getData(onComplete: (T) -> Unit, onError: () -> Unit) {
        getDataCalled = true
        this.onCompleteMockData?.let {
            onComplete(onCompleteMockData)
        }
        if (doOnError) onError()
    }
}