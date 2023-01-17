// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

import kotlinx.serialization.json.JsonElement

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
