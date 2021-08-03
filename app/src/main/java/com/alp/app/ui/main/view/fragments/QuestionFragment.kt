/*
 * *
 *  * Created by estiv on 12/07/21 10:26 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/07/21 01:22 AM
 *
 */

package com.alp.app.ui.main.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alp.app.R
import com.alp.app.data.model.DetailTopicModel
import com.alp.app.data.model.OptionsModel
import com.alp.app.databinding.FragmentQuestionBinding
import com.alp.app.databinding.TemplateCorrectQuestionBinding
import com.alp.app.utils.Functions
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"

class BlankFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private var param1: Int? = null
    private var param2: String? = null
    private var param3: ArrayList<OptionsModel>? = null
    private var param4: Int? = null
    private var param5: Int? = null
    private lateinit var functions : Functions
    private var count = 0
    private lateinit var contexto: Context
    private var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getParcelableArrayList(ARG_PARAM3)
            param4 = it.getInt(ARG_PARAM4)
            param5 = it.getInt(ARG_PARAM5)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        functions = Functions(contexto)
        with(binding){
            question.text = param2
            val layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
            for (i in param3!!.indices) {
                val radioButton = RadioButton(contexto)
                radioButton.text = param3!![i].option
                radioButton.id = param3!![i].id_option!!
                radioButton.buttonDrawable = null
                radioButton.setTextColor(ContextCompat.getColor(contexto, R.color.gray_300))
                radioButton.background = ContextCompat.getDrawable(contexto, R.drawable.ic_background_checked_option)
                radioButton.setPadding(40,20,20,20)
                radioButton.layoutParams = layoutParams
                layoutParams.setMargins(0,8,0,8)
                radioGroup.addView(radioButton)
            }
            check.setOnClickListener {
                val optionSelected = radioGroup.checkedRadioButtonId
                if(optionSelected == param4){
                    val list = listOf("¡Eres lo máximo!", "¡Correcto!", "¡Buen trabajo!")
                    loadBottomSheet(functions.stringRandom(list), R.drawable.ic_baseline_check_circle_24, true)
                } else {
                    val list = listOf("Ups, inténtalo de nuevo", "La respuesta es incorrecta", "Hmm, piénsalo de nuevo")
                    loadBottomSheet(functions.stringRandom(list), R.drawable.ic_baseline_cancel_24, false)
                }
            }
        }
        return binding.root
    }

    private fun loadBottomSheet(message: String, icon: Int, boolean: Boolean){
        val dialog = BottomSheetDialog(contexto)
        val bindingBottomSheet = TemplateCorrectQuestionBinding.inflate(layoutInflater, null, false)
        bindingBottomSheet.textResult.text = message
        if (boolean){
            count += 1
            functions.playSound(R.raw.correct)
            bindingBottomSheet.buttonResult.text = resources.getString(R.string.text_continue)
            bindingBottomSheet.buttonResult.setOnClickListener {
                dialog.dismiss()
                onButtonClickListener?.onButtonClicked(param5)
            }
        } else {
            count += 1
            functions.playSound(R.raw.wrong)
            bindingBottomSheet.buttonResult.text = resources.getString(R.string.text_retry)
            bindingBottomSheet.buttonResult.setOnClickListener {
                dialog.dismiss()
            }
        }
        bindingBottomSheet.imgResult.setImageResource(icon)
        dialog.setCancelable(false)
        dialog.setContentView(bindingBottomSheet.root)
        dialog.show()
    }

    interface OnButtonClickListener {
        fun onButtonClicked(index: Int?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onButtonClickListener = parentFragment as OnButtonClickListener?
        this.contexto = context
    }

    companion object {
        @JvmStatic fun newInstance(data: DetailTopicModel, position: Int) = BlankFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, data.id_question)
                putString(ARG_PARAM2, data.question)
                putParcelableArrayList(ARG_PARAM3, data.options as ArrayList)
                putInt(ARG_PARAM4, data.reply)
                putInt(ARG_PARAM5, position)
            }
        }
    }
}