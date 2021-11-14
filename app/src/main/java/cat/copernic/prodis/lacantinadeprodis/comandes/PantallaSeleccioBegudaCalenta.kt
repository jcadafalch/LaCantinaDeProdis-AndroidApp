package cat.copernic.prodis.lacantinadeprodis.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioBegudaCalentaBinding

class PantallaSeleccioBegudaCalenta : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaSeleccioBegudaCalentaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_beguda_calenta, container, false
        )
        binding.button2.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaSeleccioBegudaFredaDirections.actionPantallaSeleccioBegudaFredaToPantallaSeleccioAtributsBeguda())
        }
        return binding.root
    }
}