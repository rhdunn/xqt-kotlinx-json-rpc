// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrNull
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON Int or null type")
class TheJsonIntOrNullType {
    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("null", JsonIntOrNull.serialize(null).toString())
        assertEquals("1234", JsonIntOrNull.serialize(1234).toString())

        assertEquals("-2147483648", JsonIntOrNull.serialize(Int.MIN_VALUE).toString())
        assertEquals("2147483647", JsonIntOrNull.serialize(Int.MAX_VALUE).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON")
    fun can_deserialize_from_json() {
        assertEquals(null, JsonIntOrNull.deserialize(JsonNull))
        assertEquals(1234, JsonIntOrNull.deserialize(JsonPrimitive(1234)))

        assertEquals(Int.MIN_VALUE, JsonIntOrNull.deserialize(JsonPrimitive(Int.MIN_VALUE)))
        assertEquals(Int.MAX_VALUE, JsonIntOrNull.deserialize(JsonPrimitive(Int.MAX_VALUE)))
    }

    @Test
    @DisplayName("throws an error if the value is out of range")
    fun throws_an_error_if_the_value_is_out_of_range() {
        val e1 = assertFails { JsonIntOrNull.deserialize(JsonPrimitive(Int.MIN_VALUE.toLong() - 1)) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("The value '-2147483649' is out of range", e1.message)

        val e2 = assertFails { JsonIntOrNull.deserialize(JsonPrimitive(Int.MAX_VALUE.toLong() + 1)) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("The value '2147483648' is out of range", e2.message)
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonIntOrNull.deserialize(jsonObjectOf()) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonIntOrNull.deserialize(jsonArrayOf()) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonIntOrNull.deserialize(JsonPrimitive("test")) }
        assertEquals(IllegalArgumentException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { JsonIntOrNull.deserialize(JsonPrimitive(true)) }
        assertEquals(IllegalArgumentException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { JsonIntOrNull.deserialize(JsonPrimitive(1.2)) }
        assertEquals(IllegalArgumentException::class, e5::class)
        assertEquals("Unsupported kind type 'decimal'", e5.message)
    }
}
