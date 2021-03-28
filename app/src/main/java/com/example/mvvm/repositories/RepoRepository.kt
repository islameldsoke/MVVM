package com.example.mvvm.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mvvm.db.Repo
import com.example.mvvm.api.ApiService
import com.example.mvvm.db.RepoDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val service: ApiService,
    private val database: RepoDatabase
) {


    fun getRepos(fetchRemote: Boolean): kotlinx.coroutines.flow.Flow<PagingData<Repo>> {
        if (fetchRemote) {
            return Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    maxSize = 100,
                    enablePlaceholders = false,
                    initialLoadSize = NETWORK_PAGE_SIZE
                ),
                pagingSourceFactory = { ReposPagingSource(service, database) }
            ).flow
        } else {
            val pagingSourceFactory = { database.reposDao().repos() }
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 15
    }
}