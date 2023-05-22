# xqt-kotlinx-json-rpc
[![Maven Central](https://img.shields.io/maven-central/v/io.github.rhdunn/xqt-kotlinx-json-rpc)](https://central.sonatype.com/artifact/io.github.rhdunn/xqt-kotlinx-json-rpc)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
> Kotlin multiplatform JSON-RPC 2.0 library

The `xqt-kotlinx-json-rpc` library is an open-source implementation of the
JSON-RPC protocol. It supports:
1. [JSON-RPC 2.0](https://www.jsonrpc.org/specification)

-----

1. [Adding the Library as a Maven Dependency](#adding-the-library-as-a-maven-dependency)
2. [Supported Kotlin/Native Targets](#supported-kotlinnative-targets)
3. [Documentation](#documentation)
    1. [Building the Project with Gradle](#building-the-project-with-gradle)
4. [License](#license)

## Adding the Library as a Maven Dependency
The `xqt-kotlinx-json-rpc` binaries are available on
[Maven Central](https://central.sonatype.com/artifact/io.github.rhdunn/xqt-kotlinx-json-rpc).

Maven:

    <dependency>
        <groupId>io.github.rhdunn</groupId>
        <artifactId>xqt-kotlinx-json-rpc</artifactId>
        <version>1.0.2</version>
    </dependency>

Gradle (Groovy DSL):

    implementation 'io.github.rhdunn:xqt-kotlinx-json-rpc:1.0.2'

Gradle (Kotlin DSL):

    implementation("io.github.rhdunn:xqt-kotlinx-json-rpc:1.0.2")

## Supported Kotlin/Native Targets
| Target [1]                | Family       | Tier [1]       | Status          |
|---------------------------|--------------|----------------|-----------------|
| `android_arm32`           | Android      | 3              | Build Only [2]  |
| `android_arm64`           | Android      | 3              | Build Only [2]  |
| `android_x64`             | Android      | 3              | Build Only [2]  |
| `android_x86`             | Android      | 3              | Build Only [2]  |
| `ios_arm64`               | Mac iOS      | 2              | Build Only [2]  |
| `ios_simulator_arm64`     | Mac iOS      | 1              | Build and Test  |
| `ios_x64`                 | Mac iOS      | 1              | Build and Test  |
| `linux_arm64`             | Linux        | 2              | Build Only [2]  |
| `linux_x64`               | Linux        | 1 (Host)       | Build and Test  |
| `macos_arm64`             | Mac OSX      | 1 (Host)       | Build and Test  |
| `macos_x64`               | Mac OSX      | 1 (Host)       | Build and Test  |
| `mingw_x64`               | MinGW        | 3 (Host)       | Build and Test  |
| `tvos_arm64`              | Mac TV OS    | 2              | Build Only [2]  |
| `tvos_simulator_arm64`    | Mac TV OS    | 2              | Build and Test  |
| `tvos_x64`                | Mac TV OS    | 2              | Build and Test  |
| `watchos_arm32`           | Mac Watch OS | 2              | Build Only [2]  |
| `watchos_arm64`           | Mac Watch OS | 2              | Build Only [2]  |
| `watchos_simulator_arm64` | Mac Watch OS | 2              | Build and Test  |
| `watchos_device_arm64`    | Mac Watch OS | 2              | Build and Test  |
| `watchos_x64`             | Mac Watch OS | 2              | Build and Test  |

[1] See https://kotlinlang.org/docs/native-target-support.html for the list of
Kotlin/Native targets. The `target` column specifies the name used in the
`KonanTarget` instances. The `tier` column is the level of support provided by
JetBrains for the Kotlin/Native target.

[2] The tests for these targets are not supported by Kotlin/Native. A gradle
`nativeTest` task is not available for this configuration.

## Documentation
1. [API Documentation](https://rhdunn.github.io/xqt-kotlinx-json-rpc/)

### Building the Project with Gradle
1. [Build Properties](docs/build/Build%20Properties.md)
2. [Environment Variables](docs/build/Envvironment%20Variables.md)
3. [Signing Artifacts](docs/build/Signing%20Artifacts.md)

## License
Copyright (C) 2022-2023 Reece H. Dunn

`SPDX-License-Identifier:` [Apache-2.0](LICENSE)
