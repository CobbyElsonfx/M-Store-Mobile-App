package com.momokotle.agent.presentation.submissions;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.data.form.DocumentSlotRegistry;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import com.momokotle.agent.ui.theme.AppDimens;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0003\u001a4\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00030\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a\u001e\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\tH\u0003\u001a(\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00052\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\b\b\u0002\u0010\f\u001a\u00020\u0014H\u0007\u001a\u0010\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0017H\u0007\u001a\u0010\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u001aH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"dateFormatter", "Ljava/text/SimpleDateFormat;", "DetailRow", "", "label", "", "value", "MySubmissionsScreen", "onBack", "Lkotlin/Function0;", "onSubmissionClick", "Lkotlin/Function1;", "viewModel", "Lcom/momokotle/agent/presentation/submissions/MySubmissionsViewModel;", "SubmissionCard", "submission", "Lcom/momokotle/agent/models/FormSubmission;", "onClick", "SubmissionDetailScreen", "submissionId", "Lcom/momokotle/agent/presentation/submissions/SubmissionDetailViewModel;", "SubmissionStatusBadge", "status", "Lcom/momokotle/agent/models/FormSubmissionStatus;", "formatDate", "timestamp", "", "app_debug"})
public final class MySubmissionsScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat dateFormatter = null;
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MySubmissionsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSubmissionClick, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.submissions.MySubmissionsViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SubmissionDetailScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String submissionId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.submissions.SubmissionDetailViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SubmissionCard(com.momokotle.agent.models.FormSubmission submission, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SubmissionStatusBadge(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.FormSubmissionStatus status) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DetailRow(java.lang.String label, java.lang.String value) {
    }
    
    private static final java.lang.String formatDate(long timestamp) {
        return null;
    }
}