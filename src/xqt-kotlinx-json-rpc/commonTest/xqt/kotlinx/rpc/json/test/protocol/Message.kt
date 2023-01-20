// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The Message type")
class TheMessageType {
    @Test
    @DisplayName("supports notifications without parameters")
    fun supports_notifications_without_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test")
        )

        val message = Message.deserialize(json)
        assertEquals(Notification::class, message::class)

        val notification = message as Notification
        assertEquals("2.0", notification.jsonrpc)
        assertEquals("test", notification.method)
        assertEquals(null, notification.params)

        assertEquals(json.toString(), Message.serializeToJson(notification).toString())
    }

    @Test
    @DisplayName("supports notifications with parameters")
    fun supports_notifications_with_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        val message = Message.deserialize(json)
        assertEquals(Notification::class, message::class)

        val notification = message as Notification
        assertEquals("2.0", notification.jsonrpc)
        assertEquals("test", notification.method)
        assertEquals(jsonArrayOf(JsonPrimitive(1)), notification.params)

        assertEquals(json.toString(), Message.serializeToJson(notification).toString())
    }

    @Test
    @DisplayName("supports request objects without parameters")
    fun supports_request_objects_without_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        val message = Message.deserialize(json)
        assertEquals(RequestObject::class, message::class)

        val request = message as RequestObject
        assertEquals("2.0", request.jsonrpc)
        assertEquals("test", request.method)
        assertEquals(JsonIntOrString.IntegerValue(1234), request.id)
        assertEquals(null, request.params)

        assertEquals(json.toString(), Message.serializeToJson(request).toString())
    }

    @Test
    @DisplayName("supports request objects with parameters")
    fun supports_request_objects_with_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        val message = Message.deserialize(json)
        assertEquals(RequestObject::class, message::class)

        val request = message as RequestObject
        assertEquals("2.0", request.jsonrpc)
        assertEquals("test", request.method)
        assertEquals(JsonIntOrString.IntegerValue(1234), request.id)
        assertEquals(jsonArrayOf(JsonPrimitive(1)), request.params)

        assertEquals(json.toString(), Message.serializeToJson(request).toString())
    }

    @Test
    @DisplayName("supports response object")
    fun supports_response_objects() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive(1234),
            "result" to JsonPrimitive("lorem ipsum")
        )

        val message = Message.deserialize(json)
        assertEquals(ResponseObject::class, message::class)

        val response = message as ResponseObject
        assertEquals("2.0", response.jsonrpc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response.id)
        assertEquals(JsonPrimitive("lorem ipsum"), response.result)
        assertEquals(null, response.error)

        assertEquals(json.toString(), Message.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("supports error response objects")
    fun supports_error_response_object() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "id" to JsonPrimitive("abcdef"),
            "error" to jsonObjectOf(
                "code" to JsonPrimitive(-32601),
                "message" to JsonPrimitive("Method 'foo' not found.")
            )
        )

        val message = Message.deserialize(json)
        assertEquals(ResponseObject::class, message::class)

        val response = message as ResponseObject
        assertEquals("2.0", response.jsonrpc)
        assertEquals(JsonIntOrString.StringValue("abcdef"), response.id)
        assertEquals(null, response.result)

        assertEquals(ErrorCode.MethodNotFound, response.error?.code)
        assertEquals("Method 'foo' not found.", response.error?.message)
        assertEquals(null, response.error?.data)

        assertEquals(json.toString(), Message.serializeToJson(response).toString())
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { Message.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { Message.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { Message.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { Message.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { Message.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { Message.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
