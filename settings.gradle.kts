pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "bookshare-ddd"

include(
    "common",
    "member",
    "review",
    "support"
)
