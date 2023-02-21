// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.SerializationException
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

private fun JsonRpcChannel.processMessage(body: JsonElement, handler: Message.() -> Unit) {
    val message = Message.deserialize(body)
    message.channel = this
    message.handler()
}

/**
 * Processes a JSON-RPC message.
 */
fun JsonRpcChannel.jsonRpc(handler: Message.() -> Unit) {
    while (true) {
        val body = try {
            receive() ?: return
        } catch (e: SerializationException) {
            val response = ResponseObject(
                id = null,
                error = ParseError(e.message?.splitToSequence('\n')?.firstOrNull())
            )
            send(ResponseObject.serializeToJson(response))
            continue
        }

        when (body) {
            is JsonObject -> this.processMessage(body, handler)

            is JsonArray -> body.forEach { element ->
                this.processMessage(element, handler)
            }

            else -> {}
        }
    }
}
