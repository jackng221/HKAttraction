package shape.computing.hkattraction

import AttractionDbHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager


class MainActivity : AppCompatActivity() {
    private val dbHelper = AttractionDbHelper(this)
    //private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val layoutManager = GridLayoutManager(this, )

        val ibMaps = findViewById<ImageButton>(R.id.ibMaps)
        ibMaps.setOnClickListener(){
            println(dbHelper.getSize())
            for (i in 1..dbHelper.getSize()){
                println(dbHelper.getData(i, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE))
            }
        }
    }

    private fun startMapsActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
    private fun startSettingsActivity(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            startSettingsActivity()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}