package cu.wilb3r.news.repository

import android.content.Context
import cu.wilb3r.news.BuildConfig.NEWS_API_KEY
import cu.wilb3r.news.data.api.ApiService
import cu.wilb3r.news.data.db.AppDataBase
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.data.network.networkBoundResource
import cu.wilb3r.news.others.Coroutines
import cu.wilb3r.news.others.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class NewRepository (
    private val db: AppDataBase,
    private val apiService: ApiService
    ) {
    fun getEverything(from: String): Flow<Resource<List<Articles>>> {
        val param  = HashMap<String, String>().apply {
            put("q", "tesla")
            put("from", from)
            put("sortBy", "published")
            put("apikey", NEWS_API_KEY)
        }
        return networkBoundResource(
                fetchFromLocal = { db.articleDao().observeAllArticles() },
                shouldFetchFromRemote = { true },
                fetchFromRemote = { apiService.getEverything(param) },
                processRemoteResponse = { },
                saveRemoteData = { Coroutines.io {
                    val articles = ArrayList<Articles>()
                    for(item in it.articles){
                        articles.add(item.toEntity())
                    }
                    db.articleDao().insert(articles)
                }},
                onFetchFailed = { _, _ -> }
        ).flowOn(Dispatchers.IO)
    }
}