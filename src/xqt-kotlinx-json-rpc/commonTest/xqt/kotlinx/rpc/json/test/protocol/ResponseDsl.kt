// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.rpc.json.serialization.types.JsonString
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The response DSL")
class TheResponseDsl {
    @Test
    @DisplayName("supports result responses")
    fun supports_result_responses() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        val response = json.jsonRpc {
            request {
                result("lorem ipsum", JsonString)
            }
        }

        assertEquals("2.0", response?.jsonprc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response?.id)
        assertEquals(JsonPrimitive("lorem ipsum"), response?.result)
        assertEquals(null, response?.error)
    }

    @Test
    @DisplayName("supports error responses")
    fun supports_error_responses() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        val response = json.jsonRpc {
            request {
                error(ErrorCode.MethodNotFound, "method not found")
            }
        }

        assertEquals("2.0", response?.jsonprc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response?.id)
        assertEquals(null, response?.result)

        assertEquals(ErrorCode.MethodNotFound, response?.error?.code)
        assertEquals("method not found", response?.error?.message)
        assertEquals(null, response?.error?.data)
    }

    @Test
    @DisplayName("supports error responses with data")
    fun supports_error_responses_with_data() {
        val json = jsonObjectOf(
            "jsonrpc" to JsonPrimitive("2.0"),
            "method" to JsonPrimitive("test"),
            "id" to JsonPrimitive(1234)
        )

        val response = json.jsonRpc {
            request {
                error(ErrorCode.MethodNotFound, "method not found", "loremIpsum", JsonString)
            }
        }

        assertEquals("2.0", response?.jsonprc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response?.id)
        assertEquals(null, response?.result)

        assertEquals(ErrorCode.MethodNotFound, response?.error?.code)
        assertEquals("method not found", response?.error?.message)
        assertEquals(JsonPrimitive("loremIpsum"), response?.error?.data)
    }
}
