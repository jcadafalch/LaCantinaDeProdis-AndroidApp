package cat.copernic.prodis.lacantinadeprodis.ui.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaPrincipalBinding

class PantallaPrincipal : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaPrincipalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_principal, container, false
        )
        //Escoltem el botó per anar a la pantalla del client
        binding.btnPprincipalClient.setOnClickListener { view : View ->
            //Anem a la pantalla d'inici de sessió pasant el valor "clientR"
            view.findNavController().navigate(PantallaPrincipalDirections.
            actionPantallaPrincipalToPantallaIniciSessioClientAdmin("clientR"))
        }

        //Anem a la pantalla d'inici de sessió pasant el valor "cambrer"
        binding.btnPprincipalCambrer.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.
            actionPantallaPrincipalToPantallaIniciSessioClientAdmin("cambrer"))
        }

        //Anem a la pantalla d'inici de sessió pasant el valor "caixer"
        binding.btnPprincipalCaixer.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.
            actionPantallaPrincipalToPantallaIniciSessioClientAdmin("caixer"))
        }

        //Anem a la pantalla d'inici de sessió pasant el valor "cuiner"
        binding.btnPprincipalCuiner.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.
            actionPantallaPrincipalToPantallaIniciSessioClientAdmin("cuiner"))
        }

        //Anem a la pantalla d'inici de sessió pasant el valor "admin"
        binding.btnPprincipalAdministrador.setOnClickListener{ view : View ->
            view.findNavController().navigate(PantallaPrincipalDirections.
            actionPantallaPrincipalToPantallaIniciSessioClientAdmin("admin"))
        }

        return binding.root
    }
}

