package cu.wilb3r.news.others

import androidx.lifecycle.LiveData

class AbsentLiveData<T : Any?> private constructor(): LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        // Factory method
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}