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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()
        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)

        val centralPier = LatLng(22.286326204396815, 114.16138429927625)
        map.addMarker(MarkerOptions().position(centralPier).title("Central Pier"))
        map.moveCamera(CameraUpdateFactory.newLatLng(centralPier))
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