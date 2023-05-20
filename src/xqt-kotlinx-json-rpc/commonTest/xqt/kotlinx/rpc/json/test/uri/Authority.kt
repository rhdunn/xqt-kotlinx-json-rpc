// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.uri

import xqt.kotlinx.rpc.json.uri.Authority
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals

@DisplayName("The URI authority")
class TheUriAuthority {
    @Test
    @DisplayName("supports hostname-only authorities")
    fun supports_hostname_only_authorities() {
        val authority = Authority.parse("www.example.com")
        assertEquals("www.example.com", authority.host)
        assertEquals(null, authority.port)

        assertEquals("www.example.com", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4-only authorities")
    fun supports_ipv4_only_authorities() {
        val authority = Authority.parse("192.168.0.1")
        assertEquals("192.168.0.1", authority.host)
        assertEquals(null, authority.port)

        assertEquals("192.168.0.1", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6-only authorities")
    fun supports_ipv6_only_authorities() {
        val authority = Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]")
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(null, authority.port)

        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.toString())
    }

    @Test
    @DisplayName("supports hostname and port authorities")
    fun supports_hostname_and_port_authorities() {
        val authority = Authority.parse("www.example.com:8022")
        assertEquals("www.example.com", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("www.example.com:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4 and port authorities")
    fun supports_ipv4_and_port_authorities() {
        val authority = Authority.parse("192.168.0.1:8022")
        assertEquals("192.168.0.1", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("192.168.0.1:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6 and port authorities")
    fun supports_ipv6_and_port_authorities() {
        val authority = Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]:8022")
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]:8022", authority.toString())
    }
}
