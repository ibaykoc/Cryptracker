package qblmchmmd.com.cryptracker.model.datasource.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import qblmchmmd.com.cryptracker.model.entity.CryptoUiModel

@Database(entities = [CryptoUiModel::class], version = 1)
abstract class CryptoDB : RoomDatabase(){

    abstract fun cryptoDao() : CryptoDao
}