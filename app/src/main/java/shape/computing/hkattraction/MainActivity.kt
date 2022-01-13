package shape.computing.hkattraction

import AttractionDbHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import RecyclerAdapter
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = RecyclerAdapter(dbHelper, this)
        contentResolver
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