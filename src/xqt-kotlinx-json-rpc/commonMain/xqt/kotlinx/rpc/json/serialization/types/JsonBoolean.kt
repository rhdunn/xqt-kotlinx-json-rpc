// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.kindType
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a boolean.
 */
object JsonBoolean : JsonSerialization<Boolean> {
    override fun serialize(value: Boolean): JsonElement = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): Boolean = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            "boolean" -> json.content == "true"
            else -> unsupportedKindType(json)
        }
    }
}
