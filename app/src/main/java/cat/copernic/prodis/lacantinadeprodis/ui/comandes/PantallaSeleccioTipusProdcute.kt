package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.PantallaSeleccioTipusProducte_Adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.ArrayList

class PantallaSeleccioTipusProdcute : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var producteList: ArrayList<String>
    private lateinit var Padaper: PantallaSeleccioTipusProducte_Adapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )

        recyclerView = binding.recyclerViewSeleccioProducte
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        producteList = arrayListOf()

        db.collection("productes").document("categories").get().addOnSuccessListener{ document ->
            producteList.add(document.get("bCalenta").toString())
            producteList.add(document.get("bFreda").toString())
            producteList.add(document.get("bocata").toString())

            Padaper = PantallaSeleccioTipusProducte_Adapter(producteList)

            recyclerView.adapter = Padaper

        }


        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController().navigate(PantallaSeleccioTipusProdcuteDirections.actionPantallaSeleccioTipusProducteToPantallaSeleccioBegudaFreda())
        }
        return binding.root
    }

}
