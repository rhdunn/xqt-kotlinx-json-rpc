// Copyright (C) 2022 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.serialization.types

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.types.JsonDouble
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The JSON Double type")
class TheJsonDoubleType {
    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("1.2", JsonDouble.serialize(1.2).toString())

        // ISSUE: https://github.com/Kotlin/kotlinx.serialization/issues/2128
        //        Returns `0` on JS, `0.0` on JVM and Native
        assertContains(arrayOf("0", "0.0"), JsonDouble.serialize(0.0).toString())

        assertEquals("-7.5", JsonDouble.serialize(-7.5).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON integer")
    fun can_deserialize_from_json_integer() {
        assertEquals(1.0, JsonDouble.deserialize(JsonPrimitive(1)))
        assertEquals(0.0, JsonDouble.deserialize(JsonPrimitive(0)))
        assertEquals(-7.0, JsonDouble.deserialize(JsonPrimitive(-7)))
    }

    @Test
    @DisplayName("can deserialize from JSON decimal")
    fun can_deserialize_from_json_decimal() {
        assertEquals(1.2, JsonDouble.deserialize(JsonPrimitive(1.2)))
        assertEquals(0.0, JsonDouble.deserialize(JsonPrimitive(0.0)))
        assertEquals(-7.5, JsonDouble.deserialize(JsonPrimitive(-7.5)))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { JsonDouble.deserialize(JsonObject(mapOf())) }
        assertEquals(IllegalArgumentException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { JsonDouble.deserialize(JsonArray(arrayListOf())) }
        assertEquals(IllegalArgumentException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { JsonDouble.deserialize(JsonNull) }
        assertEquals(IllegalArgumentException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { JsonDouble.deserialize(JsonPrimitive("test")) }
        assertEquals(IllegalArgumentException::class, e4::class)
        assertEquals("Unsupported kind type 'string'", e4.message)

        val e5 = assertFails { JsonDouble.deserialize(JsonPrimitive(true)) }
        assertEquals(IllegalArgumentException::class, e5::class)
        assertEquals("Unsupported kind type 'boolean'", e5.message)
    }
}
