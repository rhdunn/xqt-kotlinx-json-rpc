// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.serialization

import kotlinx.serialization.json.*

/**
 * Returns a new read-only JSON object with the specified contents, given as a
 * list of pairs where the first value is the key and the second is the value.
 *
 * If multiple pairs have the same key, the resulting map will contain the value
 * from the last of those pairs.
 *
 * Entries of the map are iterated in the order they were specified.
 */
fun jsonObjectOf(vararg pairs: Pair<String, JsonElement>): JsonObject = JsonObject(mapOf(*pairs))

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.deserialize(key: String, serializer: JsonSerialization<T>): T {
    return serializer.deserialize(get(key) ?: missingKey(key))
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.deserializeOptional(key: String, serializer: JsonSerialization<T>): T? {
    return get(key)?.let { serializer.deserialize(it) }
}
