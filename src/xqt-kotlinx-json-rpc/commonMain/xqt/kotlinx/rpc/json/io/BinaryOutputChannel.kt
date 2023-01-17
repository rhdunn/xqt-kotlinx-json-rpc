// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

/**
 * A binary data channel to write data to.
 */
expect interface BinaryOutputChannel {
    /**
     * Write a single byte to the channel.
     */
    fun writeByte(byte: Byte)

    /**
     * Write a sequence of bytes to the channel.
     */
    fun writeBytes(bytes: ByteArray)

    /**
     * Flush any pending data on the channel.
     */
    fun flush()

    /**
     * Close the output channel.
     */
    fun close()

    companion object {
        /**
         * The standard output channel.
         *
         * @return the standard output channel, or null if it is not available
         */
        val stdout: BinaryOutputChannel?
    }
}

/**
 * Writes a string to the channel as a UTF-8 string.
 */
fun BinaryOutputChannel.writeUtf8String(string: String, lineEnding: LineEnding? = null) {
    if (string.isNotEmpty()) {
        writeBytes(string.encodeToByteArray())
    }
    lineEnding?.firstByte?.let { writeByte(it) }
    lineEnding?.secondByte?.let { writeByte(it) }
}
