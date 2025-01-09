package kr.techit.lion.presentation.myinfo

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.ActivityDeleteUserBinding
import kr.techit.lion.presentation.delegate.NetworkEvent
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.repeatOnStarted
import kr.techit.lion.presentation.ext.showInfinitySnackBar
import kr.techit.lion.presentation.login.LoginActivity
import kr.techit.lion.presentation.main.dialog.ConfirmDialog
import kr.techit.lion.presentation.myinfo.vm.DeleteUserViewModel

@AndroidEntryPoint
class DeleteUserActivity : AppCompatActivity() {
    private val binding: ActivityDeleteUserBinding by lazy {
        ActivityDeleteUserBinding.inflate(layoutInflater)
    }
    private val viewModel: DeleteUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            btnDelete.setOnClickListener {
                val dialog = ConfirmDialog(
                    this@DeleteUserActivity.getString(R.string.text_member_withdrawal_title),
                    this@DeleteUserActivity.getString(R.string.text_member_withdrawal_message),
                    this@DeleteUserActivity.getString(R.string.text_member_withdrawal_confirm),
                ) {
                    viewModel.withdrawal {
                        startActivity(Intent(this@DeleteUserActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, "dialog")
            }
        }
        repeatOnStarted {
            viewModel.networkEvent.collect { event ->
                when (event) {
                    NetworkEvent.Loading -> Unit
                    NetworkEvent.Success -> {
                        startActivity(Intent(this@DeleteUserActivity, LoginActivity::class.java))
                        finish()
                    }
                    is NetworkEvent.Error -> {
                        showInfinitySnackBar(binding.root, event.msg)
                    }
                }
            }
        }
    }
}