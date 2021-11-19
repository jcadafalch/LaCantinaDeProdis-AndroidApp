package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioNomClientComandaBinding

class PantallaSeleccioNomClientComanda : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioNomClientComandaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_nom_client_comanda, container, false
        )

        //TODO Mirar porque no funciona el directions en este navigate.
        binding.button2.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_nom_client_comanda_to_pantalla_seleccio_tipus_producte)
        }
        return binding.root
    }
}