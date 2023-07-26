package com.alcorp.moviecatalogue.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.moviecatalogue.core.ui.MovieAdapter
import com.alcorp.moviecatalogue.detail.DetailMovieActivity
import com.alcorp.moviecatalogue.favorite.databinding.ActivityFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadKoinModules(favoriteModule)
        binding.refreshFavorite.setOnRefreshListener(this)
        loadData()
    }

    private fun loadData() {
        val tourismAdapter = MovieAdapter()
        tourismAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailMovieActivity::class.java)
            intent.putExtra(DetailMovieActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        favoriteViewModel.favoriteMovie.observe(this) { dataMovie ->
            tourismAdapter.setData(dataMovie)
            binding.tvEmpty.visibility =
                if (dataMovie.isNotEmpty()) View.GONE else View.VISIBLE
        }

        with(binding.rvFavorite) {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = tourismAdapter
        }
    }

    override fun onRefresh() {
        loadData()
        binding.refreshFavorite.isRefreshing = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}