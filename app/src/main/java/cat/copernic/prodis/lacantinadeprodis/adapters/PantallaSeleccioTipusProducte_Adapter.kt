package cat.copernic.prodis.lacantinadeprodis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.PantallaSeleccioTipusProducte_Adapter.*

class PantallaSeleccioTipusProducte_Adapter(private val producteList: ArrayList<String>) :
    RecyclerView.Adapter<ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val nomProducte: String = producteList[i]
        viewHolder.itemNom.text = nomProducte
        viewHolder.imageView.setImageResource(R.drawable.arrow_back_foreground)


       /* if(i == 0){
            viewHolder.itemNom.setOnClickListener{
                viewB
            }
            viewHolder.imageView.setOnClickListener {
                viewB
            }
        }else if(i == 1){

        }else if(i == 2){

        }*/


    }

    override fun getItemCount(): Int {
        return producteList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val itemNom: TextView = itemView.findViewById(R.id.item_nomTipusProducte)
    }

}