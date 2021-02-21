package cu.wilb3r.news.others

data class Resource<T>(
    var status: Status,
    var data: T? = null,
    var message: String? = null
) {
    companion object {
        fun <T> loading(data: T?): Resource<T> =
            Resource(Status.LOADING, data, null)

        fun <T> success(data: T?): Resource<T> =
            Resource(Status.SUCCESS, data, null)

        fun <T> error(msg: String?, data: T?): Resource<T> =
            Resource(Status.ERROR, message = msg, data = data)
    }

    override fun toString(): String {
        val s = StringBuffer().apply {
            append(status)
            append(if (data != null) data.toString() else ", ")
            append(if (message != null) message.toString() else ", ")
        }
        return s.toString()
    }
}


