// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.io.Closeable
import xqt.kotlinx.rpc.json.serialization.JsonDeserializationException
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString

/**
 * A JSON-RPC server to send/receive message on.
 */
abstract class JsonRpcServer : Closeable {
    /**
     * Send a JSON-RPC message.
     */
    abstract fun send(message: JsonElement)

    /**
     * Receive the next JSON-RPC message, or null if no messages are pending.
     */
    abstract fun receive(): JsonElement?

    /**
     * Close the JSON-RPC server.
     */
    abstract override fun close()

    /**
     * The next request identifier.
     */
    var nextRequestId: JsonIntOrString.IntegerValue = JsonIntOrString.IntegerValue(0)
        get() {
            field = JsonIntOrString.IntegerValue(field.integer + 1)
            return field
        }
        private set

    internal val responseHandlers: MutableMap<JsonIntOrString, ResponseObject.() -> Unit> = mutableMapOf()

    fun <ResultT, ErrorDataT> registerResponseHandler(
        id: JsonIntOrString,
        handler: (TypedResponseObject<ResultT, ErrorDataT>.() -> Unit),
        responseObjectConverter: TypedResponseObjectConverter<ResultT, ErrorDataT>
    ) {
        responseHandlers[id] = { response: ResponseObject ->
            val typedResponse = responseObjectConverter.convert(response)
            handler(typedResponse)
        }
    }
}

private fun JsonRpcServer.sendError(error: ErrorObject, id: JsonIntOrString? = null) {
    val response = ResponseObject(
        id = id,
        error = error
    )
    send(ResponseObject.serializeToJson(response))
}

private fun JsonRpcServer.processMessage(body: JsonElement, handler: Message.() -> Unit) {
    val message = try {
        Message.deserialize(body)
    } catch (e: JsonDeserializationException) {
        sendError(InvalidRequest(e.message))
        return
    }

    try {
        when (message) {
            is RequestObject -> {
                message.channel = this
                message.handler()
            }
            is Notification -> {
                message.handler()
            }
            is ResponseObject -> {
                try {
                    responseHandlers[message.id]?.invoke(message)
                    message.handler()
                } finally {
                    message.id?.let { responseHandlers.remove(it) }
                }
            }
        }
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
fun JsonRpcServer.jsonRpc(handler: Message.() -> Unit) {
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
