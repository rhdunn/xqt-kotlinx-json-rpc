// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.protocol.JsonRpcServer
import kotlin.test.assertEquals

data class TestJsonRpcServer(
    private val input: MutableList<String>,
    private val output: MutableList<String>
) : JsonRpcServer {
    fun send(message: String) {
        output.add(message)
    }

    override fun send(message: JsonElement) {
        output.add(message.toString())
    }

    override fun receive(): JsonElement? {
        val json = input.removeFirstOrNull() ?: return null
        return Json.parseToJsonElement(json)
    }

    override fun close() {
    }
}

data class TestClientServer(val client: TestJsonRpcServer, val server: TestJsonRpcServer) {
    constructor(input: MutableList<String>, output: MutableList<String>) :
            this(TestJsonRpcServer(input, output), TestJsonRpcServer(output, input))
}

fun testJsonRpc(handler: TestClientServer.() -> Unit) {
    val input = mutableListOf<String>()
    val output = mutableListOf<String>()

    val test = TestClientServer(input, output)
    test.handler()

    assertEquals(null, test.client.receive())
    assertEquals(null, test.server.receive())
}
