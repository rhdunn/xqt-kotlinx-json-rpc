// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.uri

import xqt.kotlinx.rpc.json.uri.Authority
import xqt.kotlinx.rpc.json.uri.Uri
import xqt.kotlinx.rpc.json.uri.UriScheme
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The HTTP URI scheme")
class TheHttpUriScheme {
    @Test
    @DisplayName("supports authority only with host")
    fun supports_authority_only_with_host() {
        val uri = Uri.deserialize("http://localhost")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("", uri.path)
        assertEquals(null, uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("http://localhost", Uri.serializeToString(uri))
        assertEquals("http://localhost", uri.toString())
    }

    @Test
    @DisplayName("supports authority only with host and port")
    fun supports_authority_only_with_host_and_port() {
        val uri = Uri.deserialize("http://localhost:8020")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost", port = 8020), uri.authority)
        assertEquals("", uri.path)
        assertEquals(null, uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("http://localhost:8020", Uri.serializeToString(uri))
        assertEquals("http://localhost:8020", uri.toString())
    }

    @Test
    @DisplayName("supports authority and path")
    fun supports_authority_and_path() {
        val uri = Uri.deserialize("http://localhost/lorem/ipsum/dolor")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("/lorem/ipsum/dolor", uri.path)
        assertEquals(null, uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("http://localhost/lorem/ipsum/dolor", Uri.serializeToString(uri))
        assertEquals("http://localhost/lorem/ipsum/dolor", uri.toString())
    }

    @Test
    @DisplayName("supports authority and query")
    fun supports_authority_and_query() {
        val uri = Uri.deserialize("http://localhost?key1=alpha&key2=beta")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("", uri.path)
        assertEquals("key1=alpha&key2=beta", uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("http://localhost?key1=alpha&key2=beta", Uri.serializeToString(uri))
        assertEquals("http://localhost?key1=alpha&key2=beta", uri.toString())
    }

    @Test
    @DisplayName("supports authority, path, and query")
    fun supports_authority_path_and_query() {
        val uri = Uri.deserialize("http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("/lorem/ipsum/dolor", uri.path)
        assertEquals("key1=alpha&key2=beta", uri.query)
        assertEquals(null, uri.fragment)

        assertEquals("http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta", Uri.serializeToString(uri))
        assertEquals("http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta", uri.toString())
    }

    @Test
    @DisplayName("supports authority, and fragment")
    fun supports_authority_and_fragment() {
        val uri = Uri.deserialize("http://localhost#lorem-ipsum")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("", uri.path)
        assertEquals(null, uri.query)
        assertEquals("lorem-ipsum", uri.fragment)

        assertEquals("http://localhost#lorem-ipsum", Uri.serializeToString(uri))
        assertEquals("http://localhost#lorem-ipsum", uri.toString())
    }

    @Test
    @DisplayName("supports authority, path, and fragment")
    fun supports_authority_path_and_fragment() {
        val uri = Uri.deserialize("http://localhost/lorem/ipsum/dolor#lorem-ipsum")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("/lorem/ipsum/dolor", uri.path)
        assertEquals(null, uri.query)
        assertEquals("lorem-ipsum", uri.fragment)

        assertEquals("http://localhost/lorem/ipsum/dolor#lorem-ipsum", Uri.serializeToString(uri))
        assertEquals("http://localhost/lorem/ipsum/dolor#lorem-ipsum", uri.toString())
    }

    @Test
    @DisplayName("supports authority, query, and fragment")
    fun supports_authority_query_and_fragment() {
        val uri = Uri.deserialize("http://localhost?key1=alpha&key2=beta#lorem-ipsum")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("", uri.path)
        assertEquals("key1=alpha&key2=beta", uri.query)
        assertEquals("lorem-ipsum", uri.fragment)

        assertEquals("http://localhost?key1=alpha&key2=beta#lorem-ipsum", Uri.serializeToString(uri))
        assertEquals("http://localhost?key1=alpha&key2=beta#lorem-ipsum", uri.toString())
    }

    @Test
    @DisplayName("supports authority, path, query, and fragment")
    fun supports_authority_path_query_and_fragment() {
        val uri = Uri.deserialize("http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta#lorem-ipsum")
        assertEquals(UriScheme.HTTP, uri.scheme)
        assertEquals(Authority(host = "localhost"), uri.authority)
        assertEquals("/lorem/ipsum/dolor", uri.path)
        assertEquals("key1=alpha&key2=beta", uri.query)
        assertEquals("lorem-ipsum", uri.fragment)

        assertEquals(
            "http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta#lorem-ipsum",
            Uri.serializeToString(uri)
        )
        assertEquals("http://localhost/lorem/ipsum/dolor?key1=alpha&key2=beta#lorem-ipsum", uri.toString())
    }
}
