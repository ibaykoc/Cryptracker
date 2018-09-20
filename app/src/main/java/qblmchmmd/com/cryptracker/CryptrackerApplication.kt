package qblmchmmd.com.cryptracker

import android.app.Application
import org.koin.android.ext.android.startKoin
import qblmchmmd.com.cryptracker.di.cryptrackerRepositoryModule

class CryptrackerApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(cryptrackerRepositoryModule))
    }
}