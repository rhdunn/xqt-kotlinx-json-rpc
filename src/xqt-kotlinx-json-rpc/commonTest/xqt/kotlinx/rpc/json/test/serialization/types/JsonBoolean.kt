// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonBoolean
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON Boolean type")
class TheJsonBooleanType {
    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("true", JsonBoolean.serializeToJson(true).toString())
        assertEquals("false", JsonBoolean.serializeToJson(false).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON")
    fun can_deserialize_from_json() {
        assertEquals(true, JsonBoolean.deserialize(JsonPrimitive(true)))
        assertEquals(false, JsonBoolean.deserialize(JsonPrimitive(false)))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonBoolean.deserialize(jsonObjectOf()) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonBoolean.deserialize(jsonArrayOf()) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonBoolean.deserialize(JsonNull) }
        assertEquals(IllegalArgumentException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { JsonBoolean.deserialize(JsonPrimitive("test")) }
        assertEquals(IllegalArgumentException::class, e4::class)
        assertEquals("Unsupported kind type 'string'", e4.message)

        val e5 = assertFails { JsonBoolean.deserialize(JsonPrimitive(1)) }
        assertEquals(IllegalArgumentException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { JsonBoolean.deserialize(JsonPrimitive(1.2)) }
        assertEquals(IllegalArgumentException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
