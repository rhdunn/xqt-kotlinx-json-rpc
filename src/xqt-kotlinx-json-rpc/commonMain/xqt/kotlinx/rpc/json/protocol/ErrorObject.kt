// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.types.JsonInt
import kotlin.jvm.JvmInline

/**
 * A number that indicates the error type that occurred.
 *
 * @param code the error code value.
 *
 * @see <a href="https://www.jsonrpc.org/specification#error_object">JSON-RPC 2.0 Error object code</a>
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
