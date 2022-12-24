// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray

/**
 * Serialization helpers for typed arrays.
 */
object JsonTypedArray {
    /**
     * Serialize the list of values to JSON.
     *
     * @param serialization the serialization method to use for the array items.
     */
    fun <T> serialize(value: List<T>, serialization: JsonSerialization<T>): JsonElement = buildJsonArray {
        value.forEach { item -> add(serialization.serialize(item)) }
    }

    /**
     * Deserialize the list of values from the `json` array.
     *
     * @param serialization the serialization method to use for the array items.
     */
    fun <T> deserialize(json: JsonElement, serialization: JsonSerialization<T>): List<T> = when (json) {
        !is JsonArray -> unsupportedKindType(json)
        else -> json.map { item -> serialization.deserialize(item) }
    }
}
