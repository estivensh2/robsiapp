package com.alp.app.ui.main.view.activities

/**
 * Extraido de https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
       
        //Preferencias.init(this, "preferenciasDeUsuario")
        //val estado = Preferencias.leer("idoscuro",true)
        //setTheme(if (estado==true) R.style.Theme_ALP_Oscuro else R.style.Theme_ALP)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navegacion)


        val navGraphIds = listOf(R.navigation.navigation_inicio, R.navigation.navigation_perfil)

        //Configurar la vista de navegación inferior con una lista de gráficos de navegación
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragmentx,
            intent = intent
        )
        bottomNavigationView.itemIconTintList = null

        //Siempre que cambie el controlador seleccionado, configure la barra de acción.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}