package com.jokerzoid.intellij.plugin.stylelint.executable;

import org.jetbrains.annotations.NotNull;

/**
 * Links to the stylelint binary if it's globally installed using {@code yarn global add stylelint}.
 *
 * @author Roelof Roos <github@roelof.io>
 */
public class YarnGlobalExecutable extends GlobalScriptExecutable implements ExecutableProvider {

  @NotNull
  @Override
  String getBinaryDirCommand() {
    return "yarn global bin";
  }
}
