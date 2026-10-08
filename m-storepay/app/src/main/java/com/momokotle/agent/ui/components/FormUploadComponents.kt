package com.momokotle.agent.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.momokotle.agent.ui.theme.AppDimens
import java.io.File

@Composable
fun DocumentUploadField(
    label: String,
    instruction: String? = null,
    selectedFileName: String?,
    localPath: String? = null,
    errorText: String? = null,
    isValid: Boolean = false,
    showValidation: Boolean = false,
    showImagePreview: Boolean = false,
    onPick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showError = showValidation && !errorText.isNullOrBlank()
    val showSuccess = showValidation && isValid && errorText.isNullOrBlank() && !selectedFileName.isNullOrBlank()

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            if (showSuccess) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Valid",
                    tint = com.momokotle.agent.ui.theme.AppColors.Success
                )
            }
        }
        instruction?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        AppUploadBox(
            label = "",
            instruction = null,
            selectedFileName = selectedFileName,
            onPick = onPick,
            onRemove = onRemove
        )
        if (showImagePreview && localPath != null && File(localPath).exists()) {
            AsyncImage(
                model = File(localPath),
                contentDescription = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(AppDimens.InputRadius)),
                contentScale = ContentScale.Crop
            )
        }
        if (showError) {
            Text(
                text = errorText.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun InternetRequiredBanner(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppDimens.CardPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
