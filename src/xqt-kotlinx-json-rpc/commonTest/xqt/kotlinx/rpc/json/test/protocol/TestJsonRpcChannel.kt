// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.protocol.JsonRpcChannel

class TestJsonRpcChannel : JsonRpcChannel {
    private val input = mutableListOf<String>()
    val output = mutableListOf<JsonElement>()

    fun push(message: JsonElement) {
        input.add(message.toString())
    }

    fun push(message: String) {
        input.add(message)
    }

    override fun send(message: JsonElement) {
        output.add(message)
    }

    override fun receive(): JsonElement? {
        val json = input.removeFirstOrNull() ?: return null
        return Json.parseToJsonElement(json)
    }

    override fun close() {
    }
}
