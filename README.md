# xqt-kotlinx-json-rpc
> Kotlin multiplatform JSON-RPC 2.0 library

The `xqt-kotlinx-json-rpc` library is an open-source implementation of the
JSON-RPC protocol. It supports:
1. [JSON-RPC 2.0](https://www.jsonrpc.org/specification)

## Maven Central
The `xqt-kotlinx-json-rpc` binaries are available on Maven Central:

1. Gradle (Groovy DSL)
   ```
   implementation 'io.github.rhdunn:xqt-kotlinx-json-rpc:1.0.0'
   ```

2. Gradle (Kotlin DSL)
   ```
   implementation("io.github.rhdunn:xqt-kotlinx-json-rpc:1.0.0")
   ```

## Supported Kotlin/Native Targets
| Target [1]                | Family       | Tier [1]       | Status |
|---------------------------|--------------|----------------|--------|
| `android_arm32`           | Android      | 3              |        |
| `android_arm64`           | Android      | 3              |        |
| `android_x64`             | Android      | 3              |        |
| `android_x86`             | Android      | 3              |        |
| `ios_arm32`               | Mac iOS      | Deprecated [2] |        |
| `ios_arm64`               | Mac iOS      | 2              |        |
| `ios_simulator_arm64`     | Mac iOS      | 1              |        |
| `ios_x64`                 | Mac iOS      | 1              |        |
| `linux_arm32_hfp`         | Linux        | Deprecated [2] |        |
| `linux_arm64`             | Linux        | 2              |        |
| `linux_mips32`            | Linux        | Deprecated [2] |        |
| `linux_mipsel32`          | Linux        | Deprecated [2] |        |
| `linux_x64`               | Linux        | 1 (Host)       |        |
| `macos_arm64`             | Mac OSX      | 1 (Host)       |        |
| `macos_x64`               | Mac OSX      | 1 (Host)       |        |
| `mingw_x64`               | MinGW        | 3 (Host)       |        |
| `mingw_x86`               | MinGW        | Deprecated [2] |        |
| `tvos_arm64`              | Mac TV OS    | 2              |        |
| `tvos_simulator_arm64`    | Mac TV OS    | 2              |        |
| `tvos_x64`                | Mac TV OS    | 2              |        |
| `wasm32`                  | WASM         | Deprecated [2] |        |
| `watchos_arm32`           | Mac Watch OS | 2              |        |
| `watchos_arm64`           | Mac Watch OS | 2              |        |
| `watchos_simulator_arm64` | Mac Watch OS | 2              |        |
| `watchos_x64`             | Mac Watch OS | 2              |        |
| `watchos_x86`             | Mac Watch OS | Deprecated [2] |        |

[1] See https://kotlinlang.org/docs/native-target-support.html for the list of
Kotlin/Native targets. The `target` column specifies

[2] The deprecated targets are scheduled to be removed in Kotlin 1.9.20.

## Configuration Properties
The following configuration properties are available to configure the build:

### jvm.target
The `jvm.target` build property configures the version of the Java VM to target
by the Kotlin compiler. This is used by the GitHub Actions to build against the
LTS releases of JVM, 11 and 17.

### karma.browser
The `karma.browser` build property configures the name of the browser to use in
the Kotlin/JS tests when run on the browser.

### karma.browser.channel
The `karma.browser.channel` build property configures the name of the
development/release channel of the browser used to run the Kotlin/JS tests.

### karma.browser.headless
The `karma.browser.headless` build property determines whether the web browser
runs in headless mode, or with a visible graphical interface.

### konan.target
The `konan.target` build property configures the Kotlin/Native build target.
This is used by the GitHub Actions in the native build's matrix configuration.

### maven.sign
The `maven.sign` build property configures whether the `publish` actions should
sign the artifacts using the configured signing key. This is required when
publishing artifacts to Maven Central.

### nodejs.download
The `nodejs.download` build property configures whether the build should
download node when building and running the Kotlin/JS targets. If this is false
the build will use the system's node installation. This is used by the GitHub
Actions to prevent node being downloaded during the build.

### ossrh.username
The Open Source Software Repository Hosting (OSSRH) username to use when
publishing artifacts to Maven Central. This takes precedence over the
`OSSRH_USERNAME` environment variable.

### ossrh.password
The Open Source Software Repository Hosting (OSSRH) password to use when
publishing artifacts to Maven Central. This takes precedence over the
`OSSRH_PASSWORD` environment variable.

## Environment Variables
The following environment variables are available to configure the build:

### ossrh.username
The Open Source Software Repository Hosting (OSSRH) username to use when
publishing artifacts to Maven Central. This takes precedence over the
`OSSRH_USERNAME` environment variable.

### ossrh.password
The Open Source Software Repository Hosting (OSSRH) password to use when
publishing artifacts to Maven Central. This takes precedence over the
`OSSRH_PASSWORD` environment variable.

## License
Copyright (C) 2022-2023 Reece H. Dunn

SPDX-License-Identifier: [Apache-2.0](LICENSE)
