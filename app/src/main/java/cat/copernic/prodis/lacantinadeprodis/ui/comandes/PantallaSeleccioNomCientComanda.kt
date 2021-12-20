package cat.copernic.prodis.lacantinadeprodis.ui.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioNomClientComandaBinding

class PantallaSeleccioNomCientComanda : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var userArr: ArrayList<*>
    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var binding: FragmentPantallaSeleccioNomClientComandaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_nom_client_comanda, container, false
        )
        val args = PantallaSeleccioNomCientComandaArgs.fromBundle(requireArguments())
        userArr = args.arrUser
        spinner = binding.spnNomClient

        val context = this.requireContext()

        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, userArr)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        binding.btnTornaEnrerre.setOnClickListener {
            view?.findNavController()?.navigate(PantallaSeleccioNomCientComandaDirections.
            actionPantallaSeleccioNomClientComandaToPantallaSeleccioTipusProducte())
        }


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == userArr.size-1){
            binding.dtTxtNomUsuariExtern.visibility = View.VISIBLE
        }else{
            binding.dtTxtNomUsuariExtern.visibility = View.INVISIBLE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}
