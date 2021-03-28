package com.example.mvvm.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mvvm.db.Repo
import com.example.mvvm.api.ApiService
import com.example.mvvm.db.RepoDatabase
import retrofit2.HttpException
import java.io.IOException

private const val REPO_START_INDEX = 1

class ReposPagingSource(private val service: ApiService, private val dataBase: RepoDatabase) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {

            val position = params.key ?: REPO_START_INDEX
            val response = service.getRepos(position, params.loadSize)
            val repos = response.body()
            if (repos != null) {
                dataBase.reposDao().insertAll(repos)
            }

            LoadResult.Page(

                data = repos ?: listOf(),
                prevKey = if (position == REPO_START_INDEX) null else position - 1,
                nextKey = if (repos?.isEmpty()!!) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        TODO("Not yet implemented")
    }


}