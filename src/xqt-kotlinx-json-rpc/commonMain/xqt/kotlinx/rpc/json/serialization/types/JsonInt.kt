// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.kindType
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType
import xqt.kotlinx.rpc.json.serialization.valueOutOfRange

/**
 * Defines an integer number in the range of -2^31 to 2^31 - 1.
 */
object JsonInt : JsonSerialization<Int> {
    override fun serialize(value: Int): JsonElement = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): Int = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            "integer" -> json.content.toIntOrNull() ?: valueOutOfRange(json)
            else -> unsupportedKindType(json)
        }
    }
}

/**
 * Defines a nullable integer number in the range of -2^31 to 2^31 - 1.
 */
object JsonIntOrNull : JsonSerialization<Int?> {
    override fun serialize(value: Int?): JsonElement = when (value) {
        null -> JsonNull
        else -> JsonPrimitive(value)
    }

    override fun deserialize(json: JsonElement): Int? = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            "null" -> null
            "integer" -> json.content.toIntOrNull() ?: valueOutOfRange(json)
            else -> unsupportedKindType(json)
        }
    }
}
