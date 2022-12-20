// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.KindType
import xqt.kotlinx.rpc.json.serialization.kindType
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a string.
 */
object JsonString : JsonSerialization<String> {
    override fun serialize(value: String): JsonElement = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): String = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.String -> json.content
            else -> unsupportedKindType(json)
        }
    }
}
