// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.JsonElement

/**
 * Defines a type of JSON object.
 */
interface JsonObjectType<T> : JsonSerialization<T> {
    /**
     * Checks if the JSON element is an instance of the given type.
     */
    fun instanceOf(json: JsonElement): Boolean = kindOf(json)

    /**
     * Checks if the JSON object's kind property matches the type's kind value.
     */
    fun kindOf(json: JsonElement): Boolean = false
}
