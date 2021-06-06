package com.alp.app.ui.main.views.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.EditText
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.UpdatePasswordModel
import com.alp.app.databinding.FragmentChangePasswordBinding
import com.alp.app.servicios.ClaseToast
import com.alp.app.servicios.PreferencesSingleton
import com.alp.app.ui.main.views.viewmodel.DashboardViewModel
import com.alp.app.utils.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        with(binding){
            resultadoerror.visibility = View.GONE
            mostrarClave(mostrarClaveActual, claveAccesoActual)
            mostrarClave(mostrarClaveNueva, claveAccesoNueva)
            mostrarClave(mostrarClaveNuevaConfirmada, claveAccesoNuevaConfirmada)
        }
        return binding.root
    }

    private fun setupShowData() {
        val actualPassword = binding.claveAccesoActual.text.toString()
        val newPassword = binding.claveAccesoNuevaConfirmada.text.toString()
        dashboardViewModel.setPassword(actualPassword, newPassword ,PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        showHideProgressBar(false)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        showMessage(response.message!!)
                        showHideProgressBar(false)
                    }
                    Status.LOADING -> {
                        showHideProgressBar(true)
                    }
                }
            }
        })
    }

    private fun showHideProgressBar(showHide: Boolean){
        with(binding){
            if(showHide){
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.GONE
            }
        }
    }

    private fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(requireView(), message, duration).show()
    }

    private fun renderList(data: Response<UpdatePasswordModel>) {
        val response = data.body()!!
        if (response.respuesta == "1") {
            ClaseToast.mostrarx(contexto, getString(R.string.texto_clave_cambiada), ContextCompat.getColor(contexto, R.color.colorGrisOscuro), R.drawable.exclamacion)
            with(binding) {
                claveAccesoNueva.setText("")
                claveAccesoActual.setText("")
                claveAccesoNuevaConfirmada.setText("")
                findNavController().navigate(R.id.accion_cambiar_clave_a_perfil)
            }
        } else {
            activity?.runOnUiThread {
                with(binding){
                    resultadoerror.visibility = View.VISIBLE
                    resultadoerror.text = resources.getString(R.string.texto_clave_incorrecta)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cambiar_clave, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.cambiar_clave)
        item.isVisible = false
        with(binding){
            claveAccesoNueva.onChange           { habilitarBoton(item) }
            claveAccesoActual.onChange          { habilitarBoton(item) }
            claveAccesoNuevaConfirmada.onChange { habilitarBoton(item) }
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun habilitarBoton(item: MenuItem) {
        with(binding){
            if (claveAccesoActual.length()>0){
                if (claveAccesoNueva.length()>0 && claveAccesoNuevaConfirmada.length()>0){
                    if (claveAccesoNueva.length()<6 && claveAccesoNuevaConfirmada.length()<6){
                        resultadoerror.visibility = View.VISIBLE
                        resultadoerror.text = resources.getString(R.string.texto_clave_minimo)
                    } else if (claveAccesoNueva.text.toString()!=claveAccesoNuevaConfirmada.text.toString()){
                        resultadoerror.visibility = View.VISIBLE
                        resultadoerror.text = resources.getString(R.string.texto_claves_no_coinciden)
                        item.isVisible = false
                    } else {
                        resultadoerror.visibility = View.GONE
                        item.isVisible = true
                    }
                } else {
                    resultadoerror.visibility = View.GONE
                    item.isVisible = false
                }
            } else {
                resultadoerror.visibility = View.GONE
                item.isVisible = false
            }
        }
    }

    private fun mostrarClave(boton: ToggleButton, campo: EditText){
        boton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                campo.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                campo.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cambiar_clave -> setupShowData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }
}