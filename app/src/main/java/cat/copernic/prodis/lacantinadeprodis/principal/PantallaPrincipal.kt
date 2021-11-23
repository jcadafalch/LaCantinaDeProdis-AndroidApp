package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.AdministradorActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaPrincipalBinding

class PantallaPrincipal : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaPrincipalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_principal, container, false
        )
        binding.btnPprincipalClient.setOnClickListener { view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.actionPantallaPrincipalToPantallaIniciSessioClientAdmin("client"))
        }

        binding.btnPprincipalCambrer.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.actionPantallaPrincipalToPantallaIniciSessioClientAdmin("cambrer"))
        }

        binding.btnPprincipalCaixer.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.actionPantallaPrincipalToPantallaIniciSessioClientAdmin("caixer"))
        }

        binding.btnPprincipalCuiner.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.actionPantallaPrincipalToPantallaIniciSessioClientAdmin("cuiner"))
        }

        binding.btnPprincipalAdministrador.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.actionPantallaPrincipalToPantallaIniciSessioClientAdmin("admin"))
        }

        return binding.root
    }
}

