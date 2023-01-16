// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.refTo
import platform.posix.*

/**
 * A binary data channel to read data from.
 */
actual interface BinaryInputChannel {
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
    actual fun close()

    actual companion object {
        /**
         * The standard input channel.
         *
         * @return the standard input channel, or null if it is not available
         */
        actual val stdin: BinaryInputChannel? by lazy {
            platform.posix.stdin?.let {
                setmode(fileno(it), O_BINARY)
                FileInputChannel(it)
            }
        }
    }
}

private class FileInputChannel(private val input: CPointer<FILE>) : BinaryInputChannel {
    override fun readByte(): Byte? = when (val c = fgetc(input)) {
        EOF -> null
        else -> c.toByte()
    }

    override fun readBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        return when (val readBytes = fread(bytes.refTo(0), 1, size.toULong(), input).toInt()) {
            size -> bytes
            else -> bytes.sliceArray(0 until readBytes)
        }
    }

    override fun close() {
    }
}
