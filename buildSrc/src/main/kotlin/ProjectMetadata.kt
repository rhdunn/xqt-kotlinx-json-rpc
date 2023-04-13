@Suppress("MemberVisibilityCanBePrivate")
object ProjectMetadata {
    /**
     * The ID of the GitHub account to which this project belongs.
     */
    const val githubId = "rhdunn"

    /**
     * The semantic version of the current version.
     */
    const val versionTag = "0.1"

    /**
     * The build type of this project.
     */
    val buildType = BuildType.Snapshot

    /**
     * The Maven group ID.
     */
    const val groupId = "io.github.$githubId"

    /**
     * The artifact version ID.
     */
    val version = "$versionTag${buildType.suffix}"
}
