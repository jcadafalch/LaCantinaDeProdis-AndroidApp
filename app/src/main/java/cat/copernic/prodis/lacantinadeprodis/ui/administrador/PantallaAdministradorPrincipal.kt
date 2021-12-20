package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorPrincipalBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class PantallaAdministradorPrincipal : Fragment() {

    private val db = Firebase.firestore
    private var arrUser = ArrayList<String>()
    private var arrUserId = ArrayList<String>()
    private var arrUsertype = ArrayList<String>()
    private var arrayTipusProducte = ArrayList<String>()
    private var arrayProductes = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorPrincipalBinding>(
            inflater,
            R.layout.fragment_pantalla_administrador_principal, container, false
        )
        arrUsertype.clear()
        arrUser.clear()
        arrUserId.clear()
        arrayProductes.clear()
        arrayTipusProducte.clear()

        db.collection("users").document("usertypes").get().addOnSuccessListener { document ->
            val admin = document.get("admin").toString()
            val caixer = document.get("caixer").toString()
            val cambrer = document.get("cambrer").toString()
            val client = document.get("client").toString()
            val clientR = document.get("clientR").toString()
            val cuiner = document.get("cuiner").toString()

            arrUsertype.add(admin)
            arrUsertype.add(caixer)
            arrUsertype.add(cambrer)
            arrUsertype.add(client)
            arrUsertype.add(clientR)
            arrUsertype.add(cuiner)
        }
        db.collection("productes").document("categories").get().addOnSuccessListener { document ->
            val bCalenta = document.get("bCalenta").toString()
            val bFreda = document.get("bFreda").toString()
            val bocata = document.get("bocata").toString()
            if (arrayTipusProducte.isEmpty()) {
                arrayTipusProducte.add(bCalenta)
                arrayTipusProducte.add(bFreda)
                arrayTipusProducte.add(bocata)
            }
        }
        db.collection("productes").get().addOnSuccessListener { result ->
            if (arrayProductes.isEmpty()) {
                for (document in result) {
                    if (document != null) {
                        arrayProductes.add(document.get("nom").toString())
                    }
                }
            }
        }


        binding.btnPAdministradorPrincipalNouUsuari.setOnClickListener {
            view?.findNavController()
                ?.navigate(
                    PantallaAdministradorPrincipalDirections.
                    actionPantallaAdministradorPrincipalToPantallaAdministradorNouUsuari(
                        arrUsertype
                    )
                )
        }

        binding.btnPAdministradorPrincipalElimiarModificarUsuari.setOnClickListener {
            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id != "usertypes") {
                        val user =
                            document.get("username").toString() + " " + document.get("usersurname")
                                .toString()
                        arrUser.add(user)
                        arrUserId.add(document.id)
                    }
                }
                view?.findNavController()?.navigate(
                    PantallaAdministradorPrincipalDirections.
                    actionPantallaAdministradorPrincipalToPantallaAdministradorModificarUsuari(
                        arrUser, arrUserId, arrUsertype
                    )
                )

            }

        }

        binding.btnPAdministradorPrincipalNouProducte.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouProducte(
                        arrayTipusProducte
                    )
                )
        }

        binding.btnPAdministradorPrincipalAdministrarProductes.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorAdministrarProducte(
                        arrayTipusProducte,
                        arrayProductes
                    )
                )
        }
        return binding.root
    }
}