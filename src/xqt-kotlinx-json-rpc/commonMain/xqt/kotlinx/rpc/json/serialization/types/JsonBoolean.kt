// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.KindType
import xqt.kotlinx.rpc.json.serialization.kindType
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a boolean.
 *
 * @since 1.0.0
 */
object JsonBoolean : JsonSerialization<Boolean> {
    override fun serializeToJson(value: Boolean): JsonPrimitive = JsonPrimitive(value)

    override fun deserialize(json: JsonElement): Boolean = when (json) {
        !is JsonPrimitive -> unsupportedKindType(json)
        else -> when (json.kindType) {
            KindType.Boolean -> json.content == "true"
            else -> unsupportedKindType(json)
        }
    }
}
