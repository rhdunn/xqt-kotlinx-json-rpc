// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.Notification
import xqt.kotlinx.rpc.json.serialization.MissingKeyException
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.*

@DisplayName("The Notification type")
class TheNotificationType {
    @Test
    @DisplayName("supports the non-optional properties")
    fun supports_the_non_optional_properties() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test")
        )

        assertTrue(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val notification = Notification.deserialize(json)
        assertEquals("2.0", notification.jsonrpc)
        assertEquals("test", notification.method)
        assertEquals(null, notification.params)

        assertEquals(json.toString(), Notification.serializeToJson(notification).toString())
    }

    @Test
    @DisplayName("supports the params property")
    fun supports_the_params_property() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        assertTrue(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val notification = Notification.deserialize(json)
        assertEquals("2.0", notification.jsonrpc)
        assertEquals("test", notification.method)
        assertEquals(jsonArrayOf(JsonPrimitive(1)), notification.params)

        assertEquals(json.toString(), Notification.serializeToJson(notification).toString())
    }

    @Test
    @DisplayName("supports a null request id")
    fun supports_a_null_request_id() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonNull
        )

        assertTrue(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val notification = Notification.deserialize(json)
        assertEquals("2.0", notification.jsonrpc)
        assertEquals("test", notification.method)
        assertEquals(null, notification.params)

        val expected = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test")
        )
        assertEquals(expected.toString(), Notification.serializeToJson(notification).toString())
    }

    @Test
    @DisplayName("throws an error if jsonrpc is missing")
    fun throws_an_error_if_jsonrpc_is_missing() {
        val json = jsonObjectOf(
            "method" to JsonPrimitive("test")
        )

        assertFalse(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val e = assertFails { Notification.deserialize(json) }
        assertEquals(MissingKeyException::class, e::class)
        assertEquals("Missing 'jsonrpc' key", e.message)
    }

    @Test
    @DisplayName("throws an error if method is missing")
    fun throws_an_error_if_method_is_missing() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0")
        )

        assertFalse(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val e = assertFails { Notification.deserialize(json) }
        assertEquals(MissingKeyException::class, e::class)
        assertEquals("Missing 'method' key", e.message)
    }

    @Test
    @DisplayName("throws an error if method is not a string")
    fun throws_an_error_if_method_is_not_a_string() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive(1)
        )

        assertTrue(Notification.instanceOf(json), "Notification.instanceOf")
        assertFalse(Notification.kindOf(json), "Notification.kindOf")

        val e = assertFails { Notification.deserialize(json) }
        assertEquals(UnsupportedKindTypeException::class, e::class)
        assertEquals("Unsupported kind type 'integer'", e.message)
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { Notification.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'array'", e1.message)

        val e2 = assertFails { Notification.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'null'", e2.message)

        val e3 = assertFails { Notification.deserialize(JsonPrimitive("test")) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'string'", e3.message)

        val e4 = assertFails { Notification.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { Notification.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { Notification.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
