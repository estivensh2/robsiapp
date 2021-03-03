package com.alp.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.databinding.ActivityEntradaBinding

class EntradaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEntradaBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntradaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navegacion_entradax)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.iniciarOCrearCuentaFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}