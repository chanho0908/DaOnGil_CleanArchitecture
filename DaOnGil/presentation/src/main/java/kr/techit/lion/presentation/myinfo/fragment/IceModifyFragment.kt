package kr.techit.lion.presentation.myinfo.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.techit.lion.domain.model.IceInfo
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.FragmentIceModifyBinding
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.announceForAccessibility
import kr.techit.lion.presentation.ext.formatBirthday
import kr.techit.lion.presentation.ext.formatPhoneNumber
import kr.techit.lion.presentation.ext.isBirthdayValid
import kr.techit.lion.presentation.ext.isPhoneNumberValid
import kr.techit.lion.presentation.ext.isTallBackEnabled
import kr.techit.lion.presentation.ext.pronounceEachCharacter
import kr.techit.lion.presentation.ext.repeatOnViewStarted
import kr.techit.lion.presentation.ext.setAccessibilityText
import kr.techit.lion.presentation.ext.showSoftInput
import kr.techit.lion.presentation.myinfo.event.MyInfoEvent
import kr.techit.lion.presentation.myinfo.vm.MyInfoViewModel

@AndroidEntryPoint
class IceModifyFragment : Fragment(R.layout.fragment_ice_modify) {

    private val viewModel: MyInfoViewModel by activityViewModels()
    private val myInfoAnnounce = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentIceModifyBinding.bind(view)

        initMyInfo(binding)
        initTextField(binding)
        handleTextFieldEditorActions(binding)
        initializeListener(binding)
    }

    private fun initializeListener(binding: FragmentIceModifyBinding) {
        with(binding) {
            if (requireContext().isTallBackEnabled()) setupAccessibility(binding)
            else toolbarIceModify.menu.clear()
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonIceSubmit.setOnClickListener {
                if (isFormValid(binding)) {
                    repeatOnViewStarted { observeState(binding) }
                    modifyUiEvent(binding)
                }
            }
        }
    }

    private fun modifyUiEvent(binding: FragmentIceModifyBinding) {
        with(binding) {
            viewModel.onChangeUiEvent(
                MyInfoEvent.OnUiEventModifyIceInfo(
                    IceInfo(
                        birth = tvBirth.text.toString(),
                        bloodType = tvBloodType.text.toString(),
                        disease = tvDisease.text.toString(),
                        allergy = tvAllergy.text.toString(),
                        medication = tvMedicine.text.toString(),
                        part1Rel = tvRelation1.text.toString(),
                        part1Phone = tvContact1.text.toString(),
                        part2Rel = tvRelation2.text.toString(),
                        part2Phone = tvContact2.text.toString()
                    )
                )
            )
        }
    }

    private suspend fun observeState(binding: FragmentIceModifyBinding) {
        viewModel.state.collect { state ->
            observeNetworkState(binding, state.iceModifyNetworkState)
        }
    }

    private fun observeNetworkState(binding: FragmentIceModifyBinding, state: NetworkState) {
        with(binding) {
            when (state) {
                is NetworkState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    buttonIceSubmit.isEnabled = false
                }

                is NetworkState.Success -> {
                    progressBar.visibility = View.GONE
                    showSnackbar(this@with, "나의 응급 정보가 수정 되었습니다.")
                    findNavController().popBackStack()
                }

                is NetworkState.Error -> {
                    progressBar.visibility = View.GONE
                    buttonIceSubmit.isEnabled = true
                    showSnackbar(binding, state.msg)
                }
            }
        }
    }

    private fun initTextField(binding: FragmentIceModifyBinding) {
        val bloodType =
            resources.getStringArray(R.array.blood_type).map { it.pronounceEachCharacter() }
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item_blood_type, bloodType)

        with(binding.tvBloodType) {
            setDropDownBackgroundResource(R.color.background_color)
            setAdapter(arrayAdapter)

            setOnClickListener {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                showDropDown()
            }
        }

        if (requireContext().isTallBackEnabled()) setTextFieldAccessibility(binding)
    }

    private fun setTextFieldAccessibility(binding: FragmentIceModifyBinding) {
        with(binding) {
            tvBirth.doAfterTextChanged {
                if (it.isNullOrBlank()) tvBirth.setAccessibilityText(getString(R.string.text_plz_enter_birth))
                else tvBirth.setAccessibilityText(it.toString().formatBirthday())
            }

            tvBloodType.doAfterTextChanged {
                tvBloodTypeTitle.setAccessibilityText(
                    getString(R.string.text_blood_type) + it.toString()
                )
            }

            tvDisease.doAfterTextChanged {
                if (it.isNullOrBlank()) tvDisease.setAccessibilityText(getString(R.string.text_plz_enter_disease))
                else tvDisease.setAccessibilityText(it)
            }

            tvAllergy.doAfterTextChanged {
                if (it.isNullOrBlank()) tvAllergy.setAccessibilityText(getString(R.string.text_plz_enter_allergy))
                else tvAllergy.setAccessibilityText(it)
            }

            tvMedicine.doAfterTextChanged {
                if (it.isNullOrBlank()) tvMedicine.setAccessibilityText(getString(R.string.text_plz_enter_medicine))
                else tvMedicine.setAccessibilityText(it)
            }

            tvRelation1.doAfterTextChanged {
                if (it.isNullOrBlank()) tvRelation1.setAccessibilityText(getString(R.string.text_plz_enter_relation))
                else tvRelation1.setAccessibilityText(it)
            }

            tvContact1.doAfterTextChanged {
                if (it.isNullOrBlank()) tvContact1.setAccessibilityText(getString(R.string.text_plz_enter_emergency_contact))
                else tvContact1.setAccessibilityText(it.toString().formatPhoneNumber())
            }

            tvRelation2.doAfterTextChanged {
                if (it.isNullOrBlank()) tvRelation2.setAccessibilityText(getString(R.string.text_plz_enter_relation))
                else tvRelation2.setAccessibilityText(it)
            }

            tvContact2.doAfterTextChanged {
                if (it.isNullOrBlank()) tvContact2.setAccessibilityText(getString(R.string.text_plz_enter_emergency_contact))
                else tvContact2.setAccessibilityText(it.toString().formatPhoneNumber())
            }
        }
    }

    private fun initMyInfo(binding: FragmentIceModifyBinding) {
        with(binding) {
            val currentMyInfo = viewModel.state.value.iceInfo
            tvBirth.setText(currentMyInfo.birth)
            tvBloodType.setText(currentMyInfo.bloodType)
            tvDisease.setText(currentMyInfo.disease)
            tvAllergy.setText(currentMyInfo.allergy)
            tvMedicine.setText(currentMyInfo.medication)
            tvRelation1.setText(currentMyInfo.part1Rel)
            tvContact1.setText(currentMyInfo.part1Phone)
            tvRelation2.setText(currentMyInfo.part2Rel)
            tvContact2.setText(currentMyInfo.part2Phone)
            if (requireContext().isTallBackEnabled()) setTallBack(binding, currentMyInfo)
        }
    }

    private fun setTallBack(binding: FragmentIceModifyBinding, iceInfo: IceInfo) {
        with(binding) {
            tvBirthTitle.setAccessibilityText(
                if (iceInfo.birth.isEmpty()) "${tvBirthTitle.text} ${getString(R.string.text_plz_enter_birth)}"
                else "${tvBirthTitle.text} ${iceInfo.birth.formatBirthday()}"
            )

            tvBirth.setAccessibilityText(
                if (iceInfo.birth.isEmpty()) "${tvBirthTitle.text} ${getString(R.string.text_plz_enter_birth)}"
                else "${tvBirthTitle.text} ${iceInfo.birth.formatBirthday()}"
            )

            tvBloodTypeTitle.setAccessibilityText(
                if (iceInfo.bloodType.isEmpty()) "${tvBloodTypeTitle.text} ${getString(R.string.text_plz_enter_blood_type)}"
                else "${tvBloodTypeTitle.text} ${iceInfo.bloodType}"
            )

            tvDiseaseTitle.setAccessibilityText(
                if (iceInfo.disease.isEmpty()) "${tvDiseaseTitle.text} ${getString(R.string.text_plz_enter_disease)}"
                else "${tvDiseaseTitle.text} ${iceInfo.disease}}"
            )

            tvDisease.setAccessibilityText(
                if (iceInfo.disease.isEmpty()) "${tvDiseaseTitle.text} ${getString(R.string.text_plz_enter_disease)}"
                else "${tvDiseaseTitle.text} ${iceInfo.disease}}"
            )

            tvAllergyTitle.setAccessibilityText(
                if (iceInfo.allergy.isEmpty()) "${tvAllergyTitle.text} ${getString(R.string.text_plz_enter_allergy)}"
                else "${tvAllergyTitle.text} ${iceInfo.allergy}"
            )

            tvAllergy.setAccessibilityText(
                if (iceInfo.allergy.isEmpty()) "${tvAllergyTitle.text} ${getString(R.string.text_plz_enter_allergy)}"
                else "${tvAllergyTitle.text} ${iceInfo.allergy}"
            )

            tvMedicineTitle.setAccessibilityText(
                if (iceInfo.medication.isEmpty()) "${tvMedicineTitle.text} ${getString(R.string.text_plz_enter_medicine)}"
                else "${tvMedicineTitle.text} ${iceInfo.medication}"
            )
            tvMedicine.setAccessibilityText(
                if (iceInfo.medication.isEmpty()) "${tvMedicineTitle.text} ${getString(R.string.text_plz_enter_medicine)}"
                else "${tvMedicineTitle.text} ${iceInfo.medication}"
            )

            tvRelation1.setAccessibilityText(
                if (iceInfo.part1Rel.isEmpty()) getString(R.string.text_plz_enter_relation)
                else "${iceInfo.part1Rel} ${iceInfo.part1Phone.formatPhoneNumber()}"
            )

            tvContact1.setAccessibilityText(
                if (iceInfo.part1Rel.isEmpty()) getString(R.string.text_plz_enter_relation)
                else "${iceInfo.part1Rel} ${iceInfo.part1Phone.formatPhoneNumber()}"
            )
            tvRelation2.setAccessibilityText(
                if (iceInfo.part2Rel.isEmpty()) getString(R.string.text_plz_enter_relation)
                else "${iceInfo.part2Rel} ${iceInfo.part2Phone.formatPhoneNumber()}"
            )
            tvContact2.setAccessibilityText(
                if (iceInfo.part2Rel.isEmpty()) getString(R.string.text_plz_enter_relation)
                else "${iceInfo.part2Rel} ${iceInfo.part2Phone.formatPhoneNumber()}"
            )

            myInfoAnnounce.append(getString(R.string.text_birth))
            myInfoAnnounce.append(
                if (iceInfo.birth.isEmpty()) getString(R.string.text_plz_enter_birth)
                else iceInfo.birth.formatBirthday()
            )
            myInfoAnnounce.append(getString(R.string.text_blood_type))
            myInfoAnnounce.append(
                if (iceInfo.bloodType.isEmpty()) {
                    getString(R.string.text_plz_enter_blood_type)
                } else iceInfo.bloodType
            )
            myInfoAnnounce.append(getString(R.string.text_disease))
            myInfoAnnounce.append(
                if (iceInfo.disease.isEmpty()) getString(R.string.text_plz_enter_disease)
                else iceInfo.disease
            )
            myInfoAnnounce.append(getString(R.string.text_allergy))
            myInfoAnnounce.append(
                if (iceInfo.allergy.isEmpty()) getString(R.string.text_plz_enter_allergy)
                else iceInfo.allergy
            )
            myInfoAnnounce.append(getString(R.string.text_medicine))
            myInfoAnnounce.append(
                if (iceInfo.medication.isEmpty()) getString(R.string.text_plz_enter_medicine)
                else iceInfo.medication
            )
            myInfoAnnounce.append(getString(R.string.text_emergency_contact))
            myInfoAnnounce.append(
                if (iceInfo.part1Rel.isEmpty()) getString(R.string.text_relation)
                else iceInfo.part1Rel
            )
            myInfoAnnounce.append(
                if (iceInfo.part1Phone.isEmpty()) getString(R.string.text_contact_ex)
                else iceInfo.part1Phone.formatPhoneNumber()
            )
            myInfoAnnounce.append(
                if (iceInfo.part2Rel.isEmpty()) getString(R.string.text_relation)
                else iceInfo.part2Rel.formatPhoneNumber()
            )
        }
    }

    private fun handleTextFieldEditorActions(binding: FragmentIceModifyBinding) {
        with(binding) {
            tvBirth.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
                ) {
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    tvBirth.clearFocus()
                    true
                } else {
                    false
                }
            }

            tvRelation1.setOnEditorActionListener { _, _, _ ->
                tvContact1.requestFocus()
                true
            }

            tvContact1.setOnEditorActionListener { _, _, _ ->
                tvContact2.requestFocus()
                true
            }

            with(tvRelation2) {
                imeOptions = EditorInfo.IME_ACTION_NEXT
                setOnEditorActionListener { _, _, _ ->
                    tvContact2.requestFocus()
                    true
                }
            }
        }
    }

    private fun setupAccessibility(binding: FragmentIceModifyBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            requireActivity().announceForAccessibility(
                getString(R.string.text_script_guide_for_my_info) +
                        getString(R.string.text_script_read_all_text)
            )
        }
        myInfoAnnounce.append(getString(R.string.text_personal_info))

        binding.toolbarIceModify.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.read_script -> {
                    requireActivity().announceForAccessibility(
                        getString(R.string.text_script_for_modify_my_info_main)
                    )
                    true
                }

                R.id.read_info -> {
                    requireActivity().announceForAccessibility(myInfoAnnounce.toString())
                    true
                }

                else -> false
            }
        }
    }

    private fun showSnackbar(binding: FragmentIceModifyBinding, message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.text_secondary))
            .show()
    }

    private fun isFormValid(binding: FragmentIceModifyBinding): Boolean {
        with(binding) {
            var isValid = true
            var firstInvalidField: View? = null

            val birthday = tvBirth.text.toString()
            if (birthday.isNotBlank() && !birthday.isBirthdayValid()) {
                val errorMessage =
                    getString(R.string.text_plz_enter_collect_birth_type) + "\n" +
                            getString(R.string.text_birth_ex)
                textInputLayoutBirthday.error = errorMessage
                firstInvalidField = tvBirth
                announceError(errorMessage)
                isValid = false
            }

            val phoneNumber1 = tvContact1.text.toString()
            val relation1 = tvRelation1.text.toString()

            if (relation1.isEmpty() && phoneNumber1.isNotEmpty()) {
                val errorMessage = getString(R.string.text_plz_enter_relation)
                tvRelation1.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvRelation1
                }
                announceError(errorMessage)
                isValid = false
            } else if (relation1.isNotEmpty() && phoneNumber1.isEmpty()) {
                val errorMessage = getString(R.string.text_plz_enter_emergency_contact)
                tvContact1.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvContact1
                }
                announceError(errorMessage)
                isValid = false
            } else if (relation1.isNotEmpty() && phoneNumber1.isNotEmpty() && !phoneNumber1.isPhoneNumberValid()) {
                val errorMessage =
                    getString(R.string.text_plz_enter_collect_phone_type) + "\n" +
                            getString(R.string.text_contact_ex)
                tvContact1.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvContact1
                }
                announceError(errorMessage)
                isValid = false
            }

            val phoneNumber2 = tvContact2.text.toString()
            val relation2 = tvRelation2.text.toString()

            if (relation2.isEmpty() && phoneNumber2.isNotEmpty()) {
                val errorMessage = getString(R.string.text_plz_enter_relation)
                tvRelation2.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvRelation2
                }
                announceError(errorMessage)
                isValid = false
            } else if (relation2.isNotEmpty() && phoneNumber2.isEmpty()) {
                val errorMessage = getString(R.string.text_plz_enter_emergency_contact)
                tvContact2.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvContact2
                }
                announceError(errorMessage)
                isValid = false
            } else if (relation2.isNotEmpty() && phoneNumber2.isNotEmpty() && !phoneNumber2.isPhoneNumberValid()) {
                val errorMessage =
                    getString(R.string.text_plz_enter_collect_phone_type) + "\n" +
                            getString(R.string.text_contact_ex)
                tvContact2.error = errorMessage
                if (firstInvalidField == null) {
                    firstInvalidField = tvContact2
                }
                announceError(errorMessage)
                isValid = false
            }

            if (!isValid && firstInvalidField != null) {
                firstInvalidField.requestFocus()
                context?.showSoftInput(firstInvalidField)
            }
            return isValid
        }
    }

    private fun announceError(errorMessage: String) {
        if (requireContext().isTallBackEnabled()) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2500)
                requireActivity().announceForAccessibility(errorMessage)
            }
        }
    }
}