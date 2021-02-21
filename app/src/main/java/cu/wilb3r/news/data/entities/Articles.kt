package cu.wilb3r.news.data.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import cu.wilb3r.news.data.model.Source
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity (tableName = "article_table")
@Parcelize
data class Articles(
        @PrimaryKey(autoGenerate = false)
        val url: String,
        val author: String?,
        val content: String?,
        @SerializedName("description")
        val articleDescription: String?,
        val publishedAt: Date?,
        val title: String?,
        val urlToImage: String?,
        val source: Source?
): Parcelable