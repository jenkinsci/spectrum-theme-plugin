package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumRedTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-red";

    @DataBoundConstructor
    public SpectrumRedTheme() {}

    @Extension
    @Symbol("spectrumRed")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Red", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumRedTheme();
        }
    }
}
