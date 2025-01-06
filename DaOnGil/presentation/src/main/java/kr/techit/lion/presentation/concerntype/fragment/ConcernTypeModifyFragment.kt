package kr.techit.lion.presentation.concerntype.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.concerntype.vm.ConcernTypeViewModel
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypeUiModel
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypes
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.databinding.FragmentConcernTypeModifyBinding
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.repeatOnViewStarted
import kr.techit.lion.presentation.ext.showInfinitySnackBar
import kr.techit.lion.presentation.ext.showSnackbar

class ConcernTypeModifyFragment : Fragment(R.layout.fragment_concern_type_modify) {
    private val viewModel: ConcernTypeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentConcernTypeModifyBinding.bind(view)

        initView(binding)

        repeatOnViewStarted {
            launch { collectConcernTypeModifyState(binding) }
            launch { collectConnectivity(binding) }
            launch { collectUiState(binding) }
        }
    }

    private suspend fun collectUiState(binding: FragmentConcernTypeModifyBinding){
        viewModel.uiState.collect{ uiState ->
            setSelectedConcernTypeImage(binding, uiState.selectedConcernType)
        }
    }

    private suspend fun collectConcernTypeModifyState(binding: FragmentConcernTypeModifyBinding) {
        viewModel.networkState.collect { networkState ->
            when (networkState) {
                is NetworkState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkState.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is NetworkState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    requireContext().showSnackbar(requireView(), networkState.msg)
                }
            }
        }
    }

    private suspend fun collectConnectivity(binding: FragmentConcernTypeModifyBinding) {
        with(binding) {
            viewModel.connectivityStatus.collect { connectivity ->
                when (connectivity) {
                    ConnectivityStatus.Loading -> Unit
                    ConnectivityStatus.Available -> buttonConcernTypeModify.isEnabled = true
                    is ConnectivityStatus.OnLost -> {
                        buttonConcernTypeModify.isEnabled = false
                        requireContext().showInfinitySnackBar(
                            buttonConcernTypeModify,
                            requireContext().getString(R.string.can_not_access_network)
                        )
                    }
                }
            }
        }
    }

    private fun initView(binding: FragmentConcernTypeModifyBinding) {
        with(binding) {
            toolbarConcernTypeModify.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbarConcernTypeModify.setNavigationContentDescription(R.string.text_back_button)

            imageViewConcernTypeModifyPhysical.setOnClickListener {
                viewModel.onChangeConcernType(ConcernTypes.Physical)
            }
            imageViewConcernTypeModifyVisual.setOnClickListener {
                viewModel.onChangeConcernType(ConcernTypes.Visual)
            }
            imageViewConcernTypeModifyHearing.setOnClickListener {
                viewModel.onChangeConcernType(ConcernTypes.Hear)
            }
            imageViewConcernTypeModifyInfant.setOnClickListener {
                viewModel.onChangeConcernType(ConcernTypes.Child)
            }
            imageViewConcernTypeModifyElderly.setOnClickListener {
                viewModel.onChangeConcernType(ConcernTypes.Elderly)
            }
            buttonConcernTypeModify.setOnClickListener {
                viewModel.updateConcernType{
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setSelectedConcernTypeImage(
        binding: FragmentConcernTypeModifyBinding,
        concernType: ConcernTypeUiModel
    ) {
        setDefaultConcernTypeImage(binding)
        with(binding) {
            concernType.selectedConcernTypes.forEach {
                when (it) {
                    ConcernTypes.Physical -> {
                        imageViewConcernTypeModifyPhysical.setImageResource(R.drawable.cc_selected_physical_disability_icon)
                    }

                    ConcernTypes.Child -> {
                        imageViewConcernTypeModifyInfant.setImageResource(R.drawable.cc_selected_infant_family_icon)
                    }

                    ConcernTypes.Elderly -> {
                        imageViewConcernTypeModifyElderly.setImageResource(R.drawable.cc_selected_elderly_people_icon)
                    }

                    ConcernTypes.Hear -> {
                        imageViewConcernTypeModifyHearing.setImageResource(R.drawable.cc_selected_hearing_impairment_icon)
                    }

                    ConcernTypes.Visual -> {
                        imageViewConcernTypeModifyVisual.setImageResource(R.drawable.cc_selected_visual_impairment_icon)
                    }
                }
            }
        }
        settingDescriptions(binding, concernType)
    }

    private fun setDefaultConcernTypeImage(binding: FragmentConcernTypeModifyBinding){
        with(binding){
            imageViewConcernTypeModifyPhysical.setImageResource(R.drawable.physical_no_select)
            imageViewConcernTypeModifyInfant.setImageResource(R.drawable.infant_family_no_select)
            imageViewConcernTypeModifyElderly.setImageResource(R.drawable.elderly_people_no_select)
            imageViewConcernTypeModifyHearing.setImageResource(R.drawable.hearing_no_select)
            imageViewConcernTypeModifyVisual.setImageResource(R.drawable.visual_no_select)
        }
    }

    private fun settingDescriptions(binding: FragmentConcernTypeModifyBinding, concernType: ConcernTypeUiModel) {
        val titleDescription = binding.textViewConcernTypeModifyTitle.text.toString()
        val selectedDescriptions = StringBuilder()
        concernType.selectedConcernTypes.forEach {
            when (it) {
                ConcernTypes.Physical -> selectedDescriptions.append("${getString(R.string.text_physical_disability)}\n")
                ConcernTypes.Child -> selectedDescriptions.append("${getString(R.string.text_infant_family)}\n")
                ConcernTypes.Elderly -> selectedDescriptions.append("${getString(R.string.text_elderly_person)}\n")
                ConcernTypes.Hear -> selectedDescriptions.append("${getString(R.string.text_hearing_impairment)}\n")
                ConcernTypes.Visual -> selectedDescriptions.append("${getString(R.string.text_visual_impairment)}\n")
            }
        }
        val combinedDescription = "$titleDescription, 현재 선택된 관심 유형은 $selectedDescriptions 입니다"
        binding.textViewConcernTypeModifyTitle.contentDescription = combinedDescription
    }
}