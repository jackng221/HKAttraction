package shape.computing.hkattraction

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

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

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "GPS")
        enableMyLocation()

        val centralPier = LatLng(22.286326204396815, 114.16138429927625)
        map.addMarker(MarkerOptions().position(centralPier).title("Central Pier"))
        map.moveCamera(CameraUpdateFactory.newLatLng(centralPier))
        map.setMinZoomPreference(12F)
    }


    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted:Boolean ->
        if (isGranted){
            Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
        }
        else if (!isGranted){
            Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    fun requestPermission(permission:String, name:String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(permission)){
                rationaleRequest(permission, name)
            }
            else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    fun rationaleRequest(permission:String, name:String){
        val builder = AlertDialog.Builder(this)
        builder.apply{
            setMessage("$name is required, press OK to give permission")
            setTitle("Permission required")
            setPositiveButton("OK"){dialog, which -> requestPermissionLauncher.launch(permission)}
            setNegativeButton("Cancel"){dialog, which -> }
        }
        val dialog = builder.create()
        dialog.show()
    }
}