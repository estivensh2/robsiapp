package com.alp.app.ui.main.views.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.R
import com.alp.app.data.model.RespuestaInsertarToken
import com.alp.app.databinding.ActivityHomeBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.PreferencesSingleton
import com.alp.app.servicios.ServicioBuilder
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navegacion_entradax)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.iniciarOCrearCuentaFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        instanciarID()

    }

    private fun instanciarID() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                insertarToken(it.result.toString())
            }
        }
    }

    private fun insertarToken(token: String){
        val call = ServicioBuilder.buildServicio(APIServicio::class.java)
        try {
            call.insertarToken(PreferencesSingleton.leer("id","0").toString(), token).enqueue(object :
                Callback<RespuestaInsertarToken> {
                override fun onResponse(call: Call<RespuestaInsertarToken>, response: Response<RespuestaInsertarToken>) {
                    val responsex = response.body()!!
                    if (responsex.respuesta == "1") {
                        Log.d("token", "insertado")
                    } else {
                        Log.d("token", "actualizado")
                    }
                }
                override fun onFailure(call: Call<RespuestaInsertarToken>, t: Throwable) {
                    Log.d("token", t.toString()+"si")
                }
            })
        } catch (e: Throwable) {
            Log.d("token", e.toString()+"no")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}