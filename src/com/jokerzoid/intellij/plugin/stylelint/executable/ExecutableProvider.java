package com.jokerzoid.intellij.plugin.stylelint.executable;

import com.intellij.openapi.project.Project;

public interface ExecutableProvider {

  /**
   * Returns a path to Stylelint, which does or does not exist.
   *
   * @param project Project to look in
   * @return Absolute path to the stylelint executable
   */
  String getPath(Project project);

}
