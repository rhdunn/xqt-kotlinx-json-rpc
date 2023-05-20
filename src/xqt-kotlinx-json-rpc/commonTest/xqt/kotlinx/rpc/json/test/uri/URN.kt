// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.uri

import xqt.kotlinx.rpc.json.uri.Uri
import xqt.kotlinx.rpc.json.uri.UriScheme
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The URN URI scheme")
class TheUrnUriScheme {
    @Test
    @DisplayName("supports scheme and path")
    fun supports_scheme_and_path() {
        val uri = Uri.deserialize("urn:lorem:ipsum:dolor")
        assertEquals(UriScheme.URN, uri.scheme)
        assertEquals(null, uri.authority)
        assertEquals("lorem:ipsum:dolor", uri.path)
        assertEquals(null, uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("urn:lorem:ipsum:dolor", Uri.serializeToString(uri))
        assertEquals("urn:lorem:ipsum:dolor", uri.toString())
    }
}
