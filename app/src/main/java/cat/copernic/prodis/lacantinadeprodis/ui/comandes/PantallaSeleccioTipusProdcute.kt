package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.PantallaSeleccioTipusProducte_Adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding
import cat.copernic.prodis.lacantinadeprodis.model.dataclass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import kotlin.collections.ArrayList

class PantallaSeleccioTipusProdcute : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var producteList: ArrayList<dataclass>
    private lateinit var Padaper: PantallaSeleccioTipusProducte_Adapter
    private val db = FirebaseFirestore.getInstance()
    private var arrUser = java.util.ArrayList<String>()
    private val strg = FirebaseStorage.getInstance().getReference()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )

        val currentUser =
            FirebaseAuth.getInstance().currentUser

        var dni: String

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (currentUser?.email.toString() == document.get("email").toString()) {
                    dni = document.id

                    var num: Int = 0

                    db.collection("comandes").get().addOnSuccessListener { result ->
                        for (document in result) {
                            if (!document.id.equals(num.toString())) {
                                db.collection("comandes").document(num.toString()).set(
                                    hashMapOf(
                                        "user" to dni,
                                        "comandaPagada" to false,
                                        "visible" to false,
                                        "comandaId" to num.toString(),
                                        "date" to Timestamp.from(Instant.now()),
                                        "preuTotal" to 0,
                                        "preparat" to false
                                    ) as Map<String, Any>
                                )
                                break

                            } else {
                                num++
                            }
                        }
                    }
                }
            }
        }



        recyclerView = binding.recyclerViewSeleccioProducte
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        producteList = arrayListOf()

        db.collection("productes").document("categories")
            .get().addOnSuccessListener { document ->
                val stBocata = document.get("bocata").toString()
                val stCalenta = document.get("bCalenta").toString()
                val stFreda = document.get("bFreda").toString()
                val imBocata = "Bocata de pernil dolç amb formatge.png"
                val imCalenta = "Café.png"
                val imFreda = "7up.png"

                producteList.add(dataclass(stBocata, imBocata))
                producteList.add(dataclass(stFreda, imFreda))
                producteList.add(dataclass(stCalenta, imCalenta))

                Padaper = PantallaSeleccioTipusProducte_Adapter(
                    producteList
                )
                recyclerView.adapter = Padaper
            }

        binding.btnConfirmar.setOnClickListener {
            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id != "usertypes") {
                        val user =
                            document.get("username")
                                .toString() + " " + document.get("usersurname")
                                .toString()
                        arrUser.add(user)
                    }
                }
                arrUser.add(getString(R.string.extern))
                view?.findNavController()?.navigate(
                    PantallaSeleccioTipusProdcuteDirections.actionPantallaSeleccioTipusProducteToPantallaSeleccioNomClientComanda(
                        arrUser
                    )
                )
            }

        }
        return binding.root
    }


}
