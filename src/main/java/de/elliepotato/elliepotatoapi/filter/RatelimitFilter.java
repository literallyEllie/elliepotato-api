package de.elliepotato.elliepotatoapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limit feature so that only so many requests are
 * processed from a single unauthenticated ip in a time period.
 */
@Component
public class RatelimitFilter extends OncePerRequestFilter {
    private static final int LIMITS_PER_IP = 100;
    private static final long RESET_PERIOD = 60_000L;

    // todo in redis
    private final Map<String, RequestProfile> rateLimits = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // ignore authenticated requests.
        if (SecurityContextHolder.getContext().getAuthentication() != null)
            return;

        String remoteAddr = request.getRemoteAddr();
        RequestProfile profile = rateLimits.computeIfAbsent(remoteAddr, s -> new RequestProfile());

        response.addHeader("X-RateLimit-Limit", String.valueOf(LIMITS_PER_IP));
        response.addHeader("X-RateLimit-Remaining", String.valueOf(profile.remainingRequests()));
        response.addHeader("X-RateLimit-UntilReset", String.valueOf(profile.timeUntilReset()));

        if (profile.allowRequest()) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(
                    HttpStatus.TOO_MANY_REQUESTS.value(), "rate-limited"
            );
        }
    }

    /**
     * Request profile of a single unauthenticated IP.
     * </p>
     * Keeps track of how many requests they have sent within a given time.
     */
    private static class RequestProfile {
        private long lastRequest;
        private int requests;

        /**
         * @return If a request should be processed.
         */
        public boolean allowRequest() {
            long now = System.currentTimeMillis();
            long timeSinceLastRequest = now - lastRequest;

            if (timeSinceLastRequest > RESET_PERIOD) {
                lastRequest = now;
                requests = 1;
                return true;
            }

            if (requests == LIMITS_PER_IP) {
                return false;
            }

            lastRequest = now;
            requests++;
            return true;
        }

        /**
         * @return How many requests are avaliable in a given time period.
         */
        public int remainingRequests() {
            return LIMITS_PER_IP - requests;
        }

        /**
         * @return Time until available requests are replenished.
         */
        public long timeUntilReset() {
            if (lastRequest == 0) {
                return RESET_PERIOD;
            }

            return RESET_PERIOD - (System.currentTimeMillis() - lastRequest);
        }

    }
}
