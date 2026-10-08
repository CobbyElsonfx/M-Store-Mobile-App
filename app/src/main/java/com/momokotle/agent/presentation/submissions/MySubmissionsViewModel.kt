package com.momokotle.agent.presentation.submissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.models.FormSubmission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MySubmissionsViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    val submissions: StateFlow<List<FormSubmission>> = authRepository.observeCachedProfile()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                formSubmissionRepository.observeMySubmissions(userId)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class SubmissionDetailViewModel @Inject constructor(
    private val formSubmissionRepository: FormSubmissionRepository
) : ViewModel() {

    private val _submission = MutableStateFlow<FormSubmission?>(null)
    val submission: StateFlow<FormSubmission?> = _submission

    fun loadSubmission(submissionId: String) {
        viewModelScope.launch {
            _submission.value = formSubmissionRepository.getSubmission(submissionId)
        }
    }
}
