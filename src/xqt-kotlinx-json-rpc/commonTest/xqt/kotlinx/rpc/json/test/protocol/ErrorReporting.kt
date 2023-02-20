// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.*
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("Error reporting")
class ErrorReporting {
    @Test
    @DisplayName("report ErrorCode.ParseError on invalid JSON")
    fun report_parse_error_on_invalid_json() {
        val channel = TestJsonRpcChannel()
        channel.push("{\"jsonrpc\":\"2.0\",method:\"test\"}")

        var called = 0
        channel.jsonRpc {
            ++called
        }

        assertEquals(0, called, "The request DSL should have been called.")
        assertEquals(1, channel.output.size)
        assertEquals(
            jsonObjectOf(
                "jsonrpc" to JsonPrimitive("2.0"),
                "id" to JsonNull,
                "error" to jsonObjectOf(
                    "code" to JsonPrimitive(ErrorCode.ParseError.code),
                    "message" to JsonPrimitive("Unexpected JSON token at offset 22: Expected quotation mark '\"', but had 'd' instead at path: \$")
                )
            ),
            channel.output[0]
        )
    }
}
