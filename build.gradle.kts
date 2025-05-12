plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    kotlin("android") version "1.9.22" apply false
}

group = "com.example"
version = "1.0-SNAPSHOT"

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}