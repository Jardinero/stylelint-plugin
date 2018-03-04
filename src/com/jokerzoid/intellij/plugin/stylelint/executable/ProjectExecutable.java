package com.jokerzoid.intellij.plugin.stylelint.executable;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Links to the stylelint binary if it's locally installed using the package.json file.
 *
 * @author Roelof Roos <github@roelof.io>
 */
public class ProjectExecutable implements ExecutableProvider {

  /**
   * Miliseconds to wait between checks, defaults to 60s
   */
  private static final long WAIT_DURATION = 60L * 1000L;

  /**
   * Contains timed caches, in case a directory does not exist.
   */
  private static Map<String, Long> timeLockMap = new HashMap<>();

  /**
   * Contains a map of cached values
   */
  private static Map<String, String> executableMap = new HashMap<>();

  @NotNull
  private Boolean shouldCheckAgain(String directory) {
    return !timeLockMap.containsKey(directory) || timeLockMap.get(directory) < System.currentTimeMillis();
  }

  /**
   * Returns the path. Once an executable has been found, it'll be used forever for that project.
   *
   * @param project Project to look in
   * @return String to executable, or null if none
   */
  @Override
  public String getPath(Project project) {
    // Get project path
    String rootPath = project.getBasePath();

    // Check if found or waiting for next check
    if (executableMap.containsKey(rootPath)) {
      return executableMap.get(rootPath);
    } else if (!shouldCheckAgain(rootPath)) {
      return null;
    }

    // Lock the changes for a while
    timeLockMap.put(rootPath, System.currentTimeMillis() + WAIT_DURATION);

    // Get binaries directory
    File nodeBinPath = new File(rootPath + File.separator + "node_modules/.bin");
    if (!nodeBinPath.exists() || !nodeBinPath.isDirectory()) {
      return null;
    }

    // Check if stylelint exists there
    File binFile = new File(nodeBinPath.getAbsolutePath() + File.separator + StylelintExecutableHelper.getBinaryNameForOs());
    if (binFile.exists() && binFile.isFile()) {
      String executable = binFile.getAbsolutePath();
      executableMap.put(rootPath, executable);
      return executable;
    }

    return null;
  }
}
