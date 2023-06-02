// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

/**
 * Defines serialization for a data type or object to/from JSON elements.
 *
 * @since 1.0.0
 */
interface JsonSerialization<T> {
    /**
     * Serialize the data type or object `value` to JSON.
     */
    fun serializeToJson(value: T): JsonElement

    /**
     * Serialize the nullable data type or object `value` to JSON, or null.
     */
    fun serializeToJsonOrNull(value: T?): JsonElement = when (value) {
        null -> JsonNull
        else -> serializeToJson(value)
    }

    /**
     * Deserialize the data type or object from the `json` element.
     */
    fun deserialize(json: JsonElement): T

    /**
     * Deserialize the nullable data type or object from the `json` element,
     * or null.
     */
    fun deserializeOrNull(json: JsonElement): T? = when (json) {
        JsonNull -> null
        else -> deserialize(json)
    }
}
