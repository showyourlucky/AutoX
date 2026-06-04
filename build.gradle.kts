import org.jetbrains.kotlin.gradle.dsl.copyFreeCompilerArgsToArgs

initVersions(file("project-versions.json"))

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version = "1.6.21"

    extra.apply {
        set("kotlin_version", kotlin_version)
        set("compose_version", compose_version)
    }

    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath(kotlin("gradle-plugin", version = "$kotlin_version"))
        classpath("com.jakewharton:butterknife-gradle-plugin:10.2.3")
        classpath("org.codehaus.groovy:groovy-json:3.0.8")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
        //Aliyun 镜像，部分国内库（keeplive/websocket2）仅在此发布
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
    }
    // 锁定动态版本范围，避免从所有仓库获取 maven-metadata.xml 时因 Aliyun 502 失败
    configurations.all {
        resolutionStrategy {
            force("com.twofortyfouram:android-annotation:2.0.1")
        }
    }
//    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java){
//        kotlinOptions{
//            freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
//                add("-P")
//                add("plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true")
//            }
//        }
//    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
