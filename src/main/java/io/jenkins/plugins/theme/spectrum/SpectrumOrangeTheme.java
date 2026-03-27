package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumOrangeTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-orange";

    @DataBoundConstructor
    public SpectrumOrangeTheme() {}

    @Extension
    @Symbol("spectrumOrange")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Orange", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumOrangeTheme();
        }
    }
}
