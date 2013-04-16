package com.eprovement.poptavka.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Servlet that makes this application crawlable.
 */
public final class CrawlServlet implements Filter {

    private static final int TIMEOUT = 3000;
    private static final int ESCAPED_LENGTH_LONGER = 20;
    private static final int ESCAPED_LENGTH = 19;

    private static final String FRAGMENT = "_escaped_fragment_";

    private static String rewriteQueryString(String queryString) {
        StringBuilder queryStringSb = new StringBuilder(queryString);

        int i = queryStringSb.indexOf("&" + FRAGMENT);
        if (i != -1) {
            StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
            tmpSb.append("#!");
            try {
                tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + ESCAPED_LENGTH_LONGER,
                        queryStringSb.length()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO LATER Auto-generated catch block
                e.printStackTrace();
            }
            queryStringSb = tmpSb;
        }

        i = queryStringSb.indexOf(FRAGMENT);
        if (i != -1) {
            StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
            tmpSb.append("#!");
            try {
                tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + ESCAPED_LENGTH,
                        queryStringSb.length()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
                e.printStackTrace();
            }
            queryStringSb = tmpSb;
        }
        if (queryStringSb.indexOf("#!") != 0) {
            queryStringSb.insert(0, '?');
        }
        queryString = queryStringSb.toString();

        return queryString;
    }

    private FilterConfig filterConfig = null;

      /**
       * Destroys the filter configuration.
       */
    public void destroy() {
        this.filterConfig = null;
    }

      /**
       * Filters all requests and invokes headless browser if necessary.
       */
    public void doFilter(ServletRequest request, ServletResponse response,
          FilterChain chain) throws IOException {
        if (filterConfig == null) {
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String queryString = req.getQueryString();

        if ((queryString != null) && (queryString.contains("_escaped_fragment_"))) {
            StringBuilder pageNameSb = new StringBuilder("http://");
            pageNameSb.append(req.getServerName());
            if (req.getServerPort() != 0) {
                pageNameSb.append(":");
                pageNameSb.append(req.getServerPort());
            }
            pageNameSb.append(req.getRequestURI());
            queryString = rewriteQueryString(queryString);
            pageNameSb.append(queryString);

            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
            webClient.setJavaScriptEnabled(true);
            String pageName = pageNameSb.toString();
            HtmlPage page = webClient.getPage(pageName);
            webClient.waitForBackgroundJavaScriptStartingBefore(TIMEOUT);

            res.setContentType("text/html;charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<hr>");
            out.println("<center><h3>You are viewing a non-interactive page that is intended for the crawler. "
                    + "You probably want to see this page: <a href=\""
                    + pageName + "\">" + pageName + "</a></h3></center>");
            out.println("<hr>");

            out.println(page.asXml());
            webClient.closeAllWindows();
            out.close();

        } else {
            try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

      /**
       * Initializes the filter configuration.
       */
    public void init(FilterConfig config) {
        this.filterConfig = config;
    }

}
