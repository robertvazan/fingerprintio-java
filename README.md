# FingerprintIO for Java #

[![Maven Central](https://img.shields.io/maven-central/v/com.machinezoo.fingerprintio/fingerprintio)](https://search.maven.org/artifact/com.machinezoo.fingerprintio/fingerprintio)
[![Build Status](https://travis-ci.com/robertvazan/fingerprintio-java.svg?branch=master)](https://travis-ci.com/robertvazan/fingerprintio-java)
[![Coverage Status](https://codecov.io/gh/robertvazan/fingerprintio-java/branch/master/graph/badge.svg)](https://codecov.io/gh/robertvazan/fingerprintio-java)

FingerprintIO for Java is a library that decodes and encodes several [documented fingerprint template formats](https://templates.machinezoo.com/).
See [FingerprintIO homepage](https://fingerprintio.machinezoo.com/).

* Documentation: [Homepage](https://fingerprintio.machinezoo.com/), [Javadoc](https://fingerprintio.machinezoo.com/javadoc/overview-summary.html)
* Download: see the [homepage](https://fingerprintio.machinezoo.com/)
* Sources: [GitHub](https://github.com/robertvazan/fingerprintio-java), [Bitbucket](https://bitbucket.org/robertvazan/fingerprintio-java)
* Issues: [GitHub](https://github.com/robertvazan/fingerprintio-java/issues), [Bitbucket](https://bitbucket.org/robertvazan/fingerprintio-java/issues)
* License: [Apache License 2.0](LICENSE)

```java
byte[] serialized = Files.readAllBytes(Paths.get("ansi378-2004-example.dat"));
if (Ansi378v2004Template.accepts(serialized)) {
    System.out.println("This is an ANSI 378-2004 template.");
    var template = new Ansi378v2004Template(serialized);
    int fpcount = template.fingerprints.size();
    System.out.printf("Template contains %d fingerprint(s).\n", fpcount);
    var fingerprint = template.fingerprints.get(0);
    int mincount = fingerprint.minutiae.size();
    System.out.printf("First fingerprint has %d minutiae.\n", mincount);
}
```
