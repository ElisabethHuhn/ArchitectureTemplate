package com.huhn.fetchrewards.datasource.remotedatasource

// Retrofit interface

import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.User
import retrofit2.http.GET


interface UserApiService {

    @GET("/hiring.json")
    suspend fun fetchUsers() : ArrayList<User>

}
