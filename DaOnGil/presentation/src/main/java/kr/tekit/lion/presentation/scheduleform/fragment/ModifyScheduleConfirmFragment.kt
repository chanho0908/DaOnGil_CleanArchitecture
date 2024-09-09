package kr.tekit.lion.presentation.scheduleform.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.tekit.lion.domain.model.scheduleform.DailySchedule
import kr.tekit.lion.presentation.R
import kr.tekit.lion.presentation.databinding.FragmentModifyScheduleConfirmBinding
import kr.tekit.lion.presentation.ext.showSnackbar
import kr.tekit.lion.presentation.schedule.ResultCode
import kr.tekit.lion.presentation.scheduleform.FormDateFormat
import kr.tekit.lion.presentation.scheduleform.adapter.FormConfirmScheduleAdapter
import kr.tekit.lion.presentation.scheduleform.vm.ModifyScheduleFormViewModel

@AndroidEntryPoint
class ModifyScheduleConfirmFragment : Fragment(R.layout.fragment_modify_schedule_confirm) {

    private val viewModel: ModifyScheduleFormViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentModifyScheduleConfirmBinding.bind(view)

        initToolbar(binding)
        initView(binding)
    }

    private fun initToolbar(binding: FragmentModifyScheduleConfirmBinding) {
        binding.toolbarModifyConfirmForm.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView(binding: FragmentModifyScheduleConfirmBinding) {
        val title = viewModel.getScheduleTitle()
        val period = viewModel.formatPickedDates(FormDateFormat.YYYY_MM_DD)
        binding.apply {
            textMcfTitle.text = getString(R.string.text_selected_title, title)
            textMcfDate.text = getString(R.string.text_selected_period, period)
            buttonModifyFormSubmit.setOnClickListener {
                submitPlan(it)
            }
        }

        viewModel.schedule.observe(viewLifecycleOwner) {
            if (it != null) settingConfirmScheduleAdapter(binding, it)
        }
    }

    private fun settingConfirmScheduleAdapter(
        binding: FragmentModifyScheduleConfirmBinding,
        dailyScheduleList: List<DailySchedule>
    ) {
        binding.recyclerViewMcf.adapter = FormConfirmScheduleAdapter(dailyScheduleList)
    }

    private fun submitPlan(view: View) {
        viewModel.submitRevisedSchedule() { _, flag ->
            if (flag) {
                view.showSnackbar("여행 일정이 수정되었습니다")

                requireActivity().setResult(ResultCode.RESULT_SCHEDULE_EDIT)
                requireActivity().finish()
            } else {
                view.showSnackbar("다시 시도해 주세요")
            }
        }
    }
}