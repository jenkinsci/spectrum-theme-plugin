package io.jenkins.plugins.theme.spectrum.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
public class SpectrumThemeTest {

    @Test
    void themeLoads(JenkinsRule j, Page p) {
        AppearancePage appearancePage = new AppearancePage(p, j.jenkins.getRootUrl()).goTo();
        for (Theme theme : Theme.ALL) {
            appearancePage.themeIsPresent(theme).selectTheme(theme).themeIsApplied(theme);
        }
    }

    @Test
    void currentPageAccentIsScopedToBreadcrumbs(JenkinsRule j, Page p) {
        new AppearancePage(p, j.jenkins.getRootUrl())
                .goTo()
                .selectTheme(Theme.SPECTRUM_BLUE)
                .themeIsApplied(Theme.SPECTRUM_BLUE)
                .injectCurrentPageFixtures()
                .breadcrumbCurrentPageUsesAccent()
                .paginationCurrentPageKeepsLocalStyle()
                .pressedButtonCountKeepsButtonContrast()
                .pressedLinkCountKeepsButtonContrast()
                .hoveredPrimaryLinkKeepsButtonContrast();
    }
}
