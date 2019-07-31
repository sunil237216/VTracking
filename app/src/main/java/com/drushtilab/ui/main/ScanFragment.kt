package com.drushtilab.ui.main
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drushtilab.R
import android.content.Intent
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.drushtilab.databinding.ScanningFragmentBinding
import com.drushtilab.utils.barcode.BarcodeCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ScanFragment : Fragment() {

    private lateinit var binding: ScanningFragmentBinding;
    private var mainViewModel:MainViewModel?=null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val BARCODE_READER_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blank, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mainViewModel = ViewModelProviders.of(it,viewModelFactory).get(MainViewModel::class.java)
        }

        binding.imageScan.setOnClickListener {

            val intent = Intent(activity, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE)
        }

        binding.complete.setOnClickListener{

            if(binding.qrvalue.text.length == 35) {
                (activity as MainActivity).complete(binding.qrvalue.text.toString(), binding.txtreport.text.toString())
                (activity as MainActivity).onBackPressed();
            }
            else{
                (activity as MainActivity).fireToast("Please enter valid win no")
            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    val barcode = data.getStringExtra((BarcodeCaptureActivity.BarcodeObject))
                    //val p = barcode.cornerPoints
                   binding.qrvalue.setText(barcode)

                } else
                    binding.qrvalue.setText(R.string.no_barcode_captured)
            } else
               Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

}
