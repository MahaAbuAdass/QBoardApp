package com.example.slaughterhousescreen.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.slaughterhousescreen.DisplayMetricsHelper
import com.example.slaughterhousescreen.R
import com.example.slaughterhousescreen.databinding.FirstFragmentBinding
import com.example.slaughterhousescreen.util.PreferenceManager

class FirstScreen : Fragment() {

    private lateinit var binding: FirstFragmentBinding
    var checkBox : CheckBox ?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FirstFragmentBinding.inflate(inflater,container,false)


        if (PreferenceManager.isFirstLaunch(requireContext())) {
            showLaunchPopup()
        }

        // Check saved preference to conditionally call configuration
        if (PreferenceManager.isMediaPlayer(requireContext())) {
            // Call configuration for Media Player
            DisplayMetricsHelper.enforceDisplayMetrics(requireActivity())
        }

        // Button to show the popup again
       binding.tvType.setOnClickListener {
            showLaunchPopup() // Open the popup again
        }

        return binding.root
    }

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    private fun showLaunchPopup() {
        // Create the dialog with a custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_type, null)
        val switchDeviceType = dialogView.findViewById<Switch>(R.id.switch_btn)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel)
        val saveButton = dialogView.findViewById<Button>(R.id.btn_save)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView) // Custom layout with Switch
            .create()

        // Handle the "Cancel" button click
        cancelButton.setOnClickListener {
            dialog.dismiss() // Close the dialog when "Cancel" is clicked
        }

        // Handle the "Save" button click
        saveButton.setOnClickListener {
            val isMediaPlayer = switchDeviceType.isChecked
            // Save the user's choice
            PreferenceManager.saveMediaPlayerChoice(requireContext(), isMediaPlayer)
            // Mark that the first launch is complete
            PreferenceManager.setFirstLaunchFlag(requireContext())

            // Apply configuration if Media Player is selected
            if (isMediaPlayer) {
                DisplayMetricsHelper.enforceDisplayMetrics(requireActivity())
            }
            dialog.dismiss() // Close the dialog after saving the choice
        }

        // Show the dialog
        dialog.show()
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        checkAddedURL()


        binding.btnSubmit.setOnClickListener {
            checkUrlAndNavigate()
        }


        var storedUrl = PreferenceManager.getUrl(requireContext())
        binding.etUrl.setText(storedUrl)

        val storedCode = PreferenceManager.getBranchCode(requireContext())
        binding.etBranchCode.setText(storedCode)

        val storedDisplayNumer = PreferenceManager.getDisplayNumber(requireContext())
       // binding.etDisplayNum.setText(storedDisplayNumer)

        // Add touch listener to hide keyboard when clicking outside the EditText
        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val v = requireActivity().currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        hideKeyboard()
                    }
                }
            }
            false
        }
    }

    private fun refreshFragment() {
        parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }


    private fun checkAddedURL() {
        val isAddedUrl = PreferenceManager.isAddedURL(requireContext())
        Log.v("is saved", isAddedUrl.toString())

        if (isAddedUrl) {
            // Navigate to the Details screen if logged in
            findNavController().navigate(R.id.action_first_fragment_to_homeFragment)
        }
    }



    private fun checkUrlAndNavigate() {
        if (isEditTextEmpty(binding.etUrl) || isEditTextEmpty(binding.etBranchCode)
            //|| isEditTextEmpty(binding.etDisplayNum)
            )  {
            Toast.makeText(requireContext(), "Please fill the URL , branch code and display number", Toast.LENGTH_SHORT).show()
        } else {
            val branchCode = binding.etBranchCode.text.toString()
            PreferenceManager.saveBranchCode(requireContext(), branchCode,true)

          //  val displayNumber = binding.etDisplayNum.text.toString()
            //PreferenceManager.saveDisplayNumber(requireContext(),displayNumber,true)


            val url = binding.etUrl.text.toString().trim()
            // Check if the URL starts with http:// or https:// and ends with /
            if (!(url.startsWith("http://") || url.startsWith("https://")) || !url.endsWith("/")) {
                Toast.makeText(
                    requireContext(),
                    "URL must start with 'http://' or 'https://' and end with '/'",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                PreferenceManager.saveBaseUrl(requireContext(), url, true)
                PreferenceManager.saveUrl(requireContext(), url, true)

                findNavController().navigate(FirstScreenDirections.actionFirstFragmentToHomeFragment())


            }
        }
    }

    fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireActivity())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun isEditTextEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }
}