package gunjoon98.scoreboard;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FilterTest implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("{}", httpRequest.getDispatcherType());
        if(httpRequest.getDispatcherType().equals(DispatcherType.ERROR)) {
            log.info("javax.servlet.error.status_code 값 {}", httpRequest.getAttribute("javax.servlet.error.status_code"));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
