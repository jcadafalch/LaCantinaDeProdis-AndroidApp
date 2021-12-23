package cat.copernic.prodis.lacantinadeprodis.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_cuiner_producte
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class cuiner_adapter(private val comandesList: ArrayList<dtclss_cuiner>) :
    RecyclerView.Adapter<cuiner_adapter.ViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var cuinerProducteAdapter: cuiner_producte_adapter
    private lateinit var producteArrayList: ArrayList<dtclss_cuiner_producte>

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.comandes_card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val comanda: dtclss_cuiner = comandesList[i]
        viewHolder.itemButton.text = comanda.comandaId
        viewHolder.itemNom.text = comanda.user

        //RecyclerView
        val recyclerView = viewHolder.itemRcyclVw
        recyclerView.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        recyclerView.setHasFixedSize(true)
        producteArrayList = arrayListOf()
        cuinerProducteAdapter = cuiner_producte_adapter(producteArrayList)
        recyclerView.adapter = cuinerProducteAdapter

        eventChangeListener(comanda.comandaId.toString())

    }

    override fun getItemCount(): Int {
        return comandesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRcyclVw: RecyclerView = itemView.findViewById(R.id.rcyclrVwCuiner)
        val itemNom: TextView = itemView.findViewById(R.id.itemNom)
        val itemButton: Button = itemView.findViewById(R.id.bttnIdComanda)
    }

    private fun eventChangeListener(comandaId: String) {
        db.collection("comandes").addSnapshotListener(object : EventListener<QuerySnapshot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                db.collection("comandes").document(comandaId)
                    .collection("productes").get().addOnSuccessListener { result ->
                        for (document in result) {
                            producteArrayList.add(document.toObject(dtclss_cuiner_producte::class.java))
                        }
                        cuinerProducteAdapter.notifyDataSetChanged()
                    }
            }
        })
    }
}