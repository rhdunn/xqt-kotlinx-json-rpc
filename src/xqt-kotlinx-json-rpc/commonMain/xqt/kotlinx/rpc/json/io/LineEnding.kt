// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

/**
 * The line ending for different platforms.
 *
 * @param firstByte the first byte in the line ending
 * @param secondByte the optional second byte in the line ending
 *
 * @since 1.0.0
 */
enum class LineEnding(val firstByte: Byte, val secondByte: Byte? = null) {
    /**
     * Line feed (LF) on Posix systems.
     */
    Lf('\n'.code.toByte()),

    /**
     * Carriage Return (CR) Line Feed (LF) on Windows systems.
     *
     * This is also used in HTTP and related protocols.
     */
    CrLf('\r'.code.toByte(), '\n'.code.toByte()),

    /**
     * Carriage Return (CR) on Mac systems.
     */
    Cr('\r'.code.toByte())
}
