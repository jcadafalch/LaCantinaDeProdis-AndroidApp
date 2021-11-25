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


class PantallaAdministradorPrincipal : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorPrincipalBinding>(inflater,
            R.layout.fragment_pantalla_administrador_principal,container,false)

        binding.btnPAdministradorPrincipalNouUsuari.setOnClickListener {
            view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorNouUsuari())
        }

        binding.btnPAdministradorPrincipalElimiarModificarUsuari.setOnClickListener {
            view?.findNavController()?.navigate(PantallaAdministradorPrincipalDirections.actionPantallaAdministradorPrincipalToPantallaAdministradorModificarUsuari())
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