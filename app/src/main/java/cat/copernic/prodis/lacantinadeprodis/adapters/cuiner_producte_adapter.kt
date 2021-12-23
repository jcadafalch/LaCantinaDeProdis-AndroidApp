package cat.copernic.prodis.lacantinadeprodis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner_producte
import com.google.firebase.firestore.FirebaseFirestore

class cuiner_producte_adapter(private val producteList: ArrayList<dtclss_cuiner_producte>): RecyclerView.Adapter<cuiner_producte_adapter.ViewHolder>(){

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.comandes_producte_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val producte: dtclss_cuiner_producte = producteList[i]
        db.collection("productes").document(producte.idProducte.toString()).get().addOnSuccessListener { document->
            val type = document.get("tipus")
            when(type){
                // Si es de tipus bocata ....
                "Bocates" -> {
                    viewHolder.linLayBeguda.visibility = View.GONE
                    viewHolder.linLayBocata.visibility = View.VISIBLE
                    viewHolder.nomBocata.text = document.get("nom").toString()
                    viewHolder.chckBxTomaquet.isChecked = producte.tomaquet == true
                    viewHolder.chckBxEmportarBocata.isChecked = producte.emportar == true
                }
                // Si no es de tipus bocata (per tant serà beguda)
                else -> {
                    viewHolder.linLayBeguda.visibility = View.VISIBLE
                    viewHolder.linLayBocata.visibility = View.GONE
                    viewHolder.nomBeguda.text = document.get("nom").toString()
                    when(producte.sucre){
                        "SS" -> viewHolder.rdBttn.text = R.string.sense_sucre.toString()
                        "SB" -> viewHolder.rdBttn.text = R.string.sucre_blanc.toString()
                        "SM" -> viewHolder.rdBttn.text = R.string.sucre_more.toString()
                        "SC" -> viewHolder.rdBttn.text = R.string.sacarina.toString()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return producteList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //Secció de bocata
        val linLayBocata: LinearLayout = itemView.findViewById(R.id.linLay_bocata)
        val nomBocata: TextView = itemView.findViewById(R.id.nomBocata)
        val chckBxTomaquet: CheckBox = itemView.findViewById(R.id.chckBx_tomaquet)
        val chckBxEmportarBocata: CheckBox = itemView.findViewById(R.id.chckBx_emportar_bocata)

        //Secció de beguda
        val linLayBeguda: LinearLayout = itemView.findViewById(R.id.linLay_beguda)
        val nomBeguda: TextView = itemView.findViewById(R.id.nomBeguda)
        val rdBttn: RadioButton = itemView.findViewById(R.id.rdBttn_sucre)
        val chckBxEmportarBeguda: CheckBox = itemView.findViewById(R.id.chckBx_emportar_beguda)
    }
}