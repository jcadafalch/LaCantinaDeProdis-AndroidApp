package cat.copernic.prodis.lacantinadeprodis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_productes

class bocates_adapter(private val productesList: ArrayList<dtclss_productes>): RecyclerView.Adapter<bocates_adapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): bocates_adapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val nomProducte: dtclss_productes = productesList[i]
        viewHolder.itemNom.text = nomProducte.nom
        viewHolder.imageView.setImageResource(R.drawable.arrow_back_foreground)
    }

    override fun getItemCount(): Int {
        return  productesList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val itemNom: TextView = itemView.findViewById(R.id.item_nomTipusProducte)
    }
}