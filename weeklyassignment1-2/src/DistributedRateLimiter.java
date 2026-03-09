import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    long lastRefillTime;
    int refillRate;

    TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    void refill() {

        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        int tokensToAdd = (int)(elapsed / 3600000.0 * refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    int remainingTokens() {
        return tokens;
    }
}

public class DistributedRateLimiter {

    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int maxRequests = 1000;
    private int refillRate = 1000;

    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(maxRequests, refillRate));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.remainingTokens() + " requests remaining)";
        } else {
            return "Denied (0 requests remaining)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "Client not found";
        }

        int used = maxRequests - bucket.remainingTokens();

        return "{used: " + used + ", limit: " + maxRequests + "}";
    }

    public static void main(String[] args) {

        DistributedRateLimiter limiter = new DistributedRateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}