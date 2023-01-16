// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import java.io.Closeable
import java.io.InputStream

/**
 * A binary data channel to read data from.
 */
actual interface BinaryInputChannel : Closeable {
    /**
     * Reads a single byte from the channel.
     *
     * @return the byte read, or null if the channel has no more data
     */
    actual fun readByte(): Byte?

    /**
     * Reads a number of bytes from the channel.
     *
     * If there are not enough bytes in the channel to read `size` bytes, the
     * bytes that are available are read.
     *
     * @param size the number of bytes to read
     */
    actual fun readBytes(size: Int): ByteArray

    /**
     * Close the input channel.
     */
    actual override fun close()

    actual companion object {
        /**
         * The standard input channel.
         *
         * @return the standard input channel, or null if it is not available
         */
        actual val stdin: BinaryInputChannel? by lazy {
            InputStreamChannel(System.`in`)
        }
    }
}

private class InputStreamChannel(private val input: InputStream) : BinaryInputChannel {
    override fun readByte(): Byte? = when (val c = input.read()) {
        -1 -> null
        else -> c.toByte()
    }

    override fun readBytes(size: Int): ByteArray = input.readNBytes(size)

    override fun close() {
    }
}