import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.island_spotter.Place
import com.example.island_spotter.PlaceDetailActivity
import com.example.island_spotter.R

class PlaceAdapter(
    private val context: Context,
    private val placeList: List<Place>,
    private val onItemClick: (Place) -> Unit // Handle item clicks, e.g., to open details or maps
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.spot_list_layout, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]

        holder.spotName.text = place.placeName
        val imageResId = context.resources.getIdentifier(place.image_src, "drawable", context.packageName)
        holder.spotImage.setImageResource(imageResId)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                putExtra("PLACE_NAME", place.placeName)
                putExtra("PLACE_DESCRIPTION", place.description)
                putExtra("PLACE_IMAGE_SRC", place.image_src)
                putExtra("PLACE_MAP_URL", place.mapUrl)
            }
            context.startActivity(intent) // Start the detail activity
        }
    }


    override fun getItemCount(): Int = placeList.size

    // ViewHolder class to hold the references to the views inside each item (card)
    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val spotImage: ImageView = itemView.findViewById(R.id.spots_image)
        val spotName: TextView = itemView.findViewById(R.id.spots_name)
    }
}
