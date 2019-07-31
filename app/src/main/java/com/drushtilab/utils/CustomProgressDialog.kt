package com.drushtilab.utils

import android.app.ProgressDialog
import android.content.Context
import android.widget.ProgressBar
import com.drushtilab.R
import javax.inject.Inject


class CustomProgressDialog(private val context: Context) {
    private var progressDialog: ProgressDialog? = null
    private var progressBar: ProgressBar? = null

    init {
        getProgressDialog()
    }

    private fun getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context, R.style.MyTheme)
        }
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall)

    }

    fun showDialog() {
        progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        progressDialog!!.show()
        /* progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progressbar_layout);*/
    }

    fun cancelDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.cancel()
        }
    }
}
