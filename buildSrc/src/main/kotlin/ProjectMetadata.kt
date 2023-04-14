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
     * The copyright year range for the project.
     */
    const val copyrightYear = "2022-2023"

    /**
     * The person or organization owning the copyright for the project.
     */
    const val copyrightOwner = "Reece H. Dunn"

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
