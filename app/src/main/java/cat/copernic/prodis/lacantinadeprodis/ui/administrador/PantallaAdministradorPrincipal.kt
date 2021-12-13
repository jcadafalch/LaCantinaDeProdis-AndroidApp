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

class PantallaAdministradorPrincipal : Fragment() {

    private val db = Firebase.firestore
    private var arrayTipusProducte = ArrayList<String>()
    private var arrayProductes = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaAdministradorPrincipalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_principal, container, false
        )
        arrayProductes.clear()
        arrayTipusProducte.clear()


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

        binding.btnPAdministradorPrincipalNouUsuari.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouUsuari())
        }
        binding.btnPAdministradorPrincipalElimiarModificarUsuari.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorModificarUsuari())
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