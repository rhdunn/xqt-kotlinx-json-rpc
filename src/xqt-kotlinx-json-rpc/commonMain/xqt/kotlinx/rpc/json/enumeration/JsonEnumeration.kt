// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.enumeration

/**
 * Defines a JSON enumeration or namespaced constant type.
 *
 * @since 1.1.0
 */
interface JsonEnumeration<T> {
    /**
     * The underlying enumeration or namespace constant value.
     */
    val kind: T
}
