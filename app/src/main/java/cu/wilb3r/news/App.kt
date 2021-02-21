package cu.wilb3r.news

import android.annotation.SuppressLint
import android.app.Application
import cu.wilb3r.news.di.appModule
import cu.wilb3r.news.di.dataBaseModule
import cu.wilb3r.news.di.networkModule
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@SuppressLint("NonConstantResourceId")
@ReportsCrashes(
    mailTo = "wilberjasonmarlon@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_text
)

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        val appComponent = listOf(
            appModule(this@App),
                networkModule,
                dataBaseModule
        )
        startKoin {
            androidContext(this@App)
            modules(appComponent)
        }
        ACRA.init(this)
    }
}