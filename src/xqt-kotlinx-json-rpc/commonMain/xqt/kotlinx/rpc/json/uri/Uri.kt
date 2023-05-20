// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

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
)
