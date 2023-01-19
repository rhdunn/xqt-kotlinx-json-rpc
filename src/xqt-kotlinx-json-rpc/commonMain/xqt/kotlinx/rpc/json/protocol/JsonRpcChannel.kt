// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * A JSON-RPC channel to send/receive message on.
 */
expect interface JsonRpcChannel {
    /**
     * Send a JSON-RPC message.
     */
    fun send(message: JsonElement)

    /**
     * Receive the next JSON-RPC message, or null if no messages are pending.
     */
    fun receive(): JsonElement?

    /**
     * Close the output channel.
     */
    fun close()
}

/**
 * Processes a JSON-RPC message.
 */
fun JsonRpcChannel.jsonRpc(handler: Message.() -> Unit) {
    var body = receive()
    while (body != null) {
        when (body) {
            is JsonObject -> Message.deserialize(body).handler()
            is JsonArray -> body.forEach {
                Message.deserialize(it).handler()
            }

            else -> {}
        }
        body = receive()
    }
}
