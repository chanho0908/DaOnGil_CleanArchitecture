package kr.tekit.lion.presentation.myreview.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.tekit.lion.presentation.R
import kr.tekit.lion.presentation.databinding.FragmentMyReviewBinding
import kr.tekit.lion.presentation.delegate.NetworkState
import kr.tekit.lion.presentation.ext.addOnScrollEndListener
import kr.tekit.lion.presentation.ext.repeatOnViewStarted
import kr.tekit.lion.presentation.ext.showSnackbar
import kr.tekit.lion.presentation.home.ReviewListActivity
import kr.tekit.lion.presentation.main.dialog.ConfirmDialog
import kr.tekit.lion.presentation.myreview.adapter.MyReviewRVAdapter
import kr.tekit.lion.presentation.myreview.vm.MyReviewViewModel

@AndroidEntryPoint
class MyReviewFragment : Fragment(R.layout.fragment_my_review) {

    private val viewModel: MyReviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMyReviewBinding.bind(view)

        with(binding) {
            repeatOnViewStarted {
                viewModel.networkState.collect { networkState ->
                    when (networkState) {
                        is NetworkState.Loading -> {
                            myReviewProgressBar.visibility = View.VISIBLE
                        }
                        is NetworkState.Success -> {
                            myReviewProgressBar.visibility = View.GONE
                            recyclerViewMyReview.visibility = View.VISIBLE
                        }
                        is NetworkState.Error -> {
                            myReviewProgressBar.visibility = View.GONE
                            recyclerViewMyReview.visibility = View.GONE
                            myReviewErrorLayout.visibility = View.VISIBLE
                            myReviewErrorMsg.text = networkState.msg
                        }
                    }
                }
            }
        }

        if (viewModel.isFromDetail.value == false) {
            viewModel.getMyPlaceReview()
        }

        settingToolbar(binding)
        settingMyReviewRVAdapter(binding)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPress()
        }
    }

    private fun settingToolbar(binding: FragmentMyReviewBinding) {
        binding.toolbarMyReview.setNavigationOnClickListener {
            handleBackPress()
        }
    }

    private fun settingMyReviewRVAdapter(binding: FragmentMyReviewBinding) {
        viewModel.myPlaceReview.observe(viewLifecycleOwner) { myPlaceReview ->
            if(myPlaceReview.myPlaceReviewInfoList.isNotEmpty()) {
                binding.notExistReviewLayout.visibility = View.GONE

                val myReviewRVAdapter = MyReviewRVAdapter(
                    myPlaceReview,
                    myPlaceReview.myPlaceReviewInfoList,
                    onMoveReviewListClick = { reviewPlaceId ->
                        val intent = Intent(requireContext(), ReviewListActivity::class.java)
                        intent.putExtra("reviewPlaceId", reviewPlaceId)
                        startActivity(intent)
                    },
                    onModifyClick = { myPlaceReviewInfo ->
                        viewModel.setReviewData(myPlaceReviewInfo)
                        findNavController().navigate(R.id.action_myReviewFragment_to_myReviewModifyFragment)
                    },
                    onDeleteClick = { reviewId ->
                        val deleteDialog = ConfirmDialog(
                            "여행지 후기 삭제",
                            "삭제한 데이터는 되돌릴 수 없습니다.",
                            "삭제하기"
                        ) {
                            viewModel.deleteMyPlaceReview(reviewId)

                            viewModel.snackbarEvent.observe(viewLifecycleOwner) { message ->
                                message?.let {
                                    requireContext().showSnackbar(requireView(), message)
                                    viewModel.resetSnackbarEvent()
                                }
                            }
                        }
                        deleteDialog.isCancelable = false
                        deleteDialog.show(requireActivity().supportFragmentManager, "ConfirmDialogTag")
                    }
                )

                val rvState = binding.recyclerViewMyReview.layoutManager?.onSaveInstanceState()
                binding.recyclerViewMyReview.adapter = myReviewRVAdapter
                binding.recyclerViewMyReview.layoutManager = LinearLayoutManager(context)
                rvState?.let {
                    binding.recyclerViewMyReview.layoutManager?.onRestoreInstanceState(it)
                }

                binding.recyclerViewMyReview.addOnScrollEndListener {
                    if (viewModel.isLastPage.value == false) {
                        viewModel.getNextMyPlaceReview()
                    }
                }
            } else {
                binding.recyclerViewMyReview.visibility = View.GONE
                binding.notExistReviewLayout.visibility = View.VISIBLE
                binding.textViewNotExistReview.text = getString(R.string.text_my_review)
            }
        }
    }

    private fun handleBackPress() {
        if (viewModel.isReviewDelete.value == true) {
            requireActivity().setResult(Activity.RESULT_OK)
        }
        requireActivity().finish()
    }
}