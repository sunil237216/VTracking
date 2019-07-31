package com.drushtilab.ui.login
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.drushtilab.databinding.LoginActivityBinding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.drushtilab.R
import com.drushtilab.ui.base.BaseActivity
import com.drushtilab.ui.main.MainActivity
import com.drushtilab.utils.CustomProgressDialog
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginActivity : BaseActivity(){


    private lateinit var binding: LoginActivityBinding;
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnlogin.setOnClickListener {

            if (!binding.username.text.isEmpty() && !binding.password.text.isEmpty()) {
                loginViewModel.login(binding.username.text.toString(), binding.password.text.toString());
            }
            else{
                fireToast("Username and password should not be empty")
            }

        }

        loginViewModel.getLoginLiveData().observe(this, Observer {repsonse ->

           if(repsonse.Token != null)
           {
               val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
           }

        })

        loginViewModel.isLoading.observe(this, Observer {

            if(it == true)
            {
                showDialog()
            }
            else if(it == false){
                hideDialog()
            }
        })
        loginViewModel.onError.observe(this, Observer {

            if(it == 403)

                fireToast("Wrong username or password");
        })
    }

}

