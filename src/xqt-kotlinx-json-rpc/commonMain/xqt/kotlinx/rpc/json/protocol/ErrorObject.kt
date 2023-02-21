// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import xqt.kotlinx.rpc.json.serialization.*
import xqt.kotlinx.rpc.json.serialization.types.JsonInt
import xqt.kotlinx.rpc.json.serialization.types.JsonString
import kotlin.jvm.JvmInline
import xqt.kotlinx.rpc.json.serialization.types.JsonElement as JsonElementType

/**
 * A number that indicates the error type that occurred.
 *
 * @param code the error code value.
 *
 * @see <a href="https://www.jsonrpc.org/specification#error_object">JSON-RPC 2.0 Error object</a>
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#errorCodes">LSP 3.17 ErrorCodes</a>
 */
@JvmInline
value class ErrorCode(val code: Int) {
    companion object : JsonSerialization<ErrorCode> {
        override fun serializeToJson(value: ErrorCode): JsonPrimitive = JsonPrimitive(value.code)

        override fun deserialize(json: JsonElement): ErrorCode {
            return ErrorCode(JsonInt.deserialize(json))
        }

        /**
         * Invalid JSON was received by the server.
         *
         * An error occurred on the server while parsing the JSON text.
         */
        val ParseError: ErrorCode = ErrorCode(-32700)

        /**
         * Invalid method parameter(s).
         */
        val InternalError: ErrorCode = ErrorCode(-32603)

        /**
         * Invalid method parameter(s).
         */
        val InvalidParams: ErrorCode = ErrorCode(-32602)

        /**
         * The method does not exist / is not available.
         */
        val MethodNotFound: ErrorCode = ErrorCode(-32601)

        /**
         * The JSON sent is not a valid Request object.
         */
        val InvalidRequest: ErrorCode = ErrorCode(-32600)

        /**
         * Reserved for implementation-defined server-errors.
         */
        val ServerErrorRangeStart: ErrorCode = ErrorCode(-32099)

        /**
         * Reserved for implementation-defined server-errors.
         */
        val ServerErrorRangeEnd: ErrorCode = ErrorCode(-32000)
    }
}

/**
 * An error processing an RPC call.
 *
 * @see <a href="https://www.jsonrpc.org/specification#error_object">JSON-RPC 2.0 Error object</a>
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#responseError">LSP 3.17 ResponseError</a>
 */
data class ErrorObject(
    /**
     * A number indicating the error type that occurred.
     */
    val code: ErrorCode,

    /**
     * A string providing a short description of the error.
     */
    override val message: String,

    /**
     * A primitive or structured value that contains additional
     * information about the error.
     *
     * Can be omitted.
     */
    val data: JsonElement? = null
) : RuntimeException() {
    companion object : JsonSerialization<ErrorObject> {
        override fun serializeToJson(value: ErrorObject): JsonElement = buildJsonObject {
            put("code", value.code, ErrorCode)
            put("message", value.message, JsonString)
            putOptional("data", value.data, JsonElementType)
        }

        override fun deserialize(json: JsonElement): ErrorObject = when (json) {
            !is JsonObject -> unsupportedKindType(json)
            else -> ErrorObject(
                code = json.get("code", ErrorCode),
                message = json.get("message", JsonString),
                data = json.getOptional("data", JsonElementType)
            )
        }
    }
}

/**
 * Invalid JSON was received by the server.
 *
 * An error occurred on the server while parsing the JSON text.
 *
 * @param message a string providing a short description of the error
 * @param data a primitive or structured value that contains additional
 *             information about the error
 */
@Suppress("FunctionName")
fun ParseError(message: String? = null, data: JsonElement? = null): ErrorObject = ErrorObject(
    code = ErrorCode.ParseError,
    message = message ?: "Parse Error",
    data = data
)

/**
 * Invalid method parameter(s).
 *
 * @param message a string providing a short description of the error
 * @param data a primitive or structured value that contains additional
 *             information about the error
 */
@Suppress("FunctionName")
fun InternalError(message: String? = null, data: JsonElement? = null): ErrorObject = ErrorObject(
    code = ErrorCode.InternalError,
    message = message ?: "Internal Error",
    data = data
)

/**
 * Invalid method parameter(s).
 *
 * @param message a string providing a short description of the error
 * @param data a primitive or structured value that contains additional
 *             information about the error
 */
@Suppress("FunctionName")
fun InvalidParams(message: String? = null, data: JsonElement? = null): ErrorObject = ErrorObject(
    code = ErrorCode.InvalidParams,
    message = message ?: "Invalid Parameters",
    data = data
)

/**
 * The method does not exist / is not available.
 *
 * @param message a string providing a short description of the error
 * @param data a primitive or structured value that contains additional
 *             information about the error
 */
@Suppress("FunctionName")
fun MethodNotFound(message: String? = null, data: JsonElement? = null): ErrorObject = ErrorObject(
    code = ErrorCode.ParseError,
    message = message ?: "Method Not Found",
    data = data
)

/**
 * The JSON sent is not a valid Request object.
 *
 * @param message a string providing a short description of the error
 * @param data a primitive or structured value that contains additional
 *             information about the error
 */
@Suppress("FunctionName")
fun InvalidRequest(message: String? = null, data: JsonElement? = null): ErrorObject = ErrorObject(
    code = ErrorCode.InvalidRequest,
    message = message ?: "Invalid Request",
    data = data
)
