package com.example.mvvm.api

import com.example.mvvm.db.Repo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {


    @GET("/users/JakeWharton/repos")
    suspend fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<List<Repo>>

}