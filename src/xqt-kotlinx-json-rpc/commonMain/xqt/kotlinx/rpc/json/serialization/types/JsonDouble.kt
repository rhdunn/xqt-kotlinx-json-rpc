// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.*

/**
 * Defines a decimal number with double precision.
 */
object JsonDouble : JsonSerialization<Double> {
    override fun serializeToJson(value: Double): JsonElement = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): Double = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.Integer, KindType.Decimal -> json.content.toDoubleOrNull() ?: valueOutOfRange(json)
            else -> unsupportedKindType(json)
        }
    }
}
