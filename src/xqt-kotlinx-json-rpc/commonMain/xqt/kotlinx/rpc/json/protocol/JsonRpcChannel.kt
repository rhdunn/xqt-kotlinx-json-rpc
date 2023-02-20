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
                error = ErrorObject(
                    code = ErrorCode.ParseError,
                    message = e.message?.splitToSequence('\n')?.firstOrNull() ?: "Parse Error"
                )
            )
            send(ResponseObject.serializeToJson(response))
            continue
        }

        when (body) {
            is JsonObject -> {
                val message = Message.deserialize(body)
                message.channel = this
                message.handler()
            }

            is JsonArray -> body.forEach {
                val message = Message.deserialize(it)
                message.channel = this
                message.handler()
            }

            else -> {}
        }
    }
}
