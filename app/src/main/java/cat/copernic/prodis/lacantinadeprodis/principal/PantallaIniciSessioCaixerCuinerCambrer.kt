package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioCaixerCuinerCambrerBinding

class PantallaIniciSessioCaixerCuinerCambrer : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaIniciSessioCaixerCuinerCambrerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_caixer_cuiner_cambrer, container, false
        )


        return binding.root
    }
}