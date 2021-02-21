package cu.wilb3r.news.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.databinding.FragmentDetailBinding
import cu.wilb3r.news.others.Constants.formatter2
import cu.wilb3r.news.others.RoundedCornersTransformation
import org.koin.android.ext.android.inject
import java.util.*

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var article: Articles
    private val args: DetailFragmentArgs by navArgs()
    private val glide: RequestManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = args.article
        }
        setHasOptionsMenu(false);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            source.text = "${article.source?.name}"
            title.text = "${article.title}"
            article.publishedAt!!.let {
                txtdate.text = formatter2.format(it)
            }
            article.urlToImage?.let {
                val sCorner = 15F
                val sMargin = 2
                glide.load(it)
                        .transform(RoundedCornersTransformation(context, sCorner, sMargin))
                        .into(image)
            }
            val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("${article.content}", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml("${article.content}")
            }
            val desc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("${article.articleDescription}", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml("${article.articleDescription}")
            }
            txtbody.text = article.articleDescription ?: content
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return
    }
}