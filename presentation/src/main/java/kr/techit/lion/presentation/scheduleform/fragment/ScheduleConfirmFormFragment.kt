package kr.techit.lion.presentation.scheduleform.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kr.techit.lion.domain.model.scheduleform.DailySchedule
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.FragmentScheduleConfirmFormBinding
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.setAccessibilityText
import kr.techit.lion.presentation.ext.showSnackbar
import kr.techit.lion.presentation.schedule.ResultCode
import kr.techit.lion.presentation.scheduleform.FormDateFormat.YYYY_MM_DD
import kr.techit.lion.presentation.scheduleform.adapter.FormConfirmScheduleAdapter
import kr.techit.lion.presentation.scheduleform.vm.ScheduleFormViewModel


class ScheduleConfirmFormFragment : Fragment(R.layout.fragment_schedule_confirm_form) {
    private val viewModel: ScheduleFormViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentScheduleConfirmFormBinding.bind(view)

        settingProgressBarVisibility(binding)

        initToolbar(binding)
        initConfirmView(binding)
        initButtonSubmitSchedule(binding)
    }

    private fun settingProgressBarVisibility(binding: FragmentScheduleConfirmFormBinding) {
        viewModel.resetNetworkState()

        with(binding) {
            lifecycleScope.launch {
                viewModel.networkState.collect{ state ->
                    when(state){
                        is NetworkState.Loading -> {
                            progressBarScf.visibility = View.VISIBLE
                        }
                        is NetworkState.Success -> {
                            progressBarScf.visibility = View.GONE
                        }
                        is NetworkState.Error -> {
                            progressBarScf.visibility = View.GONE
                            val errorMsg = state.msg.replace("\n ".toRegex(), "\n")
                            buttonScheduleFormSubmit.showSnackbar(errorMsg)
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar(binding: FragmentScheduleConfirmFormBinding) {
        binding.toolbarScheduleConfirmForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initConfirmView(binding: FragmentScheduleConfirmFormBinding){
        val title = viewModel.getScheduleTitle()
        val period = viewModel.getSchedulePeriod(YYYY_MM_DD)
        binding.apply {
            textScfTitle.text = getString(R.string.text_selected_title, title)
            textScfDate.apply {
                text = getString(R.string.text_selected_period, period)
                setAccessibilityText(viewModel.getSchedulePeriodAccessibilityText())
            }
        }

        viewModel.schedule.observe(viewLifecycleOwner) {
            if(it != null){
                settingConfirmScheduleAdapter(binding, it)
            }
        }
    }

    private fun settingConfirmScheduleAdapter(
        binding: FragmentScheduleConfirmFormBinding,
        dailyScheduleList: List<DailySchedule>
    ) {
        binding.recyclerViewScf.adapter = FormConfirmScheduleAdapter(dailyScheduleList)
    }

    private fun initButtonSubmitSchedule(binding: FragmentScheduleConfirmFormBinding){
        binding.buttonScheduleFormSubmit.setOnClickListener { _ ->
            viewModel.submitNewPlan{ _, requestFlag ->
                if(requestFlag){
                    requireActivity().setResult(ResultCode.RESULT_SCHEDULE_WRITE)
                    requireActivity().finish()
                }
            }
        }
    }
}