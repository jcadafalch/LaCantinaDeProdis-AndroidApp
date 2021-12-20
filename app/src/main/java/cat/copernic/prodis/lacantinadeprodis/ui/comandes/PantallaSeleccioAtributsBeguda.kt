package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioAtributsBegudaBinding

class PantallaSeleccioAtributsBeguda : Fragment() {

    lateinit var binding: FragmentPantallaSeleccioAtributsBegudaBinding

    //Definició de les variables per determinar els atributs
    lateinit var atributs: String
    var perEmportar: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioAtributsBegudaBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_atributs_beguda, container, false
        )

        binding.btnConfirmar.setOnClickListener { view: View ->
            setAtributs()
            view.findNavController()
                .navigate(PantallaSeleccioAtributsBegudaDirections.actionPantallaSeleccioAtributsBegudaToPantallaSeleccioTipusProducte(atributs,perEmportar))
        }

        binding.btnTornaEnrerre.setOnClickListener{
            findNavController().popBackStack()
        }

        return binding.root
    }

    fun setAtributs() {
        binding.rarioGroupAtributs
            .setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioSenseSucre -> {
                        atributs = "senseSucre"
                    }
                    R.id.radioSucreBlanc -> {
                        atributs = "sucreBlanc"
                    }
                    R.id.radioSucreMore -> {
                        atributs = "sucreMore"
                    }
                    R.id.radioSacarina -> {
                        atributs = "sacarina"
                    }
                }
            }
        perEmportar = binding.checkPerEmportar.isChecked
    }
}
