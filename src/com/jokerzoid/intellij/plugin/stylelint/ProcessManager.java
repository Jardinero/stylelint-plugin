package com.jokerzoid.intellij.plugin.stylelint;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * This class executes stylelint assuming two things:
 * 1. There is an STYLELINT_HOME variable define. This path contains the stylelint executable.
 * 2. There is a stylelint config file in the root directory of the project.
 *
 * @author Raul
 */
class ProcessManager {
  private static final Logger LOGGER = Logger.getInstance(ProcessManager.class.getPackage().getName());
  private static final String STYLELINT_HOME = "STYLELINT_HOME";

  static StylelintOutput runStylelint(final PsiFile file) {
    final Process process = createStylelintProcess(file);

    final String source = getOutputAsString(process);

    LOGGER.debug("Stylelint output: \n" + source);

    return getOutput(source, file);
  }

  private static Process createStylelintProcess(final PsiFile psiFile) {
    final VirtualFile virtualFile = psiFile.getVirtualFile();

    if (virtualFile == null) {
      LOGGER.error("The file only exists in memory. It will be checked after saved to disk.");
      return null;
    }

    final GeneralCommandLine commandLine = new GeneralCommandLine();

    commandLine.setExePath(getDefaultExePath());
    commandLine.setWorkDirectory(psiFile.getProject().getBasePath());
    commandLine.withEnvironment(System.getenv());
    commandLine.addParameters("-f", "json");
    commandLine.addParameter(virtualFile.getPath());
    commandLine.setCharset(Charset.forName("UTF8"));

    try {
      return commandLine.createProcess();
    } catch (ExecutionException ex) {
      LOGGER.error("Could not create stylelint process.", ex);
      throw new StylelintPluginException(ex);
    }
  }

  private static StylelintOutput getOutput(final String source, final PsiFile psiFile) {
    final Gson gson = new Gson();
    final JsonParser parser = new JsonParser();
    final String text = psiFile.getText();

        /* Stylelint can process several files with one execution, since this process is ran once per file we only need
         * the first result */
    final JsonElement element = parser.parse(source).getAsJsonArray().get(0);
    final StylelintOutput output = gson.fromJson(element, StylelintOutput.class);

        /* Since IntelliJ uses offsets instead of (line, col) we need to convert them */
    output.getWarnings().forEach(warning -> {
      warning.setOffset(StringUtil.lineColToOffset(text, warning.getLine() - 1, warning.getColumn() - 1));
    });

    return output;
  }

  private static String getOutputAsString(final Process process) {
    if (process == null) {
      LOGGER.warn("Process is null. Nothing to process.");
      return null;
    }
    final Reader inputStreamReader = new InputStreamReader(process.getInputStream());
    final BufferedReader input = new BufferedReader(inputStreamReader);

    StringBuilder output = new StringBuilder();
    try {
      String line = input.readLine();

      while (line != null) {
        output.append(line);
        line = input.readLine();
      }

            /* Need to handle special cases for different error codes. For example when the
             * stylelint configuration file could not be found. */
      process.waitFor();
      LOGGER.debug("Process exit code: " + process.exitValue());
    } catch (IOException | InterruptedException ex) {
      LOGGER.error("There was a problem while trying to read the stylelint process output.", ex);
      throw new StylelintPluginException(ex);
    }

    return output.toString();
  }

  /**
   * This method just takes the value of the STYLELINT_HOME variable if it is set. This is the
   * default value when there is nothing configured in Preferences.
   * @return The executable path.
   */
  static String getDefaultExePath() {
    try {
      String stylelintHome = StringUtils.defaultIfEmpty((System.getenv(STYLELINT_HOME)), "");

      return stylelintHome + File.separator + getDefaultExe();
    } catch (final SecurityException ex) {
      LOGGER.error("Could not retrieve the value of the STYLELINT_HOME variable.", ex);
      return "";
    }
  }

  /**
   * This function detects the operating system based on the os.name property, if it is Windows
   * it will append .cmd at the end.
   * Left this here for the few people who started using the plugin since the first version which
   * required the STYLELINT_HOME variable set.
   * @return The executable name
   */
  private static String getDefaultExe() {

    /* I'm assuming only windows uses .cmd extension. */
    String osName = StringUtils.defaultIfEmpty(System.getProperty("os.name"), "").toLowerCase();
    String exe = "stylelint";

    if (osName.contains("windows")) {
      return exe.concat(".cmd");
    }

    return exe;
  }
}
