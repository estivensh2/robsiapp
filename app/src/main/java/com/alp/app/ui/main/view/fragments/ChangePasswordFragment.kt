package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alp.app.R
import com.alp.app.data.model.UpdatePasswordModel
import com.alp.app.databinding.FragmentChangePasswordBinding
import com.alp.app.singleton.PreferencesSingleton
import com.alp.app.ui.main.viewmodel.DashboardViewModel
import com.alp.app.utils.Functions
import com.alp.app.utils.Status
import com.google.android.material.textfield.TextInputEditText
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private lateinit var functions: Functions
    private val binding get() = _binding!!
    private lateinit var contexto: Context
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupShowData() {
        val actualPassword = binding.iECurrentPassword.text.toString()
        val newPassword = binding.iEConfirmedNewPassword.text.toString()
        dashboardViewModel.setPassword(actualPassword, newPassword , PreferencesSingleton.leer("id","0").toString()).observe(requireActivity(), Observer { response ->
            response?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        functions.showHideProgressBar(false, binding.progress)
                        resource.data?.let { data -> renderList(data) }
                    }
                    Status.ERROR   -> {
                        functions.showHideProgressBar(false, binding.progress)
                        DynamicToast.makeError(contexto, response.message!!, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        functions.showHideProgressBar(true, binding.progress)
                    }
                }
            }
        })
    }

    private fun renderList(data: Response<UpdatePasswordModel>) {
        val response = data.body()!!
        if (response.data == "1") {
            DynamicToast.makeSuccess(contexto, getString(R.string.texto_clave_cambiada), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.accion_cambiar_clave_a_perfil)
        } else {
            DynamicToast.makeError(contexto, resources.getString(R.string.texto_clave_incorrecta), Toast.LENGTH_LONG).show()
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
            iECurrentPassword.onChange           { habilitarBoton(item) }
            iENewPassword.onChange          { habilitarBoton(item) }
            iEConfirmedNewPassword.onChange { habilitarBoton(item) }
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun habilitarBoton(item: MenuItem) {
        with(binding){
            if (iECurrentPassword.length()>0){
                if (iENewPassword.length()>0 && iEConfirmedNewPassword.length()>0){
                    if (iENewPassword.length()<6 && iEConfirmedNewPassword.length()<6){
                        DynamicToast.makeError(contexto, resources.getString(R.string.texto_clave_minimo), Toast.LENGTH_LONG).show()
                    } else if (iENewPassword.text.toString()!=iEConfirmedNewPassword.text.toString()){
                        DynamicToast.makeError(contexto, resources.getString(R.string.texto_claves_no_coinciden), Toast.LENGTH_LONG).show()
                        item.isVisible = false
                    } else {
                        item.isVisible = true
                    }
                } else {
                    item.isVisible = false
                }
            } else {
                item.isVisible = false
            }
        }
    }

    private fun TextInputEditText.onChange(cb: (String) -> Unit) {
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