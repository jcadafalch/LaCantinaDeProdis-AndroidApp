package cat.copernic.prodis.lacantinadeprodis.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.comandes.PantallaSeleccioBocataDirections
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorPrincipalBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaPrincipalBinding


class PantallaAdministradorPrincipal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaAdministradorPrincipalBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_principal, container, false
        )
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
                .navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouProducte())
        }
        binding.btnPAdministradorPrincipalAdministrarProductes.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorAdministrarProducte())
        }
        return binding.root
    }

}