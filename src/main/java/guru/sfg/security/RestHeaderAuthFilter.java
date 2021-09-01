package guru.sfg.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * Demo custom authentication filter
 * <ul>
 *     <li>AbstractAuthenticationProcessingFilter</li>
 *     <li>Authentication</li>
 *     <li>UsernamePasswordAuthenticationToken</li>
 *     <li>StringUtils.defaultString</li>
 * </ul>
 */
@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {
    public static final String KEY_HDR = "Api-Key";
    public static final String SECRET_HDR = "Api-Secret";


    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        Authentication authResult = attemptAuthentication(request, response);

        // Authentication success
        if (authResult != null) {
            successfulAuthentication(request, response, chain, authResult);
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = StringUtils.defaultString(getUser(request));
        String password = StringUtils.defaultString(getPassword(request));

        log.debug("Authenticating user: {}", username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        if (StringUtils.isNotEmpty(username)) {
            return this.getAuthenticationManager().authenticate(token);
        } else {
            return null;
        }
    }

    private String getPassword(HttpServletRequest request) {
        return request.getHeader(SECRET_HDR);
    }

    private String getUser(HttpServletRequest request) {
        return request.getHeader(KEY_HDR);
    }
}
