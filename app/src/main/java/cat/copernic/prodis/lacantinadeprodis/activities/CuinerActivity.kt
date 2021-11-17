package cat.copernic.prodis.lacantinadeprodis.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cat.copernic.prodis.lacantinadeprodis.R
import cat.copernic.prodis.lacantinadeprodis.databinding.ActivityCuinerBinding

class CuinerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCuinerBinding>(this, R.layout.fragment_pantalla_cuiner)

        //val navController = this.findNavController(R.id.myNavHostFragment)

        //Fletxa enderrere i faltaria metode onSupportNavigateUp
        //NavigationUI.setupActionBarWithNavController(this, navController)

    }
}