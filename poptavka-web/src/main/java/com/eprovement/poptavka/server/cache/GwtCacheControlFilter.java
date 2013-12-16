package com.eprovement.poptavka.server.cache;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter to disable caching of generated GWT files to ensure that the correct files get cached.
 *
 * Copied from <a href="https://github.com/realityforge/gwt-cache-filter">gwt-cache-filter</a>.
 *
 * <p>
 *     This filter caches all resources matching the pattern "*.cache.*"
 *     and explicitly disallows caching for resources matching the pattern "*.nocache.*".
 * </p>
 */
public class GwtCacheControlFilter implements Filter {
    public static final int YEAR_IN_SECONDS = 365 * 24 * 60 * 60;
    public static final int FOUR_WEEKS_IN_SECONDS = 4 * 7 * 24 * 60 * 60;

    @Override
    public void init(final FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
        throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String requestURI = httpRequest.getRequestURI();

        if (requestURI.contains(".nocache.")) {
            final Date now = new Date();
            // set create date to current timestamp
            httpResponse.setDateHeader("Date", now.getTime());
            // set modify date to current timestamp
            httpResponse.setDateHeader("Last-Modified", now.getTime());
            // set expiry to back in the past (makes us a bad candidate for caching)
            httpResponse.setDateHeader("Expires", 0);
            // HTTP 1.0 (disable caching)
            httpResponse.setHeader("Pragma", "no-cache");
            // HTTP 1.1 (disable caching of any kind)
            // HTTP 1.1 'pre-check=0, post-check=0' => (Internet Explorer should always check)
            httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate, pre-check=0, post-check=0");
        } else if (requestURI.contains(".cache.")) {
            setCache(httpResponse, YEAR_IN_SECONDS);
        } else if (requestURI.endsWith(".jpg") || requestURI.endsWith(".png") || requestURI.endsWith(".gif")
                    || requestURI.endsWith(".css")) {
            setCache(httpResponse, FOUR_WEEKS_IN_SECONDS);
        }

        filterChain.doFilter(request, response);
    }

    private void setCache(HttpServletResponse httpResponse, int cacheExpiryInSeconds) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, cacheExpiryInSeconds);
        httpResponse.setDateHeader("Expires", calendar.getTime().getTime());
        httpResponse.setHeader("Cache-control", "max-age=" + cacheExpiryInSeconds + ", public");
        httpResponse.setHeader("Pragma", "");
    }

    @Override
    public void destroy() {
    }
}
