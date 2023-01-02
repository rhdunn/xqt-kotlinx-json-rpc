// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonElement

/**
 * Defines serialization for a data type or object to/from JSON elements.
 */
interface JsonSerialization<T> {
    /**
     * Serialize the data type or object `value` to JSON.
     */
    fun serializeToJson(value: T): JsonElement

    /**
     * Deserialize the data type or object from the `json` element.
     */
    fun deserialize(json: JsonElement): T
}
