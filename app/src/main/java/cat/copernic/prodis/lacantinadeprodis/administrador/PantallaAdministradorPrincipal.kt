package cat.copernic.prodis.lacantinadeprodis.administrador

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
            //view: View -> view.findNavController().navigate((R.id.action_pantalla_administrador_principal_to_pantalla_administrador_nou_usuari))
        }

        binding.btnPAdministradorPrincipalElimiarModificarUsuari.setOnClickListener {
            //view: View -> view.findNavController().navigate((R.id.action_pantalla_administrador_principal_to_pantalla_administrador_modificar_usuari))
        }

        binding.btnPAdministradorPrincipalNouProducte.setOnClickListener {
            //view: View -> view.findNavController().navigate((R.id.action_pantalla_administrador_principal_to_pantalla_administrador_nou_producte))
        }

        binding.btnPAdministradorPrincipalAdministrarProductes.setOnClickListener {
            //view: View -> view.findNavController().navigate((R.id.action_pantalla_administrador_principal_to_pantalla_administrador_administrar_productes))
        }

        return binding.root
    }

}