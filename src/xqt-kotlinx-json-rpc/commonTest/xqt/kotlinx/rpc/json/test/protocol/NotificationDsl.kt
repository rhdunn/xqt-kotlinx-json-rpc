// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("The notification DSL")
class TheNotificationDSL {
    @Test
    @DisplayName("supports notifications without parameters")
    fun supports_notifications_without_parameters() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test")
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                assertEquals(1, called)

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(null, params)
            }
        }

        assertEquals(1, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports notifications with parameters")
    fun supports_notifications_with_parameters() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("test"),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                assertEquals(1, called)

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
            }
        }

        assertEquals(1, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }

    @Test
    @DisplayName("supports multiple notification messages")
    fun supports_multiple_notification_messages() {
        val channel = TestJsonRpcChannel()
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("lorem")
            )
        )
        channel.input.add(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "method" to JsonPrimitive("ipsum"),
                "params" to jsonArrayOf(JsonPrimitive(1))
            )
        )

        var called = 0
        channel.jsonRpc {
            ++called
            notification {
                when (method) {
                    "lorem" -> {
                        assertEquals(1, called)

                        assertEquals("2.0", jsonprc)
                        assertEquals(null, params)
                    }

                    "ipsum" -> {
                        assertEquals(2, called)

                        assertEquals("2.0", jsonprc)
                        assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
                    }

                    else -> assertTrue(false, "Unknown notification event: $method")
                }
            }
        }

        assertEquals(2, called, "The notification DSL should have been called.")
        assertEquals(0, channel.output.size)
    }
}
