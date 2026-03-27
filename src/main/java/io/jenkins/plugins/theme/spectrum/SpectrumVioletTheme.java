package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumVioletTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-violet";

    @DataBoundConstructor
    public SpectrumVioletTheme() {}

    @Extension
    @Symbol("spectrumViolet")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Violet", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumVioletTheme();
        }
    }
}
