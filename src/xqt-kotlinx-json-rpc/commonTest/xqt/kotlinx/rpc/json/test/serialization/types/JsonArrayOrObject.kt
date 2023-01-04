// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonArrayOrObject
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON array|object type")
class TheJsonArrayOrObjectType {
    @Test
    @DisplayName("supports the 'array' kind type")
    fun supports_the_array_kind_type() {
        assertEquals(jsonArrayOf(), JsonArrayOrObject.serializeToJson(jsonArrayOf()))
        assertEquals(jsonArrayOf(), JsonArrayOrObject.deserialize(jsonArrayOf()))

        val array = jsonArrayOf(JsonPrimitive(1234))
        assertEquals(array, JsonArrayOrObject.serializeToJson(array))
        assertEquals(array, JsonArrayOrObject.deserialize(array))
    }

    @Test
    @DisplayName("supports the 'object' kind type")
    fun supports_the_object_kind_type() {
        assertEquals(jsonObjectOf(), JsonArrayOrObject.serializeToJson(jsonObjectOf()))
        assertEquals(jsonObjectOf(), JsonArrayOrObject.deserialize(jsonObjectOf()))

        val `object` = jsonObjectOf("test" to JsonPrimitive(1234))
        assertEquals(`object`, JsonArrayOrObject.serializeToJson(`object`))
        assertEquals(`object`, JsonArrayOrObject.deserialize(`object`))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonArrayOrObject.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'null'", e1.message)

        val e2 = assertFails { JsonArrayOrObject.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'string'", e2.message)

        val e3 = assertFails { JsonArrayOrObject.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'boolean'", e3.message)

        val e4 = assertFails { JsonArrayOrObject.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'decimal'", e4.message)
    }
}
