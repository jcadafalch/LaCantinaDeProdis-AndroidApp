package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class PantallaSeleccioTipusProdcute : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )
        binding.imgBocata.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantalla_seleccio_bocata)
        }
        binding.imgBegudesFredes.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantallaSeleccioBegudaFreda)
        }
        binding.txtBegudesCalentes.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantallaSeleccioBegudaCalenta)
        }
        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_tipus_producte_to_pantalla_seleccio_nom_client_comanda)
        }
        return binding.root
    }
}
