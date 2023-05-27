// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.enumeration

import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.rpc.json.serialization.StringSerialization

/**
 * Implements a String-based JSON enumeration or namespaced constant type.
 *
 * A String-based enumeration or namespaced constant type can be defined as:
 *
 * ```kotlin
 * @JvmInline
 * value class StringEnum(override val kind: String) : JsonEnumeration<String> {
 *     companion object : JsonStringEnumerationType<StringEnum>() {
 *         override fun valueOf(value: String): StringEnum = StringEnum(value)
 *     }
 * }
 * ```
 */
abstract class JsonStringEnumerationType<T : JsonEnumeration<String>> :
    JsonEnumerationType<T, String>,
    StringSerialization<T> {

    override fun deserialize(value: String): T = valueOf(value)

    abstract override fun valueOf(value: String): T
}
