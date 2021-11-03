package cat.copernic.prodis.lacantinadeprodis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding
import cat.copernic.prodis.lacantinadeprodis.pantalla_seleccio_tipus_producte as PantallaSeleccioTipusProducteBinding

class pantalla_seleccio_tipus_producte : Fragment() {

    private lateinit var binding: PantallaSeleccioTipusProducteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_pantalla_seleccio_tipus_producte)

        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_pantalla_seleccio_tipus_producte,
            container,
            false
        )

    }

}