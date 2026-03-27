package io.jenkins.plugins.theme.spectrum.playwright;

import io.jenkins.plugins.theme.spectrum.SpectrumAmberTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumBlueTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumCyanTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumGreenTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumOrangeTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumRedTheme;
import io.jenkins.plugins.theme.spectrum.SpectrumVioletTheme;
import io.jenkins.plugins.thememanager.ThemeManagerFactoryDescriptor;
import java.util.List;

public record Theme(String name, String id, CssVariable variableToCheck) {

    public static final Theme SPECTRUM_RED =
            new Theme(new SpectrumRedTheme.DescriptorImpl(), CssVariable.instanceAccent("#b5475a"));
    public static final Theme SPECTRUM_ORANGE =
            new Theme(new SpectrumOrangeTheme.DescriptorImpl(), CssVariable.instanceAccent("#c46a2c"));
    public static final Theme SPECTRUM_AMBER =
            new Theme(new SpectrumAmberTheme.DescriptorImpl(), CssVariable.instanceAccent("#b8892d"));
    public static final Theme SPECTRUM_GREEN =
            new Theme(new SpectrumGreenTheme.DescriptorImpl(), CssVariable.instanceAccent("#2f8f5b"));
    public static final Theme SPECTRUM_CYAN =
            new Theme(new SpectrumCyanTheme.DescriptorImpl(), CssVariable.instanceAccent("#2f7c8f"));
    public static final Theme SPECTRUM_BLUE =
            new Theme(new SpectrumBlueTheme.DescriptorImpl(), CssVariable.instanceAccent("#3e6fb6"));
    public static final Theme SPECTRUM_VIOLET =
            new Theme(new SpectrumVioletTheme.DescriptorImpl(), CssVariable.instanceAccent("#6f5bb2"));
    public static final List<Theme> ALL = List.of(
            SPECTRUM_RED,
            SPECTRUM_ORANGE,
            SPECTRUM_AMBER,
            SPECTRUM_GREEN,
            SPECTRUM_CYAN,
            SPECTRUM_BLUE,
            SPECTRUM_VIOLET);

    public Theme {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Theme name cannot be null or empty");
        }
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Theme id cannot be null or empty");
        }
        if (variableToCheck == null) {
            throw new IllegalArgumentException("Variable to check cannot be null");
        }
    }

    public Theme(ThemeManagerFactoryDescriptor theme, CssVariable variableToCheck) {
        this(theme.getDisplayName(), theme.getThemeKey(), variableToCheck);
    }

    public record CssVariable(String name, String expected) {
        public CssVariable {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("CSS variable name cannot be null or empty");
            }
            if (!name.startsWith("--")) {
                throw new IllegalArgumentException("CSS variable name must start with '--'");
            }
            if (expected == null || expected.isEmpty()) {
                throw new IllegalArgumentException("Expected value cannot be null or empty");
            }
        }

        public static CssVariable background(String expectedValue) {
            return new CssVariable("--background", expectedValue);
        }

        public static CssVariable instanceAccent(String expectedValue) {
            return new CssVariable("--instance-accent", expectedValue);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
