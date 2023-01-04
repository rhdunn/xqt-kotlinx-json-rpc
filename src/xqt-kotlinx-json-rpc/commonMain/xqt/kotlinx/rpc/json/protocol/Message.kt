// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.protocol

/**
 * A general message as defined by JSON-RPC.
 *
 * @see <a href="https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/#abstractMessage">LSP 3.17 Abstract Message</a>
 */
interface Message {
    /**
     * The version of the JSON-RPC protocol.
     *
     * This must be exactly "2.0".
     */
    val jsonprc: String
}
