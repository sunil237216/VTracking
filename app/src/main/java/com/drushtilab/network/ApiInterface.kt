package com.drushtilab.network

import com.drushtilab.network.model.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("Login/fetchtoken")
    fun login(@Header("UN") username:String, @Header("PD")  password:String): Observable<LoginResponse>

    @GET("Search/SearchWin")
    fun search(@Header("Authorization") token:String,@Query("winno") winno:String):Observable<List<String>>

    @GET("Search/Search")
    fun searchLocation(@Header("Authorization") token:String,@Query("winno") winno:String):Observable<List<locationSearch>>

    @GET("DropOff/CheckStatus")
    fun pickupdropStatus(@Header("Authorization") token:String):Observable<StatusResponse>

    @Headers("Accept: application/json")
    @POST("pickup/insert")
    fun pickupinsert(@Header("Authorization") token:String,@Body request: prequest):Observable<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("DropOff/Insert")
    fun dropupinsert(@Header("Authorization") token:String,@Body request: DropRequest):Observable<ResponseBody>

    @POST("Report/Insert")
    fun reportinsert(@Header("Authorization") token:String,@Body request: ReportRequest):Observable<ResponseBody>







}