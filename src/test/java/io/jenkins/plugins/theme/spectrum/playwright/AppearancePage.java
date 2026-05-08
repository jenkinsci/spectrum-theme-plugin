package io.jenkins.plugins.theme.spectrum.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppearancePage extends JenkinsPage<AppearancePage> {

    private static final Logger log = LoggerFactory.getLogger(AppearancePage.class);

    public AppearancePage(Page page, String rootUrl) {
        super(page, rootUrl + "/manage/appearance/");
    }

    public AppearancePage themeIsPresent(Theme theme) {
        log.info("Checking if theme '{}' is present", theme);
        assertThat(getThemeCard(theme)).isVisible();
        return this;
    }

    public AppearancePage selectTheme(Theme theme) {
        log.info("Selecting theme '{}'", theme.name());
        getThemeCard(theme).click();
        return this;
    }

    public AppearancePage themeIsApplied(Theme theme) {
        log.info("Checking if theme '{}' is applied", theme);
        assertThat(getThemeCard(theme).getByRole(AriaRole.RADIO)).isChecked();
        assertThat(page.locator("html")).hasAttribute("data-theme", theme.id());
        checkAppearance("body", theme.variableToCheck());
        return this;
    }

    public AppearancePage injectCurrentPageFixtures() {
        log.info("Injecting breadcrumb and pagination fixtures");
        page.evaluate("""
            () => {
              document.querySelector('#accent-scope-fixtures')?.remove();
              document.body.insertAdjacentHTML('beforeend', `
                <section id="accent-scope-fixtures" style="margin: 24px; display: grid; gap: 12px;">
                  <nav class="jenkins-breadcrumbs" aria-label="Breadcrumb">
                    <ol class="jenkins-breadcrumbs__list" style="color: rgb(11, 22, 33);">
                      <li class="jenkins-breadcrumbs__list-item">
                        <span id="test-breadcrumb-current" aria-current="page">Current breadcrumb</span>
                      </li>
                    </ol>
                  </nav>
                  <nav aria-label="Pagination" style="color: rgb(44, 55, 66); font-weight: 400;">
                    <span id="test-pagination-current" aria-current="page">2</span>
                  </nav>
                </section>
              `);
            }""");
        return this;
    }

    public AppearancePage breadcrumbCurrentPageUsesAccent() {
        log.info("Checking that breadcrumb current-page styling uses the theme accent");
        checkComputedStyle("#test-breadcrumb-current", "color", resolveColor("var(--instance-accent)"));
        checkComputedStyle("#test-breadcrumb-current", "font-weight", "600");
        return this;
    }

    public AppearancePage paginationCurrentPageKeepsLocalStyle() {
        log.info("Checking that non-breadcrumb current-page styling keeps local styles");
        checkComputedStyle("#test-pagination-current", "color", "rgb(44, 55, 66)");
        checkComputedStyle("#test-pagination-current", "font-weight", "400");
        return this;
    }

    private Locator getThemeCard(Theme theme) {
        log.debug("Locating theme box for '{}'", theme);
        return page.getByRole(AriaRole.RADIO, new GetByRoleOptions().setName(theme.name()))
                .locator("..");
    }

    /**
     * Checks the appearance of an element based on a CSS variable.
     *
     * @param selector the CSS selector of the element to check
     * @param variable the CSS variable to check against
     */
    private void checkAppearance(String selector, Theme.CssVariable variable) {
        log.debug("Checking appearance for selector '{}' with CSS variable '{}'", selector, variable);
        try {
            page.waitForFunction("""
        ([selector, cssVar, expected]) => {
          const el = document.querySelector(selector);
          if (!el) return false;
          return getComputedStyle(el).getPropertyValue(cssVar).trim() === expected;
        }""", List.of(selector, variable.name(), variable.expected()));
        } catch (TimeoutError e) {
            Object value = page.evaluate("""
                ([selector, cssVar]) => {
                  const el = document.querySelector(selector);
                  if (!el) return null;
                  return getComputedStyle(el).getPropertyValue(cssVar).trim();
                }""", List.of(selector, variable.name()));
            throw new AssertionError(
                    "Could not verify that '%s' was equal to '%s', found '%s'"
                            .formatted(variable.name(), variable.expected(), value),
                    e);
        }
    }
}
