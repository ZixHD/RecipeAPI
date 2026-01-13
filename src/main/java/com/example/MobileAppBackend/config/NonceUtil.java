package com.example.MobileAppBackend.config;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NonceUtil{

    private static Map<String, Deque<String>> noncesPerApiKey = new ConcurrentHashMap<>();
    private static int MAX_NONCES = 100;

    public static boolean checkNonce(String apiKey, String nonce) {
        noncesPerApiKey.putIfAbsent(apiKey, new LinkedList<>());
        Deque<String> deque = noncesPerApiKey.get(apiKey);

        synchronized (deque) {
            if (deque.contains(nonce)) return false; // replay
            deque.addLast(nonce);
            if (deque.size() > MAX_NONCES) deque.removeFirst();
        }
        return true;
    }
}
