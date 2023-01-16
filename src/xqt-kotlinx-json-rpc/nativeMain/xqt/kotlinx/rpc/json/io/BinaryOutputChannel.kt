// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.refTo
import platform.posix.*

/**
 * A binary data channel to write data to.
 */
actual interface BinaryOutputChannel {
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
    actual fun close()

    actual companion object {
        /**
         * The standard output channel.
         *
         * @return the standard output channel, or null if it is not available
         */
        actual val stdout: BinaryOutputChannel? by lazy {
            platform.posix.stdout?.let {
                setmode(fileno(it), O_BINARY)
                FileOutputChannel(it)
            }
        }
    }
}

private class FileOutputChannel(val output: CPointer<FILE>) : BinaryOutputChannel {
    override fun writeByte(byte: Byte) {
        fputc(byte.toInt(), output)
    }

    override fun writeBytes(bytes: ByteArray) {
        fwrite(bytes.refTo(0), bytes.size.toULong(), 1, output)
    }

    override fun flush() {
        fflush(output)
    }

    override fun close() {
    }
}
