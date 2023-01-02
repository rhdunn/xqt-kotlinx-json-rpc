// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.*

/**
 * Defines an unsigned integer number in the range of 0 to 2^32 - 1.
 */
object JsonUInt : JsonSerialization<UInt> {
    override fun serializeToJson(value: UInt): JsonElement = JsonPrimitive(value.toLong())

    override fun deserialize(json: JsonElement): UInt = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.Integer -> json.content.toUIntOrNull() ?: valueOutOfRange(json)
            else -> unsupportedKindType(json)
        }
    }
}
