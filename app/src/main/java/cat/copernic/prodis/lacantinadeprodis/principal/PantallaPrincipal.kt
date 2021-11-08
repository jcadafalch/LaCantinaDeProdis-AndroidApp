package cat.copernic.prodis.lacantinadeprodis.principal

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityMainBinding
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaPrincipalBinding

private lateinit var binding: FragmentPantallaPrincipalBinding

class PantallaPrincipal : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentPantallaPrincipalBinding>(inflater,
            R.layout.fragment_pantalla_principal,container,false)

        binding.btnPprincipalClient.setOnClickListener{
            //view: View -> view.findNavController().navigate((R.id.action_pantalla_principal_to_pantalla_inici_sessio_client_admin))
        }

        binding.btnPprincipalCambrer.setOnClickListener {

        }

        binding.btnPprincipalCaixer.setOnClickListener {

        }

        binding.btnPprincipalCuiner.setOnClickListener {

        }

        binding.btnPprincipalAdministrador.setOnClickListener {

        }
        return binding.root
    }



}