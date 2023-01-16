// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import java.io.Closeable

/**
 * A binary data channel to write data to.
 */
actual interface BinaryOutputChannel : Closeable {
    /**
     * Write a single byte to the channel.
     */
    actual fun writeByte(byte: Byte)

    /**
     * Write a sequence of bytes to the channel.
     */
    actual fun writeBytes(bytes: ByteArray)

    /**
     * Flush any pending data on the channel.
     */
    actual fun flush()

    /**
     * Close the output channel.
     */
    actual override fun close()

    actual companion object {
        /**
         * The standard output channel.
         *
         * @return the standard output channel, or null if it is not available
         */
        actual val stdout: BinaryOutputChannel? = null
    }
}
