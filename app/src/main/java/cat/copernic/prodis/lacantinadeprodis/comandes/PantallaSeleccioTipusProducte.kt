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
import cat.copernic.prodis.lacantinadeprodis.principal.PantallaPrincipalDirections

class PantallaSeleccioTipusProducte : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )

        binding.btnBocata.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaSeleccioTipusProducteDirections.actionPantallaSeleccioTipusProducteToPantallaSeleccioBocata())
        }
        binding.btnBegudaFreda.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaSeleccioTipusProducteDirections.actionPantallaSeleccioTipusProducteToPantallaSeleccioBegudaFreda())
        }
        binding.btnBegudaCalenta.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PantallaSeleccioTipusProducteDirections.actionPantallaSeleccioTipusProducteToPantallaSeleccioBegudaCalenta())
        }
        //TODO Mirar porque no funciona el directions en este navigate, en los otros funciona correctamente.
        binding.btnConfirmar.setOnClickListener { view: View ->
            /*view.findNavController()
                .navigate(PantallaSeleccioTipusProducteDirections.)*/
        }
        return binding.root
    }
}