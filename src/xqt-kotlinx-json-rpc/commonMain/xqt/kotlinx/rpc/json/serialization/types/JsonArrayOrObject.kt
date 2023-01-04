// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON array|object union type.
 */
object JsonArrayOrObject : JsonSerialization<JsonElement> {
    override fun serializeToJson(value: JsonElement): JsonElement = when (value) {
        is JsonArray -> value
        is JsonObject -> value
        else -> unsupportedKindType(value)
    }

    override fun deserialize(json: JsonElement): JsonElement = when (json) {
        is JsonArray -> json
        is JsonObject -> json
        else -> unsupportedKindType(json)
    }
}
