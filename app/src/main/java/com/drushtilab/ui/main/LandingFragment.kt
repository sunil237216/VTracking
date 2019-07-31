package com.drushtilab.ui.main
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.drushtilab.databinding.LandingFragmentBinding
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_landing.*
import javax.inject.Inject
import android.text.Editable
import android.text.TextWatcher
import com.drushtilab.R
import com.drushtilab.ui.login.LoginActivity
import com.drushtilab.utils.AppConstants.Companion.DROP
import com.drushtilab.utils.AppConstants.Companion.PICK
import com.drushtilab.utils.AppConstants.Companion.REPORT
import com.drushtilab.utils.AppConstants.Companion.RFD
import com.drushtilab.utils.AppConstants.Companion.ROLL
import com.drushtilab.utils.replaceFragment


class LandingFragment : Fragment() {

    private lateinit var binding: LandingFragmentBinding;
    private var mainViewModel:MainViewModel?= null

    val emptyString ="";

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        activity?.let {
            mainViewModel = ViewModelProviders.of(it,viewModelFactory).get(MainViewModel::class.java)
        }

        mainViewModel?.pickResponse?.observe(this, Observer {

            if(it == 200)
            {
                (activity as MainActivity).fireToast("Vehicle picked Successfully");
            }
            else if(it == 406)
            {
                (activity as MainActivity).fireToast("Win no is not picked  up, kindly pick up");
            }

        })


        mainViewModel?.dropResponse?.observe(this, Observer {

            if(it == 200)
            {
                (activity as MainActivity).fireToast("Vehicle droped Successfully");
            }
            else if(it == 406)
            {
                (activity as MainActivity).fireToast("Win no is not dropped  up, kindly drop off");
            }

        })
        mainViewModel?.reportResponse?.observe(this, Observer {

            if(it == true)
                (activity as MainActivity).fireToast("Report sent successfully");
        })

        mainViewModel?.getSearchData()?.observe(this, Observer {

            println(it)
            val adapter = ArrayAdapter(activity,android.R.layout.simple_dropdown_item_1line,it)
            search.setAdapter(adapter)
           // adapter.notifydataChanged()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        // Inflate the layout for this fragment\
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                search.setText("")

             binding.search.setText("")


         mainViewModel?.pickupdropStatus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       // mainViewModel?.pickupdropStatus()
        binding.pick.setOnClickListener {
            mainViewModel?.status?.value = 1
            mainViewModel?.requestType?.value = PICK;
            (activity as MainActivity).replaceFragment()
        }
        binding.drop.setOnClickListener {
            mainViewModel?.status?.value = 2
            mainViewModel?.requestType?.value = DROP;
            (activity as MainActivity).replaceFragment()
        }
        binding.rfd.setOnClickListener {
            mainViewModel?.requestType?.value = RFD
            mainViewModel?.status?.value = 1

            (activity as MainActivity).replaceFragment()
        }
        binding.report.setOnClickListener {
            mainViewModel?.requestType?.value = REPORT
            mainViewModel?.status?.value = 4
            (activity as MainActivity).replaceFragment()
        }
        binding.fab.setOnClickListener {
            mainViewModel?.requestType?.value = ROLL
            mainViewModel?.status?.value = 3
            (activity as MainActivity).replaceFragment()
        }
        binding.logout.setOnClickListener {

            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //search.threshold = 2
        // Set an item click listener for auto complete text view
        search.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
             val fragment = MapFragment()
             val bundle = Bundle()
              bundle.putString("win", selectedItem)
               binding.search.setText("")
             fragment.setArguments(bundle);
            (activity as MainActivity).replaceFragment(fragment, R.id.container, "tag")

        }

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val text = search.text

                if(text.length > 1 && text.length<30) {
                    mainViewModel?.serach(text.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        search.setOnClickListener{

            (activity as MainActivity).replaceFragment()
        }
        search.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                // Display the suggestion dropdown on focus
                search.showDropDown()
            }
        }



    }

}
