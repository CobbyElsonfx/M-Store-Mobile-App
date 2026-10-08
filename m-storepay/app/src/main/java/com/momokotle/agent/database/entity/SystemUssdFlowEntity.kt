package com.momokotle.agent.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.momokotle.agent.ussd.UssdFlow

/**
 * Cached built-in (admin/system) USSD flows. Local Room is the primary lookup;
 * the network is only used to refresh this table in the background.
 */
@Entity(tableName = "system_ussd_flows")
data class SystemUssdFlowEntity(
    @PrimaryKey val flowKey: String,
    val name: String,
    val ussdCode: String,
    /** Serialized [UssdFlow] JSON for full step definitions. */
    val flowJson: String,
    val updatedAt: Long
)

fun SystemUssdFlowEntity.toUssdFlow(gson: Gson): UssdFlow? =
    runCatching {
        val flow = gson.fromJson(flowJson, UssdFlow::class.java) ?: return@runCatching null
        // Reject graphs broken by minify/type-erasure (null fields or LinkedTreeMap steps).
        if (flow.name.isBlank() || flow.ussdCode.isBlank() || flow.steps.isEmpty()) {
            return@runCatching null
        }
        flow.steps.forEach { step ->
            requireNotNull(step.actionType)
            step.inputValue
            step.screenHints.size
        }
        flow.requiresPhone
        flow
    }.getOrNull()

fun UssdFlow.toSystemEntity(gson: Gson, updatedAt: Long = System.currentTimeMillis()) =
    SystemUssdFlowEntity(
        flowKey = transactionType.name,
        name = name,
        ussdCode = ussdCode,
        flowJson = gson.toJson(this),
        updatedAt = updatedAt
    )
