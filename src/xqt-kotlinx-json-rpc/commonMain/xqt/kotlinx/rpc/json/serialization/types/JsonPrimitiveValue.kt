// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON boolean|number|string primitive type.
 *
 * @since 1.0.0
 */
object JsonPrimitiveValue : JsonSerialization<JsonPrimitive> {
    override fun serializeToJson(value: JsonPrimitive): JsonPrimitive = when (value) {
        is JsonNull -> unsupportedKindType(value)
        else -> value
    }

    override fun deserialize(json: JsonElement): JsonPrimitive = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        is JsonNull -> unsupportedKindType(json)
        else -> json
    }
}
