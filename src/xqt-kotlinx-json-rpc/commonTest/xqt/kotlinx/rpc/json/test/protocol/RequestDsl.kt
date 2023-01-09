// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.jsonRpc
import xqt.kotlinx.rpc.json.protocol.request
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The request DSL")
class TheRequestDsl {
    @Test
    @DisplayName("supports requests without parameters")
    fun supports_requests_without_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        var called = false
        json.jsonRpc {
            request {
                called = true

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(JsonIntOrString.IntegerValue(1234), id)
                assertEquals(null, params)
            }
        }

        assertEquals(true, called, "The request DSL should have been called.")
    }

    @Test
    @DisplayName("supports requests with parameters")
    fun supports_requests_with_parameters() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234),
            "params" to jsonArrayOf(JsonPrimitive(1))
        )

        var called = false
        json.jsonRpc {
            request {
                called = true

                assertEquals("2.0", jsonprc)
                assertEquals("test", method)
                assertEquals(JsonIntOrString.IntegerValue(1234), id)
                assertEquals(jsonArrayOf(JsonPrimitive(1)), params)
            }
        }

        assertEquals(true, called, "The request DSL should have been called.")
    }
}
