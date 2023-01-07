// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The notification DSL")
class TheNotificationDSL {
    @Test
    @DisplayName("supports notifications without parameters")
    fun supports_notifications_without_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test")
        )

        var called = false
        val response = json.jsonRpc {
            notification {
                called = true

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(null, params)
            }
        }

        assertEquals(true, called, "The notification DSL should have been called.")
        assertEquals(null, response)
    }

    @Test
    @DisplayName("supports notifications with parameters")
    fun supports_notifications_with_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        var called = false
        val response = json.jsonRpc {
            notification {
                called = true

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
            }
        }

        assertEquals(true, called, "The notification DSL should have been called.")
        assertEquals(null, response)
    }
}
