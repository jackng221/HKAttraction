import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import shape.computing.hkattraction.MapsActivity
import shape.computing.hkattraction.R


class RecyclerAdapter (private val dbHelper: AttractionDbHelper, private val context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgButton: ImageButton = view.findViewById(R.id.imageButton)
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE).toString()
        holder.textView.text = name

        when (dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI).toString()){
            "" -> {
                val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG).toString()
                val img = ResourcesCompat.getDrawable(context.resources, context.resources.getIdentifier(name, "drawable", context.packageName), null)
                holder.imgButton.setImageDrawable(img)
            }
            else -> {

            }
        }

        holder.imgButton.setOnClickListener(){
            val intent = Intent(context, MapsActivity::class.java)
            val lat = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_LAT).toString().toDouble()
            val lng = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_LNG).toString().toDouble()
            intent.putExtra("latitude", lat)
            intent.putExtra("longitude", lng)
            intent.putExtra("locationName", name)
            intent.putExtra("position", position + 1)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dbHelper.getSize()
    }
}