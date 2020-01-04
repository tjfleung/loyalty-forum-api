package com.terrence.loyalty.forumapi.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@Order
public class ForumPerformanceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.trace("Forum performance filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Entering performance filter");

        Instant startTime = Instant.now();

        try {
            chain.doFilter(request, response);
        } finally {
            Instant endTime = Instant.now();
            long durationInMs = Duration.between(startTime, endTime).toMillis();

            log.info("Duration: {}ms", durationInMs);

        }

        log.trace("Exiting performance filter");
    }
}
