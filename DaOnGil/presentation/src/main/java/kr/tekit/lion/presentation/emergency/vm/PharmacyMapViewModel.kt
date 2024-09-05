package kr.tekit.lion.presentation.emergency.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.tekit.lion.domain.exception.onError
import kr.tekit.lion.domain.exception.onSuccess
import kr.tekit.lion.domain.model.PharmacyMapInfo
import kr.tekit.lion.domain.repository.NaverMapRepository
import kr.tekit.lion.domain.repository.PharmacyRepository
import kr.tekit.lion.presentation.delegate.NetworkErrorDelegate
import javax.inject.Inject

@HiltViewModel
class PharmacyMapViewModel @Inject constructor(
    private val naverMapRepository: NaverMapRepository,
    private val pharmacyRepository: PharmacyRepository
): ViewModel() {

    @Inject
    lateinit var networkErrorDelegate: NetworkErrorDelegate

    private val _area = MutableLiveData<String?>()
    val area : LiveData<String?> = _area

    private val _pharmacyMapInfo = MutableLiveData<List<PharmacyMapInfo>>()
    val pharmacyMapInfo : LiveData<List<PharmacyMapInfo>> = _pharmacyMapInfo

    fun getPharmacyMapInfo(Q0: String?, Q1: String?) =
        viewModelScope.launch {
            val areaDetail = if (Q0 == "세종특별자치시") null else Q1
            pharmacyRepository.getPharmacy(Q0, Q1).onSuccess {
                _pharmacyMapInfo.value = it
            }.onError {
                networkErrorDelegate.handleNetworkError(it)
            }
        }
    fun getUserLocationRegion(coords: String)= viewModelScope.launch {
        naverMapRepository.getReverseGeoCode(coords).onSuccess {
            if(it.code == 0){
                _area.value = "${it.results[0].area} ${it.results[0].areaDetail}"
            }
        }.onError {
            networkErrorDelegate.handleNetworkError(it)
        }
    }

    fun setArea(area: String?, areaDetail: String?) {
        if(areaDetail.isNullOrEmpty()){
            _area.value = "$area"
        } else {
            _area.value = "$area $areaDetail"
        }
    }
}