// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

/**
 * The authority part of a URI.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2">RFC 3986 (3.2) Authority</a>
 */
data class Authority(
    /**
     * The user information subcomponent.
     *
     * The userinfo subcomponent may consist of a username and, optionally,
     * scheme-specific information about how to gain authorization to access
     * the resource.
     *
     * Use of the format "user:password" in the userinfo field is
     * deprecated.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2.1">RFC 3986 (3.2.1) User Information</a>
     */
    val userinfo: String? = null,

    /**
     * The host subcomponent.
     *
     * The host subcomponent of authority is identified by an IP literal
     * encapsulated within square brackets, an IPv4 address in dotted-decimal
     * form, or a registered name.  The host subcomponent is case-insensitive.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2.2">RFC 3986 (3.2.2) Host</a>
     */
    val host: String,

    /**
     * The port subcomponent.
     *
     * A scheme may define a default port.  For example, the "http" scheme
     * defines a default port of "80", corresponding to its reserved TCP
     * port number.  The type of port designated by the port number (e.g.,
     * TCP, UDP, SCTP) is defined by the URI scheme.
     *
     * URI producers and normalizers should omit the port component and its
     * ":" delimiter if port is empty or if its value would be the same as
     * that of the scheme's default.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2.3">RFC 3986 (3.2.3) Port</a>
     */
    val port: Int? = null
) {
    override fun toString(): String = when {
        userinfo == null && port == null -> host
        userinfo == null -> "$host:$port"
        port == null -> "$userinfo@$host"
        else -> "$userinfo@$host:$port"
    }

    companion object {
        /**
         * Parse the authority part of a URI.
         */
        fun parse(authority: String): Authority {
            val (userinfo, hostAndPort) = parseUserInfo(authority)
            return parseHostAndPort(userinfo, hostAndPort)
        }

        private fun parseUserInfo(
            authority: String
        ): Pair<String?, String> = when (val index = authority.indexOf('@')) {
            -1 -> null to authority
            else -> {
                val userinfo = authority.substring(0, index)
                val host = authority.substring(index + 1)
                userinfo to host
            }
        }

        private fun parseHostAndPort(userinfo: String?, authority: String): Authority = when {
            // IPv6 or IPvFuture host
            authority.startsWith('[') -> when (val portIndex = authority.indexOf(']')) {
                -1 -> invalidHost(authority)
                authority.length - 1 -> Authority(userinfo = userinfo, host = authority)
                else -> {
                    val host = authority.substring(0, portIndex + 1)
                    val port = authority.substring(portIndex + 2)
                    Authority(
                        userinfo = userinfo,
                        host = host,
                        port = port.toIntOrNull() ?: invalidPortNumber(port)
                    )
                }
            }

            // IPv4 or named host
            else -> when (val portIndex = authority.indexOf(':')) {
                -1 -> Authority(userinfo = userinfo, host = authority)
                else -> {
                    val host = authority.substring(0, portIndex)
                    val port = authority.substring(portIndex + 1)
                    Authority(
                        userinfo = userinfo,
                        host = host,
                        port = port.toIntOrNull() ?: invalidPortNumber(port)
                    )
                }
            }
        }
    }
}
