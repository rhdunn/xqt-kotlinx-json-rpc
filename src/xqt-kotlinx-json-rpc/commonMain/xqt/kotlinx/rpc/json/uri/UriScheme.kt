// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

import kotlin.jvm.JvmInline

/**
 * A Universal Resource Identifier (URI) scheme.
*
 * @param name the name of the URI scheme
 *
* @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-3.1">RFC 3986 (3.1) Scheme</a>
 */
@JvmInline
value class UriScheme(val name: String)
