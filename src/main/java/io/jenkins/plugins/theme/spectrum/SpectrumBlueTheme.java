package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumBlueTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-blue";

    @DataBoundConstructor
    public SpectrumBlueTheme() {}

    @Extension
    @Symbol("spectrumBlue")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Blue", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumBlueTheme();
        }
    }
}
