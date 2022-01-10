package shape.computing.hkattraction

import PermissionHandler
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var map: GoogleMap
    private val permissionHandler = PermissionHandler(this)
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var location: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lat = intent.getDoubleExtra("latitude", 0.0)
        lng = intent.getDoubleExtra("longitude", 0.0)
        location = intent.getStringExtra("locationName")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()
        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)

        val latLng = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(latLng).title(location))
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.setMinZoomPreference(12F)
        map.setPadding(0, 150, 0, 0)
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true;
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            permissionHandler.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, "GPS")
        }
    }

    override fun onMyLocationClick(location:Location) {

    }
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }
}