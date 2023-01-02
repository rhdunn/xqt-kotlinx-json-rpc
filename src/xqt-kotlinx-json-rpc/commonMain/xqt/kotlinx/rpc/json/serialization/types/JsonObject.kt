// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON object.
 */
object JsonObject : JsonSerialization<JsonObject> {
    override fun serializeToJson(value: JsonObject): JsonElement = value

    override fun deserialize(json: JsonElement): JsonObject = when (json) {
        is JsonObject -> json
        else -> unsupportedKindType(json)
    }
}
