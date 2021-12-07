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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorPrincipalBinding>(
            inflater,
            R.layout.fragment_pantalla_administrador_principal, container, false
        )
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
        binding.btnPAdministradorPrincipalNouUsuari.setOnClickListener {
            view?.findNavController()
                ?.navigate(
                    PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouUsuari(
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
                    PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorModificarUsuari(
                        arrUser, arrUserId, arrUsertype
                    )
                )

            }

        }

        binding.btnPAdministradorPrincipalNouProducte.setOnClickListener {
            view?.findNavController()
                ?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouProducte())
        }

        binding.btnPAdministradorPrincipalAdministrarProductes.setOnClickListener {
            view?.findNavController()
                ?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorAdministrarProducte())
        }

        return binding.root
    }
}