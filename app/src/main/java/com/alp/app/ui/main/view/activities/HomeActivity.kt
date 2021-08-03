/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 2/08/21, 2:34 p. m.
 *
 */

package com.alp.app.ui.main.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        navController = findNavController(R.id.navigation_home)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        instantiateID()
    }

    private fun insertToken(token: String) {
        val idUser = PreferencesSingleton.read("id_user", 0)
        dashboardViewModel.tokenUser(idUser, token).observe(this) { response ->
            response?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR -> {
                        Log.d("token", response.message!!)
                    }
                    Status.LOADING -> {
                        Log.d("token", "error")
                    }
                }
            }
        }
    }

    private fun renderList(data: Response<InsertTokenModel>) {
        val response = data.body()!!
        if (response.response == 1) {
            Log.d("token", "Inserted")
        } else {
            Log.d("token", "Updated")
        }
    }

    private fun instantiateID() {
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