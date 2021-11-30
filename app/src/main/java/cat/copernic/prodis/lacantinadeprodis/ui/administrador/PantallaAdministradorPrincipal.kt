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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorPrincipalBinding>(inflater,
            R.layout.fragment_pantalla_administrador_principal,container,false)

        binding.btnPAdministradorPrincipalNouUsuari.setOnClickListener {
            view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouUsuari())
        }

        binding.btnPAdministradorPrincipalElimiarModificarUsuari.setOnClickListener {
            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    val user = document.get("username").toString() + " " + document.get("usersurname").toString()
                    println("USERNAME ==== "+document.get("username").toString())
                    println("USERSURNAME ===== " + document.get("usersurname").toString() )
                    println("----------------------------------------------------------")
                    arrUser.add(user)
                }
                println("*************************")
                println(arrUser)
                println("------------------------------")
                view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorModificarUsuari(
                    arrUser
                ))

            }

        }

        binding.btnPAdministradorPrincipalNouProducte.setOnClickListener {
            view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouProducte()   )
        }

        binding.btnPAdministradorPrincipalAdministrarProductes.setOnClickListener {
            view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorAdministrarProducte())
        }

        return binding.root
    }
}