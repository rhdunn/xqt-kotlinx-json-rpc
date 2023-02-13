// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

import kotlinx.serialization.json.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.KindType
import xqt.kotlinx.rpc.json.serialization.kindType
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType
import kotlin.jvm.JvmInline

/**
 * Defines an integer|string union type.
 */
sealed interface JsonIntOrString {
    /**
     * The integer number value of an integer|string in the range of -2^31 to 2^31 - 1.
     *
     * @throws IllegalArgumentException if this is not an integer instance.
     */
    val integer: Int

    /**
     * The string value of an integer|string.
     */
    val string: String

    /**
     * Defines an integer number instance of an integer|string type.
     */
    @JvmInline
    value class IntegerValue(override val integer: Int) : JsonIntOrString {
        override val string: String
            get() = integer.toString()
    }

    /**
     * Defines a string instance of an integer|string type.
     */
    @JvmInline
    value class StringValue(override val string: String) : JsonIntOrString {
        override val integer: Int
            get() = unsupportedKindType(KindType.Integer)
    }

    companion object : JsonSerialization<JsonIntOrString> {
        override fun serializeToJson(value: JsonIntOrString): JsonElement = when (value) {
            is IntegerValue -> JsonPrimitive(value.integer)
            is StringValue -> JsonPrimitive(value.string)
        }

        override fun deserialize(json: JsonElement): JsonIntOrString = when (json) {
            !is JsonPrimitive -> unsupportedKindType(json)
            else -> when (json.kindType) {
                KindType.String -> StringValue(json.content)
                KindType.Integer -> when (val integer = json.content.toIntOrNull()) {
                    null -> StringValue(json.content)
                    else -> IntegerValue(integer)
                }

                else -> unsupportedKindType(json)
            }
        }
    }
}

/**
 * Defines an array|object union type.
 */
object JsonArrayOrObject : JsonSerialization<JsonElement> {
    override fun serializeToJson(value: JsonElement): JsonElement = when (value) {
        is JsonArray -> value
        is JsonObject -> value
        else -> unsupportedKindType(value)
    }

    override fun deserialize(json: JsonElement): JsonElement = when (json) {
        is JsonArray -> json
        is JsonObject -> json
        else -> unsupportedKindType(json)
    }
}

/**
 * Defines a typed JSON object|array.
 *
 * If there is only one item in the array it will be treated as an object of
 * that type.
 *
 * @param itemSerialization how to serialize the items in the array
 */
data class JsonTypedObjectOrArray<T>(private val itemSerialization: JsonSerialization<T>) : JsonSerialization<List<T>> {
    override fun serializeToJson(value: List<T>): JsonElement = when (value.size) {
        1 -> itemSerialization.serializeToJson(value[0])
        else -> buildJsonArray {
            value.forEach { item -> add(itemSerialization.serializeToJson(item)) }
        }
    }

    override fun deserialize(json: JsonElement): List<T> = when (json) {
        is JsonArray -> json.map { item -> itemSerialization.deserialize(item) }
        else -> listOf(itemSerialization.deserialize(json))
    }
}
