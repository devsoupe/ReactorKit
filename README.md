# ReactorKit

[![](https://img.shields.io/badge/Kotlin-1.3.21-orange.svg)](https://kotlinlang.org/)
[![](https://img.shields.io/badge/gradle-3.5.0--alpha04-blue.svg)](https://gradle.org/)
[![](https://img.shields.io/badge/platform-android-lightgrey.svg)](https://developer.android.com/)
[![](https://jitpack.io/v/perelandrax/reactorkit.svg)](https://jitpack.io/#perelandrax/reactorkit) [![](https://travis-ci.org/perelandrax/ReactorKit.svg?branch=master)](https://travis-ci.org/perelandrax/ReactorKit)

<!-- ![License MIT](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square) -->

A framework for a reactive and unidirectional android application architecture (Port of [ReactorKit](https://github.com/ReactorKit/ReactorKit) to Kotlin, which corresponds to [ReactorKit/1.2.1](https://github.com/ReactorKit/ReactorKit/releases/tag/1.2.1))

## Installation

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

```groovy
dependencies {
  implementation 'com.github.perelandrax:reactorkit:${version}'
}
```


