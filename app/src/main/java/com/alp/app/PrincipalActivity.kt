package com.alp.app

/**
 * Extraido de https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.servicios.Preferencias
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrincipalActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        Preferencias.init(this, "preferenciasDeUsuario")
        when(Preferencias.leer("idoscuro",true)){
            true -> setTheme(R.style.Tema_App_Oscuro)
            false -> setTheme(R.style.Tema_App_Claro)
        }
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

        val navGraphIds = listOf(R.navigation.navegacion_inicio, R.navigation.navegacion_perfil)

        //Configurar la vista de navegación inferior con una lista de gráficos de navegación
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragmentx,
            intent = intent
        )
        bottomNavigationView.itemIconTintList = null

        //Siempre que cambie el controlador seleccionado, configure la barra de acción.
        controller.observe(this, { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}