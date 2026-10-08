package com.momokotle.agent.presentation.referral

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.usecase.ObserveReferralInfoUseCase
import com.momokotle.agent.domain.usecase.ObserveReferralsUseCase
import com.momokotle.agent.domain.usecase.ObserveWalletTransactionsUseCase
import com.momokotle.agent.models.ReferralInfo
import com.momokotle.agent.models.ReferralRecord
import com.momokotle.agent.models.WalletTransactionRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ReferralCenterUiState(
    val referralInfo: ReferralInfo? = null,
    val referrals: List<ReferralRecord> = emptyList(),
    val transactions: List<WalletTransactionRecord> = emptyList(),
    val referralCode: String = "",
    val referralLink: String = ""
)

@HiltViewModel
class ReferralViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeReferralInfoUseCase: ObserveReferralInfoUseCase,
    private val observeReferralsUseCase: ObserveReferralsUseCase,
    private val observeWalletTransactionsUseCase: ObserveWalletTransactionsUseCase
) : ViewModel() {

    val uiState: StateFlow<ReferralCenterUiState> = authRepository.observeAuthState()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) {
                flowOf(ReferralCenterUiState())
            } else {
                combine(
                    observeReferralInfoUseCase(userId),
                    observeReferralsUseCase(userId),
                    observeWalletTransactionsUseCase(userId)
                ) { info, referrals, transactions ->
                    val code = profile.referralCode
                    val link = buildReferralLink(code, info?.referralLink.orEmpty())
                    ReferralCenterUiState(
                        referralInfo = info?.copy(referralCode = code, referralLink = link),
                        referrals = referrals,
                        transactions = transactions,
                        referralCode = code,
                        referralLink = link
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReferralCenterUiState())

    fun shareReferralLink(context: Context, link: String) {
        if (link.isBlank()) return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Join MoMo Kotle using my referral link: $link")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(
            Intent.createChooser(intent, "Share referral link").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    fun copyToClipboard(context: Context, label: String, value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
    }

    private fun buildReferralLink(code: String, base: String): String {
        if (code.isBlank()) return base
        return if (base.endsWith("/")) "$base$code" else "$base/$code"
    }
}
