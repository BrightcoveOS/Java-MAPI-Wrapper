About
=====

This project provides a starting point for integrating the Brightcove Media API into your application. It provides simple ways to interact with the API.

This project relies on the [Brightcove Commons open source libraries](https://github.com/BrightcoveOS/Java-Commons).

Downloads
=========

**Version 3.0.5**:

This patch fixes a bug in the CuePoint object when serializing to JSON.

Full Download - including Brightcove Commons libraries:

- [BC Java MAPI Wrapper v3.0.5 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.5-with-dep.zip)

Library Only:

- [BC Java MAPI Wrapper v3.0.5](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.5.jar)

**Version 3.0.4**:

This patch fixes a bug when we open a new connection for each request.  Basically it makes sure to fully parse the response before closing the socket.

This also includes the 3.0.4 BC Commons libraries, which fix a few bugs in the BC Catalog Objects library.

Full Download - including Brightcove Commons libraries:

- [BC Java MAPI Wrapper v3.0.4 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.4-with-dep.zip)

Library Only:

- [BC Java MAPI Wrapper v3.0.4](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.4.jar)

**Version 3.0.2**:

This patch release allows UpdateVideo to send video fields via JSON even if the field value is null.

Library Only:

- [BC Java MAPI Wrapper v3.0.2](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.2.jar)

Full Download - including Brightcove Commons libraries:

- [BC Java MAPI Wrapper v3.0.2 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.2-with-dep.zip)

**Version 3.0.1**:

This patch release creates a new HttpClient object for every request.  Under very high load, this isn't as efficient, but it allows us to easily close socket connections properly which is more important.

Library Only:

- [BC Java MAPI Wrapper v3.0.1](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.1.jar)

Full Download - including Brightcove Commons libraries:

- [BC Java MAPI Wrapper v3.0.1 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.1-with-dep.zip)

**Version 3.0**:

Library Only:

- [BC Java MAPI Wrapper v3.0](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0.jar)

Full Download - including Brightcove Commons libraries:

- [BC Java MAPI Wrapper v3.0 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-3.0-with-dep.zip)

Latest Source
=============

You can check out the latest source code at the
[GitHub](http://github.com/brightcoveos/Java-MAPI-Wrapper) page; please
note that there is no guarantee of code usability or stability.