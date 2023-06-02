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

/**
 * A JSON property that can be null or missing from a JSON object.
 *
 * @since 1.1.0
 */
data class JsonProperty<T> internal constructor(
    /**
     * The state of the property in the JSON object.
     */
    val state: JsonPropertyState,

    /**
     * The value of the property in the JSON object.
     *
     * A null value will be stored in a JSON object as a `JsonNull` if the state
     * is `Present`, or won't be added to the object if the state is `Missing`.
     */
    val value: T?
) {
    constructor(value: T?) : this(JsonPropertyState.Present, value)

    companion object {
        /**
         * Returns a JsonProperty representing a missing value from a JSON object.
         */
        fun <T> missing(): JsonProperty<T> = JsonProperty(JsonPropertyState.Missing, null)
    }
}
