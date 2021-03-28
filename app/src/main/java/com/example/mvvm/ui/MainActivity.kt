package com.example.mvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val repoViewModel: RepoViewModel by viewModels()
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var binding: ActivityMainBinding
    private var searchJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        repoAdapter = RepoAdapter()
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvRepos.layoutManager = LinearLayoutManager(this)
        binding.rvRepos.addItemDecoration(decoration)
        binding.rvRepos.adapter =
            repoAdapter.withLoadStateHeaderAndFooter(
                header = ReposLoadStateAdapter { repoAdapter.retry() },
                footer = ReposLoadStateAdapter { repoAdapter.retry() }
            )




        repoAdapter.addLoadStateListener { loadState ->

            binding.progress.isVisible = loadState.mediator?.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }



        searchJob?.cancel()
        searchJob = lifecycleScope.launch{
            repoViewModel.repos.collectLatest {
                repoAdapter.submitData(it)
            }
        }

        Log.e("dsdsdsd","${repoViewModel.net}")
    }
}