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
interface TypedResponseObject<ResultT, ErrorDataT> {
    /**
     * The request id.
     *
     * It *must* be the same as the value of the id member in the Request Object.
     *
     * If there was an error in detecting the id in the Request object (e.g.
     * Parse error/Invalid Request), it *must* be `null`.
     */
    val id: JsonIntOrString?

    /**
     * The result of a request.
     *
     * This member is *required* on success.
     *
     * This member *must not* exist if there was an error invoking the method.
     */
    val result: ResultT?

    /**
     * The error object in case a request fails.
     *
     * This member is *required* on error.
     *
     * This member *must not* exist if there was no error triggered during invocation.
     */
    val error: TypedErrorObject<ErrorDataT>?

    /**
     * The version of the JSON-RPC protocol.
     *
     * This must be exactly "2.0".
     */
    val jsonrpc: String
}

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
    override val id: JsonIntOrString?,
    override val result: JsonElement? = null,
    override val error: ErrorObject? = null,
    override val jsonrpc: String = Message.JSON_RPC_2_0
) : Message, TypedResponseObject<JsonElement, JsonElement> {
    init {
        if (result == null && error == null)
            missingKey("result", "error")
        if (result != null && error != null)
            conflictingKey("result", "error")
    }

    companion object : JsonObjectType<ResponseObject> {
        override fun instanceOf(json: JsonElement): Boolean {
            if (!json.containsKeys("jsonrpc", "id")) {
                return false
            }
            return json.containsKeys("result") || json.containsKeys("error")
        }

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
 * Send a result-based response to the channel the message originated from.
 */
fun <T> RequestObject.sendResult(result: T, serializer: JsonSerialization<T>) {
    val response = ResponseObject(
        id = id,
        result = serializer.serializeToJson(result),
        jsonrpc = jsonrpc
    )
    channel?.send(ResponseObject.serializeToJson(response))
}

/**
 * Processes a JSON-RPC response message.
 */
fun Message.response(handler: ResponseObject.() -> Unit) {
    if (this is ResponseObject) {
        this.handler()
    }
}
