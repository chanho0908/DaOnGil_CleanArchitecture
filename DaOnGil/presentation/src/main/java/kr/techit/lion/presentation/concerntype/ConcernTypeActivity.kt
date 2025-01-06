package kr.techit.lion.presentation.concerntype

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.concerntype.vm.ConcernTypeViewModel

@AndroidEntryPoint
class ConcernTypeActivity : AppCompatActivity() {
    private val viewModel: ConcernTypeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concern_type)
        val nickName = intent.getStringExtra("nickName")
        nickName?.let { viewModel.setNickName(nickName) }
    }
}