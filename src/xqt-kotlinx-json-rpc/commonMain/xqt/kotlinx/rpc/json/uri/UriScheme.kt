// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

import xqt.kotlinx.rpc.json.enumeration.JsonEnumeration
import xqt.kotlinx.rpc.json.enumeration.JsonStringEnumerationType
import kotlin.jvm.JvmInline

/**
 * A Universal Resource Identifier (URI) scheme.
 *
 * @param kind the name of the URI scheme
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.1">RFC 3986 (3.1) Scheme</a>
 *
 * @since 1.1.0
 */
@JvmInline
value class UriScheme(override val kind: String) : JsonEnumeration<String> {
    override fun toString(): String = kind

    companion object : JsonStringEnumerationType<UriScheme>() {
        /**
         * The "file" URI scheme.
         *
         * @see <a href="https://www.rfc-editor.org/rfc/rfc8089">RFC 8089 The "file" URI Scheme</a>
         */
        val File: UriScheme = UriScheme("file")

        /**
         * The "http" URL scheme.
         *
         * @see <a href="https://www.rfc-editor.org/rfc/rfc1945#section-3.2.2">RFC 1945 (HTTP/1.0) http URL</a>
         */
        val HTTP: UriScheme = UriScheme("http")

        /**
         * The "https" URL scheme.
         */
        val HTTPS: UriScheme = UriScheme("https")

        /**
         * The Universal Resource Name (URN) URI scheme.
         *
         * @see <a href="https://www.rfc-editor.org/rfc/rfc2141">RFC 2141 URN Syntax</a>
         */
        val URN: UriScheme = UriScheme("urn")

        /**
         * Lookup the URI scheme.
         */
        override fun valueOf(value: String): UriScheme = UriScheme(value)
    }
}
