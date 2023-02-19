// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.protocol

import kotlinx.serialization.json.JsonElement
import xqt.kotlinx.rpc.json.protocol.JsonRpcChannel

class TestJsonRpcChannel : JsonRpcChannel {
    private val input = mutableListOf<JsonElement>()
    val output = mutableListOf<JsonElement>()

    fun push(message: JsonElement) {
        input.add(message)
    }

    override fun send(message: JsonElement) {
        output.add(message)
    }

    override fun receive(): JsonElement? = input.removeFirstOrNull()

    override fun close() {
    }
}
