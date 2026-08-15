plugins {
    java
    application
    id("org.sonarqube")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src"))
        }
        resources {
            setSrcDirs(emptyList<String>())
        }
    }
    test {
        java {
            setSrcDirs(listOf("test"))
        }
        resources {
            setSrcDirs(emptyList<String>())
        }
    }
}

dependencies {
    implementation(fileTree("lib") { include("*.jar") })
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "app.Main"
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

val formatter: Configuration = configurations.create("formatter")

dependencies {
    formatter("com.google.googlejavaformat:google-java-format:1.24.0")
}

fun javaSources(): List<String> =
    listOf("src", "test")
        .filter { file(it).isDirectory }
        .flatMap { fileTree(it) { include("**/*.java") }.files }
        .map { it.absolutePath }

val formatterJvmArgs =
    listOf(
        "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    )

tasks.register<JavaExec>("formatCheck") {
    group = "verification"
    description = "Fails if any source file is not formatted."
    classpath = formatter
    mainClass = "com.google.googlejavaformat.java.Main"
    jvmArgs = formatterJvmArgs
    args = listOf("--aosp", "--dry-run", "--set-exit-if-changed") + javaSources()
}

tasks.register<JavaExec>("format") {
    group = "formatting"
    description = "Reformats all Java sources in place."
    classpath = formatter
    mainClass = "com.google.googlejavaformat.java.Main"
    jvmArgs = formatterJvmArgs
    args = listOf("--aosp", "--replace") + javaSources()
}

sonar {
    properties {
        property("sonar.projectKey", "bp")
        property("sonar.projectName", "bp")
        property("sonar.sources", "src")
        property("sonar.tests", "test")
        property("sonar.java.source", "21")
        property("sonar.java.binaries", "build/classes/java/main")
        property("sonar.java.libraries", "lib/*.jar")
        property("sonar.sourceEncoding", "UTF-8")
        property(
            "sonar.host.url",
            providers.gradleProperty("sonar.host.url").getOrElse("http://localhost:9000"),
        )
    }
}
