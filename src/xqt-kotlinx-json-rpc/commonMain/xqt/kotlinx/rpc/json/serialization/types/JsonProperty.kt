// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization.types

/**
 * The state of the JSON property in the containing JSON object.
 *
 * @since 1.1.0
 */
enum class JsonPropertyState {
    /**
     * The property exist within the JSON object.
     *
     * A null value will be serialized as `JsonNull`.
     */
    Present,

    /**
     * The property does not exist within the JSON object.
     */
    Missing,
}
