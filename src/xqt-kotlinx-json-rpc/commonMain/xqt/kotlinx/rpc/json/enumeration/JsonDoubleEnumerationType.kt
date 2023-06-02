// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.enumeration

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonEnumerationType
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.types.JsonDouble

/**
 * Implements a Double-based JSON enumeration or namespaced constant type.
 *
 * A Double-based enumeration or namespaced constant type can be defined as:
 *
 * ```kotlin
 * @JvmInline
 * value class DoubleEnum(override val kind: Double) : JsonEnumeration<Double> {
 *     companion object : JsonDoubleEnumerationType<DoubleEnum>() {
 *         override fun valueOf(value: Double): DoubleEnum = DoubleEnum(value)
 *     }
 * }
 * ```
 *
 * @since 1.1.0
 */
abstract class JsonDoubleEnumerationType<T : JsonEnumeration<Double>> :
    JsonEnumerationType<T, Double>,
    JsonSerialization<T> {

    override fun deserialize(json: JsonElement): T {
        return deserialize(JsonDouble.deserialize(json))
    }

    override fun serializeToJson(value: T): JsonPrimitive {
        return JsonDouble.serializeToJson(serializeToValue(value))
    }

    override fun serializeToValue(value: T): Double = value.kind

    override fun deserialize(value: Double): T = valueOf(value)

    abstract override fun valueOf(value: Double): T
}
