package com.alp.app.ui.main.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.R
import com.alp.app.data.model.InsertTokenModel
import com.alp.app.databinding.ActivityHomeBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var navController: NavController
    private val dashboardViewModel: DashboardViewModel by viewModels()

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

    private fun insertToken(token: String) {
        dashboardViewModel.setToken(PreferencesSingleton.leer("id","0").toString(), token).observe(this, Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        Log.d("token", response.message!!)
                    }
                    Status.LOADING -> {
                        Log.d("token", "error")
                    }
                }
            }
        })
    }

    private fun renderList(data: Response<InsertTokenModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            Log.d("token", "insertado")
        } else {
            Log.d("token", "actualizado")
        }
    }

    private fun instanciarID() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                insertToken(it.result.toString())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}