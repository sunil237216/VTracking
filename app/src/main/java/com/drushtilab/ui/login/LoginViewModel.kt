package com.drushtilab.ui.login
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drushtilab.network.model.LoginResponse
import com.drushtilab.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject


class LoginViewModel@Inject constructor(
    var repository: Repository): ViewModel() {
    private val loginLiveData = MutableLiveData<LoginResponse>()
    val isLoading = MutableLiveData<Boolean>()
    val onError = MutableLiveData<Int>()
    fun login(username:String,pass:String)
    {
        isLoading.postValue(true)
        repository.login(username,pass).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result.Token?.let {
                        repository.savetoken(it);
                        isLoading.postValue(false)
                        getLoginLiveData().postValue(result);
                    }
                },
                { error ->
                    isLoading.postValue(false)
                    val error = error as HttpException
                    onError.postValue(error.response().code())
                    println(error)})
    }

    fun getLoginLiveData() = loginLiveData

}