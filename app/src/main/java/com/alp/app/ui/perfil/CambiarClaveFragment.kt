package com.alp.app.ui.perfil

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alp.app.R

class CambiarClaveFragment : Fragment() {

    companion object {
        fun newInstance() = CambiarClaveFragment()
    }

    private lateinit var viewModel: CambiarClaveViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cambiar_clave, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CambiarClaveViewModel::class.java)
        // TODO: Use the ViewModel
    }

}