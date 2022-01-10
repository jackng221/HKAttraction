import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import shape.computing.hkattraction.R

class RecyclerAdapter (private val dbHelper: AttractionDbHelper, val context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgButton: ImageButton = view.findViewById(R.id.imageButton)
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE).toString()

        when (dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_CUSTOM_IMG_DIRECTORY).toString()){
            "" -> {
                val name = dbHelper.getData(position + 1, AttractionDbHelper.AttractionEntry.COLUMN_NAME_DEFAULT_IMG).toString()
                val img = ResourcesCompat.getDrawable(context.resources, context.resources.getIdentifier(name, "drawable", context.packageName), null)
                holder.imgButton.setImageDrawable(img)
            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return dbHelper.getSize()
    }
}