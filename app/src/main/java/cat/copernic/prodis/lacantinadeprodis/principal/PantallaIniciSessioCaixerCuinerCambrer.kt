package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.ComandesActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioCaixerCuinerCambrerBinding

class PantallaIniciSessioCaixerCuinerCambrer : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaIniciSessioCaixerCuinerCambrerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_caixer_cuiner_cambrer, container, false
        )

        binding.btnPiniciarSessioClient.setOnClickListener { view : View ->
            //goPantallaInicisessioCaixerCuinerCambrer()
        }
        return binding.root

    }

    fun goPantallaInicisessioCaixerCuinerCambrer(view: View) {
        /*val intent = Intent(this, ComandesActivity::class.java).apply {
        }
        startActivity(intent)*/

    }

}