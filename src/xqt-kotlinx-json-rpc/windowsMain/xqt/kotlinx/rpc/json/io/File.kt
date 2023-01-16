// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.io

import kotlinx.cinterop.CPointer
import platform.posix.*

internal fun fileno(file: CPointer<FILE>?): Int = _fileno(file)

internal fun setmode(file: Int, mode: Int): Int = _setmode(file, mode)

internal const val O_BINARY: Int = _O_BINARY
