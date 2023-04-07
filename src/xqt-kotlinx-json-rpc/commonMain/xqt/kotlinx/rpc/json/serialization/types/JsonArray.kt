// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON array.
 */
object JsonArray : JsonSerialization<JsonArray> {
    override fun serializeToJson(value: JsonArray): JsonArray = value

    override fun deserialize(json: JsonElement): JsonArray = when (json) {
        is JsonArray -> json
        else -> unsupportedKindType(json)
    }
}

/**
 * Defines a typed JSON array.
 *
 * @param itemSerialization how to serialize the items in the array
 */
data class JsonTypedArray<T>(private val itemSerialization: JsonSerialization<T>) : JsonSerialization<List<T>> {
    override fun serializeToJson(value: List<T>): JsonArray = buildJsonArray {
        value.forEach { item -> add(itemSerialization.serializeToJson(item)) }
    }

    override fun deserialize(json: JsonElement): List<T> = when (json) {
        !is JsonArray -> unsupportedKindType(json)
        else -> json.map { item -> itemSerialization.deserialize(item) }
    }
}
