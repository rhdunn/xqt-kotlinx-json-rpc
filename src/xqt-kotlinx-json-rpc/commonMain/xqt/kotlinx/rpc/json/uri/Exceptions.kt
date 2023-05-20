// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.uri

/**
 * The host part of the URI authority is invalid.
 *
 * @param host the host name or IP address
 */
class InvalidHost(val host: String) : RuntimeException("Invalid host: $host")

internal fun invalidHost(host: String): Nothing = throw InvalidHost(host)

/**
 * The port part of the URI authority is invalid.
 *
 * @param port the port number
 */
class InvalidPortNumber(val port: String) : RuntimeException("Invalid port number: $port")

internal fun invalidPortNumber(port: String): Nothing = throw InvalidPortNumber(port)
