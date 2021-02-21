package cu.wilb3r.news.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import cu.wilb3r.news.R
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.databinding.FragmentHomeBinding
import cu.wilb3r.news.others.Constants.formatter
import org.koin.android.ext.android.inject
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var articles: ArrayList<Articles>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val vm:HomeViewModel by inject()
    private val glide: RequestManager by inject()
    private lateinit var articleAdapter: ArticlesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        suscribeObserver()
        initRecyclerView()
        vm.getEverything(formatter.format(Calendar.getInstance().time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.item_calendar -> {
                // Get Current Date
                val c = Calendar.getInstance();
                val mYear = c.get(Calendar.YEAR);
                val mMonth = c.get(Calendar.MONTH);
                val mDay = c.get(Calendar.DAY_OF_MONTH);
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        vm.getEverything(year.toString() + "-" + (monthOfYear + 1).toString() + "-" + (dayOfMonth))
                    }, mYear, mMonth, mDay
                )
                datePickerDialog.show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun suscribeObserver() {
        vm.articles.observe(viewLifecycleOwner, { resource ->
            println(resource)
            when {
                resource.status.isLoading() -> {
                    resource.data?.let {
                        //println("Loading $it")
                        articleAdapter.submitList(it)
                    }
                }
                resource.status.isSuccessful() -> {
                    resource.data?.let {
                        //println("Succefull $it")
                        articleAdapter.submitList(it)
                    }
                }
                resource.status.isError() -> {
                    resource.message?.let {
                        //Toast.makeText(this, it, Toast.LENGTH_SHORT)
                    }
                    resource.data?.let {
                        articleAdapter.submitList(it)
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        articleAdapter = ArticlesAdapter(requireActivity(), glide)
        binding.recycler.apply {
            adapter = articleAdapter.apply {
                setOnItemClickListener { view: View, position: Int ->
                    val article = articleAdapter.getItem(position)
                    when(view.id){
                        R.id.articleitem -> {
                            val direction =
                                HomeFragmentDirections.actionHomeFragment2ToDetailFragment(
                                    article
                                )
                            findNavController().navigate(direction)
                        }
                    }
                }
            }
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }


}