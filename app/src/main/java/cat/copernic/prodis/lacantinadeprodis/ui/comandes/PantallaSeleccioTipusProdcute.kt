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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.adapters.PantallaSeleccioTipusProducte_Adapter
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding
import cat.copernic.prodis.lacantinadeprodis.model.dataclass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.time.Instant
import kotlin.collections.ArrayList

class PantallaSeleccioTipusProdcute : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var producteList: ArrayList<dataclass>
    private lateinit var Padaper: PantallaSeleccioTipusProducte_Adapter
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var arrUser = java.util.ArrayList<String>()
    private lateinit var dni: String
    private lateinit var docId: String
    private lateinit var binding: FragmentPantallaSeleccioTipusProducteBinding
    private var preu = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (currentUser?.email.toString() == document.get("email").toString()) {
                    dni = document.id
                    var exists = false
                    db.collection("comandes").get().addOnSuccessListener { result ->
                        for (document in result) {
                            if (document.get("comandaComencada")
                                    .toString() == "true" && document.get("user").toString() == dni
                            ) {
                                exists = true
                                docId = document.id

                                sumaPreu(docId)
                            }
                        }
                        println("EXISTS == $exists")
                        if (!exists) {
                            createComanda()
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


            db.collection("users").document(dni).get().addOnSuccessListener { document ->
                if (document.get("usertype").toString() == "cambrer") {
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
                                arrUser, docId, dni
                            )
                        )
                    }
                } else {
                    db.collection("comandes").get().addOnSuccessListener { result ->
                        for (dc in result) {
                            if (dc.get("comandaComencada").toString() == "true" && dc.get("user")
                                    .toString() == dni
                            ) {
                                val dcId = document.id
                                val username = document.get("username")
                                    .toString() + " " + document.get("usersurname").toString()

                                db.collection("comandes").document(docId).update(
                                    hashMapOf(
                                        "comandaComencada" to false,
                                        "visible" to true,
                                        "user" to username,
                                    ) as Map<String, Any>
                                )
                                break
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createComanda() {

        var dni: String

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (currentUser?.email.toString() == document.get("email").toString()) {
                    dni = document.id

                    var num: Int = 0

                    db.collection("comandes").get().addOnSuccessListener { result ->
                        for (document in result) {
                            num++
                        }
                        num++
                        db.collection("comandes").document().set(
                            hashMapOf(
                                "comandaComencada" to true,
                                "comandaId" to num.toString(),
                                "comandaPagada" to false,
                                "date" to Timestamp.from(Instant.now()),
                                "preparat" to false,
                                "preuTotal" to 0,
                                "user" to dni,
                                "visible" to false
                            ) as Map<String, Any>
                        )
                    }
                }
            }
        }
    }

    private fun sumaPreu(a: String) {
        binding.btnConfirmar.setOnClickListener() {
            db.collection("comandes").document(a).collection("productes").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var idProducte = document.get("idProducte")
                        db.collection("productes").get().addOnSuccessListener {
                            for (document in it) {
                                if (document.get("idProducte").toString() == idProducte) {
                                    preu += document.get("preu") as Double
                                }

                                db.collection("comandes").document(a).update(
                                    hashMapOf(
                                        "preuTotal" to preu
                                    ) as Map<String, Any>
                                )
                            }
                        }
                    }


                }
        }
    }
}
