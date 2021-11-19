package cat.copernic.prodis.lacantinadeprodis.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaEdicioPerfilBinding

class PantallaEdicioPerfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<FragmentPantallaEdicioPerfilBinding>(this, R.layout.fragment_pantalla_edicio_perfil)

    }
}