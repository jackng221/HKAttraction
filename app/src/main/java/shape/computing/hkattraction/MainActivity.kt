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

    private fun resetPictures(){
        for (i in 1..dbHelper.getSize()){
            dbHelper.updateData(i, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI, "")
        }
        finish()
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = RecyclerAdapter(dbHelper, this)
        contentResolver
    }
    override fun onResume() {
        super.onResume()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter?.notifyDataSetChanged()
        if (sensor != null){
            sensorManager.requestTriggerSensor(triggerListener, sensor)
        }
    }
    override fun onPause() {
        super.onPause()
        if (sensor != null) {
            sensorManager.cancelTriggerSensor(triggerListener, sensor)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
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