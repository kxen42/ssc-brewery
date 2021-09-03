package guru.sfg.brewery.security;

import javax.servlet.http.HttpServletRequest;

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
public class RestHeaderAuthFilter extends RestAuthFilter {

    public RestHeaderAuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public String getPassword(HttpServletRequest request) {
        return request.getHeader(RestAuthFilter.API_SECRET);
    }

    @Override
    public String getUser(HttpServletRequest request) {
        return request.getHeader(RestAuthFilter.API_KEY);
    }

}
