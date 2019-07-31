package com.drushtilab.repository

import android.content.SharedPreferences
import com.drushtilab.network.ApiInterface
import com.drushtilab.network.model.*
import com.drushtilab.utils.AppConstants
import com.drushtilab.utils.AppConstants.Companion.TOKEN
import com.google.android.gms.common.api.GoogleApiClient
import okhttp3.ResponseBody
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class Repository@Inject constructor(

    val apiClient: ApiInterface,val preferences: SharedPreferences
):AppConstants

{
    fun login(username:String,password:String):Observable<LoginResponse>
    {
        return apiClient.login(username,password)
    }

    fun savetoken(token:String)
    {
       preferences.edit().putString(TOKEN,token).apply()

    }

    fun search(str:String):Observable<List<String>>
    {
        return apiClient.search(preferences.getString(TOKEN,null),str)

    }
    fun searchLocation(winno:String):Observable<List<locationSearch>>
    {
        return apiClient.searchLocation(preferences.getString(TOKEN,null),winno)

    }
    fun pickVehicle(request:prequest):Observable<ResponseBody>
    {
        return apiClient.pickupinsert(preferences.getString(TOKEN,null),request)
    }
    fun dropVehicle(request:DropRequest):Observable<ResponseBody>
    {
        return apiClient.dropupinsert(preferences.getString(TOKEN,null),request)
    }
    fun sendReport(request:ReportRequest):Observable<ResponseBody>
    {
        return apiClient.reportinsert(preferences.getString(TOKEN,null),request)
    }

    fun vehicleStatus():Observable<StatusResponse>
    {
        return apiClient.pickupdropStatus(preferences.getString(TOKEN,null))
    }
}