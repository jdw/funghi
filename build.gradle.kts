plugins {
	id("org.jetbrains.kotlin.multiplatform")
	id("maven-publish")
}

val javaVersion: String by properties
val kotlinVersion: String by properties

repositories {
	mavenLocal()
	mavenCentral()
	google()
	gradlePluginPortal()
}

kotlin {
	jvm()

	sourceSets {
		val commonMain by getting
		val commonTest by getting {
			dependencies {
				implementation("junit:junit:4.13.1")
				implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
				implementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
				//implementation("org.jetbrains.kotlinx:kotlinx-coroutines:0.19.2")
			}
		}
	}
}

tasks.withType<Test> {
	testLogging {
		showStandardStreams = true
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(javaVersion))
	}

	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)
}