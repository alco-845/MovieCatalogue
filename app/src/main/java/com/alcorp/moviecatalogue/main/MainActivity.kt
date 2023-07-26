package com.alcorp.moviecatalogue.main

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.core.data.Resource
import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.core.ui.MovieAdapter
import com.alcorp.moviecatalogue.core.utils.LoadingDialog
import com.alcorp.moviecatalogue.databinding.ActivityMainBinding
import com.alcorp.moviecatalogue.detail.DetailMovieActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var txtSearch: String = ""
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.refreshHome.setOnRefreshListener(this)
        loadData("load")
    }

    private fun loadData(condition: String) {
        val loadingDialog = LoadingDialog(this)

        val movieAdapter = MovieAdapter()
        movieAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailMovieActivity::class.java)
            intent.putExtra(DetailMovieActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        val scheme: LiveData<Resource<List<Movie>>> =
            if (condition == "load") {
                mainViewModel.movie
            } else {
                mainViewModel.searchMovie(txtSearch)
            }

        scheme.observe(this) { movie ->
            if (movie != null) {
                when (movie) {
                    is Resource.Loading -> loadingDialog.showLoading(true)
                    is Resource.Success -> {
                        loadingDialog.showLoading(false)
                        movieAdapter.setData(movie.data)
                    }
                    is Resource.Error -> {
                        loadingDialog.showLoading(false)
                        Toast.makeText(this, movie.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(binding.rvHome) {
            layoutManager = StaggeredGridLayoutManager(2, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    loadData("load")
                } else {
                    txtSearch = query
                    loadData("search")
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val uri = Uri.parse("moviecatalogue://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        loadData("load")
        binding.refreshHome.isRefreshing = false
    }

    private fun registerBroadCastReceiver() {
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        Toast.makeText(this@MainActivity, getString(R.string.toast_power_connected), Toast.LENGTH_SHORT).show()
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        Toast.makeText(this@MainActivity, getString(R.string.toast_power_disconnected), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadCastReceiver()
    }
}