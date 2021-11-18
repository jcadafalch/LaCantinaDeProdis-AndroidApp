package cat.copernic.prodis.lacantinadeprodis.principal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.prodis.lacantinadeprodis.AdministradorActivity
import cat.copernic.prodis.lacantinadeprodis.CaixerActivity
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.activities.CambrerActivity
import cat.copernic.prodis.lacantinadeprodis.activities.ComandesActivity
import cat.copernic.prodis.lacantinadeprodis.activities.CuinerActivity
import cat.copernic.prodis.lacantinadeprodis.administrador.PantallaAdministradorPrincipal
import cat.copernic.prodis.lacantinadeprodis.databinding.FragmentPantallaIniciSessioClientAdminBinding
import android.widget.Toast.makeText as makeText

class PantallaIniciSessioClientAdmin : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPantallaIniciSessioClientAdminBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pantalla_inici_sessio_client_admin, container, false
        )

        val args = PantallaIniciSessioClientAdminArgs.fromBundle(requireArguments())
        val usertype = args.usertype

        binding.btnPiniciarSessioClient.setOnClickListener {
            when (usertype){
                "client" -> showCambrerClient(usertype)
                "cambrer" -> showCambrerClient(usertype)
                "caixer" -> showCaixer()
                "cuiner" -> showCuiner()
                "admin" -> showAdmin()
                else -> {
                    Toast.makeText(context, "Error en l'inici de sessió", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.txtPiniciarSessioClientOblidatContrasenya.setOnClickListener{
            view?.findNavController()?.navigate(PantallaIniciSessioClientAdminDirections.actionPantallaIniciSessioClientAdminToPantallaRecuperarContrasenya1(usertype))
        }

        binding.txtPiniciarSessioClientRegistre.setOnClickListener {
            view?.findNavController()?.navigate(PantallaIniciSessioClientAdminDirections.actionPantallaIniciSessioClientAdminToPantallaRegistre(usertype))
        }

        return binding.root
    }

    private fun showCambrerClient(username: String){
        val intent = Intent(this.context, ComandesActivity::class.java).apply{

        }
        startActivity(intent)
    }

    private fun showCaixer(){
        val intent = Intent(this.context, CaixerActivity::class.java).apply{

        }
        startActivity(intent)
    }

    private fun showCuiner(){
        val intent = Intent(this.context, CuinerActivity::class.java).apply{

        }
        startActivity(intent)
    }

    private fun showAdmin(){
        val intent = Intent(this.context, AdministradorActivity::class.java).apply{

        }
        startActivity(intent)

    }
}