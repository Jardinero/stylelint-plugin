package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.Nullable;

/**
 * @author rrosa
 */
@State(name = "StylelintService")
public class ProjectService implements PersistentStateComponent<ProjectService> {
  private static final Logger LOGGER = Logger.getInstance(ProjectService.class.getPackage().getName());

  public String executable = "";

  public ProjectService() {
    LOGGER.debug("ProjectService instantiated.");
  }

  static ProjectService getInstance(Project project) {
    return ServiceManager.getService(project, ProjectService.class);
  }

  @Nullable
  @Override
  public ProjectService getState() {
    return this;
  }

  @Override
  public void loadState(ProjectService state) {
    XmlSerializerUtil.copyBean(state, this);
    if (this.executable.equals("/stylelint") || this.executable.equals("/stylelint.cmd")) {
      this.executable = "";
    }
  }

}
