// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonElement
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The JSON Element type")
class TheJsonElementType {
    @Test
    @DisplayName("supports the 'array' kind type")
    fun supports_the_array_kind_type() {
        assertEquals(jsonArrayOf(), JsonElement.serialize(jsonArrayOf()))
        assertEquals(jsonArrayOf(), JsonElement.deserialize(jsonArrayOf()))

        val array = jsonArrayOf(JsonPrimitive(1234))
        assertEquals(array, JsonElement.serialize(array))
        assertEquals(array, JsonElement.deserialize(array))
    }

    @Test
    @DisplayName("supports the 'object' kind type")
    fun supports_the_object_kind_type() {
        assertEquals(jsonObjectOf(), JsonElement.serialize(jsonObjectOf()))
        assertEquals(jsonObjectOf(), JsonElement.deserialize(jsonObjectOf()))

        val `object` = jsonObjectOf("test" to JsonPrimitive(1234))
        assertEquals(`object`, JsonElement.serialize(`object`))
        assertEquals(`object`, JsonElement.deserialize(`object`))
    }

    @Test
    @DisplayName("supports the 'null' kind type")
    fun supports_the_null_kind_type() {
        assertEquals(JsonNull, JsonElement.serialize(JsonNull))
        assertEquals(JsonNull, JsonElement.deserialize(JsonNull))
    }

    @Test
    @DisplayName("supports the 'string' kind type")
    fun supports_the_string_kind_type() {
        assertEquals(JsonPrimitive("lorem ipsum"), JsonElement.serialize(JsonPrimitive("lorem ipsum")))
        assertEquals(JsonPrimitive("lorem ipsum"), JsonElement.deserialize(JsonPrimitive("lorem ipsum")))
    }

    @Test
    @DisplayName("supports the 'boolean' kind type")
    fun supports_the_boolean_kind_type() {
        assertEquals(JsonPrimitive(true), JsonElement.serialize(JsonPrimitive(true)))
        assertEquals(JsonPrimitive(true), JsonElement.deserialize(JsonPrimitive(true)))

        assertEquals(JsonPrimitive(false), JsonElement.serialize(JsonPrimitive(false)))
        assertEquals(JsonPrimitive(false), JsonElement.deserialize(JsonPrimitive(false)))
    }

    @Test
    @DisplayName("supports the 'integer' kind type")
    fun supports_the_integer_kind_type() {
        assertEquals(JsonPrimitive(1234), JsonElement.serialize(JsonPrimitive(1234)))
        assertEquals(JsonPrimitive(1234), JsonElement.deserialize(JsonPrimitive(1234)))

        assertEquals(JsonPrimitive(Long.MIN_VALUE), JsonElement.serialize(JsonPrimitive(Long.MIN_VALUE)))
        assertEquals(JsonPrimitive(Long.MIN_VALUE), JsonElement.deserialize(JsonPrimitive(Long.MIN_VALUE)))

        assertEquals(JsonPrimitive(Long.MAX_VALUE), JsonElement.serialize(JsonPrimitive(Long.MAX_VALUE)))
        assertEquals(JsonPrimitive(Long.MAX_VALUE), JsonElement.deserialize(JsonPrimitive(Long.MAX_VALUE)))
    }

    @Test
    @DisplayName("supports the 'decimal' kind type")
    fun supports_the_decimal_kind_type() {
        assertEquals(JsonPrimitive(1.2), JsonElement.serialize(JsonPrimitive(1.2)))
        assertEquals(JsonPrimitive(1.2), JsonElement.deserialize(JsonPrimitive(1.2)))

        assertEquals(JsonPrimitive(Double.MIN_VALUE), JsonElement.serialize(JsonPrimitive(Double.MIN_VALUE)))
        assertEquals(JsonPrimitive(Double.MIN_VALUE), JsonElement.deserialize(JsonPrimitive(Double.MIN_VALUE)))

        assertEquals(JsonPrimitive(Double.MAX_VALUE), JsonElement.serialize(JsonPrimitive(Double.MAX_VALUE)))
        assertEquals(JsonPrimitive(Double.MAX_VALUE), JsonElement.deserialize(JsonPrimitive(Double.MAX_VALUE)))
    }
}
