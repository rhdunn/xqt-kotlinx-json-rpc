// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonDeserializationException
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString

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

private fun JsonRpcChannel.sendError(error: ErrorObject, id: JsonIntOrString? = null) {
    val response = ResponseObject(
        id = id,
        error = error
    )
    send(ResponseObject.serializeToJson(response))
}

private fun JsonRpcChannel.processMessage(body: JsonElement, handler: Message.() -> Unit) {
    val message = try {
        Message.deserialize(body)
    } catch (e: JsonDeserializationException) {
        sendError(InvalidRequest(e.message))
        return
    }

    message.channel = this
    try {
        message.handler()
    } catch (e: ErrorObject) {
        sendError(
            error = e,
            id = when (message) {
                is RequestObject -> message.id
                is ResponseObject -> message.id
                else -> null
            }
        )
    } catch (e: Throwable) {
        sendError(
            error = InternalError(e.message),
            id = when (message) {
                is RequestObject -> message.id
                is ResponseObject -> message.id
                else -> null
            }
        )
    }
}

/**
 * Processes a JSON-RPC message.
 */
fun JsonRpcChannel.jsonRpc(handler: Message.() -> Unit) {
    while (true) {
        val body = try {
            receive() ?: return
        } catch (e: SerializationException) {
            sendError(ParseError(e.message?.splitToSequence('\n')?.firstOrNull()))
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
