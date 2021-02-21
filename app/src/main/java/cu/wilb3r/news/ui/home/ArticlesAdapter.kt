package cu.wilb3r.news.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cu.wilb3r.news.R
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.databinding.ItemNewsBinding
import cu.wilb3r.news.others.RoundedCornersTransformation
import java.util.*
import kotlin.collections.ArrayList


class ArticlesAdapter (
     var context: Context,
     val glide:RequestManager
) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(), Filterable {

    inner class ViewHolder(val binding: ItemNewsBinding) :
            RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: Articles) {
            binding.apply {
                item.title!!.let {
                    source.text = it
                }
                val sCorner = 15F
                val sMargin = 2
                glide.load(item.urlToImage)
                    .transform(RoundedCornersTransformation(context, sCorner, sMargin))
                    .into(imageView)

                }
            }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    lateinit var filterList: List<Articles> ////items

    fun getItem(pos: Int): Articles = differ.currentList[pos]

    fun submitList(items: List<Articles>?) : List<Articles>{
        differ.submitList(items)
        this.filterList = differ.currentList
        return differ.currentList
    }

    //this call what ever func that you pass as parameter
    private var onItemClickListener: ((View, Int) -> Unit)? = null
    private var onLongItemClickListener: ((View, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (View, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnLongItemClickListener(listener: (View, Int) -> Unit) {
        onLongItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticlesAdapter.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.apply {
            bind(item)
            binding.root.apply {
                setOnClickListener { view ->
                    onItemClickListener?.invoke(view, position)
                }
                setOnLongClickListener { view ->
                    onLongItemClickListener?.invoke(view, position)
                    false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = differ.currentList
                } else {
                    val resultList = ArrayList<Articles>()
                    for (item in differ.currentList) {
                        if (
                                item.title!!.toLowerCase(Locale.ROOT)
                                        .contains(charSearch.toLowerCase(Locale.ROOT)) ||
                                item.articleDescription!!.toLowerCase(Locale.ROOT)
                                        .contains(charSearch.toLowerCase(Locale.ROOT)) ||
                                item.content!!.toLowerCase(Locale.ROOT)
                                        .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(item)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as List<Articles>
                notifyDataSetChanged()
            }

        }
    }
}