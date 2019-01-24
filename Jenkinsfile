#!/usr/bin/env groovy
coreJdk11Version="2.161"

/* `buildPlugin` step provided by: https://github.com/jenkins-infra/pipeline-library */
buildPlugin(configurations: [
  [ platform: "linux", jdk: "8", jenkins: null ],
  [ platform: "windows", jdk: "8", jenkins: null ],
  [ platform: "linux", jdk: "8", jenkins: coreJdk11Version, javaLevel: "8" ],
  [ platform: "windows", jdk: "8", jenkins: coreJdk11Version, javaLevel: "8" ],
  [ platform: "linux", jdk: "11", jenkins: coreJdk11Version, javaLevel: "8" ],
  [ platform: "windows", jdk: "11", jenkins: coreJdk11Version, javaLevel: "8" ]
])
