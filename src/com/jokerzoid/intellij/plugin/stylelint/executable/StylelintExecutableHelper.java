package com.jokerzoid.intellij.plugin.stylelint.executable;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Links to the stylelint installation from a path set from the settings.
 *
 * @author Roelof Roos <github@roelof.io>
 */
public class StylelintExecutableHelper {

  private static final Map<Project, String> executableDictionary = new HashMap<>();

  /**
   * Possible classes that can resolve a path to Stylelint
   */
  private final static ExecutableProvider[] executableProviders = new ExecutableProvider[]{
    new ProjectExecutable(),
    new NpmGlobalExecutable(),
    new YarnGlobalExecutable(),
    new PathExecutable(),
  };

  /**
   * Return the name the program is most likely reachable over. For Windows this is somewhat unreliable as users may
   * rename the {@code .cmd} to {@code .bat} or {@code .com}
   *
   * @return Returns the filename for stylelint, optionally with an extension
   */
  static String getBinaryNameForOs() {
    String osName = StringUtils.defaultIfEmpty(System.getProperty("os.name"), "").toLowerCase();
    String programName = "stylelint";

    if (osName.contains("windows")) {
      return programName.concat(".cmd");
    }

    return programName;
  }

  /**
   * Finds the executable for the path. Cached indefinitely.
   *
   * @param project
   * @return path to exectuable, or null if none was eligible
   */
  public String findExecutable(Project project) {
    if (executableDictionary.containsKey(project)) {
      return executableDictionary.get(project);
    }

    String foundPath = null;
    File foundFile;
    for (ExecutableProvider provider : executableProviders) {
      foundPath = provider.getPath(project);
      if (foundPath == null) {
        continue;
      }

      foundFile = new File(foundPath);
      if (!foundFile.exists() || !foundFile.isFile() || !foundFile.canExecute()) {
        foundPath = null;
        continue;
      }

      break;
    }

    executableDictionary.put(project, foundPath);
    return foundPath;
  }
}
