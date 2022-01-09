package shape.computing.hkattraction

import AttractionDbHelper
import AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_DIRECTORY
import AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG
import AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE
import AttractionDbHelper.AttractionEntry.TABLE_NAME
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = AttractionDbHelper(this)

        val ibMaps = findViewById<ImageButton>(R.id.ibMaps)
        ibMaps.setOnClickListener(){
            val db = dbHelper.readableDatabase
            var cursor = db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
                )
            cursor.moveToFirst()
            println(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)))
            println(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE)))

        }

        setSupportActionBar(findViewById(R.id.my_toolbar))

        //val layoutManager = GridLayoutManager()
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