# Xactus

## About

Xactus is [FINGO's](https://github.com/fingo) open-source XPath 2.0 processing library based on the [Eclipse's XPath 2.0 implementation](https://github.com/eclipse/webtools.sourceediting/tree/master/xpath/bundles/org.eclipse.wst.xml.xpath2) from the [Eclipse Web Tools Platform Project's](https://www.eclipse.org/webtools/).

## Contributing

Pull requests are welcome. We will respond as much as possible. For major changes please open an issue first to discuss what you would like to do.

To protect the rights of others, we use the [Developer Certificate of Origin](https://developercertificate.org/). If you want to develop the project with us, please confirm authorship by [signing-off your commits](https://git-scm.com/docs/git-commit#Documentation/git-commit.txt---signoff). The procedure is verified using [Probot: DCO](https://probot.github.io/apps/dco/).

## License

The library is published under the Eclipse Public License v2.0 license, a copy you will find in the [license file](/LICENSE). If you received this library from another party, conditions other than those stated here might apply. Please check it with your Redistributor.

## Development

### Building

* `./gradlew jar` - builds the library.
* `./gradlew sourcesJar` - builds sources JAR.
* `./gradlew javadoc` - builds Javadocs.
* `./gradlew javadocJar` - builds Javadoc JAR.

### Running tests

* `./gradlew test` - run all tests excluding JUnit 3 test suites.
* `./gradlew testSuite` - runs JUnit 3 test suites only.

### Releasing

Xactus is using [Axion Release Plugin](https://github.com/allegro/axion-release-plugin) for the release process support. Please refer to [Axion's documentation](https://axion-release-plugin.readthedocs.io/en/latest/).

### Publishing

#### General

There are a few publishing targets:
* local folder ('build/repos/releases' and 'build/repos/snapshots'):

      ./gradlew publishMavenJavaPublicationToLocalRepository

* M2 local cache:

      ./gradlew publishToMavenLocal
  
* Maven Central (requires OSSRH credentials to be set - [see below](#publishing-to-maven-central)):

      ./gradlew publishMavenJavaPublicationToOSSRHRepository

#### PGP configuration

Publishing tasks require some [project properties](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties) to be set:
* `pgpSecretKey` - ascii-armored PGP secret key to sign the artifact.
* `pgpSecretKeyPassword` - the password for the PGP secret key.
* `pgpKeyId` (optional) - the id of the OpenPGP sub-key.

Specifying `pgpSecretKey` and `pgpSecretKeyPassword` causes the signing mechanism to use [in-memory ascii armored keys](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:in-memory-keys) or [in-memory ascii-armored OpenPGP subkeys](https://docs.gradle.org/current/userguide/signing_plugin.html#using_in_memory_ascii_armored_openpgp_subkeys) (when `signing.pgp.keyId` is set as well).

When neither of the `pgpSecretKey` and `pgpSecretKeyPassword` parameters are supplied then [the default set of settings](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials) is taken into account.

#### Publishing to Maven Central

Command to publish Xactus to Maven Central:

      ./gradlew publishMavenJavaPublicationToOSSRHRepository

To publish an artifact to Maven Central [OSSRH](https://central.sonatype.org/publish/publish-guide/) credentials need to be supplied in addition to the [PGP configuration](#pgp-configuration):
* `ossrhUserName` - OSSRH username to be used to deploy the artifact.
* `ossrhPassword` - password for the OSSRH user.

Links:
* [Xactus releases in Maven Central Repository](https://repo.maven.apache.org/maven2/info/fingo/xactus/xactus/)
* [Xactus snapshots in OSS Sonatype Snapshot Repository](https://oss.sonatype.org/content/repositories/snapshots/info/fingo/xactus/xactus/).
* [Xactus in Maven Central Repository Search](https://search.maven.org/artifact/info.fingo.xactus/xactus)
* [Xactus on MVNRepository](https://mvnrepository.com/artifact/info.fingo.xactus/xactus)