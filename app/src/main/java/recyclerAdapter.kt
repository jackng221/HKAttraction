import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import shape.computing.hkattraction.R

class recyclerAdapter (private val dbHelper: AttractionDbHelper): RecyclerView.Adapter<recyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgButton: ImageButton
        val textView: TextView
        init{
            imgButton = view.findViewById(R.id.imageButton)
            textView = view.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dbHelper.getData(position, AttractionDbHelper.AttractionEntry.COLUMN_NAME_TITLE).toString()
    }

    override fun getItemCount(): Int {
        return dbHelper.getSize()
    }
}