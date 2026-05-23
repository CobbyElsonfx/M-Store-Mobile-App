package com.momokotle.agent.presentation.submissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.models.FormSubmission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MySubmissionsViewModel @Inject constructor(
    formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    val submissions: StateFlow<List<FormSubmission>> = formSubmissionRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
