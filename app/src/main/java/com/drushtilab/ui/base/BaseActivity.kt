package com.drushtilab.ui.base

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.drushtilab.R
import com.drushtilab.ui.login.LoginActivity
import com.drushtilab.utils.AppConstants
import com.drushtilab.utils.CustomProgressDialog

open class BaseActivity : AppCompatActivity(),AppConstants {

    private lateinit var customProgressDialog:CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
//
//     fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
//                             listener: View.OnClickListener) {
//
//        Toast.makeText(this, getString(mainTextStringId), Toast.LENGTH_LONG).show()
//    }

   fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun logoutAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to logout ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                 val intent = Intent(this,LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                //finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun fireToast(msg:String)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

   fun showDialog()
    {
        customProgressDialog= CustomProgressDialog(this)
        customProgressDialog.showDialog()
    }
   fun hideDialog()
    {
        //  customProgressDialog=new CustomProgressDialog(this);
        customProgressDialog.cancelDialog();
    }





}

