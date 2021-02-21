package cu.wilb3r.news.data.db

import androidx.room.*
import cu.wilb3r.news.data.entities.Articles
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticle(item: List<Articles>)

    @Delete
    suspend fun deleteArticle(article: Articles)

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC")
    fun observeAllArticles(): Flow<List<Articles>>

    @Query("DELETE FROM article_table")
    fun cleanAllArticles(): Int

    @Query("DELETE FROM article_table")
    fun deleteAllArticles(): Int

    @Transaction
    suspend fun insert(item: List<Articles>) {
        cleanAllArticles()
        insertArticle(item)
    }
}