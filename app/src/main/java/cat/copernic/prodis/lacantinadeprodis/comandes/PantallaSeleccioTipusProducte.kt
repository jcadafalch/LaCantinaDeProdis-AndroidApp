package cat.copernic.prodis.lacantinadeprodis.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class PantallaSeleccioTipusProducte : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )

        binding.button.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantalla_seleccio_bocata)
        }
        binding.button2.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantallaSeleccioBegudaFreda)
        }
        binding.button3.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantallaSeleccioBegudaCalenta)
        }
        return binding.root
    }
}