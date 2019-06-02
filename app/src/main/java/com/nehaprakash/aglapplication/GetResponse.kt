package com.nehaprakash.aglapplication

import com.nehaprakash.aglapplication.model.People
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers


interface GetResponse {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("/people.json")
    fun getResponse() : Observable<List<People>>

}