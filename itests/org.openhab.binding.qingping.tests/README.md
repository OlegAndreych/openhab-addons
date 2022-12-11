# Integration tests config

In order to run integration tests that interact with Qingping API, one has to provide credentials for their account.

Credentials have to be specified in
the `itests/org.openhab.binding.qingping.tests/src/main/resources/env/qingping-client-test.properties`.

Although this property file is excluded from git via `.gitignore`, one should be careful to not put their credentials to
the code repository.

The properties one has to provide are:

* `app.key` - application key from one's qingping account;
* `app.secret` - application secret from one's qingping account;