package shape.computing.hkattraction

import AttractionDbHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import RecyclerAdapter
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    // sensor listener on phone shaken (significant motion), select a random attraction if available
    class TriggerListener(private val context: Context): TriggerEventListener(){
        private val dbHelper = AttractionDbHelper(context)
        override fun onTrigger(event: TriggerEvent?) {
            if (dbHelper.getSize() >= 1){
                val position = Random.nextInt(1, dbHelper.getSize())
                val intent = Intent(context, MapsActivity::class.java)
                val name = dbHelper.getData(position, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE).toString()
                val lat = dbHelper.getData(position, AttractionDbHelper.AttractionEntry.COLUMN_NAME_LAT).toString().toDouble()
                val lng = dbHelper.getData(position, AttractionDbHelper.AttractionEntry.COLUMN_NAME_LNG).toString().toDouble()
                intent.putExtra("latitude", lat)
                intent.putExtra("longitude", lng)
                intent.putExtra("locationName", name)
                intent.putExtra("position", position + 1)
                context.startActivity(intent)
            }
        }
    }
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var triggerListener = TriggerListener(this)
    private val dbHelper = AttractionDbHelper(this)

    // clear custom image from database and view
    private fun resetPictures(){
        // if any item in database(table)
        for (i in 1..dbHelper.getSize()){
            // clear custom uri (reference)
            dbHelper.updateData(i, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // update recyclerView items
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set layout and action bar
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        // setup significant motion sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
        // setup recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = RecyclerAdapter(dbHelper, this)
    }
    override fun onResume() {
        super.onResume()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // update recyclerView items
        recyclerView.adapter?.notifyDataSetChanged()
        // start monitoring significant motion
        if (sensor != null){
            sensorManager.requestTriggerSensor(triggerListener, sensor)
        }
    }
    override fun onPause() {
        super.onPause()
        // stop monitoring significant motion
        if (sensor != null) {
            sensorManager.cancelTriggerSensor(triggerListener, sensor)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // apply menu_main.xml to action bar
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // show dialog box and call resetPictures() if positive
        R.id.action_reset_pic -> {
            val builder = AlertDialog.Builder(this)
            builder.apply{
                setTitle("Warning!")
                setMessage("Reset to default pictures?")
                setPositiveButton("OK"){dialog, which -> resetPictures()}
                setNegativeButton("Cancel"){dialog, which -> }
            }
            val dialog = builder.create()
            dialog.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}