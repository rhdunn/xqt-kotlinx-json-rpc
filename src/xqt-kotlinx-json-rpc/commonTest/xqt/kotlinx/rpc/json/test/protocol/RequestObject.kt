// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.RequestObject
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The RequestObject type")
class TheRequestObjectType {
    @Test
    @DisplayName("supports the non-optional properties")
    fun supports_the_non_optional_properties() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        val request = RequestObject.deserialize(json)
        assertEquals("2.0", request.jsonrpc)
        assertEquals("test", request.method)
        assertEquals(JsonIntOrString.IntegerValue(1234), request.id)
        assertEquals(null, request.params)

        assertEquals(json.toString(), RequestObject.serializeToJson(request).toString())
    }

    @Test
    @DisplayName("supports the params property")
    fun supports_the_params_property() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        val request = RequestObject.deserialize(json)
        assertEquals("2.0", request.jsonrpc)
        assertEquals("test", request.method)
        assertEquals(JsonIntOrString.IntegerValue(1234), request.id)
        assertEquals(jsonArrayOf(JsonPrimitive(1)), request.params)

        assertEquals(json.toString(), RequestObject.serializeToJson(request).toString())
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { RequestObject.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { RequestObject.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { RequestObject.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { RequestObject.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { RequestObject.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { RequestObject.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
