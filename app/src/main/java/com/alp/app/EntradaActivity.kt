package com.alp.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.data.RespuestaInsertarToken
import com.alp.app.databinding.ActivityEntradaBinding
import com.alp.app.servicios.APIServicio
import com.alp.app.servicios.Preferencias
import com.alp.app.servicios.ServicioBuilder
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntradaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEntradaBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntradaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Preferencias.init(this, "preferenciasDeUsuario")
        val estado = Preferencias.leer("idoscuro",true)
        if (estado==true){
            setTheme(R.style.Theme_ALP_Oscuro)
        } else {
            setTheme(R.style.Theme_ALP)
        }


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
            call.insertarToken(Preferencias.leer("id","0").toString(), token).enqueue(object :
                Callback<RespuestaInsertarToken> {
                override fun onResponse(call: Call<RespuestaInsertarToken>, response: Response<RespuestaInsertarToken>) {
                    val responsex = response.body()!!
                    if (responsex.respuesta == "1") {
                        Log.d("token", "insertado")
                    } else {
                        Log.d("token", "ya existe")
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