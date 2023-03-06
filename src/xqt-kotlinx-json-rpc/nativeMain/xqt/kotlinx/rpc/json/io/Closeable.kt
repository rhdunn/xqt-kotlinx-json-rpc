// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

/**
 * A resource that can be closed.
 */
actual interface Closeable {
    /**
     * Close the resource.
     */
    actual fun close()
}
