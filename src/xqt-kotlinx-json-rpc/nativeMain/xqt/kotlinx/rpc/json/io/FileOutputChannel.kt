// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.refTo
import platform.posix.*

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

internal actual fun createStdout(): BinaryOutputChannel? = stdout?.let {
    setmode(fileno(it), O_BINARY)
    FileOutputChannel(it)
}
