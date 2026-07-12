package com.arnstudios.capai.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnstudios.capai.data.local.PrefsManager
import com.arnstudios.capai.domain.model.CapAI
import com.arnstudios.capai.domain.model.CaptionResult
import com.arnstudios.capai.domain.model.Length
import com.arnstudios.capai.domain.usecase.GetImageCapUseCase
import com.arnstudios.capai.domain.usecase.RoomDatabaseOperationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapAiViewModel @Inject constructor(
    private val _getImageCaptionUseCase : GetImageCapUseCase,
    private val _getRoomDatabaseOpUseCase : RoomDatabaseOperationsUseCase,
    private val _prefsManager: PrefsManager
) :
    ViewModel(){
    private val _result = MutableStateFlow(CaptionResult())
    var result = _result.asStateFlow()

    var isOnboardingCompleted = _prefsManager.isOnboardingCompleted
    var onHistoryItemOpenedCount = _prefsManager.onHistoryItemOpenedCount

    fun setOnboardingCompleted() {
        _prefsManager.isOnboardingCompleted = true
        isOnboardingCompleted = true
    }

    fun setOnHistoryItemOpenedCount() {
        if(onHistoryItemOpenedCount == 2){
            _prefsManager.onHistoryItemOpenedCount = 0
            onHistoryItemOpenedCount = 0
        }else{
            _prefsManager.onHistoryItemOpenedCount++
            onHistoryItemOpenedCount = _prefsManager.onHistoryItemOpenedCount
        }
    }

    var imageUri = MutableStateFlow<Uri?>(null)
    private val _historyList = MutableStateFlow<List<CapAI>>(emptyList())
    val historyList = _historyList.asStateFlow()

    private var _hasCaptionBeenRequested = false
    init {
       getHistoryList()
    }

    fun prepareForCaptionGeneration() {
        _result.value = CaptionResult(isGenerating = true)
        _hasCaptionBeenRequested = false
    }

    fun getImageCaption(context: Context, length: Length) {
        if (_hasCaptionBeenRequested) return
        _hasCaptionBeenRequested = true
        viewModelScope.launch {
            _result.value = _result.value.copy(isGenerating = true)
            val caption = _getImageCaptionUseCase(imageUri.value, length, context)
            _result.value = _result.value.copy(capAI = caption, isGenerating = false)
            if(caption.isSuccess){
                addCaptionToHistory(caption)
            }
        }
    }

    fun addCaptionToHistory(capAI: CapAI){
        viewModelScope.launch {
            _getRoomDatabaseOpUseCase.insertCapAI(capAI,imageUri.value!!)
        }
    }

    fun deleteCaptionFromHistory(capAI: CapAI){
        viewModelScope.launch {
            _getRoomDatabaseOpUseCase.deleteCapAI(capAI)
        }
    }

    private fun getHistoryList(){
        viewModelScope.launch {
            _getRoomDatabaseOpUseCase.getAllCaptions().collect {
                _historyList.value = it
            }
        }
    }
}