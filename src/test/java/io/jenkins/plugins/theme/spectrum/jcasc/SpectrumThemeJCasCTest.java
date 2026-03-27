package io.jenkins.plugins.theme.spectrum.jcasc;

import io.jenkins.plugins.casc.misc.junit.jupiter.WithJenkinsConfiguredWithCode;
import io.jenkins.plugins.theme.spectrum.SpectrumBlueTheme;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;

@WithJenkinsConfiguredWithCode
class SpectrumThemeJCasCTest extends AbstractThemeJCasCTest {

    @Override
    protected Class<? extends ThemeManagerFactory> getThemeManagerFactory() {
        return SpectrumBlueTheme.class;
    }
}
