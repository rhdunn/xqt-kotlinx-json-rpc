// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import io.ktor.http.*
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.protocol.ResponseObject
import xqt.kotlinx.rpc.json.protocol.jsonRpc
import xqt.kotlinx.rpc.json.protocol.request
import xqt.kotlinx.rpc.json.protocol.result
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

        var response: ResponseObject? = null
        json.jsonRpc {
            response = request {
                result("lorem ipsum", JsonString)
            }
        }

        assertEquals("2.0", response?.jsonprc)
        assertEquals(JsonIntOrString.IntegerValue(1234), response?.id)
        assertEquals(JsonPrimitive("lorem ipsum"), response?.result)
        assertEquals(null, response?.error)
    }
}
