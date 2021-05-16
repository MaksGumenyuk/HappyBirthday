package com.example.happybirthday.view.detailsScreen

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.happybirthday.databinding.DetailsScreenFragmentBinding
import com.example.happybirthday.helpers.UriPathHelper
import com.example.happybirthday.viewModel.HappyBirthdayViewModel
import java.text.DateFormat
import java.util.*

class DetailsScreenFragment : Fragment() {

    private val REQUEST_CODE = 100
    private lateinit var viewModel: HappyBirthdayViewModel
    private lateinit var binding: DetailsScreenFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            requireActivity(),
            SavedStateViewModelFactory(activity?.application, requireActivity())
        ).get(HappyBirthdayViewModel::class.java)
        binding = DetailsScreenFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            navDirection = DetailsScreenFragmentDirections.openBirthdayScreenFragment()
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnNameChangeListener()
        binding.birthdayDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.setPictureBtn.setOnClickListener {
            getImageFromGallery()
        }
    }

    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        context?.let {
            DatePickerDialog(it, { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateString = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.time)
                viewModel.setDateOfBirthday(dateString)
            }, year, month, day)
        }?.apply { show() }
    }

    private fun setOnNameChangeListener() {
        binding.name.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val name: String = binding.name.text.toString()
                viewModel.setName(name)
                view.clearFocus()
                binding.name.clearFocus()
                return@OnKeyListener true
            }
            false
        })
        binding.name.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val name: String = binding.name.text.toString()
                viewModel.setName(name)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            binding.picture.setImageURI(data?.data)
            val imageUri = data?.data
            if (context != null && imageUri != null) {
                val filePath = UriPathHelper().getPath(context, imageUri)
                viewModel.setImageFilePath(filePath)
            }
        }
    }
}