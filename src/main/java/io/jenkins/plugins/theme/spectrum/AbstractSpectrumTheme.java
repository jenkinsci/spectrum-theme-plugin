package io.jenkins.plugins.theme.spectrum;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.jenkins.plugins.thememanager.Theme;
import io.jenkins.plugins.thememanager.ThemeManagerFactory;
import io.jenkins.plugins.thememanager.ThemeManagerFactoryDescriptor;
import java.util.List;

@SuppressWarnings("lgtm[jenkins/plaintext-password-storage]")
abstract class AbstractSpectrumTheme extends ThemeManagerFactory {

    static final String ID = "spectrum";
    static final String CSS = "spectrum.css";
    static final String BASE_CSS = "spectrum-base.css";

    protected AbstractSpectrumTheme() {
        // Stapler
    }

    @Override
    public Theme getTheme() {
        return Theme.builder().withCssUrls(List.of(getCssUrl())).build();
    }

    abstract static class DescriptorImpl extends ThemeManagerFactoryDescriptor {
        private final String displayName;
        private final String themeKey;

        protected DescriptorImpl(String displayName, String themeKey) {
            this.displayName = displayName;
            this.themeKey = themeKey;
        }

        @Override
        public String getThemeKey() {
            return themeKey;
        }

        @Override
        public String getThemeCssSuffix() {
            return CSS;
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getIconClassName() {
            return "symbol-spectrum plugin-spectrum-theme";
        }

        @Override
        public String getThemeId() {
            return ID;
        }

        @Override
        public boolean isNamespaced() {
            return true;
        }
    }
}
