package io.jenkins.plugins.theme.spectrum;

import hudson.Extension;
import hudson.Plugin;
import hudson.model.UnprotectedRootAction;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Set;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;

@Extension
public class SpectrumRootAction implements UnprotectedRootAction {

    private static final Set<String> ALLOWED_CSS = Set.of(
            AbstractSpectrumTheme.CSS,
            AbstractSpectrumTheme.BASE_CSS,
            "spectrum-red.css",
            "spectrum-orange.css",
            "spectrum-amber.css",
            "spectrum-green.css",
            "spectrum-cyan.css",
            "spectrum-blue.css",
            "spectrum-violet.css");

    @Override
    public String getIconFileName() {
        return null; /* no UI */
    }

    @Override
    public String getDisplayName() {
        return null; /* no UI */
    }

    @Override
    public String getUrlName() {
        return "theme-" + AbstractSpectrumTheme.ID;
    }

    public void doDynamic(StaplerRequest2 req, StaplerResponse2 rsp) throws IOException, ServletException {
        String cssFile = req.getRestOfPath();
        if (cssFile.startsWith("/")) {
            cssFile = cssFile.substring(1);
        }
        if (!ALLOWED_CSS.contains(cssFile)) {
            rsp.sendError(404);
            return;
        }
        final Plugin plugin = Jenkins.get().getPlugin("spectrum-theme");
        if (plugin == null) {
            rsp.sendError(404);
            return;
        }
        plugin.doDynamic(req, rsp);
    }
}
