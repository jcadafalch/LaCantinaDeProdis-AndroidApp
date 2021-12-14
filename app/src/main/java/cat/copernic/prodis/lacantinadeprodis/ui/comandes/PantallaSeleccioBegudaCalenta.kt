package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.bcalenta_adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioBegudaCalentaBinding
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_productes
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlin.math.truncate

class PantallaSeleccioBegudaCalenta : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productesArrayList: ArrayList<dtclss_productes>
    private lateinit var bcalentaAdapter: bcalenta_adapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaSeleccioBegudaCalentaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_beguda_calenta, container, false
        )
        binding.backButton.setOnClickListener{
            view?.findNavController()?.navigate(PantallaSeleccioBegudaCalentaDirections.
            actionPantallaSeleccioBegudaCalentaToPantallaSeleccioTipusProducte())
        }

        recyclerView = binding.rcyclrVwBcalenta
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        productesArrayList = arrayListOf()

        bcalentaAdapter = bcalenta_adapter(productesArrayList)

        recyclerView.adapter = bcalentaAdapter

        eventChangeListener()

        return binding.root
    }

    private fun eventChangeListener() {
        db.collection("productes").addSnapshotListener(object : EventListener<QuerySnapshot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                db.collection("productes").get().addOnSuccessListener { result ->
                    for (document in result){
                        if (document.id != "categories"){
                            if (document.get("tipus") == "Beguda Calenta" && document.get("visible") == true){
                                productesArrayList.add(document.toObject(dtclss_productes::class.java))
                            }
                        }
                    }

                    bcalentaAdapter.notifyDataSetChanged()
                }
            }

        })
    }
}