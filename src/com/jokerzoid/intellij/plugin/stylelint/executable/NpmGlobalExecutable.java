package com.jokerzoid.intellij.plugin.stylelint.executable;

import org.jetbrains.annotations.NotNull;

/**
 * Locates the stylelint package if it's installed using {@code npm install -g stylelint}.
 */
class NpmGlobalExecutable extends GlobalScriptExecutable implements ExecutableProvider {

  @NotNull
  @Override
  String getBinaryDirCommand() {
    return "npm -g bin";
  }
}
