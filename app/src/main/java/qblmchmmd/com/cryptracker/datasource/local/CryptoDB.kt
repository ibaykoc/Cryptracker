package qblmchmmd.com.cryptracker.datasource.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import qblmchmmd.com.cryptracker.model.CryptoUiModel

@Database(entities = [CryptoUiModel::class], version = 1)
abstract class CryptoDB : RoomDatabase(){

    abstract fun cryptoDao() : CryptoDao
}