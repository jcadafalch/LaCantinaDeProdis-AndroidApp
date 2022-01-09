package cat.copernic.prodis.lacantinadeprodis.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner_producte
import com.google.firebase.firestore.FirebaseFirestore


class cuiner_adapter(private val comandesList: ArrayList<dtclss_cuiner>, val cntxt: Context) :
    RecyclerView.Adapter<cuiner_adapter.ViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var cuinerProducteAdapter: cuiner_producte_adapter
    private lateinit var producteArrayList: ArrayList<dtclss_cuiner_producte>

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.comandes_card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val comanda: dtclss_cuiner = comandesList[i]
        viewHolder.itemButtonIdComanda.text = comanda.comandaId
        viewHolder.itemButtonIdComanda.setOnClickListener {
            if (viewHolder.itemLinLay.visibility == 8) {
                viewHolder.itemLinLay.visibility = View.VISIBLE
            } else {
                viewHolder.itemLinLay.visibility = View.GONE
            }
        }

        viewHolder.itemNom.text = comanda.user

        viewHolder.itemButtonMinimitzar.setOnClickListener {
            viewHolder.itemLinLay.visibility = View.GONE
        }

        viewHolder.itemButtonConfirmar.setOnClickListener {
            comandaAcabada(viewHolder, i)
        }


        producteArrayList = arrayListOf()
        db.collection("comandes").get().addOnSuccessListener { result ->
            for (dc in result) {
                if (dc.get("comandaId").toString() == comanda.comandaId.toString() && dc.get("user")
                        .toString() == comanda.user.toString()
                ) {
                    db.collection("comandes").document(dc.id).collection("productes").get()
                        .addOnSuccessListener { document ->
                            for (doc in document) {
                                producteArrayList.add(doc.toObject(dtclss_cuiner_producte::class.java))
                            }
                            cuinerProducteAdapter.notifyDataSetChanged()
                        }
                }
            }
            val recyclerView = viewHolder.itemRcyclVw
            recyclerView.layoutManager = LinearLayoutManager(cntxt)
            recyclerView.setHasFixedSize(true)
            cuinerProducteAdapter = cuiner_producte_adapter(producteArrayList)
            recyclerView.adapter = cuinerProducteAdapter

        }
    }

    override fun getItemCount(): Int {
        return comandesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRcyclVw: RecyclerView = itemView.findViewById(R.id.rcyclrVwCuinerComanda)
        val itemNom: TextView = itemView.findViewById(R.id.itemProducte)
        val itemLinLay: LinearLayout = itemView.findViewById(R.id.LinLayVer)
        val itemButtonIdComanda: Button = itemView.findViewById(R.id.bttnIdComanda)
        val itemButtonMinimitzar: ImageView = itemView.findViewById(R.id.imgVwMinimitzar)
        val itemButtonConfirmar: ImageButton = itemView.findViewById(R.id.imgBtnConfirmar)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun comandaAcabada(viewHolder: ViewHolder, i: Int) {
        db.collection("comandes").get().addOnSuccessListener { document ->
            for (dc in document) {
                if (dc.get("comandaId").toString() == comandesList[i].comandaId && dc.get("user")
                        .toString() == comandesList[i].user
                ) {
                    if (dc.get("comandaPagada").toString() == "true" && dc.get("visible")
                            .toString() == "true"
                    ) {
                        db.collection("comandes").document(dc.id).update(
                            hashMapOf(
                                "preparat" to true,
                                "visible" to false
                            ) as Map<String, Any>
                        )
                        comandesList.removeAt(i)
                    } else {
                        db.collection("comandes").document(dc.id).update(
                            hashMapOf(
                                "preparat" to true
                            ) as Map<String, Any>
                        )
                        comandesList.drop(i)
                        println(comandesList)


                    }

                }
            }
        }


    }
}