package cu.wilb3r.news.ui.home

import android.util.Log
import androidx.lifecycle.*
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.others.AbsentLiveData
import cu.wilb3r.news.others.Resource
import cu.wilb3r.news.repository.NewRepository

class HomeViewModel(
        private val repo: NewRepository,
): ViewModel() {

//    private val _result = MutableLiveData<Resource<List<Articles>>?>()
//    val result: LiveData<Resource<List<Articles>>>
//        get() = Transformations.switchMap(_result) {
//        Log.d("ViewModel", "Running into switchMap")
//        repo.getEverything("2021-01-18").asLiveData(viewModelScope.coroutineContext)
//
//    }

    private val _query: MutableLiveData<QueryWrapper> = MutableLiveData()
    private val query: LiveData<QueryWrapper>
        get() = _query

    val articles: LiveData<Resource<List<Articles>>> = Transformations
            .switchMap(_query) { input ->
                input.ifExists {
                    repo.getEverything(it).asLiveData(viewModelScope.coroutineContext)
                }
            }


    fun getEverything(from: String) {
        //_result.postValue(null)
        val query = QueryWrapper(from)
        if (_query.value == query) {
            return
        }
        _query.value = query
    }

    data class QueryWrapper(val query: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (query.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(query)
            }
        }
    }
}