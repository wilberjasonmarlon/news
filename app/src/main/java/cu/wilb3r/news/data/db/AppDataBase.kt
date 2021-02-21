package cu.wilb3r.news.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.data.entities.TypeConverter
import cu.wilb3r.news.others.Constants.DATABASE_NAME
import cu.wilb3r.news.others.Constants.DB_VERSION

@Database(entities = [Articles::class], version = DB_VERSION, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun articleDao(): ArticleDAO

    companion object {

        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also {
                instance = it
            }
        }

        private fun buildDataBase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        DATABASE_NAME
                ).build()
    }
}