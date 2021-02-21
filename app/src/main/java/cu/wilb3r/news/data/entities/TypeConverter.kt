package cu.wilb3r.news.data.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import cu.wilb3r.news.data.model.Source
import org.json.JSONObject
import java.util.*


class TypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toSource(source: String): Source {
        val json = JSONObject(source)
        return Source(json.getString("id") ?: "", json.getString("name") ?: "")
    }
}