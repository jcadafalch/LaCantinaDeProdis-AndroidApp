package cat.copernic.prodis.lacantinadeprodis.comandes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaEdicioPerfilBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class PantallaIniciSessio : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaIniciSessioBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio, container, false
        )
        return binding.root
    }
}