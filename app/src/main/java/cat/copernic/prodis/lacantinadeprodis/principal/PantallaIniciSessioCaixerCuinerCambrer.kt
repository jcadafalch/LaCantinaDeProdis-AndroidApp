package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.CaixerActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioCaixerCuinerCambrerBinding

class PantallaIniciSessioCaixerCuinerCambrer : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaIniciSessioCaixerCuinerCambrerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_caixer_cuiner_cambrer, container, false
        )

        binding.btnPiniciarSessioClient.setOnClickListener { view : View ->
            showHome()
        }
        return binding.root
    }

    private fun showHome() {
        /*val homeIntent = Intent(this, CaixerActivity::class.java).apply {


        }
        startActivity(homeIntent)*/
    }
}