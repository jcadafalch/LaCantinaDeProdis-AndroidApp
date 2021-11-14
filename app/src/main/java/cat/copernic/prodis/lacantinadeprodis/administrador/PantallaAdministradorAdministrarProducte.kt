package cat.copernic.prodis.lacantinadeprodis.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorAdministrarProducteBinding


class PantallaAdministradorAdministrarProducte : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaAdministradorAdministrarProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_administrador_administrar_producte, container, false
        )

        return binding.root
    }
}