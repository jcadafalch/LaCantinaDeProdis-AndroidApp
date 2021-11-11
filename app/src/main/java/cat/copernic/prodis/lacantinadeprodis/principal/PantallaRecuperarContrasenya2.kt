package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaRecuperarContrasenya2Binding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class PantallaRecuperarContrasenya2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaRecuperarContrasenya2Binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_recuperar_contrasenya2, container, false
        )

        return binding.root
    }
}