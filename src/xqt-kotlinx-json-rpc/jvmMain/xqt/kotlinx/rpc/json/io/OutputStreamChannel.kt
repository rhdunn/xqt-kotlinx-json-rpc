// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import java.io.OutputStream

private class OutputStreamChannel(private val output: OutputStream) : BinaryOutputChannel {
    override fun writeByte(byte: Byte) {
        output.write(byte.toInt())
    }

    override fun writeBytes(bytes: ByteArray) {
        output.write(bytes)
    }

    override fun flush() {
        output.flush()
    }

    override fun close() {
    }
}

internal actual fun createStdout(): BinaryOutputChannel? = OutputStreamChannel(System.out)
