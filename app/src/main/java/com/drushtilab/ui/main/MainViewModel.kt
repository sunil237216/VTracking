package com.drushtilab.ui.main

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.drushtilab.network.model.DropRequest
import com.drushtilab.network.model.ReportRequest
import com.drushtilab.network.model.locationSearch
import com.drushtilab.network.model.prequest
import com.drushtilab.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import retrofit2.adapter.rxjava2.Result.response
import android.R.string
import com.drushtilab.ui.base.BaseViewModel
import retrofit2.HttpException


class MainViewModel @Inject constructor(private val repo: Repository): BaseViewModel() {

    private val search = MutableLiveData<List<String>>()
    val status = MutableLiveData<Int>();
    val requestType = MutableLiveData<String>();
    val vehicleStatus = MutableLiveData<Int>();
    val location = MutableLiveData<Location>();
    val pickResponse = MutableLiveData<Int>();
    val dropResponse = MutableLiveData<Int>();
    val reportResponse = MutableLiveData<Boolean>();

      val isLoading = MutableLiveData<Boolean>()

    private val locationData = MutableLiveData<List<locationSearch>>();

    fun serach(str: String) {

        repo.search(str).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result?.let {
                        getSearchData().postValue(result)
                                            }

                },
                { error -> println(error) })
    }


    fun complete(request: prequest) {
        isLoading.postValue(true)
        repo.pickVehicle(request).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposable -> addToDisposable(disposable) }
            .subscribe({ result ->
                isLoading.postValue(false)
                result?.let {
                    pickResponse.postValue(200)
                    isLoading.postValue(false)

                }
            },
                { error ->
                    val error = error as HttpException
                    pickResponse.postValue(error.response().code())

                    isLoading.postValue(false)
                  //  pickResponse.postValue(error)
                })
    }

    fun completeDrop(request: DropRequest) {
        isLoading.postValue(true)
        repo.dropVehicle(request).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->

                    //  getSearchData().postValue(result)
                        isLoading.postValue(false)

                    dropResponse.postValue(200)

                    isLoading.postValue(false)
            },
                { error ->
                    isLoading.postValue(false)
                    val error = error as HttpException
                    dropResponse.postValue(error.response().code())
                })

    }

    fun sendReport(request: ReportRequest) {
        isLoading.postValue(true)
        repo.sendReport(request).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                result?.let {
                    reportResponse.postValue(true)
                    //  getSearchData().postValue(result)
                    isLoading.postValue(false)
                }.also {
                    print("result is null")
                }

            },
                { error -> println(error)
                    reportResponse.postValue(false)})

    }

    fun serachLocation(winno: String) {
        isLoading.postValue(true)
        repo.searchLocation(winno).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result?.let {
                        getLocationData().postValue(result)
                        isLoading.postValue(false)

                    }.also {
                        print("result is null")
                    }
                },
                { error -> println(error) })
    }

    fun pickupdropStatus() {
        isLoading.postValue(true)
        repo.vehicleStatus().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result?.let {
                        vehicleStatus.postValue(result.Status)
                        isLoading.postValue(false)
                    }
                },
                { error -> println(error) })
    }

    fun getSearchData() = search

    fun getLocationData()= locationData;

}