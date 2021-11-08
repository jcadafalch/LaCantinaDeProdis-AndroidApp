package cat.copernic.prodis.lacantinadeprodis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityMainBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaSeleccioTipusProducteBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

     fun onCreate(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentPantallaSeleccioTipusProducteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_seleccio_tipus_producte, container, false
        )
        return binding.root
    }
}