import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import shape.computing.hkattraction.MapsActivity
import shape.computing.hkattraction.R
import java.io.InputStream


class RecyclerAdapter(private val dbHelper: AttractionDbHelper, private val context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    // set item views (elements)
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgButton: ImageButton = view.findViewById(R.id.imageButton)
        val textView: TextView = view.findViewById(R.id.textView)
    }
    // set items count
    override fun getItemCount(): Int {
        return dbHelper.getSize()
    }
    // apply item_grid_xml to items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }
    // set item contents
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // set name
        val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE).toString()
        holder.textView.text = name
        // if database has no custom image data, use default image as item image
        // else, use custom image (uri)
        when (dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI).toString()){
            "" -> {
                val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG).toString()
                val img = ResourcesCompat.getDrawable(context.resources, context.resources.getIdentifier(name, "drawable", context.packageName), null)
                holder.imgButton.setImageDrawable(img)
            }
            else -> {
                try{
                    val uri = Uri.parse(dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_URI).toString())
                    // check if file exists
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    inputStream?.close()
                    holder.imgButton.setImageURI(uri)
                }
                catch (e: Exception){
                    val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG).toString()
                    val img = ResourcesCompat.getDrawable(context.resources, context.resources.getIdentifier(name, "drawable", context.packageName), null)
                    holder.imgButton.setImageDrawable(img)
                }
            }
        }
        // on clicking item image, start map activity with data respective to the item (attraction)
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
}