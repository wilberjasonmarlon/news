package cu.wilb3r.news.others

import cu.wilb3r.news.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val API_URL = "https://newsapi.org/v2/"
    const val DATABASE_NAME = "news_db"
    const val DB_VERSION = 1
    const val PREF_NAME = BuildConfig.APPLICATION_ID + ".PREF_NAME"
    const val KEY_VALUE = BuildConfig.APPLICATION_ID + ".KEY_VALUE"
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val formatter2 = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ROOT)
}