package com.nehaprakash.aglapplication

import com.nehaprakash.aglapplication.model.People
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

// Interface to hit the call to the network
interface GetResponse {
    @GET("/people.json")
    fun getResponse() : Observable<List<People>>

}