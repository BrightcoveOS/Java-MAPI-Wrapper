About
=====

**WARNING:** The Media API is deprecated and should not be used for new projects. [More information](https://support.brightcove.com/en/service-changes).

This project provides a starting point for integrating the Brightcove Media API into your application. It provides simple ways to interact with the API.

This project relies on the [Brightcove Commons open source libraries](https://github.com/BrightcoveOS/Java-Commons).

Documentation
=============

[JavaDocs](http://brightcoveos.github.com/Java-MAPI-Wrapper/javadoc/)

Downloads
=========

**Version 4.1.11**:

- Allowing creation of a video with renditions only (mainly for remote asset titles)

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.11 with dependencies](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-4.1.11.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.11 with minimal dependencies](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-minimal-4.1.11.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.11](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-4.1.11.jar)


**Version 4.1.10**:

- Moving downloads to Amazon S3.
- Including 4.1.10 commons lib with fixes for Playlists, new log redacting, etc.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.10 with dependencies](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-4.1.10.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.10 with minimal dependencies](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-minimal-4.1.10.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.10](http://bcos-release-files.s3.amazonaws.com/java-mapi-wrapper/bc-java-mapi-wrapper-4.1.10.jar)


**Version 4.1.9**:

Including 4.1.9 commons lib with support for LIVE_STREAMING controller type.
Fix to make optional URL parameters (page size, page number, sort by, sort order) actually optional - passing in null will remove the parameter from the URL request.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.9 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.9.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.9 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.9.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.9](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.9.jar)


**Version 4.1.8**:

Adding iOS Rendition objects.  Deprecating enableUDS methods - replacing with get/setDeliveryMethod().  Delivery method can be set to "http" (PD downloads if enabled at the account level) or "http_ios" (for iOS m3u8 delivery).

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.8 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.8.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.8 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.8.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.8](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.8.jar)


**Version 4.1.7**:

Includes fixes from Java-Commons 4.1.7 (primarily just a few new sort by type enum values).

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.7 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.7.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.7 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.7.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.7](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.7.jar)


**Version 4.1.6**:

Includes fixes from Java-Commons 4.1.6.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.6 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.6.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.6 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.6.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.6](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.6.jar)


**Version 4.1.5**:

Includes fixes from Java-Commons 4.1.5.
Fixing bug when obtaining null playlist.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.5 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.5.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.5 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.5.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.5](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.5.jar)


**Version 4.1.3**:

Fixes bug in search_videos - the "none" parameter is multi-list rather than multi-value.
i.e. none=foo&none=bar rather than none=foo&bar

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.3 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.3.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.3 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.3.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.3](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.3.jar)

**Version 4.1.2**:

Includes commons library v4.1.2 - primarily just bug fixes to the commons
applications library.  Shouldn't change behavior of the MAPI wrapper much.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.2 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.2.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.2 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.2.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.2](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.2.jar)


**Version 4.1.1**:

This version an ExceptionHandler to the ReadApi (similar to the handler added
to the WriteApi in 4.1.0).  It also includes the minor revision 4.1.1 of the
Java Commons library.

The ExceptionHandler object basically allows a user's custom code determine
whether or not a command should be retried if an exception is thrown.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.1 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.1.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.1 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.1.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.1](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.1.jar)


**Version 4.1.0**:

This version adds adds two major new features:

- Use the new 4.1.0 commons library to allow video serialization to/from XML
- Allow the WriteApi to specify an "ExceptionHandler" object (ReadApi support coming soon)

The ExceptionHandler object basically allows a user's custom code determine
whether or not a command should be retried if an exception is thrown.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.0 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.0.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.1.0 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.1.0.zip)

Library Only:

- [BC Java MAPI Wrapper v4.1.0](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.1.0.jar)


**Version 4.0.3**:

This version uses the new major release of the Brightcove Commons libraries.
This allows both the read and write api to specify an HttpClientFactory, which
can dictate how HttpClients are created each time a method is called.

This is especially necessary if a user wishes to use https instead of http to
communicate with the Media API.

Full Download - including all Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.0.3 with dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.0.3.zip)

Minimal Download - including only necessary Brightcove Commons libraries:

- [BC Java MAPI Wrapper v4.0.3 with minimal dependencies](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-minimal-4.0.3.zip)

Library Only:

- [BC Java MAPI Wrapper v4.0.3](https://github.com/downloads/BrightcoveOS/Java-MAPI-Wrapper/bc-java-mapi-wrapper-4.0.3.jar)

Latest Source
=============

You can check out the latest source code at the
[GitHub](http://github.com/brightcoveos/Java-MAPI-Wrapper) page; please
note that there is no guarantee of code usability or stability.
