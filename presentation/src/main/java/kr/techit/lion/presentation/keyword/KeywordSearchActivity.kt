package kr.techit.lion.presentation.keyword

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.ActivityKeywordSearchBinding
import kr.techit.lion.presentation.keyword.fragment.SearchResultFragment
import kr.techit.lion.presentation.keyword.vm.model.KeywordInputStatus
import kr.techit.lion.presentation.keyword.vm.KeywordSearchViewModel

@AndroidEntryPoint
class KeywordSearchActivity : AppCompatActivity() {
    private lateinit var backPressedCallback: OnBackPressedCallback
    private val viewModel: KeywordSearchViewModel by viewModels()
    private val binding: ActivityKeywordSearchBinding by lazy {
        ActivityKeywordSearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleBackPress()
        settingSearchState()
    }

    private fun settingSearchState() {
        setToolbarListener()
        setSearchTextEditListener()
        setSearchBarListener()
    }

    private fun setSearchTextEditListener() {
        binding.searchEdit.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                viewModel.keywordInputStateChanged(KeywordInputStatus.Empty)
                if (isSearchResultFragment()) {
                    moveToBackStack()
                }
            } else if (it.length >= 2) {
                viewModel.inputTextChanged(it.toString())
                viewModel.keywordInputStateChanged(KeywordInputStatus.NotEmpty)
            } else if (it.length < 2) {
                viewModel.keywordInputStateChanged(KeywordInputStatus.Erasing)
            }
        }
    }

    private fun setSearchBarListener() {
        binding.saerchBarContainer.setOnClickListener {
            binding.searchEdit.text = null
            moveToBackStack()
        }
    }

    private fun setToolbarListener() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                if (isSearchResultFragment()) {
                    searchEdit.text = null
                } else {
                    finish()
                }
            }

            searchEdit.setOnEditorActionListener { _, actionId, event ->
                val isImeActionDone = (event == null && actionId == EditorInfo.IME_ACTION_DONE)
                val isEnterKeyPressed =
                    (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)

                if (isImeActionDone || isEnterKeyPressed) {
                    val keyword = searchEdit.text.toString()
                    if (keyword.isEmpty()) {
                        Snackbar.make(root, getString(R.string.hint_search), Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.insertKeyword(searchEdit.text.toString())
                        lifecycleScope.launch(Dispatchers.Main) {
                            findNavController(R.id.fragment_container_view).navigate(
                                R.id.action_to_searchResultFragment,
                                bundleOf("searchText" to searchEdit.text.toString())
                            )
                        }
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun moveToBackStack() {
        findNavController(R.id.fragment_container_view).navigate(R.id.action_to_onSearchFragment)
    }

    private fun isSearchResultFragment(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]
        return currentFragment is SearchResultFragment
    }

    private fun handleBackPress() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isSearchResultFragment()) {
                    binding.searchEdit.text = null
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
    }
}
