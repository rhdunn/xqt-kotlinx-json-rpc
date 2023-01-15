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

    override val jsonprc: String = Message.JSON_RPC_2_0
) : Message {
    init {
        if (result == null && error == null)
            missingKey("result", "error")
        if (result != null && error != null)
            conflictingKey("result", "error")
    }

    companion object : JsonSerialization<ResponseObject> {
        override fun serializeToJson(value: ResponseObject): JsonElement = buildJsonObject {
            put("jsonrpc", value.jsonprc, JsonString)
            putNullable("id", value.id, JsonIntOrString)
            putOptional("result", value.result, JsonElementType)
            putOptional("error", value.error, ErrorObject)
        }

        override fun deserialize(json: JsonElement): ResponseObject = when (json) {
            !is JsonObject -> unsupportedKindType(json)
            else -> ResponseObject(
                jsonprc = json.get("jsonrpc", JsonString),
                id = json.getNullable("id", JsonIntOrString),
                result = json.getOptional("result", JsonElementType),
                error = json.getOptional("error", ErrorObject)
            )
        }
    }
}

/**
 * Set a JSON-RPC void/empty result for the request.
 */
fun RequestObject.void() {
    response = ResponseObject(
        id = id,
        result = JsonNull
    )
}

/**
 * Set a JSON-RPC response result for the request.
 */
fun <T> RequestObject.result(value: T, serialization: JsonSerialization<T>) {
    response = ResponseObject(
        id = id,
        result = serialization.serializeToJson(value)
    )
}

/**
 * Set a JSON-RPC response error for the request.
 */
fun RequestObject.error(code: ErrorCode, message: String, data: JsonElement? = null) {
    response = ResponseObject(
        id = id,
        error = ErrorObject(
            code = code,
            message = message,
            data = data
        )
    )
}

/**
 * Set a JSON-RPC response error for the request.
 */
fun <T> RequestObject.error(
    code: ErrorCode,
    message: String,
    data: T,
    serialization: JsonSerialization<T>
) {
    error(
        code = code,
        message = message,
        data = serialization.serializeToJson(data)
    )
}
