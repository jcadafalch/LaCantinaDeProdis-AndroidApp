package cat.copernic.prodis.lacantinadeprodis.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioAtributsBegudaBinding

class PantallaSeleccioAtributsBeguda : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioAtributsBegudaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_atributs_beguda, container, false
        )

        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_atributs_beguda_to_pantalla_seleccio_tipus_producte)
        }
        return binding.root
    }
}