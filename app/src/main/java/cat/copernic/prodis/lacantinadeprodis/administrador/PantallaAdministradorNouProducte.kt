package cat.copernic.prodis.lacantinadeprodis.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.prodis.lacantinadeprodis.R

class PantallaAdministradorNouProducte : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_pantalla_administrador_nou_producte,
            container,
            false
        )
    }
}