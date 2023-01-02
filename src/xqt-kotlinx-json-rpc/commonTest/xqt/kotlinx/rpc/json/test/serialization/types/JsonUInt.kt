// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonUInt
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON UInt type")
class TheJsonUIntType {
    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("1234", JsonUInt.serializeToJson(1234u).toString())

        assertEquals("0", JsonUInt.serializeToJson(UInt.MIN_VALUE).toString())
        assertEquals("4294967295", JsonUInt.serializeToJson(UInt.MAX_VALUE).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON")
    fun can_deserialize_from_json() {
        assertEquals(1234u, JsonUInt.deserialize(JsonPrimitive(1234)))

        assertEquals(UInt.MIN_VALUE, JsonUInt.deserialize(JsonPrimitive(UInt.MIN_VALUE.toLong())))
        assertEquals(UInt.MAX_VALUE, JsonUInt.deserialize(JsonPrimitive(UInt.MAX_VALUE.toLong())))
    }

    @Test
    @DisplayName("throws an error if the value is out of range")
    fun throws_an_error_if_the_value_is_out_of_range() {
        val e1 = assertFails { JsonUInt.deserialize(JsonPrimitive(-1)) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("The value '-1' is out of range", e1.message)

        val e2 = assertFails { JsonUInt.deserialize(JsonPrimitive(UInt.MAX_VALUE.toLong() + 1)) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("The value '4294967296' is out of range", e2.message)
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonUInt.deserialize(jsonObjectOf()) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonUInt.deserialize(jsonArrayOf()) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonUInt.deserialize(JsonNull) }
        assertEquals(IllegalArgumentException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { JsonUInt.deserialize(JsonPrimitive("test")) }
        assertEquals(IllegalArgumentException::class, e4::class)
        assertEquals("Unsupported kind type 'string'", e4.message)

        val e5 = assertFails { JsonUInt.deserialize(JsonPrimitive(true)) }
        assertEquals(IllegalArgumentException::class, e5::class)
        assertEquals("Unsupported kind type 'boolean'", e5.message)

        val e6 = assertFails { JsonUInt.deserialize(JsonPrimitive(1.2)) }
        assertEquals(IllegalArgumentException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
