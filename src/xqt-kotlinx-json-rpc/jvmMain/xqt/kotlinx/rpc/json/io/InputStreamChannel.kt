// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import java.io.InputStream

private class InputStreamChannel(private val input: InputStream) : BinaryInputChannel {
    override fun readByte(): Byte? = when (val c = input.read()) {
        -1 -> null
        else -> c.toByte()
    }

    override fun readBytes(size: Int): ByteArray = input.readNBytes(size)

    override fun close() {
    }
}

internal actual fun createStdin(): BinaryInputChannel? = InputStreamChannel(System.`in`)
