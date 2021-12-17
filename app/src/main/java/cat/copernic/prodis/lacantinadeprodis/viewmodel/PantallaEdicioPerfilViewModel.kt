package cat.copernic.prodis.lacantinadeprodis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.MutableData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

lateinit var dni: String


class PantallaEdicioPerfilViewModel() : ViewModel() {

    private val _nom = MutableLiveData<String>()
    val nom: LiveData<String>
        get() = _nom

    private val _cognom = MutableLiveData<String>()
    val cognom: LiveData<String>
        get() = _cognom

    init{
        var tasca1: Job? = crearCorrutina()

        tasca1.let {}
    }

    /*fun setNom(): String? {
        println("Fuera " + nom.value)
        return nom.value.toString()
    }*/

    fun crearCorrutina()= GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.IO){
            val db = FirebaseFirestore.getInstance()

            val currentUser =
                FirebaseAuth.getInstance().currentUser

            db.collection("users").get().addOnSuccessListener { result ->
                for (document in result) {
                    if (currentUser?.email.toString() == document.get("email").toString()) {
                        dni = document.id
                        db.collection("users").document(dni).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    _nom.value = document.get("username").toString()
                                    println("Dentro del for " + _nom.value)
                                }
                            }
                    }
                }
            }
        }
    }
}