package com.jokerzoid.intellij.plugin.stylelint.executable;

import com.intellij.openapi.project.Project;

import java.io.File;

/**
 * Checks if the Stylelint binary exists on the path.
 */
public class PathExecutable implements ExecutableProvider {

  /**
   * Path to the stylelint binary, empty string if not found.
   * <p>
   * Since the PATH variable cannot change during execution, we're caching the response value indefinitely.
   */
  private String binaryPath = null;

  @Override
  public String getPath(Project project) {
    // if we've already searched, use that data
    if (binaryPath != null) {
      return binaryPath.isEmpty() ? null : binaryPath;
    }

    // Find the path and split it by path separator
    String systemPath = System.getenv("PATH");
    String[] pathSections = systemPath.split(File.pathSeparator);

    // Get the OS name
    String binaryName = StylelintExecutableHelper.getBinaryNameForOs();

    // Prepare variables
    File testFile;
    String foundPath = "";

    // Loop through each section
    for (String section : pathSections) {
      testFile = new File(section + binaryName);
      if (testFile.exists() && testFile.isFile() && testFile.canExecute()) {
        foundPath = testFile.getAbsolutePath();
        break;
      }
    }

    // Cache result indefinitely and return value
    binaryPath = foundPath;
    return binaryPath.isEmpty() ? null : binaryPath;
  }
}
