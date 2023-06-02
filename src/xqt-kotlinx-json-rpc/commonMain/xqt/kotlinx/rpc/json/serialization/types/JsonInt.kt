// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.*

/**
 * Defines an integer number in the range of -2^31 to 2^31 - 1.
 *
 * @since 1.0.0
 */
object JsonInt : JsonSerialization<Int> {
    override fun serializeToJson(value: Int): JsonPrimitive = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): Int = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.Integer -> json.content.toIntOrNull() ?: valueOutOfRange(json)
            else -> unsupportedKindType(json)
        }
    }
}
