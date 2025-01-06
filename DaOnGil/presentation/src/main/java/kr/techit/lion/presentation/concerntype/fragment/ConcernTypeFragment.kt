package kr.techit.lion.presentation.concerntype.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.concerntype.vm.ConcernTypeViewModel
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypeUiModel
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypes
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.databinding.FragmentConcernTypeBinding
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.isTallBackEnabled
import kr.techit.lion.presentation.ext.repeatOnViewStarted

@AndroidEntryPoint
class ConcernTypeFragment : Fragment(R.layout.fragment_concern_type) {
    private val viewModel: ConcernTypeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentConcernTypeBinding.bind(view)
        viewModel.getConcernType()
        settingToolbar(binding)
        moveConcernTypeModify(binding)

        repeatOnViewStarted {
            launch { collectConcernTypeState(binding) }
            launch { observeConnectivity(binding) }
            launch { collectUiState(binding) }
        }
    }

    private suspend fun collectUiState(binding: FragmentConcernTypeBinding) {
        viewModel.uiState.collect { uiState ->
            setNickName(binding, uiState.nickName)
            setSelectedConcernTypeImage(binding, uiState.savedConcernType)
        }
    }

    private suspend fun collectConcernTypeState(binding: FragmentConcernTypeBinding) {
        with(binding) {
            viewModel.networkState.collect { networkState ->
                when (networkState) {
                    is NetworkState.Loading -> {
                        concernTypeProgressBar.visibility = View.VISIBLE
                    }

                    is NetworkState.Success -> {
                        concernTypeProgressBar.visibility = View.GONE
                    }

                    is NetworkState.Error -> {
                        concernTypeProgressBar.visibility = View.GONE
                        concernTypeLayout.visibility = View.GONE
                        concernTypeDivider.visibility = View.GONE
                        concernTypeModifyLayout.visibility = View.GONE
                        concernTypeErrorLayout.visibility = View.VISIBLE
                        concernTypeErrorMsg.text = networkState.msg
                    }
                }
            }
        }
    }

    private suspend fun observeConnectivity(binding: FragmentConcernTypeBinding) {
        with(binding) {
            viewModel.connectivityStatus.collect { status ->
                when (status) {
                    ConnectivityStatus.Loading -> Unit
                    ConnectivityStatus.Available -> {
                        concernTypeLayout.visibility = View.VISIBLE
                        concernTypeDivider.visibility = View.VISIBLE
                        concernTypeModifyLayout.visibility = View.VISIBLE
                        concernTypeErrorLayout.visibility = View.GONE
                    }

                    is ConnectivityStatus.OnLost -> {
                        concernTypeLayout.visibility = View.GONE
                        concernTypeDivider.visibility = View.GONE
                        concernTypeModifyLayout.visibility = View.GONE
                        concernTypeErrorLayout.visibility = View.VISIBLE
                        concernTypeErrorMsg.text =
                            requireContext().getString(R.string.can_not_access_network)
                    }
                }
            }
        }
    }

    private fun settingToolbar(binding: FragmentConcernTypeBinding) {
        with(binding.toolbarConcernType) {
            setNavigationOnClickListener {
                requireActivity().finish()
            }
            setNavigationContentDescription(R.string.text_back_button)
        }
    }

    private fun setNickName(binding: FragmentConcernTypeBinding, nickName: String) {
        binding.textViewConcernTypeUseNickname.text =
            getString(R.string.concern_type_nickname, nickName)
    }

    private fun setSelectedConcernTypeImage(
        binding: FragmentConcernTypeBinding,
        concernType: ConcernTypeUiModel
    ) {
        with(binding) {
            concernType.selectedConcernTypes.forEach {
                when (it) {
                    ConcernTypes.Physical -> {
                        imageViewConcernTypePhysical.setImageResource(R.drawable.cc_selected_physical_disability_icon)
                    }
                    ConcernTypes.Child -> {
                        imageViewConcernTypeInfant.setImageResource(R.drawable.cc_selected_infant_family_icon)
                    }
                    ConcernTypes.Elderly -> {
                        imageViewConcernTypeElderly.setImageResource(R.drawable.cc_selected_elderly_people_icon)
                    }
                    ConcernTypes.Hear -> {
                        imageViewConcernTypeHearing.setImageResource(R.drawable.cc_selected_hearing_impairment_icon)
                    }
                    ConcernTypes.Visual -> {
                        imageViewConcernTypeVisual.setImageResource(R.drawable.cc_selected_visual_impairment_icon)
                    }
                }
            }
        }
        if (requireContext().isTallBackEnabled()) {
            settingDescriptions(binding, concernType)
        }
    }

    private fun settingDescriptions(binding: FragmentConcernTypeBinding, concernType: ConcernTypeUiModel) {
        val nicknameDescription = binding.textViewConcernTypeUseNickname.text.toString()
        val selectedDescriptions = StringBuilder()
        concernType.selectedConcernTypes.forEach {
            when (it) {
                ConcernTypes.Physical -> selectedDescriptions.append(getString(R.string.text_physical_disability))
                ConcernTypes.Child -> selectedDescriptions.append(getString(R.string.text_infant_family))
                ConcernTypes.Elderly -> selectedDescriptions.append(getString(R.string.text_elderly_person))
                ConcernTypes.Hear -> selectedDescriptions.append(getString(R.string.text_hearing_impairment))
                ConcernTypes.Visual -> selectedDescriptions.append(getString(R.string.text_visual_impairment))
            }
        }
        val combinedDescription = "$nicknameDescription $selectedDescriptions"
        binding.concernTypeLayout.contentDescription = combinedDescription
    }

    private fun moveConcernTypeModify(binding: FragmentConcernTypeBinding) {
        binding.buttonConcernType.setOnClickListener {
            findNavController().navigate(R.id.action_concernTypeFragment_to_concernTypeModifyFragment)
        }
    }
}