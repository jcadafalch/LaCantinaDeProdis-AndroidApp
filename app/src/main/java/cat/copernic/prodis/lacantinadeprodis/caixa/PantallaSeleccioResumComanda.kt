package cat.copernic.prodis.lacantinadeprodis.caixa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioResumComandaBinding

class PantallaSeleccioResumComanda : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioResumComandaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_resum_comanda, container, false
        )

        binding.button.setOnClickListener { view: View ->
             view.findNavController().navigate(R.id.action_pantallaPrincipal_to_pantallaIniciSessioClientAdmin)
        }

        return binding.root
    }

}