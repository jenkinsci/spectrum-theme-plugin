package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumGreenTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-green";

    @DataBoundConstructor
    public SpectrumGreenTheme() {}

    @Extension
    @Symbol("spectrumGreen")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Green", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumGreenTheme();
        }
    }
}
