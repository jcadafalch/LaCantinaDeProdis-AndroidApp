package cat.copernic.prodis.lacantinadeprodis.ui.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaAdministradorNouProducteBinding

class PantallaAdministradorNouProducte : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var tipusProducte: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPantallaAdministradorNouProducteBinding>(inflater,
            R.layout.fragment_pantalla_administrador_nou_producte,container,false)


        val spinner: Spinner = binding.spinTipusProducte

        val context = this.requireContext()

        ArrayAdapter.createFromResource(
            context,
            R.array.tipus_producte,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        tipusProducte = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        showAlert("Has de seleccionar un tipus de prodcute")
    }

    private fun showAlert(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        builder.setTitle("¡¡¡Error!!!")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }
}