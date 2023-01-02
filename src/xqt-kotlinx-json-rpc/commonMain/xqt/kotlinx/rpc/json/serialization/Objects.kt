// Copyright (C) 2022-2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
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
 * Serialization helpers for typed maps.
 */
object JsonTypedMap {
    /**
     * Serialize the map of values to JSON.
     *
     * @param keySerializer how to serialize the keys in the map.
     * @param valueSerializer how to serialize the values in the map.
     */
    fun <K, V> serialize(
        value: Map<K, V>,
        keySerializer: StringSerialization<K>,
        valueSerializer: JsonSerialization<V>
    ): JsonElement = buildJsonObject {
        value.forEach { (key, value) ->
            val k = keySerializer.serializeToString(key)
            val v = valueSerializer.serializeToJson(value)
            put(k, v)
        }
    }

    /**
     * Deserialize the map of values from the `json` object.
     *
     * @param keySerializer how to deserialize the keys in the map.
     * @param valueSerializer how to deserialize the values in the map.
     */
    fun <K, V> deserialize(
        json: JsonElement,
        keySerializer: StringSerialization<K>,
        valueSerializer: JsonSerialization<V>
    ): Map<K, V> = when (json) {
        !is JsonObject -> unsupportedKindType(json)
        else -> json.entries.associate { (key, value) ->
            val k = keySerializer.deserialize(key)
            val v = valueSerializer.deserialize(value)
            k to v
        }
    }
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.get(key: String, serializer: JsonSerialization<T>): T {
    return serializer.deserialize(get(key) ?: missingKey(key))
}

/**
 * Deserialize the array of data types or objects from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON elements in the array.
 */
fun <T> JsonObject.getArray(key: String, serializer: JsonSerialization<T>): List<T> {
    val data = get(key) ?: missingKey(key)
    return JsonTypedArray.deserialize(data, serializer)
}

/**
 * Deserialize the data type or object from the `json` element.
 *
 * @param key the name of the required key to deserialize.
 * @param serializer how to deserialize the JSON element value.
 */
fun <T> JsonObject.getOptional(key: String, serializer: JsonSerialization<T>): T? {
    return get(key)?.let { serializer.deserialize(it) }
}

/**
 * Deserialize the array of data types or objects from the `json` element.
 *
 * @param key the name of the optional key to deserialize.
 * @param serializer how to deserialize the JSON elements in the array.
 */
fun <T> JsonObject.getOptionalArray(key: String, serializer: JsonSerialization<T>): List<T> {
    val data = get(key) ?: return listOf()
    return JsonTypedArray.deserialize(data, serializer)
}

/**
 * Serialize the map of data types to the JSON element.
 *
 * If the map is empty, this will not add the map to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param keySerializer how to deserialize the keys in the map.
 * @param valueSerializer how to deserialize the values in the map.
 */
fun <K, V> JsonObject.getOptionalMap(
    key: String,
    keySerializer: StringSerialization<K>,
    valueSerializer: JsonSerialization<V>
): Map<K, V> {
    val data = get(key) ?: return mapOf()
    return JsonTypedMap.deserialize(data, keySerializer, valueSerializer)
}

/**
 * Serialize the data type or object to the JSON element.
 *
 * If the value is null, this will add it to the JSON element as a null.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.put(key: String, value: T?, serializer: JsonSerialization<T>) {
    put(key, value?.let { serializer.serializeToJson(it) } ?: JsonNull)
}

/**
 * Serialize the array of data types to the JSON element.
 *
 * If the list is empty, this will add the empty array in the specified key of
 * the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.putArray(key: String, value: List<T>, serializer: JsonSerialization<T>) {
    put(key, JsonTypedArray.serialize(value, serializer))
}

/**
 * Serialize the data type or object to the JSON element.
 *
 * If the value is null, this will not add it to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.putOptional(key: String, value: T?, serializer: JsonSerialization<T>) {
    value?.let { put(key, serializer.serializeToJson(it)) }
}

/**
 * Serialize the array of data types to the JSON element.
 *
 * If the list is empty, this will not add the array to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param serializer how to serialize the JSON element value.
 */
fun <T> JsonObjectBuilder.putOptionalArray(key: String, value: List<T>, serializer: JsonSerialization<T>) {
    if (value.isNotEmpty()) {
        put(key, JsonTypedArray.serialize(value, serializer))
    }
}

/**
 * Serialize the map of data types to the JSON element.
 *
 * If the map is empty, this will not add the map to the JSON element.
 *
 * @param key the name of the key to serialize to.
 * @param value the content of the array to serialize to.
 * @param keySerializer how to serialize the keys in the map.
 * @param valueSerializer how to serialize the values in the map.
 */
fun <K, V> JsonObjectBuilder.putOptionalMap(
    key: String,
    value: Map<K, V>,
    keySerializer: StringSerialization<K>,
    valueSerializer: JsonSerialization<V>
) {
    if (value.isNotEmpty()) {
        put(key, JsonTypedMap.serialize(value, keySerializer, valueSerializer))
    }
}
