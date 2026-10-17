package com.momokotle.agent.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.momokotle.agent.ui.theme.AppColors
import com.momokotle.agent.ui.theme.AppDimens
import com.momokotle.agent.ui.theme.appFormCardColors
import com.momokotle.agent.ui.theme.appFormFieldColors
import com.momokotle.agent.ui.theme.AppFormStyles
import com.momokotle.agent.ui.theme.formInputTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable (padding: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (title != null) {
                AppTopBar(title = title, onBack = onBack, actions = actions)
            }
        },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        snackbarHost = snackbarHost,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = { actions() },
        windowInsets = WindowInsets(0, 0, 0, 0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun AppBottomNav(
    items: List<Triple<String, ImageVector, Boolean>>,
    onItemClick: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(AppDimens.CardPadding),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(AppDimens.CardRadius)
    val cardModifier = modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colorScheme.outline, shape)
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = cardModifier,
            shape = shape,
            colors = AppCardDefaults(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
    } else {
        Card(
            modifier = cardModifier,
            shape = shape,
            colors = AppCardDefaults(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
    }
}

@Composable
fun AppCardDefaults() = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.surface
)

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    style: AppButtonStyle = AppButtonStyle.Primary
) {
    val shape = RoundedCornerShape(AppDimens.InputRadius)
    val clickable = enabled && !isLoading
    when (style) {
        AppButtonStyle.Primary, AppButtonStyle.Accent -> Button(
            onClick = onClick,
            enabled = clickable,
            modifier = modifier
                .fillMaxWidth()
                .height(AppDimens.ButtonHeight),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                AppButtonLabel(text)
            }
        }
        AppButtonStyle.Outlined -> OutlinedButton(
            onClick = onClick,
            enabled = clickable,
            modifier = modifier
                .fillMaxWidth()
                .height(AppDimens.ButtonHeight),
            shape = shape,
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            AppButtonLabel(text, color = MaterialTheme.colorScheme.onBackground)
        }
        AppButtonStyle.Destructive -> Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
                .fillMaxWidth()
                .height(AppDimens.ButtonHeight),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            AppButtonLabel(text)
        }
    }
}

enum class AppButtonStyle { Primary, Accent, Outlined, Destructive }

@Composable
private fun AppButtonLabel(text: String, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = if (color == Color.Unspecified) Color.Unspecified else color
    )
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    placeholder: String? = null,
    errorText: String? = null,
    isValid: Boolean = false,
    showValidation: Boolean = false,
    maxLength: Int? = null,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val showError = showValidation && !errorText.isNullOrBlank()
    val showSuccess = showValidation && isValid && errorText.isNullOrBlank() && value.isNotBlank()
    val transformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else -> visualTransformation
    }

    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val next = maxLength?.let { max -> input.take(max) } ?: input
            onValueChange(next)
        },
        label = {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = AppFormStyles.label)
        },
        placeholder = placeholder?.let {
            {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = AppFormStyles.label)
            }
        },
        enabled = enabled,
        singleLine = singleLine,
        isError = showError,
        supportingText = if (showError) {
            { Text(errorText.orEmpty(), color = AppFormStyles.error) }
        } else {
            null
        },
        trailingIcon = when {
            isPassword -> {
                {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            color = AppFormStyles.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            showSuccess -> {
                {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Valid",
                        tint = AppFormStyles.success
                    )
                }
            }
            else -> null
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = transformation,
        textStyle = formInputTextStyle(),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.InputRadius),
        colors = appFormFieldColors(showError = showError, showSuccess = showSuccess)
    )
}

@Composable
fun AppStatusBadge(
    text: String,
    tone: AppStatusTone = AppStatusTone.Neutral,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (tone) {
        AppStatusTone.Success -> AppColors.SuccessContainer to AppColors.Success
        AppStatusTone.Warning -> AppColors.WarningContainer to AppColors.Warning
        AppStatusTone.Error -> AppColors.ErrorContainer to AppColors.Error
        AppStatusTone.Accent -> MaterialTheme.colorScheme.primaryContainer to
            MaterialTheme.colorScheme.onPrimary
        AppStatusTone.Info -> AppColors.InfoContainer to AppColors.Info
        AppStatusTone.Neutral -> MaterialTheme.colorScheme.surfaceVariant to
            MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bg
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}

enum class AppStatusTone { Success, Warning, Error, Accent, Info, Neutral }

@Composable
fun AppSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AppEmptyState(
    title: String,
    message: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun AppUploadBox(
    label: String,
    instruction: String? = null,
    selectedFileName: String?,
    onPick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (label.isNotBlank()) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
        instruction?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    AppFormStyles.border,
                    RoundedCornerShape(AppDimens.InputRadius)
                )
                .clip(RoundedCornerShape(AppDimens.InputRadius))
                .clickable(onClick = onPick)
                .padding(AppDimens.CardPadding),
            color = AppFormStyles.surface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = AppFormStyles.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = selectedFileName ?: "Tap to upload",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppFormStyles.label,
                    textAlign = TextAlign.Center
                )
                if (selectedFileName != null) {
                    TextButton(onClick = onRemove) { Text("Remove file") }
                }
            }
        }
    }
}

@Composable
fun AppActionGridButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    highlighted: Boolean = false,
    showCustomBadge: Boolean = false,
    compact: Boolean = false,
    featured: Boolean = false,
    locked: Boolean = false
) {
    val cornerRadius = when {
        featured -> 16.dp
        compact -> 12.dp
        else -> 14.dp
    }
    val shape = RoundedCornerShape(cornerRadius)
    val containerColor = when {
        !enabled -> MaterialTheme.colorScheme.surfaceVariant
        highlighted -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
        highlighted -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onBackground
    }
    val buttonHeight = when {
        featured -> AppDimens.DashboardFeaturedButtonHeight
        compact -> AppDimens.DashboardGridButtonHeight
        else -> AppDimens.GridButtonHeight
    }
    val iconWell = when {
        featured -> 40.dp
        compact -> 26.dp
        else -> 32.dp
    }
    val iconSize = when {
        featured -> 24.dp
        compact -> 16.dp
        else -> 20.dp
    }
    val labelSize = when {
        featured -> 13.sp
        compact -> 10.sp
        else -> 11.sp
    }
    val labelLineHeight = when {
        featured -> 16.sp
        compact -> 12.sp
        else -> 13.sp
    }
    val contentPadding = when {
        featured -> 10.dp
        compact -> 6.dp
        else -> 8.dp
    }
    val badgeTopPadding = if (compact) 12.dp else 14.dp

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(buttonHeight)
            .then(
                if (highlighted) Modifier else Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    shape
                )
            ),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(contentPadding)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (locked) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Subscription required",
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopEnd),
                    tint = AppColors.MutedText
                )
            } else if (showCustomBadge) {
                AppStatusBadge(
                    text = "Custom",
                    tone = AppStatusTone.Accent,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (showCustomBadge) badgeTopPadding else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    when {
                        featured -> 8.dp
                        compact -> 4.dp
                        else -> 6.dp
                    }
                )
            ) {
                icon?.let {
                    Surface(
                        modifier = Modifier.size(iconWell),
                        shape = CircleShape,
                        color = when {
                            !enabled -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            highlighted -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                it,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize),
                                tint = when {
                                    !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
                                    highlighted -> MaterialTheme.colorScheme.onPrimary
                                    else -> MaterialTheme.colorScheme.onBackground
                                }
                            )
                        }
                    }
                }
                Text(
                    text = label,
                    fontSize = labelSize,
                    fontWeight = if (featured) FontWeight.Bold else FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = if (compact && !featured) 2 else 3,
                    lineHeight = labelLineHeight,
                    color = contentColor
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTransactionCard(
    typeLabel: String,
    phoneNumber: String?,
    amount: String,
    profit: String?,
    statusLabel: String,
    statusTone: AppStatusTone,
    dateTime: String,
    flowName: String? = null,
    modifier: Modifier = Modifier,
    onRepeat: (() -> Unit)? = null
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val copyPhone: () -> Unit = remember(phoneNumber) {
        {
            val number = phoneNumber?.trim().orEmpty()
            if (number.isNotEmpty()) {
                clipboard.setText(AnnotatedString(number))
                Toast.makeText(context, "Phone number copied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = typeLabel,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    AppStatusBadge(text = statusLabel, tone = statusTone)
                }
                flowName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                phoneNumber?.takeIf { it.isNotBlank() }?.let { number ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = number,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = copyPhone
                                )
                                .padding(vertical = 2.dp)
                        )
                        IconButton(
                            onClick = copyPhone,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy phone number",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                profit?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.Success
                    )
                }
                Text(
                    text = dateTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            onRepeat?.let {
                TextButton(onClick = it) {
                    Text("Repeat", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun AppProductCard(
    name: String,
    price: String,
    stockLabel: String,
    imageUrl: String?,
    quantity: Int,
    isOnline: Boolean,
    onQuantityChange: (Int) -> Unit,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier,
    imageContent: @Composable () -> Unit = {}
) {
    AppCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                imageContent()
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                AppStatusBadge(
                    text = stockLabel,
                    tone = if (stockLabel.contains("Out", ignoreCase = true)) {
                        AppStatusTone.Error
                    } else {
                        AppStatusTone.Success
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                        enabled = quantity > 1,
                        modifier = Modifier.size(36.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) { Text("−") }
                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    OutlinedButton(
                        onClick = { onQuantityChange(quantity + 1) },
                        modifier = Modifier.size(36.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) { Text("+") }
                }
                AppButton(
                    text = "Buy",
                    onClick = onBuy,
                    enabled = isOnline,
                    style = AppButtonStyle.Accent,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AppSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search…",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, style = formInputTextStyle().copy(color = AppFormStyles.label))
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true,
        textStyle = formInputTextStyle(),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.InputRadius),
        colors = appFormFieldColors()
    )
}

@Composable
fun AppFormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(AppDimens.CardRadius)),
        shape = RoundedCornerShape(AppDimens.CardRadius),
        colors = appFormCardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(AppDimens.CardPadding),
            content = content
        )
    }
}

@Composable
fun AppFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AppFormStyles.primary,
            selectedLabelColor = AppFormStyles.onPrimary,
            containerColor = AppFormStyles.surface,
            labelColor = AppFormStyles.onSurface
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = AppFormStyles.border,
            selectedBorderColor = AppFormStyles.primary
        )
    )
}

@Composable
fun AppMenuListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showArrow: Boolean = true,
    titleColor: Color = Color.Unspecified
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = AppDimens.CardPadding, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (titleColor == Color.Unspecified) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    titleColor
                }
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (showArrow) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AppLogoHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "MK",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = "MoMo Kotle",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppErrorBanner(message: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.InputRadius),
        color = AppColors.ErrorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = AppColors.Error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AppInfoBanner(message: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.InputRadius),
        color = AppColors.InfoContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = AppColors.Info,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AppSuccessBanner(message: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.InputRadius),
        color = AppColors.SuccessContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            color = AppColors.Success,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AppDetailRow(label: String, value: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value ?: "—",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AppAuthCard(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, shape),
        shape = shape,
        colors = appFormCardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(AppDimens.CardPadding),
            verticalArrangement = Arrangement.spacedBy(AppDimens.InputSpacing),
            content = content
        )
    }
}

@Composable
fun AppIconActionFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun statusToneForLabel(status: String): AppStatusTone = when {
    status.contains("Inactive", ignoreCase = true) -> AppStatusTone.Neutral
    status.contains("Success", ignoreCase = true) ||
        status.contains("Completed", ignoreCase = true) ||
        status.contains("Approved", ignoreCase = true) ||
        status.contains("Active", ignoreCase = true) ||
        status.contains("Delivered", ignoreCase = true) ||
        status.contains("Paid", ignoreCase = true) -> AppStatusTone.Success
    status.contains("Expired", ignoreCase = true) -> AppStatusTone.Error
    status.contains("Pending", ignoreCase = true) ||
        status.contains("Processing", ignoreCase = true) ||
        status.contains("Attempted", ignoreCase = true) -> AppStatusTone.Warning
    status.contains("Fail", ignoreCase = true) ||
        status.contains("Cancel", ignoreCase = true) ||
        status.contains("Reject", ignoreCase = true) -> AppStatusTone.Error
    else -> AppStatusTone.Neutral
}
