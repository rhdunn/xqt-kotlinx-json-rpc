// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import xqt.kotlinx.rpc.json.serialization.*
import xqt.kotlinx.rpc.json.serialization.types.JsonIntOrString
import xqt.kotlinx.rpc.json.serialization.types.JsonString
import xqt.kotlinx.rpc.json.serialization.types.JsonElement as JsonElementType

/**
 * A response message sent as a result of a request.
 *
 * If a request doesn't provide a result value the receiver of a request still
 * needs to return a response message to conform to the JSON-RPC specification.
 * The result property should be set to `null` in this case to signal a
 * successful request.
 *
 * @see <a href="https://www.jsonrpc.org/specification#response_object">JSON-RPC 2.0 Response object</a>
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#responseMessage">LSP 3.17 ResponseMessage</a>
 */
data class ResponseObject(
    /**
     * The request id.
     *
     * It *must* be the same as the value of the id member in the Request Object.
     *
     * If there was an error in detecting the id in the Request object (e.g.
     * Parse error/Invalid Request), it *must* be `null`.
     */
    val id: JsonIntOrString?,

    /**
     * The result of a request.
     *
     * This member is *required* on success.
     *
     * This member *must not* exist if there was an error invoking the method.
     */
    val result: JsonElement? = null,

    /**
     * The error object in case a request fails.
     *
     * This member is *required* on error.
     *
     * This member *must not* exist if there was no error triggered during invocation.
     */
    val error: ErrorObject? = null,

    override val jsonrpc: String = Message.JSON_RPC_2_0
) : Message {
    override var channel: JsonRpcChannel? = null

    init {
        if (result == null && error == null)
            missingKey("result", "error")
        if (result != null && error != null)
            conflictingKey("result", "error")
    }

    companion object : JsonSerialization<ResponseObject> {
        override fun serializeToJson(value: ResponseObject): JsonElement = buildJsonObject {
            put("jsonrpc", value.jsonrpc, JsonString)
            putNullable("id", value.id, JsonIntOrString)
            putOptional("result", value.result, JsonElementType)
            putOptional("error", value.error, ErrorObject)
        }

        override fun deserialize(json: JsonElement): ResponseObject = when (json) {
            !is JsonObject -> unsupportedKindType(json)
            else -> ResponseObject(
                jsonrpc = json.get("jsonrpc", JsonString),
                id = json.getNullable("id", JsonIntOrString),
                result = json.getOptional("result", JsonElementType),
                error = json.getOptional("error", ErrorObject)
            )
        }
    }
}

/**
 * Send a response to the channel the message originated from.
 */
fun RequestObject.sendResponse(response: ResponseObject) {
    channel?.send(ResponseObject.serializeToJson(response))
}

/**
 * Send a result-based response to the channel the message originated from.
 */
fun RequestObject.sendResult(result: JsonElement?) {
    val response = ResponseObject(
        id = id,
        result = result ?: JsonNull,
        jsonrpc = jsonrpc
    )
    channel?.send(ResponseObject.serializeToJson(response))
}

/**
 * Send an error-based response to the channel the message originated from.
 */
fun RequestObject.sendError(error: ErrorObject) {
    val response = ResponseObject(
        id = id,
        error = error,
        jsonrpc = jsonrpc
    )
    channel?.send(ResponseObject.serializeToJson(response))
}

/**
 * Send an error-based response to the channel the message originated from.
 */
fun RequestObject.sendError(code: ErrorCode, message: String, data: JsonElement? = null) {
    val error = ErrorObject(
        code = code,
        message = message,
        data = data
    )
    sendError(error = error)
}

/**
 * Processes a JSON-RPC response message.
 */
fun Message.response(handler: ResponseObject.() -> Unit) {
    if (this is ResponseObject) {
        this.handler()
    }
}
