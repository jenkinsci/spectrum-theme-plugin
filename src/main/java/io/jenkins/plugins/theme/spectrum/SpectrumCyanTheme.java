package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SpectrumCyanTheme extends AbstractSpectrumTheme {

    static final String KEY = "spectrum-cyan";

    @DataBoundConstructor
    public SpectrumCyanTheme() {}

    @Extension
    @Symbol("spectrumCyan")
    public static class DescriptorImpl extends AbstractSpectrumTheme.DescriptorImpl {

        public DescriptorImpl() {
            super("Spectrum Cyan", KEY);
        }

        @Override
        public ThemeManagerFactory getInstance() {
            return new SpectrumCyanTheme();
        }
    }
}
