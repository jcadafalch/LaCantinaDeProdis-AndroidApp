package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRegistreBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class PantallaRegistre : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaRegistreBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_registre, container, false
        )

        binding.btnRegistreContunuar.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_pantallaRegistre_to_pantallaIniciSessioClientAdmin)
        }

        binding.txtRegistreIniciSessio.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_pantallaRegistre_to_pantallaIniciSessioClientAdmin)
        }
        return binding.root
    }
}