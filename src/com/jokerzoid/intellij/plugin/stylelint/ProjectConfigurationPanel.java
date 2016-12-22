package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;

/**
 * @author rrosa
 */
public class ProjectConfigurationPanel implements SearchableConfigurable {
  private static final Logger LOGGER = Logger.getInstance(ProjectConfigurationPanel.class.getPackage().getName());
  private JLabel executableLabel;
  private JTextField executableTextField;
  private JButton executableDirButton;
  private JPanel rootPanel;
  private JFileChooser fileChooser;

  private ProjectService state;

  public ProjectConfigurationPanel(@NotNull Project project) {
    this.state = ProjectService.getInstance(project).getState();
    fileChooser = new JFileChooser();

    this.addListeners();
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
    return rootPanel;
  }

  @Override
  public boolean isModified() {
    return !executableTextField.getText().equals(state.executable);
  }

  @Override
  public void apply() throws ConfigurationException {
    state.executable = executableTextField.getText();
  }

  @Override
  public void reset() {
    if (StringUtils.isEmpty(state.executable)) {
      executableTextField.setText(ProcessManager.getDefaultExePath());
    } else {
      executableTextField.setText(state.executable);
    }
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

  private void addListeners() {
    executableDirButton.addActionListener((ActionEvent event) -> {
      int returnValue = fileChooser.showDialog(rootPanel, "Select");

      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();

        LOGGER.debug("Selected file: " + file.getPath());

        executableTextField.setText(file.getPath());
      } else {
        LOGGER.debug("User canceled the file selection.");
      }
    });
  }

}
