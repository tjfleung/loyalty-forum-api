package com.terrence.loyalty.forumapi.filter;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ForumPerformanceFilterTest {

    @InjectMocks
    private ForumPerformanceFilter filter;

    @Test
    public void testForumPerformanceFilter() {
        ServletRequest request = new MockHttpServletRequest();
        ServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        Assertions.assertDoesNotThrow(() -> filter.doFilter(request, response, filterChain));
    }
}
