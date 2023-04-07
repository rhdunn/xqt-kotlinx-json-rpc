// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.StringSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * Defines a JSON object.
 */
object JsonObject : JsonSerialization<JsonObject> {
    override fun serializeToJson(value: JsonObject): JsonObject = value

    override fun deserialize(json: JsonElement): JsonObject = when (json) {
        is JsonObject -> json
        else -> unsupportedKindType(json)
    }
}

/**
 * Defines a typed JSON object.
 *
 * @param keySerialization how to serialize the keys in the object map
 * @param valueSerialization how to serialize the values in the object map
 */
data class JsonTypedObject<K, V>(
    private val keySerialization: StringSerialization<K>,
    private val valueSerialization: JsonSerialization<V>
) : JsonSerialization<Map<K, V>> {
    override fun serializeToJson(value: Map<K, V>): JsonObject = buildJsonObject {
        value.forEach { (key, value) ->
            val k = keySerialization.serializeToString(key)
            val v = valueSerialization.serializeToJson(value)
            put(k, v)
        }
    }

    override fun deserialize(json: JsonElement): Map<K, V> = when (json) {
        !is JsonObject -> unsupportedKindType(json)
        else -> json.entries.associate { (key, value) ->
            val k = keySerialization.deserialize(key)
            val v = valueSerialization.deserialize(value)
            k to v
        }
    }
}
