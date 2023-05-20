// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

import xqt.kotlinx.rpc.json.serialization.StringSerialization

/**
 * A Universal Resource Identifier (URI).
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc3986">RFC 3986 Uniform Resource Identifier (URI): Generic Syntax</a>
 */
data class Uri(
    /**
     * The URI scheme.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.1">RFC 3986 (3.1) Scheme</a>
     */
    val scheme: UriScheme,

    /**
     * The URI authority.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.2">RFC 3986 (3.2) Authority</a>
     */
    val authority: Authority? = null,

    /**
     * The URI path.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.3">RFC 3986 (3.3) Path</a>
     */
    val path: String,

    /**
     * The URI query.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.4">RFC 3986 (3.4) Query</a>
     */
    val query: String? = null,

    /**
     * The URI fragment.
     *
     * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.5">RFC 3986 (3.5) Fragment</a>
     */
    val fragment: String? = null
) {
    override fun toString(): String = when {
        authority != null -> "$scheme://$authority$path"
        else -> "$scheme:$path"
    }

    companion object : StringSerialization<Uri> {
        /**
         * Parse a Universal Resource Identifier (URI).
         *
         * @see <a href="https://www.rfc-editor.org/rfc/rfc3986">RFC 3986 Uniform Resource Identifier (URI): Generic Syntax</a>
         */
        override fun deserialize(value: String): Uri {
            val (scheme, authorityAndPath) = parseScheme(value)
            return when {
                authorityAndPath.startsWith("//") -> {
                    val (authority, pathAndQuery) = parseAuthority(authorityAndPath)
                    Uri(
                        scheme = scheme,
                        authority = authority,
                        path = pathAndQuery
                    )
                }

                else -> Uri(
                    scheme = scheme,
                    path = authorityAndPath,
                )
            }
        }

        override fun serializeToString(value: Uri): String = value.toString()

        private fun parseScheme(uri: String): Pair<UriScheme, String> = when (val index = uri.indexOf(':')) {
            -1 -> UriScheme.valueOf(uri) to ""
            else -> {
                val scheme = uri.substring(0, index)
                val path = uri.substring(index + 1)
                UriScheme.valueOf(scheme) to path
            }
        }

        private fun parseAuthority(uri: String): Pair<Authority, String> = when (val index = uri.indexOf('/', 2)) {
            -1 -> Authority.parse(uri.substring(2)) to ""
            else -> {
                val authority = uri.substring(2, index)
                val path = uri.substring(index)
                Authority.parse(authority) to path
            }
        }
    }
}
