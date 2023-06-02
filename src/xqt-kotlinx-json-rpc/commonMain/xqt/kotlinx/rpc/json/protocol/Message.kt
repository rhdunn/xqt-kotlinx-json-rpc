// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import xqt.kotlinx.rpc.json.serialization.JsonSerialization
import xqt.kotlinx.rpc.json.serialization.unsupportedKindType

/**
 * A general message as defined by JSON-RPC.
 *
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#abstractMessage">LSP 3.17 Abstract Message</a>
 *
 * @since 1.0.0
 */
sealed interface Message {
    /**
     * The version of the JSON-RPC protocol.
     *
     * This must be exactly "2.0".
     */
    val jsonrpc: String

    companion object : JsonSerialization<Message> {
        /**
         * JSON-RPC 2.0
         */
        const val JSON_RPC_2_0 = "2.0"

        override fun serializeToJson(value: Message): JsonElement = when (value) {
            is RequestObject -> RequestObject.serializeToJson(value)
            is ResponseObject -> ResponseObject.serializeToJson(value)
            is Notification -> Notification.serializeToJson(value)
        }

        override fun deserialize(json: JsonElement): Message = when {
            json !is JsonObject -> unsupportedKindType(json)
            json.containsKey("method") -> when {
                json.containsKey("id") -> RequestObject.deserialize(json)
                else -> Notification.deserialize(json)
            }

            else -> ResponseObject.deserialize(json)
        }
    }
}
