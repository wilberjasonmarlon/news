package cu.wilb3r.news.data.model


import android.os.Parcelable
import cu.wilb3r.news.data.entities.Articles
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String,
    val urlToImage: String?,
    val source: Source?
) : Parcelable {
    // Create Entity from this object
    fun toEntity(): Articles {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ROOT)
        sdf.timeZone = TimeZone.getDefault()
        val date = publishedAt?.let { sdf.parse(it) }
                ?: sdf.parse(Calendar.getInstance().toString())
        return Articles(
                url,
                author ?: "",
                content ?: "",
                description ?: "",
                date,
                title ?: "",
                urlToImage ?: "",
            Source(source?.id ?: "", source?.name ?: "")
        )
    }
}