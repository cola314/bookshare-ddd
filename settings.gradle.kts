pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "bookshare-ddd"

include(
    "member",
    "review",
    "support"
)
