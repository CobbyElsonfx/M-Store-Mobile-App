package com.momokotle.agent.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser

object JsonStringMaps {

    fun fromJson(json: String?): Map<String, String> {
        if (json.isNullOrBlank() || json.equals("null", ignoreCase = true)) {
            return emptyMap()
        }
        val element = runCatching { JsonParser.parseString(json) }.getOrNull() ?: return emptyMap()
        return fromElement(element)
    }

    fun fromElement(element: JsonElement?): Map<String, String> {
        if (element == null || element.isJsonNull || !element.isJsonObject) {
            return emptyMap()
        }
        val result = LinkedHashMap<String, String>()
        for ((key, value) in element.asJsonObject.entrySet()) {
            result[key] = jsonValueToString(value)
        }
        return result
    }

    private fun jsonValueToString(value: JsonElement): String {
        if (value.isJsonNull) return ""
        if (value.isJsonPrimitive) {
            return value.asJsonPrimitive.asString
        }
        return value.toString()
    }
}
