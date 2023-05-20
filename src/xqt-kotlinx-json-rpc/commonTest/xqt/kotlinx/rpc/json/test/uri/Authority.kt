// Copyright (C) 2023 Reece H. Dunn. SPDX-License-Identifier: Apache-2.0
package xqt.kotlinx.rpc.json.test.uri

import xqt.kotlinx.rpc.json.uri.Authority
import xqt.kotlinx.rpc.json.uri.InvalidPortNumber
import xqt.kotlinx.test.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@DisplayName("The URI authority")
class TheUriAuthority {
    @Test
    @DisplayName("supports hostname-only authorities")
    fun supports_hostname_only_authorities() {
        val authority = Authority.parse("www.example.com")
        assertEquals(null, authority.userinfo)
        assertEquals("www.example.com", authority.host)
        assertEquals(null, authority.port)

        assertEquals("www.example.com", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4-only authorities")
    fun supports_ipv4_only_authorities() {
        val authority = Authority.parse("192.168.0.1")
        assertEquals(null, authority.userinfo)
        assertEquals("192.168.0.1", authority.host)
        assertEquals(null, authority.port)

        assertEquals("192.168.0.1", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6-only authorities")
    fun supports_ipv6_only_authorities() {
        val authority = Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]")
        assertEquals(null, authority.userinfo)
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(null, authority.port)

        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.toString())
    }

    @Test
    @DisplayName("supports hostname and port authorities")
    fun supports_hostname_and_port_authorities() {
        val authority = Authority.parse("www.example.com:8022")
        assertEquals(null, authority.userinfo)
        assertEquals("www.example.com", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("www.example.com:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4 and port authorities")
    fun supports_ipv4_and_port_authorities() {
        val authority = Authority.parse("192.168.0.1:8022")
        assertEquals(null, authority.userinfo)
        assertEquals("192.168.0.1", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("192.168.0.1:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6 and port authorities")
    fun supports_ipv6_and_port_authorities() {
        val authority = Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]:8022")
        assertEquals(null, authority.userinfo)
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]:8022", authority.toString())
    }

    @Test
    @DisplayName("supports hostname and userinfo authorities")
    fun supports_hostname_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@www.example.com")
        assertEquals("lorem", authority.userinfo)
        assertEquals("www.example.com", authority.host)
        assertEquals(null, authority.port)

        assertEquals("lorem@www.example.com", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4 and userinfo authorities")
    fun supports_ipv4_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@192.168.0.1")
        assertEquals("lorem", authority.userinfo)
        assertEquals("192.168.0.1", authority.host)
        assertEquals(null, authority.port)

        assertEquals("lorem@192.168.0.1", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6 and userinfo authorities")
    fun supports_ipv6_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@[2001:db8:3333:4444:5555:6666:7777:8888]")
        assertEquals("lorem", authority.userinfo)
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(null, authority.port)

        assertEquals("lorem@[2001:db8:3333:4444:5555:6666:7777:8888]", authority.toString())
    }

    @Test
    @DisplayName("supports hostname, port, and userinfo authorities")
    fun supports_hostname_port_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@www.example.com:8022")
        assertEquals("lorem", authority.userinfo)
        assertEquals("www.example.com", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("lorem@www.example.com:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv4, port, and userinfo authorities")
    fun supports_ipv4_port_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@192.168.0.1:8022")
        assertEquals("lorem", authority.userinfo)
        assertEquals("192.168.0.1", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("lorem@192.168.0.1:8022", authority.toString())
    }

    @Test
    @DisplayName("supports IPv6, port, and userinfo authorities")
    fun supports_ipv6_port_and_userinfo_authorities() {
        val authority = Authority.parse("lorem@[2001:db8:3333:4444:5555:6666:7777:8888]:8022")
        assertEquals("lorem", authority.userinfo)
        assertEquals("[2001:db8:3333:4444:5555:6666:7777:8888]", authority.host)
        assertEquals(8022, authority.port)

        assertEquals("lorem@[2001:db8:3333:4444:5555:6666:7777:8888]:8022", authority.toString())
    }

    @Test
    @DisplayName("throws an error if the port contains multiple colons")
    fun throws_an_error_if_the_port_contains_multiple_colons() {
        val e1 = assertFails { Authority.parse("localhost:123:456") }
        assertEquals(InvalidPortNumber::class, e1::class)
        assertEquals("Invalid port number: 123:456", e1.message)
        assertEquals("123:456", (e1 as InvalidPortNumber).port)

        val e2 = assertFails { Authority.parse("192.168.0.1:123:456") }
        assertEquals(InvalidPortNumber::class, e2::class)
        assertEquals("Invalid port number: 123:456", e2.message)
        assertEquals("123:456", (e2 as InvalidPortNumber).port)

        val e3 = assertFails { Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]:123:456") }
        assertEquals(InvalidPortNumber::class, e3::class)
        assertEquals("Invalid port number: 123:456", e3.message)
        assertEquals("123:456", (e3 as InvalidPortNumber).port)
    }

    @Test
    @DisplayName("throws an error if the port contains non-numeric characters")
    fun throws_an_error_if_the_port_contains_non_numeric_characters() {
        val e1 = assertFails { Authority.parse("localhost:12ab") }
        assertEquals(InvalidPortNumber::class, e1::class)
        assertEquals("Invalid port number: 12ab", e1.message)
        assertEquals("12ab", (e1 as InvalidPortNumber).port)

        val e2 = assertFails { Authority.parse("192.168.0.1:12ab") }
        assertEquals(InvalidPortNumber::class, e2::class)
        assertEquals("Invalid port number: 12ab", e2.message)
        assertEquals("12ab", (e2 as InvalidPortNumber).port)

        val e3 = assertFails { Authority.parse("[2001:db8:3333:4444:5555:6666:7777:8888]:12ab") }
        assertEquals(InvalidPortNumber::class, e3::class)
        assertEquals("Invalid port number: 12ab", e3.message)
        assertEquals("12ab", (e3 as InvalidPortNumber).port)
    }
}
