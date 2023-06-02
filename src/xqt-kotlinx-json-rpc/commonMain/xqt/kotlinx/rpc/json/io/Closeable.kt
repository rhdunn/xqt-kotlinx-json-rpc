// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

/**
 * A resource that can be closed.
 *
 * @since 1.0.0
 */
expect interface Closeable {
    /**
     * Close the resource.
     */
    fun close()
}
