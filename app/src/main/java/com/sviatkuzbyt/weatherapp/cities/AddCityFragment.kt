package com.sviatkuzbyt.weatherapp.cities


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sviatkuzbyt.weatherapp.R


class AddCityFragment(val activity: CitiesActivity) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        val editTextTextCity = view.findViewById<EditText>(R.id.editTextTextCity)
        val addBtnCity = view.findViewById<Button>(R.id.addBtnCity)
        getActivity()?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        addBtnCity.setOnClickListener {
            activity.addRecord(editTextTextCity.text.toString())
            dismiss()
        }


        editTextTextCity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity.addRecord(if (editTextTextCity.text.isNotEmpty()) editTextTextCity.text.toString()
                else "Empty")
                dismiss()
            }
            false
        })
    }
    }
