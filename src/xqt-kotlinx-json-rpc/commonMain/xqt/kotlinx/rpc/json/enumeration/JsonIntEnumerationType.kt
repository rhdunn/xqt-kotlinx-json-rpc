// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.enumeration

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.types.JsonInt

/**
 * Implements an Int-based JSON enumeration or namespaced constant type.
 *
 * An Int-based enumeration or namespaced constant type can be defined as:
 *
 * ```kotlin
 * @JvmInline
 * value class IntEnum(override val kind: Int) : JsonEnumeration<Int> {
 *     companion object : JsonIntEnumerationType<IntEnum>() {
 *         override fun valueOf(value: Int): IntEnum = IntEnum(value)
 *     }
 * }
 * ```
 *
 * @since 1.1.0
 */
abstract class JsonIntEnumerationType<T : JsonEnumeration<Int>> :
    JsonEnumerationType<T, Int>,
    JsonSerialization<T> {

    override fun deserialize(json: JsonElement): T {
        return deserialize(JsonInt.deserialize(json))
    }

    override fun serializeToJson(value: T): JsonPrimitive {
        return JsonInt.serializeToJson(serializeToValue(value))
    }

    override fun serializeToValue(value: T): Int = value.kind

    override fun deserialize(value: Int): T = valueOf(value)

    abstract override fun valueOf(value: Int): T
}
