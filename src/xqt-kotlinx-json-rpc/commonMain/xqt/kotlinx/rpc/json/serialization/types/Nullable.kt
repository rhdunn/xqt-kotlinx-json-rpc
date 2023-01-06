// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.*

/**
 * Defines a nullable integer|string union type.
 */
object JsonIntStringOrNull : JsonSerialization<JsonIntOrString?> {
    override fun serializeToJson(value: JsonIntOrString?): JsonElement = when (value) {
        null -> JsonNull
        is JsonIntOrString.IntegerValue -> JsonPrimitive(value.integer)
        is JsonIntOrString.StringValue -> JsonPrimitive(value.string)
    }

    override fun deserialize(json: JsonElement): JsonIntOrString? = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.Null -> null
            KindType.String -> JsonIntOrString.StringValue(json.content)
            KindType.Integer -> when (val integer = json.content.toIntOrNull()) {
                null -> JsonIntOrString.StringValue(json.content)
                else -> JsonIntOrString.IntegerValue(integer)
            }

            else -> unsupportedKindType(json)
        }
    }
}
