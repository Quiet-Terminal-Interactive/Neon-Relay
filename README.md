# QTI Neon Relay

The official Quiet Terminal Interactive Neon relay and currently the only officially endorsed public relay. This repo also serves as a reference configuration and starting point if you want to run your own.

## What Is This?

[Neon](https://github.com/Quiet-Terminal-interactive/QTINeon) is a minimal, game-agnostic, relay-based UDP multiplayer library for Java. Clients never connect to each other directly, all packets are routed through the relay by destination ID, which keeps NAT traversal simple and host addresses private. The host is just another participant with a special protocol role, not a special network position.

This repo runs the QTI public relay that anyone can connect to for testing or use in their own projects.

## Connecting to the Relay

To connect securely, trust the relay's public certificate using `DtlsConfig.withTrustStore`:

```java
KeyStore trustStore = KeyStore.getInstance("PKCS12");
try (var is = new FileInputStream("qti-neon-official-relay.cert")) {
    trustStore.load(is, null);
}

SSLContext ctx = DtlsConfig.withTrustStore(trustStore);

NeonConfig cfg = NeonConfig.builder()
    .sslContext(ctx)
    // ... your other config values
    .build();
```

The certificate is included in this repository at [`qti-neon-official-relay.cert`](./qti-neon-official-relay.cert). It pins trust to the QTI relay specifically — clients will only connect to a relay presenting this certificate.

> **Note:** `DtlsConfig.insecureTrustAll()` is available for local development and testing, but should never be used in production.

## Running Your Own Relay

The config values in this repo are a reasonable starting point. Copy and adjust to suit your needs. Tweak rate limits, session sizes, timeouts, and connection caps as appropriate for your use case.

Set the keystore password via the `NEON_KEYSTORE_PASS` environment variable before starting:

```bash
export NEON_KEYSTORE_PASS=your_keystore_password
```

## Reporting Issues

This relay runs on [Neon](https://github.com/Quiet-Terminal-interactive/QTINeon). If you encounter issues, please open them there rather than in this repo.