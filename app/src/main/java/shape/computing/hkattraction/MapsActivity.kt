package shape.computing.hkattraction

import AttractionDbHelper
import PermissionHandler
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var location: String? = ""
    private var position: Int = 0
    private var uri: Uri? = null

    private lateinit var map: GoogleMap
    private val dbHelper = AttractionDbHelper(this)
    private val permissionHandler = PermissionHandler(this)
    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ isSuccess ->
        if (isSuccess){
            Toast.makeText(applicationContext, "Photo saved", Toast.LENGTH_SHORT).show()
            dbHelper.updateData(position, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, uri.toString())
        }
        else {
            Toast.makeText(applicationContext, "Error: Photo not saved", Toast.LENGTH_SHORT).show()
        }
    }

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
        position = intent.getIntExtra("position", 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()
        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)

        val latLng = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(latLng).title(location))
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(22.38489484051435, 114.1160620127353))) // center of HK
        map.setMinZoomPreference(9.8F)
        map.setPadding(0, 150, 0, 0)
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true;
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            permissionHandler.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, "GPS", true)
        }
    }

    override fun onMyLocationClick(location:Location) {

    }
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_camera -> {
            permissionHandler.getPermission(android.Manifest.permission.CAMERA, "Camera", false)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

                    println("TEST")

                    val fileName = location?.replace(" ", "_", false) + "_custom_"
                    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val tempFile = File.createTempFile(fileName, ".png", storageDir).apply{
                        createNewFile()
                        //deleteOnExit()
                    }
                    uri = FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tempFile)
                    cameraResultLauncher.launch(uri)
                }
            }
/*            else {
                permissionHandler.getPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, "read external", false)
                permissionHandler.getPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "write external", false)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        registerForActivityResult(ActivityResultContracts.TakePicture()){

                        }
                    }
            }*/
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}