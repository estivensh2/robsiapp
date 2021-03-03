package com.alp.app.ui.perfil

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alp.app.R
import com.alp.app.databinding.PerfilDetalleFragmentBinding
import com.alp.app.servicios.Preferencias
import com.squareup.picasso.Picasso

class PerfilDetalleFragment : Fragment() {

    private lateinit var viewModel: PerfilDetalleViewModel
    private var _binding: PerfilDetalleFragmentBinding? = null
    private val binding get() = _binding!!
    val args: PerfilDetalleFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = PerfilDetalleFragmentBinding.inflate(inflater, container, false)
        Preferencias.init(requireContext(), "preferenciasDeUsuario")
        with(binding){
            Picasso.get().load(args.imagen).into(imagen)
            nombres.setText(args.nombres)
            apellidos.setText(args.apellidos)
            correoElectronico.setText(args.correoElectronico)
            claveAcceso.setText(args.claveAcceso)
            claveAcceso.isEnabled = false
            botonCerrarSesion.setOnClickListener { v ->
                Preferencias.eliminar("sesionActiva")
                Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_iniciar_o_crear)
            }
            botonCambiarClave.setOnClickListener { v ->
                Navigation.findNavController(v).navigate(R.id.accion_configuracion_a_cambiar_clave)
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PerfilDetalleViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_configuracion, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.guardar -> Toast.makeText(context, "Seleccionado $item", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}