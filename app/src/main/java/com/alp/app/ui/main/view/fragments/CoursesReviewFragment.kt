package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alp.app.databinding.FragmentCoursesReviewBinding

class CoursesReviewFragment : Fragment() {

    private var _binding: FragmentCoursesReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var contexto: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesReviewBinding.inflate(inflater, container, false)
        val bundle = this.arguments
        if (bundle!= null) {
            val idcursox = bundle.getString("idcursox", "no")
            Toast.makeText(contexto, idcursox, Toast.LENGTH_SHORT).show()
        }
        val route = arrayOf("PYTHON","HTML", "CSS", "JAVASCRIPT")
        for(i in route) {
            val radioButton = RadioButton(contexto)
            radioButton.setText(i)
            binding.radio.addView(radioButton)
        }
        binding.validar.setOnClickListener { 
            val obtener = binding.radio.checkedRadioButtonId
            Toast.makeText(contexto, "$obtener", Toast.LENGTH_SHORT).show()
            if (obtener==3) {
                Toast.makeText(contexto, "si", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contexto, "error", Toast.LENGTH_SHORT).show()
            }

        }
        //recuperarExamenes()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contexto = context
    }
}