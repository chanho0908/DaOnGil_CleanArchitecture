package kr.techit.lion.presentation.main.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.DialogThemeTempBinding

class ThemeTempDialog : DialogFragment(R.layout.dialog_theme_temp) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogThemeTempBinding.bind(view)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dialogThemeTempPositiveBtn.setOnClickListener {
            dismiss()
            setFragmentResult("completeButtonClick", bundleOf())
        }
    }
}