package cu.wilb3r.news.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import cu.wilb3r.news.R
import cu.wilb3r.news.data.api.ApiService
import cu.wilb3r.news.data.db.AppDataBase
import cu.wilb3r.news.repository.NewRepository
import cu.wilb3r.news.ui.home.HomeViewModel
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataBaseModule = module {
    single { AppDataBase(get())}
}

val networkModule = module {
    single { HttpLoggingInterceptor.Logger.DEFAULT }
    single { HttpLoggingInterceptor(get()) }
    single { ApiService(get()) }
}

fun appModule(context: Context) =  module {
    single { Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
                .placeholder(R.drawable.image_scrim)
                .error(R.drawable.image_scrim)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
    )}
    single { NewRepository(get(), get())}
    viewModel { HomeViewModel(get()) }
}





