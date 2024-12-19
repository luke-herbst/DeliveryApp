package iu.c323.fall2024.afinal

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import iu.c323.fall2024.afinal.model.Restaurant

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var userLatLng: LatLng
    private lateinit var restaurantLatLng: LatLng
    private lateinit var restaurant: Restaurant

    private var destinationLat: Double = 0.0
    private var destinationLng: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve the latitude and longitude from arguments
        restaurant = arguments?.getParcelable<Restaurant>("restaurant")!!
        arguments?.let {
            val destinationLatString = it.getString("destinationLat")
            val destinationLngString = it.getString("destinationLng")
            if (destinationLatString != null) {
                destinationLat = destinationLatString.toDouble()
            }
            if (destinationLngString != null) {
                destinationLng = destinationLngString.toDouble()
            }
        }


        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        userLatLng = LatLng(destinationLat, destinationLng)
        restaurantLatLng = restaurant.location?.let { LatLng(it.latitude, restaurant.location!!.longitude) }!!
//        Log.d("MapFragment", "Latitude: ${restaurant.location!!.latitude}, Longitude: ${restaurant.location!!.longitude}")
        Log.d("MapFragment", "Latitude: ${restaurantLatLng.latitude}, Longitude: ${restaurantLatLng.longitude}")

        map.addMarker(MarkerOptions().position(userLatLng).title("Your Address"))
        map.addMarker(MarkerOptions().position(restaurantLatLng).title("Restaurant"))

        // Move the camera to show both markers
        val bounds = LatLngBounds.Builder()
            .include(userLatLng)
            .include(restaurantLatLng)
            .build()
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        // Draw a line between the two points
        drawLineBetweenPoints(userLatLng, restaurantLatLng)
    }

    private fun drawLineBetweenPoints(startLatLng: LatLng, endLatLng: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(startLatLng)
            .add(endLatLng)
            .width(5f)
            .color(Color.BLUE)
        map.addPolyline(polylineOptions)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
