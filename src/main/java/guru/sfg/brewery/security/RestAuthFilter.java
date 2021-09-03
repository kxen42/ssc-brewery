package guru.sfg.brewery.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
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
public abstract class RestAuthFilter extends AbstractAuthenticationProcessingFilter {

    public static final String API_KEY = "Api-Key";
    public static final String API_SECRET = "Api-Secret";

    protected RestAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (log.isDebugEnabled()) {
            log.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = attemptAuthentication(request, response);
            // Authentication success
            if (authResult != null) {
                successfulAuthentication(request, response, chain, authResult);
            } else {
                chain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            unsuccessfulAuthentication(request, response, e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // the default AuthenticationFailureHandler does this too
        SecurityContextHolder.clearContext();

        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: " + failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        // For REST just return 401, there is no failure page to redirect to
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        if (log.isDebugEnabled()) {
            log.debug("Authentication success. Updating SecurityContextHolder to contain: "
                + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
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

     public abstract String getPassword(HttpServletRequest request);

     public abstract String getUser(HttpServletRequest request);

}
