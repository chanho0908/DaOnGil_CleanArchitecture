package kr.tekit.lion.presentation.myschedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.tekit.lion.presentation.myschedule.adapter.MyScheduleElapsedAdapter
import kr.tekit.lion.presentation.myschedule.adapter.MyScheduleUpcomingAdapter
import kr.tekit.lion.presentation.databinding.ActivityMyScheduleBinding
import kr.tekit.lion.presentation.delegate.NetworkState
import kr.tekit.lion.presentation.ext.addOnScrollEndListener
import kr.tekit.lion.presentation.ext.showSnackbar
import kr.tekit.lion.presentation.myschedule.vm.MyScheduleViewModel
import kr.tekit.lion.presentation.schedule.ResultCode
import kr.tekit.lion.presentation.schedule.ResultCode.RESULT_SCHEDULE_REVIEW_CANCELED
import kr.tekit.lion.presentation.schedule.ScheduleDetailActivity
import kr.tekit.lion.presentation.scheduleform.ScheduleFormActivity
import kr.tekit.lion.presentation.schedulereview.WriteScheduleReviewActivity

@AndroidEntryPoint
class MyScheduleActivity : AppCompatActivity() {
    private val viewModel: MyScheduleViewModel by viewModels()

    private val binding: ActivityMyScheduleBinding by lazy {
        ActivityMyScheduleBinding.inflate(layoutInflater)
    }

    private val scheduleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_SCHEDULE_REVIEW_CANCELED) return@registerForActivityResult

            // 일정 목록 갱신
            viewModel.refreshScheduleList()
            when (result.resultCode) {
                ResultCode.RESULT_REVIEW_WRITE -> {
                    binding.root.showSnackbar("후기가 저장되었습니다")
                }
                ResultCode.RESULT_SCHEDULE_WRITE -> {
                    binding.root.showSnackbar("일정이 저장되었습니다")
                }
                RESULT_OK -> {
                    binding.root.showSnackbar("일정이 삭제되었습니다")
                }
            }
        }

    private val upcomingAdapter by lazy {
        MyScheduleUpcomingAdapter { planPosition ->
            val planId = viewModel.getUpcomingPlanId(planPosition)
            if (planId != -1L) {
                navigateToScheduleDetailActivity(planId)
            }
        }
    }

    private val elapsedAdapter by lazy {
        MyScheduleElapsedAdapter(
            onReviewButtonClicked = { planPosition ->
                val planId = viewModel.getElapsedPlanId(planPosition)
                if (planId != -1L) {
                    val intent = Intent(this, WriteScheduleReviewActivity::class.java)
                    intent.putExtra("planId", planId)
                    scheduleLauncher.launch(intent)
                }
            },
            onScheduleItemClicked = { planPosition ->
                val planId = viewModel.getElapsedPlanId(planPosition)
                if (planId != -1L) {
                    navigateToScheduleDetailActivity(planId)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        initProgressBarState()

        settingToolbar()
        settingButtonClickListener()
        settingMyScheduleTab()
        settingUpcomingScheduleAdapter()
    }

    private fun initProgressBarState() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.networkState.collect { state ->
                    when(state){
                        is NetworkState.Loading -> {
                            progressBarMySchedule.visibility = View.VISIBLE
                        }
                        is NetworkState.Success -> {
                            progressBarMySchedule.visibility = View.GONE
                        }
                        is NetworkState.Error -> {
                            progressBarMySchedule.visibility = View.GONE
                            tabLayoutMySchedule.visibility = View.GONE
                            recyclerViewMyScheduleList.visibility = View.GONE
                            textMyScheduleError.apply {
                                text = state.msg
                                visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun settingToolbar() {
        binding.toolbarMySchedule.setNavigationOnClickListener {
            finish()
        }
    }

    private fun settingButtonClickListener() {
        binding.textMyScheduleAdd.setOnClickListener {
            val intent = Intent(this, ScheduleFormActivity::class.java)
            scheduleLauncher.launch(intent)
        }
    }

    private fun settingMyScheduleTab() {
        binding.tabLayoutMySchedule.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) { // TO DO - List 없을 때 처리할 것
                    0 -> settingUpcomingScheduleAdapter()
                    1 -> settingElapsedScheduleAdapter()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun settingUpcomingScheduleAdapter() {
        binding.recyclerViewMyScheduleList.apply {
            adapter = upcomingAdapter
            addOnScrollEndListener {
                with(viewModel) {
                    if (!isUpcomingLastPage()) {
                        fetchNextUpcomingSchedules()
                    }
                }
            }
        }

        viewModel.upcomingSchedules.observe(this@MyScheduleActivity) {
            if ((it?.size ?: 0) > 0) {

                hideScheduleEmptyPrompt()
                upcomingAdapter.submitList(it)
            } else {
                showScheduleEmptyPrompt()
            }
        }
    }

    private fun settingElapsedScheduleAdapter() {
        binding.recyclerViewMyScheduleList.apply {
            adapter = elapsedAdapter
            addOnScrollEndListener {
                with(viewModel) {
                    if (!isElapsedLastPage()) {
                        fetchNextElapsedSchedules()
                    }
                }
            }
        }

        viewModel.elapsedSchedules.observe(this@MyScheduleActivity) {
            if ((it?.size ?: 0) > 0) {
                hideScheduleEmptyPrompt()
                elapsedAdapter.submitList(it)
            } else {
                showScheduleEmptyPrompt()
            }
        }
    }

    private fun hideScheduleEmptyPrompt(){
        binding.layoutMyScheduleEmpty.visibility = View.GONE
        binding.recyclerViewMyScheduleList.visibility = View.VISIBLE
    }

    private fun showScheduleEmptyPrompt(){
        binding.layoutMyScheduleEmpty.visibility = View.VISIBLE
        binding.recyclerViewMyScheduleList.visibility = View.GONE
    }

    private fun navigateToScheduleDetailActivity(planId: Long) {
        val intent = Intent(this, ScheduleDetailActivity::class.java)
        intent.putExtra("planId", planId)
        scheduleLauncher.launch(intent)
    }

}