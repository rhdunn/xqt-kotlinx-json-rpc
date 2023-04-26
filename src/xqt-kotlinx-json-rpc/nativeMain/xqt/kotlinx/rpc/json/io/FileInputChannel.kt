// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import platform.posix.*

private class FileInputChannel(private val input: CPointer<FILE>) : BinaryInputChannel {
    override fun readByte(): Byte? = when (val c = fgetc(input)) {
        EOF -> null
        else -> c.toByte()
    }

    override fun readBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        return when (val readBytes = fread(bytes.refTo(0), 1, size.convert<size_t>(), input).toInt()) {
            size -> bytes
            else -> bytes.sliceArray(0 until readBytes)
        }
    }

    override fun close() {
    }
}

internal actual fun createStdin(): BinaryInputChannel? = stdin?.let {
    setmode(fileno(it), O_BINARY)
    FileInputChannel(it)
}
