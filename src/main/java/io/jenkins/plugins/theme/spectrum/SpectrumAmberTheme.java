package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumAmberTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-amber";

    @DataBoundConstructor
    public SpectrumAmberTheme() {}

    @Extension
    @Symbol("spectrumAmber")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Amber", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumAmberTheme();
        }
    }
}
