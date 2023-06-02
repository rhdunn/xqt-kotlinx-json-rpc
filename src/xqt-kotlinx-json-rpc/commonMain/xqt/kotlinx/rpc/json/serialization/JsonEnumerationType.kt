// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration

/**
 * Defines serialization for a JSON enumeration or namespaced constant type.
 *
 * @since 1.1.0
 */
interface JsonEnumerationType<T : JsonEnumeration<ValueT>, ValueT> {
    /**
     * Serialize the enumeration or namespace constant value to the kind type value.
     */
    fun serializeToValue(value: T): ValueT = value.kind

    /**
     * Deserialize the enumeration or namespace constant value from the kind type value.
     */
    fun deserialize(value: ValueT): T = valueOf(value)

    /**
     * Returns the enumeration or namespaced constant values.
     */
    fun valueOf(value: ValueT): T
}
