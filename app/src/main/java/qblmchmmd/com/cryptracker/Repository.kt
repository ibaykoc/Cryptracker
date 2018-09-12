package qblmchmmd.com.cryptracker

interface Repository<T> {
    fun getData(onComplete : (T)->Unit, onError : ()->Unit)
}