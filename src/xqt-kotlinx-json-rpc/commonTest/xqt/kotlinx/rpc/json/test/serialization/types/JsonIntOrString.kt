// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonInt
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON integer|string type")
class TheJsonIntOrStringType {
    private fun integer(value: Int): JsonIntOrString = JsonIntOrString.IntegerValue(value)
    private fun string(value: String): JsonIntOrString = JsonIntOrString.StringValue(value)

    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("1234", JsonIntOrString.serializeToJson(integer(1234)).toString())

        assertEquals("-2147483648", JsonIntOrString.serializeToJson(integer(Int.MIN_VALUE)).toString())
        assertEquals("2147483647", JsonIntOrString.serializeToJson(integer(Int.MAX_VALUE)).toString())

        assertEquals("\"1234\"", JsonIntOrString.serializeToJson(string("1234")).toString())
        assertEquals("\"abcdef\"", JsonIntOrString.serializeToJson(string("abcdef")).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON")
    fun can_deserialize_from_json() {
        assertEquals(integer(1234), JsonIntOrString.deserialize(JsonPrimitive(1234)))

        assertEquals(integer(Int.MIN_VALUE), JsonIntOrString.deserialize(JsonPrimitive(Int.MIN_VALUE)))
        assertEquals(integer(Int.MAX_VALUE), JsonIntOrString.deserialize(JsonPrimitive(Int.MAX_VALUE)))

        assertEquals(string("-2147483649"), JsonIntOrString.deserialize(JsonPrimitive(Int.MIN_VALUE.toLong() - 1)))
        assertEquals(string("2147483648"), JsonIntOrString.deserialize(JsonPrimitive(Int.MAX_VALUE.toLong() + 1)))

        assertEquals(string("1234"), JsonIntOrString.deserialize(JsonPrimitive("1234")))
        assertEquals(string("abcdef"), JsonIntOrString.deserialize(JsonPrimitive("abcdef")))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonInt.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonInt.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonInt.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { JsonInt.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { JsonInt.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'decimal'", e5.message)
    }
}
