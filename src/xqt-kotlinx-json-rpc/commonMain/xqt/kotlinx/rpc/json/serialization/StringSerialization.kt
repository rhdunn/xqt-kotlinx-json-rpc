// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

/**
 * Defines serialization for a data type or object to/from string values.
 */
interface StringSerialization<T> {
    /**
     * Serialize the data type or object `value` to a string.
     */
    fun serializeToString(value: T): String

    /**
     * Deserialize the data type or object from a string representation.
     */
    fun deserialize(value: String): T
}
