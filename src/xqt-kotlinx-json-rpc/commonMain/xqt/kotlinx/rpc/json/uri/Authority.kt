// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

/**
 * The authority part of a URI.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2">RFC 3986 (3.2) Authority</a>
 */
data class Authority(
    /**
     * The host subcomponent.
     *
     * The host subcomponent of authority is identified by an IP literal
     * encapsulated within square brackets, an IPv4 address in dotted-decimal
     * form, or a registered name.  The host subcomponent is case-insensitive.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2.2">RFC 3986 (3.2.2) Host</a>
     */
    val host: String
) {
    override fun toString(): String = host

    companion object {
        /**
         * Parse the authority part of a URI.
         */
        fun parse(authority: String): Authority {
            return Authority(
                host = authority
            )
        }
    }
}
