package qblmchmmd.com.cryptracker.model.datasource.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.RoomDatabase

abstract class LocalData<T>:RoomDatabase() {
    abstract fun getAll() : LiveData<T>
    abstract fun saveAll(item: T)
}