package shape.computing.hkattraction

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION, "GPS")
        }
    }

    override fun onMyLocationClick(location:Location) {

    }
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    fun getPermission(permission:String, name:String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(permission)){
                rationaleRequest(permission, name)
            }
            else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
    //Dialog for permission request rationale
    fun rationaleRequest(permission:String, name:String){
        val builder = AlertDialog.Builder(this)
        builder.apply{
            setTitle("$name access denied")
            setMessage("press OK to give permission")
            setPositiveButton("OK"){dialog, which -> requestPermissionLauncher.launch(permission)}
            setNegativeButton("Cancel"){dialog, which -> }
        }
        val dialog = builder.create()
        dialog.show()
    }
    //Message feedback for permissionRequest
    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted:Boolean ->
        if (isGranted){
            Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
            finish();
            startActivity(intent);
        }
        else {
            Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}