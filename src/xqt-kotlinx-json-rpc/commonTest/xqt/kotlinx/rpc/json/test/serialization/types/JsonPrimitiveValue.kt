// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonPrimitiveValue
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON primitive value")
class TheJsonPrimitiveValue {
    @Test
    @DisplayName("supports the 'string' kind type")
    fun supports_the_string_kind_type() {
        assertEquals(JsonPrimitive("lorem ipsum"), JsonPrimitiveValue.serializeToJson(JsonPrimitive("lorem ipsum")))
        assertEquals(JsonPrimitive("lorem ipsum"), JsonPrimitiveValue.deserialize(JsonPrimitive("lorem ipsum")))
    }

    @Test
    @DisplayName("supports the 'boolean' kind type")
    fun supports_the_boolean_kind_type() {
        assertEquals(JsonPrimitive(true), JsonPrimitiveValue.serializeToJson(JsonPrimitive(true)))
        assertEquals(JsonPrimitive(true), JsonPrimitiveValue.deserialize(JsonPrimitive(true)))

        assertEquals(JsonPrimitive(false), JsonPrimitiveValue.serializeToJson(JsonPrimitive(false)))
        assertEquals(JsonPrimitive(false), JsonPrimitiveValue.deserialize(JsonPrimitive(false)))
    }

    @Test
    @DisplayName("supports the 'integer' kind type")
    fun supports_the_integer_kind_type() {
        assertEquals(JsonPrimitive(1234), JsonPrimitiveValue.serializeToJson(JsonPrimitive(1234)))
        assertEquals(JsonPrimitive(1234), JsonPrimitiveValue.deserialize(JsonPrimitive(1234)))

        assertEquals(JsonPrimitive(Long.MIN_VALUE), JsonPrimitiveValue.serializeToJson(JsonPrimitive(Long.MIN_VALUE)))
        assertEquals(JsonPrimitive(Long.MIN_VALUE), JsonPrimitiveValue.deserialize(JsonPrimitive(Long.MIN_VALUE)))

        assertEquals(JsonPrimitive(Long.MAX_VALUE), JsonPrimitiveValue.serializeToJson(JsonPrimitive(Long.MAX_VALUE)))
        assertEquals(JsonPrimitive(Long.MAX_VALUE), JsonPrimitiveValue.deserialize(JsonPrimitive(Long.MAX_VALUE)))
    }

    @Test
    @DisplayName("supports the 'decimal' kind type")
    fun supports_the_decimal_kind_type() {
        assertEquals(JsonPrimitive(1.2), JsonPrimitiveValue.serializeToJson(JsonPrimitive(1.2)))
        assertEquals(JsonPrimitive(1.2), JsonPrimitiveValue.deserialize(JsonPrimitive(1.2)))

        assertEquals(JsonPrimitive(Double.MIN_VALUE), JsonPrimitiveValue.serializeToJson(JsonPrimitive(Double.MIN_VALUE)))
        assertEquals(JsonPrimitive(Double.MIN_VALUE), JsonPrimitiveValue.deserialize(JsonPrimitive(Double.MIN_VALUE)))

        assertEquals(JsonPrimitive(Double.MAX_VALUE), JsonPrimitiveValue.serializeToJson(JsonPrimitive(Double.MAX_VALUE)))
        assertEquals(JsonPrimitive(Double.MAX_VALUE), JsonPrimitiveValue.deserialize(JsonPrimitive(Double.MAX_VALUE)))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonPrimitiveValue.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonPrimitiveValue.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonPrimitiveValue.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)
    }
}
