// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON array.
 */
object JsonArray : JsonSerialization<JsonArray> {
    override fun serialize(value: JsonArray): JsonElement = value

    override fun deserialize(json: JsonElement): JsonArray = when (json) {
        is JsonArray -> json
        else -> unsupportedKindType(json)
    }
}
