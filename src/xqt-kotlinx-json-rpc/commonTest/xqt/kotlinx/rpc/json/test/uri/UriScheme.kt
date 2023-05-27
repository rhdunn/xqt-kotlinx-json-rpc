// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.uri

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import xqt.kotlinx.rpc.json.serialization.UnsupportedKindTypeException
import xqt.kotlinx.rpc.json.serialization.jsonArrayOf
import xqt.kotlinx.rpc.json.serialization.jsonObjectOf
import xqt.kotlinx.rpc.json.uri.UriScheme
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The UriScheme type")
class TheUriSchemeType {
    @Test
    @DisplayName("can serialize to JSON")
    fun can_serialize_to_json() {
        assertEquals("\"file\"", UriScheme.serializeToJson(UriScheme.File).toString())
        assertEquals("\"http\"", UriScheme.serializeToJson(UriScheme.HTTP).toString())
        assertEquals("\"https\"", UriScheme.serializeToJson(UriScheme.HTTPS).toString())
        assertEquals("\"urn\"", UriScheme.serializeToJson(UriScheme.URN).toString())

        assertEquals("\"jar\"", UriScheme.serializeToJson(UriScheme("jar")).toString())
    }

    @Test
    @DisplayName("can deserialize from JSON")
    fun can_deserialize_from_json() {
        assertEquals(UriScheme.File, UriScheme.deserialize(JsonPrimitive("file")))
        assertEquals(UriScheme.HTTP, UriScheme.deserialize(JsonPrimitive("http")))
        assertEquals(UriScheme.HTTPS, UriScheme.deserialize(JsonPrimitive("https")))
        assertEquals(UriScheme.URN, UriScheme.deserialize(JsonPrimitive("urn")))

        assertEquals(UriScheme("jar"), UriScheme.deserialize(JsonPrimitive("jar")))
    }

    @Test
    @DisplayName("throws an error if the kind type is not supported")
    fun throws_an_error_if_the_kind_type_is_not_supported() {
        val e1 = assertFails { UriScheme.deserialize(jsonObjectOf()) }
        assertEquals(UnsupportedKindTypeException::class, e1::class)
        assertEquals("Unsupported kind type 'object'", e1.message)

        val e2 = assertFails { UriScheme.deserialize(jsonArrayOf()) }
        assertEquals(UnsupportedKindTypeException::class, e2::class)
        assertEquals("Unsupported kind type 'array'", e2.message)

        val e3 = assertFails { UriScheme.deserialize(JsonNull) }
        assertEquals(UnsupportedKindTypeException::class, e3::class)
        assertEquals("Unsupported kind type 'null'", e3.message)

        val e4 = assertFails { UriScheme.deserialize(JsonPrimitive(true)) }
        assertEquals(UnsupportedKindTypeException::class, e4::class)
        assertEquals("Unsupported kind type 'boolean'", e4.message)

        val e5 = assertFails { UriScheme.deserialize(JsonPrimitive(1)) }
        assertEquals(UnsupportedKindTypeException::class, e5::class)
        assertEquals("Unsupported kind type 'integer'", e5.message)

        val e6 = assertFails { UriScheme.deserialize(JsonPrimitive(1.2)) }
        assertEquals(UnsupportedKindTypeException::class, e6::class)
        assertEquals("Unsupported kind type 'decimal'", e6.message)
    }
}
