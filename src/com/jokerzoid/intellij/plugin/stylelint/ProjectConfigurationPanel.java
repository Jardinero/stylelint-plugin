package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author rrosa
 */
public class ProjectConfigurationPanel implements SearchableConfigurable {
  private JLabel executableLabel;
  private JTextField executableTextField;
  private JButton executableDirButton;
  private JTextField commandLineArgumentsTextField;
  private JLabel commandLineArgumentsLabel;
  private JLabel configurationFileLabel;
  private JTextField configurationFileTextField;
  private JPanel rootPanel;
  private JButton configurationFileButton;

  private ProjectService state;
  private Project project;
  public ProjectConfigurationPanel(@NotNull Project project) {
    this.project = project;
  }


  @Nls
  @Override
  public String getDisplayName() {
    return "Stylelint";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    this.state = ProjectService.getInstance(project).getState();
    return rootPanel;
  }

  @Override
  public boolean isModified() {
    boolean modified;

    modified = !executableTextField.getText().equals(state.executable);
    modified |= !commandLineArgumentsTextField.getText().equals(state.arguments);
    modified |= !configurationFileTextField.getText().equals(state.configuration);

    return modified;
  }

  @Override
  public void apply() throws ConfigurationException {
    state.executable = executableTextField.getText();
    state.arguments = commandLineArgumentsTextField.getToolTipText();
    state.configuration = configurationFileTextField.getText();
  }

  @Override
  public void reset() {
    executableTextField.setText(state.executable);
    commandLineArgumentsTextField.setText(state.arguments);
    configurationFileTextField.setText(state.configuration);
  }

  @NotNull
  @Override
  public String getId() {
    return "Stylelint";
  }

  @Nullable
  @Override
  public Runnable enableSearch(String option) {
    return null;
  }
}
