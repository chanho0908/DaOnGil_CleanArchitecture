package kr.tekit.lion.presentation.keyword.viewholder

import androidx.recyclerview.widget.RecyclerView
import kr.tekit.lion.domain.model.search.AutoCompleteKeyword
import kr.tekit.lion.presentation.databinding.ItemSearchSuggestionBinding

class SearchSuggestionsViewHolder(
    private val binding: ItemSearchSuggestionBinding,
    private val onClick: (String) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AutoCompleteKeyword){
        binding.root.setOnClickListener {
            onClick(item.keyword)
        }
        binding.tvKeyword.text = item.keyword
    }
}