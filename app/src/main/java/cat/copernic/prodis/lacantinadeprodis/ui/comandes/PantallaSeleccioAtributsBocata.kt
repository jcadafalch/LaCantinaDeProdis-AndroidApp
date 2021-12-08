package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioAtributsBocataBinding

class PantallaSeleccioAtributsBocata : Fragment() {

    var senseTomaquet : Boolean = false

    var emportar : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaSeleccioAtributsBocataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_atributs_bocata, container, false
        )


        binding.btnConfirmar.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pantalla_seleccio_atributs_bocata_to_pantalla_seleccio_tipus_producte)

            /*setResultListener("requestKey") { key, bundle ->
                // We use a String here, but any type that can be put in a Bundle is supported
                val result = bundle.getString("bundleKey")
                // Do something with the result...
            }*/

            if(binding.checkBoxSenseTomaquet.isChecked){
                senseTomaquet = true
            }

            if(binding.checkBoxSenseTomaquet.isChecked){
                senseTomaquet = true
            }
        }
        return binding.root
    }
}