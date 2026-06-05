package com.quietterminal.qtirelay;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import com.quietterminal.neon.core.DtlsConfig;
import com.quietterminal.neon.core.NeonConfig;
import com.quietterminal.neon.relay.NeonRelay;

public class QTIRelay {

    public static void main(String[] args) {

        try {
            char[] keystorePass = System.getenv("NEON_KEYSTORE_PASS").toCharArray();

            KeyStore ks = KeyStore.getInstance("PKCS12");

            try (var is = new FileInputStream("relay.p12")) {
                ks.load(is, keystorePass);
            }

            SSLContext ctx = DtlsConfig.fromKeyStore(ks, keystorePass);

            NeonConfig cfg = NeonConfig.builder()
                    .bufferPoolInitSize(256)
                    .bufferPoolMaxSize(4096)
                    .bufferSize(1400)
                    .clientConnectionTimeoutMs(5000)
                    .clientDisconnectNoticeDelayMs(500)
                    .clientInitialReconnectDelayMs(1000)
                    .clientMaxReconnectAttempts(5)
                    .clientMaxReconnectDelayMs(30000)
                    .clientPingIntervalMs(5000)
                    .clientProcessingLoopSleepMs(1)
                    .clientSocketTimeoutMs(10000)
                    .enforceBufferSize(true)
                    .hostAckTimeoutMs(5000)
                    .hostProcessingLoopSleepMs(1)
                    .hostSessionMaxPacketSize((short) 1400)
                    .hostSessionTickRate((short) 64)
                    .hostSessionTokenTimeoutMs(300000)
                    .hostSocketTimeoutMs(10000)
                    .maxClientsPerSession(64)
                    .maxPacketsPerSecond(100)
                    .maxPendingConnections(128)
                    .maxRateLimiters(1024)
                    .maxTotalConnections(512)
                    .relayCleanupIntervalMs(30000)
                    .relayClientTimeoutMs(15000)
                    .relayMainLoopSleepMs(1)
                    .relayPort(7777)
                    .relaySocketTimeoutMs(10000)
                    .reliablePacketMaxRetries(5)
                    .reliablePacketTimeoutMs(2000)
                    .sslContext(ctx)
                    .build();

            try (NeonRelay relay = new NeonRelay("0.0.0.0", cfg)) {
                Thread t = Thread.ofVirtual().start(() -> {
                    try {
                        relay.startAndRun();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                });
                t.join();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}