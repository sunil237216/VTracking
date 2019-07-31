package com.drushtilab.ui.main
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.beust.klaxon.*
import com.drushtilab.R
import com.drushtilab.databinding.MapFragmentBinding
import com.drushtilab.network.model.locationSearch
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL
import javax.inject.Inject

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var  strtext:String = "";
    private lateinit var binding:MapFragmentBinding
    private lateinit var locationSearch: locationSearch;
    private var mainViewModel:MainViewModel?=null


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       strtext = arguments!!.getString("win")
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
         val mapFragment = getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
       mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mainViewModel = ViewModelProviders.of(it,viewModelFactory).get(MainViewModel::class.java)
        }

          mainViewModel?.serachLocation(strtext)

        mainViewModel?.getLocationData()?.observe(this, Observer {


          if(it.size > 0) {
              locationSearch = it[0];
              //  mMap = googleMap!!
              println(locationSearch)
              val pick = LatLng(locationSearch.P_Lat, locationSearch.P_Long)
              val drop = LatLng(locationSearch.D_Lat, locationSearch.D_Long)


              if (::mMap.isInitialized) {
                  mMap.addMarker(MarkerOptions().position(pick).title("Pick at " + locationSearch.ZoneName))
                  mMap.addMarker(MarkerOptions().position(drop).title("Drop at " + locationSearch.ZoneName))
                  mMap.moveCamera(CameraUpdateFactory.newLatLng(pick))
                  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pick, 15f))

                  val line: Polyline = mMap.addPolyline(
                      PolylineOptions()
                          .add(pick, drop)
                          .width(5f)
                          .color(Color.RED)
                  );
              }
          }
           // drawpolyLine(pick,drop)

        })
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    private fun drawpolyLine(pick:LatLng,drop:LatLng)
    {
        val LatLongB = LatLngBounds.Builder()
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)

        // build URL to call API
        val url = getURL(pick, drop)

        async {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            uiThread {
                // When API call is done, create parser and convert into JsonObjec
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }
                // Add  points to polyline and bounds
                options.add(pick)
                LatLongB.include(pick)
                for (point in polypts)  {
                    options.add(point)
                    LatLongB.include(point)
                }
                options.add(drop)
                LatLongB.include(drop)
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                mMap!!.addPolyline(options)
                // show map with route centered
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // declare bounds object to fit whole route in screen
    }
        fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

}
