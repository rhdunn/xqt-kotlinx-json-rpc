// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.ErrorCode
import xqt.kotlinx.rpc.json.protocol.ResponseObject
import xqt.kotlinx.rpc.json.serialization.*
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.*

@DisplayName("The ResponseObject type")
class TheResponseObjectType {
    @Test
    @DisplayName("throws an error if jsonrpc is missing")
    fun throws_an_error_if_jsonrpc_is_missing() {
        val json = jsonObjectOf(
            "id" to JsonPrimitive(1234),
            "result" to JsonPrimitive("lorem ipsum")
        )

        assertFalse(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val e1 = assertFails { ResponseObject.deserialize(json) }
        assertEquals(MissingKeyException::class, e1::class)
        assertEquals("Missing 'jsonrpc' key", e1.message)
    }

    @Test
    @DisplayName("throws an error if id is missing")
    fun throws_an_error_if_id_is_missing() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "result" to JsonPrimitive("lorem ipsum")
        )

        assertFalse(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val e1 = assertFails { ResponseObject.deserialize(json) }
        assertEquals(MissingKeyException::class, e1::class)
        assertEquals("Missing 'id' key", e1.message)
    }

    @Test
    @DisplayName("throws an error if no result or error are specified")
    fun throws_an_error_if_no_result_or_error_are_specified() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive(1234)
        )

        assertFalse(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val e1 = assertFails { ResponseObject.deserialize(json) }
        assertEquals(MissingKeyException::class, e1::class)
        assertEquals("Missing 'result', or 'error' key", e1.message)
    }

    @Test
    @DisplayName("throws an error if result and error are both specified")
    fun throws_an_error_if_result_and_error_are_both_specified() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive(1234),
            "result" to JsonPrimitive("lorem ipsum"),
            "error" to jsonObjectOf(
                "code" to JsonPrimitive(-32601),
                "message" to JsonPrimitive("Method 'foo' not found.")
            )
        )

        assertTrue(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val e1 = assertFails { ResponseObject.deserialize(json) }
        assertEquals(ConflictingKeyException::class, e1::class)
        assertEquals("Conflicting 'result', or 'error' key", e1.message)
    }

    @Test
    @DisplayName("supports a null id property")
    fun supports_a_null_id_property() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonNull,
            "result" to JsonPrimitive("lorem ipsum")
        )

        assertTrue(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val response = ResponseObject.deserialize(json)
        assertEquals("2.0", response.jsonrpc)
        assertEquals(null, response.id)
        assertEquals(JsonPrimitive("lorem ipsum"), response.result)
        assertEquals(null, response.error)

        assertEquals(json.toString(), ResponseObject.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("supports void responses")
    fun supports_void_responses() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive(1234),
            "result" to JsonNull
        )

        assertTrue(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val response = ResponseObject.deserialize(json)
        assertEquals("2.0", response.jsonrpc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response.id)
        assertEquals(JsonNull, response.result)
        assertEquals(null, response.error)

        assertEquals(json.toString(), ResponseObject.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("supports result properties")
    fun supports_result_properties() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive(1234),
            "result" to JsonPrimitive("lorem ipsum")
        )

        assertTrue(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val response = ResponseObject.deserialize(json)
        assertEquals("2.0", response.jsonrpc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response.id)
        assertEquals(JsonPrimitive("lorem ipsum"), response.result)
        assertEquals(null, response.error)

        assertEquals(json.toString(), ResponseObject.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("supports error properties")
    fun supports_error_properties() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive("abcdef"),
            "error" to jsonObjectOf(
                "code" to JsonPrimitive(-32601),
                "message" to JsonPrimitive("Method 'foo' not found.")
            )
        )

        assertTrue(ResponseObject.instanceOf(json), "ResponseObject.instanceOf")
        assertFalse(ResponseObject.kindOf(json), "ResponseObject.kindOf")

        val response = ResponseObject.deserialize(json)
        assertEquals("2.0", response.jsonrpc)
        assertEquals(JsonIntOrString.StringValue("abcdef"), response.id)
        assertEquals(null, response.result)

        assertEquals(ErrorCode.MethodNotFound, response.error?.code)
        assertEquals("Method 'foo' not found.", response.error?.message)
        assertEquals(null, response.error?.data)

        assertEquals(json.toString(), ResponseObject.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { ResponseObject.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { ResponseObject.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { ResponseObject.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { ResponseObject.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { ResponseObject.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { ResponseObject.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
