package kr.tekit.lion.presentation.main.adapter.multi_viewholder

import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kr.tekit.lion.presentation.R
import kr.tekit.lion.presentation.databinding.ItemListSearchAreaBinding
import kr.tekit.lion.presentation.ext.setClickEvent
import kr.tekit.lion.presentation.main.model.SortByLatest
import kr.tekit.lion.presentation.main.model.SortByLetter
import kr.tekit.lion.presentation.main.model.SortByPopularity

class ListSearchAreaViewHolder(
    private val uiScope: CoroutineScope,
    private val binding: ItemListSearchAreaBinding,
    private val onSelectArea: (String) -> Unit,
    private val onSelectSigungu: (String) -> Unit,
    private val onClickSearchButton: () -> Unit,
    private val onClickSortByLatestBtn: (String) -> Unit,
    private val onClickSortByPopularityBtn: (String) -> Unit,
    private val onClickSortByLetterBtn: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(areaList: List<String>, sigunguList: List<String>) {

        with(binding) {
            val defaultColor = root.context.getColor(R.color.select_area_text)
            val selectedColor = root.context.getColor(R.color.search_view_main)

            btnPopularity.setClickEvent(uiScope){
                onClickSortByPopularityBtn(SortByPopularity.sortCoed)
                btnPopularity.setTextColor(selectedColor)
                btnLatest.setTextColor(defaultColor)
                btnLetter.setTextColor(defaultColor)
            }

            btnLatest.setClickEvent(uiScope){
                onClickSortByLatestBtn(SortByLatest.sortCoed)
                btnLatest.setTextColor(selectedColor)
                btnLetter.setTextColor(defaultColor)
                btnPopularity.setTextColor(defaultColor)
            }

            btnLetter.setClickEvent(uiScope){
                onClickSortByLetterBtn(SortByLetter.sortCoed)
                btnLetter.setTextColor(selectedColor)
                btnPopularity.setTextColor(defaultColor)
                btnLatest.setTextColor(defaultColor)
            }

            val areaAdapter = ArrayAdapter(
                root.context,
                android.R.layout.simple_list_item_1,
                areaList
            )
            selectedArea.setAdapter(areaAdapter)
            selectedArea.doAfterTextChanged {
                if (it != null) {
                    detailAreaSelectLayout.visibility = View.VISIBLE
                    detailSelectedArea.text = null
                    onSelectArea(it.toString())
                }
            }
            val sigunguAdapter = ArrayAdapter(
                root.context,
                android.R.layout.simple_list_item_1,
                sigunguList
            )
            detailSelectedArea.setAdapter(sigunguAdapter)
            detailSelectedArea.doAfterTextChanged {
                if (it != null) {
                    onSelectSigungu(it.toString())
                }
            }

            btnSearch.setOnClickListener {
                onClickSearchButton()
            }
        }
    }
}