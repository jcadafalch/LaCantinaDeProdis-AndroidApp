package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.CambrerActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioCaixerCuinerCambrerBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaPrincipalBinding

class PantallaIniciSessioCaixerCuinerCambrer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentPantallaIniciSessioCaixerCuinerCambrerBinding>(
                inflater,
                R.layout.fragment_pantalla_inici_sessio_caixer_cuiner_cambrer, container, false
            )

        binding.btnPiniciarSessioClient.setOnClickListener {
            //TODO configurar metode que cridi a startActivity per obrir l'activity cambrer
        }
        return binding.root

    }

    private fun showCambrer(username: String){
        val cambrerIntent: Intent = Intent(this, CambrerActivity::class.java).apply{
            putExtra("username", username)
        }

        startActivity(cambrerIntent)

    }
}