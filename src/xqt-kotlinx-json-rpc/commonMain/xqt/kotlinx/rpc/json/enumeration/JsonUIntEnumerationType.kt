// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.enumeration

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.types.JsonUInt

/**
 * Implements a UInt-based JSON enumeration or namespaced constant type.
 *
 * A UInt-based enumeration or namespaced constant type can be defined as:
 *
 * ```kotlin
 * @JvmInline
 * value class UIntEnum(override val kind: UInt) : JsonEnumeration<UInt> {
 *     companion object : JsonIntEnumerationType<UIntEnum>() {
 *         override fun valueOf(value: UInt): UIntEnum = UIntEnum(value)
 *     }
 * }
 * ```
 *
 * @since 1.1.0
 */
abstract class JsonUIntEnumerationType<T : JsonEnumeration<UInt>> :
    JsonEnumerationType<T, UInt>,
    JsonSerialization<T> {

    override fun deserialize(json: JsonElement): T {
        return deserialize(JsonUInt.deserialize(json))
    }

    override fun serializeToJson(value: T): JsonPrimitive {
        return JsonUInt.serializeToJson(serializeToValue(value))
    }

    override fun serializeToValue(value: T): UInt = value.kind

    override fun deserialize(value: UInt): T = valueOf(value)

    abstract override fun valueOf(value: UInt): T
}
