// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

/**
 * A binary data channel to read data from.
 */
expect interface BinaryInputChannel {
    /**
     * Reads a single byte from the channel.
     *
     * @return the byte read, or null if the channel has no more data
     */
    fun readByte(): Byte?

    /**
     * Reads a number of bytes from the channel.
     *
     * If there are not enough bytes in the channel to read `size` bytes, the
     * bytes that are available are read.
     *
     * @param size the number of bytes to read
     */
    fun readBytes(size: Int): ByteArray

    /**
     * Close the input channel.
     */
    fun close()

    companion object {
        /**
         * The standard input channel.
         *
         * @return the standard input channel, or null if it is not available
         */
        val stdin: BinaryInputChannel?
    }
}

/**
 * Reads a line from the channel as a UTF-8 string.
 */
fun BinaryInputChannel.readUtf8Line(lineEnding: LineEnding, maxLength: Int = 512): String? {
    val line = ByteArray(maxLength)
    var c: Byte? = readByte() ?: return null

    var i = 0
    while (i < line.size && c != null) {
        line[i] = c
        when (lineEnding.secondByte) {
            null -> if (line[i] == lineEnding.firstByte) {
                return line.decodeToString(endIndex = i)
            }

            else -> if (i > 0 && line[i - 1] == lineEnding.firstByte && line[i] == lineEnding.secondByte) {
                return line.decodeToString(endIndex = i - 1)
            }
        }
        ++i
        c = readByte()
    }
    return line.decodeToString()
}
