package shape.computing.hkattraction

import AttractionDbHelper
import PermissionHandler
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    // variables relevant to the attraction
    private var lat: Double = 0.0       // coordinate
    private var lng: Double = 0.0       // ^
    private var location: String = ""   // name
    private var position: Int = 0       // position in database
    private var uri: Uri = Uri.EMPTY    // uri of custom image

    private lateinit var map: GoogleMap
    private val dbHelper = AttractionDbHelper(this)
    private val permissionHandler = PermissionHandler(this)
    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ isSuccess ->
        if (isSuccess){ // if photo is successfully saved
            Toast.makeText(applicationContext, "Photo saved", Toast.LENGTH_SHORT).show()
            // update uri in database with uri of the photo taken
            dbHelper.updateData(
                position,
                AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI,
                uri.toString()
            )
        }
        else {
            Toast.makeText(applicationContext, "Error: Photo not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set layout and action bar
        setContentView(R.layout.activity_maps)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setup google map (provided by map activity template)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // get attraction data from intent
        lat = intent.getDoubleExtra("latitude", 0.0)
        lng = intent.getDoubleExtra("longitude", 0.0)
        if(intent.getStringExtra("locationName") != null){
            location = intent.getStringExtra("locationName")!!
        }
        position = intent.getIntExtra("position", 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableMyLocation()
        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)
        // zoom to HK, add attraction marker
        val latLng = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(latLng).title(location))
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(22.38489484051435, 114.1160620127353))) // center of HK
        map.setMinZoomPreference(9.8F)
        // pad map to bottom of action bar
        map.setPadding(0, 150, 0, 0)
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // if permission acquired, enable GPS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true;
        } else {
            // request permission, reload activity (map) if permission is given
            permissionHandler.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, "GPS", true)
        }
    }

    override fun onMyLocationClick(location: Location) {

    }
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // apply menu_map.xml to action bar
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // take and store photo with camera app
        R.id.action_camera -> {
            // check permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                permissionHandler.getPermission(android.Manifest.permission.CAMERA, "Camera", false)
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // create file name from location name
                    val fileName = location.replace("[^A-Za-z0-9]".toRegex(), " ")?.trim()?.replace("\\s+".toRegex(), "_") + "_custom_"
                    val calendar = Calendar.getInstance()
                    val dateTime = calendar.get(Calendar.YEAR).toString() + (calendar.get(Calendar.MONTH) +1).toString() + calendar.get(Calendar.DAY_OF_MONTH).toString() +
                            calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND)
                    // set file uri and launch camera
                    // store to system images, persist after uninstall
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName$dateTime.jpg")
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    values.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES
                    )
                    if (contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) != null){
                        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
                        cameraResultLauncher.launch(uri)
                    }
                    else{
                        Toast.makeText(applicationContext, "Error: failed to create file uri", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}