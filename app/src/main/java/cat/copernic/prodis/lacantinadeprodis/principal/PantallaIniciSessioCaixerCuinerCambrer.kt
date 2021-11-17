package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.CaixerActivity
import cat.copernic.prodis.lacantinadeprodis.MainActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.ComandesActivity
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityMainBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioClientAdminBinding

class PantallaIniciSessioCaixerCuinerCambrer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPantallaIniciSessioClientAdminBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pantalla_inici_sessio_client_admin,
                container,
                false
            )

        binding.btnPiniciarSessioClient.setOnClickListener { view: View ->
            goComandesActivity()
        }
        return binding.root

    }

    fun goComandesActivity() {
        val intent = Intent(this.context, ComandesActivity::class.java).apply {
        }
        startActivity(intent)
    }

    fun goCaixerActivity() {
        val intent = Intent(this.context, CaixerActivity::class.java).apply {
        }
        startActivity(intent)
    }
}