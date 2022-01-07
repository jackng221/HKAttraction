package shape.computing.hkattraction

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ibMaps = findViewById<ImageButton>(R.id.ibMaps)
        ibMaps.setOnClickListener(){
            startMapsActivity()
        }

        setSupportActionBar(findViewById(R.id.my_toolbar))
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