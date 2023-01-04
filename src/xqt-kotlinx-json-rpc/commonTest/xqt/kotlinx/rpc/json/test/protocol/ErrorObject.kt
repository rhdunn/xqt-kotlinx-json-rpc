// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.ErrorCode
import xqt.kotlinx.rpc.json.protocol.ErrorObject
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The ErrorObject type")
class TheErrorObjectType {
    @Test
    @DisplayName("supports the non-optional properties")
    fun supports_the_non_optional_properties() {
        val json = jsonObjectOf(
            "code" to JsonPrimitive(-32601),
            "message" to JsonPrimitive("Method 'foo' not found.")
        )

        val error = ErrorObject.deserialize(json)
        assertEquals(ErrorCode.MethodNotFound, error.code)
        assertEquals("Method 'foo' not found.", error.message)
        assertEquals(null, error.data)

        assertEquals(json.toString(), ErrorObject.serializeToJson(error).toString())
    }

    @Test
    @DisplayName("supports the data property")
    fun supports_the_data_property() {
        val json = jsonObjectOf(
            "code" to JsonPrimitive(-32601),
            "message" to JsonPrimitive("Method 'foo' not found."),
            "data" to JsonPrimitive("abc")
        )

        val error = ErrorObject.deserialize(json)
        assertEquals(ErrorCode.MethodNotFound, error.code)
        assertEquals("Method 'foo' not found.", error.message)
        assertEquals(JsonPrimitive("abc"), error.data)

        assertEquals(json.toString(), ErrorObject.serializeToJson(error).toString())
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { ErrorObject.deserialize(jsonArrayOf()) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { ErrorObject.deserialize(JsonNull) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { ErrorObject.deserialize(JsonPrimitive("test")) }
        assertEquals(IllegalArgumentException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { ErrorObject.deserialize(JsonPrimitive(true)) }
        assertEquals(IllegalArgumentException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { ErrorObject.deserialize(JsonPrimitive(1)) }
        assertEquals(IllegalArgumentException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { ErrorObject.deserialize(JsonPrimitive(1.2)) }
        assertEquals(IllegalArgumentException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
