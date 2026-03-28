# Spectrum Theme Plugin

[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/spectrum-theme.svg)](https://plugins.jenkins.io/spectrum-theme/)
[![Jenkins Security Scan](https://github.com/jenkinsci/spectrum-theme-plugin/actions/workflows/jenkins-security-scan.yml/badge.svg)](https://github.com/jenkinsci/spectrum-theme-plugin/actions/workflows/jenkins-security-scan.yml)
[![License](https://img.shields.io/github/license/jenkinsci/spectrum-theme-plugin)](LICENSE.md)

中文说明见 [README.zh-CN.md](README.zh-CN.md)。

Spectrum Theme provides seven accent-driven Jenkins themes inspired by rainbow colors.
The palette is softened and balanced for clear instance recognition in everyday Jenkins use.

![Spectrum Theme preview](docs/images/preview.png)

## Variants

- `Spectrum Red`
- `Spectrum Orange`
- `Spectrum Amber`
- `Spectrum Green`
- `Spectrum Cyan`
- `Spectrum Blue`
- `Spectrum Violet`

## Usage

After installing this plugin, go to _Manage Jenkins > Appearance > Themes_ and select one of the `Spectrum` variants.

Configuration as Code is supported too:

```yaml
appearance:
  themeManager:
    disableUserThemes: true
    theme: "spectrumBlue"
```

Available JCasC symbols:

- `spectrumRed`
- `spectrumOrange`
- `spectrumAmber`
- `spectrumGreen`
- `spectrumCyan`
- `spectrumBlue`
- `spectrumViolet`

## Design Notes

- Uses a shared CSS asset so Jenkins can switch between Spectrum variants instantly on the appearance page.
- Keeps the Jenkins light theme structure and uses color mainly for instance identity surfaces such as the header border, current navigation item, link hover states, focus accents, and menu hover states.
- Leaves primary actions on the standard Jenkins blue so warning-like colors such as red do not read as error states.
- Aims for high instance recognition without turning the interface into a full custom redesign.

## Development

Run the test suite with:

```bash
mvn test
```

Refer to our [contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md).


Licensed under MIT, see [LICENSE](LICENSE.md).
