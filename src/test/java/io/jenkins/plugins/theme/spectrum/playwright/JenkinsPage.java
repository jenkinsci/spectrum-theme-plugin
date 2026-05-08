package io.jenkins.plugins.theme.spectrum.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class JenkinsPage<T extends JenkinsPage<T>> {

    private static final Logger log = LoggerFactory.getLogger(JenkinsPage.class);
    protected final String pageUrl;
    protected final Page page;

    protected JenkinsPage(Page page, String pageUrl) {
        this.page = page;
        this.pageUrl = pageUrl;
    }

    @SuppressWarnings("unchecked")
    T waitForLoaded() {
        isAtUrl(pageUrl);
        return (T) this;
    }

    public T goTo() {
        log.info("Navigating to {}", pageUrl);
        page.navigate(pageUrl);
        return waitForLoaded();
    }

    void isAtUrl(String url) {
        log.info("Waiting for url to be {}", url);
        try {
            page.waitForURL(url);
        } catch (TimeoutError e) {
            throw new TimeoutError(
                    "Timeout exceeding waiting for the url to be " + url + " but it was " + page.url(), e);
        }
    }

    protected void checkComputedStyle(String selector, String property, String expected) {
        try {
            page.waitForFunction("""
        ([selector, property, expected]) => {
          const el = document.querySelector(selector);
          if (!el) return false;
          return getComputedStyle(el).getPropertyValue(property).trim() === expected;
        }""", List.of(selector, property, expected));
        } catch (TimeoutError e) {
            String value = computedStyle(selector, property);
            throw new AssertionError(
                    "Could not verify that computed style '%s' for '%s' was equal to '%s', found '%s'"
                            .formatted(property, selector, expected, value),
                    e);
        }
    }

    protected String computedStyle(String selector, String property) {
        return (String) page.evaluate("""
            ([selector, property]) => {
              const el = document.querySelector(selector);
              if (!el) return null;
              return getComputedStyle(el).getPropertyValue(property).trim();
            }""", List.of(selector, property));
    }

    protected String resolveColor(String color) {
        return (String) page.evaluate("""
            (color) => {
              const el = document.createElement('div');
              el.style.color = color;
              el.style.display = 'none';
              document.body.appendChild(el);
              const resolved = getComputedStyle(el).color;
              el.remove();
              return resolved;
            }""", color);
    }
}
