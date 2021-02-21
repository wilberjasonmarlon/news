package cu.wilb3r.news.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import cu.wilb3r.news.R
import cu.wilb3r.news.data.entities.Articles
import cu.wilb3r.news.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var back: Boolean = false
    private val glide: RequestManager by inject()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val customTarget = object: CustomTarget<Bitmap>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?
        ) {
            val drawable: Drawable = BitmapDrawable(resource)
            binding.toolbar.logo = drawable
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            binding.toolbar.logo = placeholder
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(
            binding.toolbar,
            findNavController(R.id.nav_host_fragment)
        )

          navController = findNavController(R.id.nav_host_fragment)
          appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            if (destination.id == R.id.detailFragment) {
                binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_back)
                val article = arguments?.get("article") as Articles
                article.urlToImage.let { url ->
                    glide.asBitmap().apply {
                            load(url)
                                .apply(
                                    RequestOptions().override(200, 200).circleCrop()
                                )
                                .into(customTarget)
                        }
                }
                article.author!!.let { author ->
                    if (author.isNotEmpty()){
                        binding.toolbar.title = getString(R.string.author)
                        binding.toolbar.subtitle = author
                    }
                }
            } else {
                resetToolBar()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    override fun onBackPressed() {
        glide.clear(customTarget)
       // resetToolBar()
        val navController = findNavController(R.id.nav_host_fragment)
        when (navController.graph.startDestination) {
            navController.currentDestination?.id -> {
                if (back) {
                    super.onBackPressed()
                    overridePendingTransition(0, 0)
                    return
                }
                back = true
                Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show()
                android.os.Handler(Looper.getMainLooper()).postDelayed({ back = false }, 2000)
            }
            else -> super.onBackPressed()
        }
    }

   private fun resetToolBar(){
        binding.toolbar.apply {
            logo = null
            title = null
            subtitle = null
        }
    }

}