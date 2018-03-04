package com.jokerzoid.intellij.plugin.stylelint.executable;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

/**
 * Extends the executable to contain a cache that is invalidated whenever the target directory gets changed.
 */
public abstract class GlobalScriptExecutable implements ExecutableProvider {

  /**
   * Miliseconds to wait between checks, defaults to 60s
   */
  private static final long WAIT_DURATION = 60L * 1000L;

  /**
   * Cached path to binaries
   */
  private String resolvedBinaryDir = null;

  /**
   * Cached found executable
   */
  private String foundExecutable = null;

  /**
   * Timeout until next search
   */
  private long timeout = 0L;

  /**
   * Command to run to determine directory
   *
   * @return A command name, with optional args
   */
  @NotNull
  abstract String getBinaryDirCommand();

  private String getBinaryDir() {
    if (resolvedBinaryDir != null) {
      return resolvedBinaryDir.isEmpty() ? null : resolvedBinaryDir;
    }

    // Set binary dir to empty string, which prevents further runs
    resolvedBinaryDir = "";

    // Start a new process
    try {
      Process proc = Runtime.getRuntime().exec(getBinaryDirCommand());
      // Wait for it to finish
      try {
        // Wait a max of 100ms, otherwise return null
        if (!proc.waitFor(100L, TimeUnit.MILLISECONDS)) {
          return null;
        }
      } catch (InterruptedException e) {
        return null;
      }

      // Get the return value of the process into a reader
      InputStream stream = proc.getInputStream();
      ObjectInputStream reader = new ObjectInputStream(stream);

      // Read data, close stream
      String result = reader.available() > 0 ? reader.readUTF() : "";
      reader.close();

      // Save that longest line (or an empty value)
      resolvedBinaryDir = result.trim();
    } catch (IOException e) {
      return null;
    }

    // Return path or null on empty
    return resolvedBinaryDir.isEmpty() ? null : resolvedBinaryDir;
  }

  /**
   * Returns the path. Once an executable has been found, it'll be used forever for that project.
   *
   * @param project Project to look in
   * @return String to executable, or null if none
   */
  @Override
  public String getPath(Project project) {

    // Return path
    if (foundExecutable != null) {
      return foundExecutable.isEmpty() ? null : foundExecutable;
    } else if (timeout > System.currentTimeMillis() || getBinaryDir() == null) {
      return null;
    }

    // Sleep for a bit after this run
    timeout = System.currentTimeMillis() + WAIT_DURATION;

    // Check if vendor bin directory exists
    File nodeBinPath = new File(getBinaryDir());
    if (!nodeBinPath.exists() || !nodeBinPath.isDirectory()) {
      return null;
    }

    // Check if file exists in that directory
    File binFile = new File(nodeBinPath.getAbsolutePath() + File.separator + StylelintExecutableHelper.getBinaryNameForOs());
    if (binFile.exists() && binFile.isFile()) {
      foundExecutable = binFile.getAbsolutePath();
      return foundExecutable;
    }

    return null;
  }

}
