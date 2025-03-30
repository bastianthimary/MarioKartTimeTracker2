// Root build.gradle.kts

buildscript {
    val kotlinVersion = "2.1.20" // Variable innerhalb von buildscript definieren
    val objectboxVersion by extra("4.2.0")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("io.objectbox:objectbox-gradle-plugin:4.2.0") // ObjectBox-Plugin
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
