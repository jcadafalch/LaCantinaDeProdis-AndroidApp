package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioClientAdminBinding

class PantallaIniciSessioClientAdmin : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPantallaIniciSessioClientAdminBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_client_admin, container, false
        )

        return binding.root
    }
}