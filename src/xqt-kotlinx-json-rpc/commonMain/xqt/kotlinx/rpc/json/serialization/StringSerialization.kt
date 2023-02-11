// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

/**
 * Defines serialization for a data type or object to/from string values.
 */
interface StringSerialization<T> : JsonSerialization<T> {
    /**
     * Serialize the data type or object `value` to a string.
     */
    fun serializeToString(value: T): String

    /**
     * Deserialize the data type or object from a string representation.
     */
    fun deserialize(value: String): T

    override fun serializeToJson(value: T): JsonElement = JsonPrimitive(serializeToString(value))

    override fun deserialize(json: JsonElement): T = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.String -> deserialize(json.content)
            else -> unsupportedKindType(json)
        }
    }
}
