// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.protocol.JsonRpcChannel

data class TestJsonRpcChannel(
    private val input: MutableList<String>,
    private val output: MutableList<String>
) : JsonRpcChannel {
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

fun testJsonRpcChannels(): Pair<TestJsonRpcChannel, TestJsonRpcChannel> {
    val input = mutableListOf<String>()
    val output = mutableListOf<String>()
    return TestJsonRpcChannel(input, output) to TestJsonRpcChannel(output, input)
}
