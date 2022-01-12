package cat.copernic.prodis.lacantinadeprodis.ui.caixa

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.seleccio_resum_comanda_adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaResumComandesBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioResumComandaBinding
import cat.copernic.prodis.lacantinadeprodis.model.dtclss_seleccio_resum_comanda
import com.google.firebase.firestore.FirebaseFirestore

class PantallaSeleccioResumComanda : Fragment() {

    private lateinit var recycerView: RecyclerView
    private lateinit var comandaList: ArrayList<dtclss_seleccio_resum_comanda>
    private lateinit var seleccioResumComandaAdpt: seleccio_resum_comanda_adapter
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SwitchIntDef")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioResumComandaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_resum_comanda, container, false
        )

        recycerView = binding.rcyclrVwSeleccioComandes!!
        val gridLayout = GridLayoutManager(this.context, 2)
        //recycerView.layoutManager = gridLayout

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                recycerView.layoutManager = gridLayout
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                recycerView.layoutManager =
                    GridLayoutManager(this.context, 2, RecyclerView.HORIZONTAL, false)
            }
            Configuration.ORIENTATION_UNDEFINED -> {
                recycerView.layoutManager =
                    GridLayoutManager(this.context, 2, RecyclerView.HORIZONTAL, false)
            }
        }

        comandaList = arrayListOf()
        recycerView.setHasFixedSize(true)

        seleccioResumComandaAdpt = seleccio_resum_comanda_adapter(comandaList)

        recycerView.adapter = seleccioResumComandaAdpt

        eventChangeListener()


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun eventChangeListener() {
        db.collection("comandes").get().addOnSuccessListener { result ->
            comandaList.clear()
            for (document in result) {
                if (document.get("visible").toString() == "true" && document.get("comandaPagada")
                        .toString() == "false"
                ) {
                    val nom = document.get("user").toString()
                    val comandaId = document.get("comandaId").toString()
                    val documentId = document.id
                    val preuTotal: Double = document.get("preuTotal").toString().toDouble()
                    comandaList.add(dtclss_seleccio_resum_comanda(nom, comandaId, documentId, preuTotal))
                }
            }
            seleccioResumComandaAdpt.notifyDataSetChanged()
        }
    }
}