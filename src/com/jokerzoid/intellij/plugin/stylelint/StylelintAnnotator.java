package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Raul
 */
public class StylelintAnnotator extends ExternalAnnotator<PsiFile, List<StylelintWarning>> {
  private static final Logger LOGGER = Logger.getInstance(StylelintAnnotator.class);

  public StylelintAnnotator() {
    LOGGER.info("StylelintAnnotator");
  }

  @Nullable
  @Override
  public PsiFile collectInformation(@NotNull PsiFile file) {
    return file;
  }

  @Nullable
  @Override
  public PsiFile collectInformation(@NotNull PsiFile file, @NotNull Editor editor, boolean hasErrors) {
    return super.collectInformation(file, editor, hasErrors);
  }

  @Nullable
  @Override
  public List<StylelintWarning> doAnnotate(PsiFile file) {
    final StylelintOutput output = ProcessManager.runStylelint(file);
    return output.getWarnings();
  }

  @Override
  public void apply(@NotNull PsiFile file, List<StylelintWarning> warnings, @NotNull AnnotationHolder holder) {
    warnings.forEach(warning -> {
      final PsiElement element = PsiUtilCore.getElementAtOffset(file, warning.getOffset());

      holder.createWarningAnnotation(element, warning.getText());
      LOGGER.info("Apply");
    });
  }
}
