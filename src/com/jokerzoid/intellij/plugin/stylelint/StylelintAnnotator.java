package com.jokerzoid.intellij.plugin.stylelint;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raul
 */
public class StylelintAnnotator extends ExternalAnnotator<PsiFile, List<StylelintAnnotator.AnnotationResult>> {
    private static final Logger LOGGER = Logger.getInstance(StylelintAnnotator.class);

    static class AnnotationResult {
        String message;
        PsiElement node;

        AnnotationResult(String message, PsiElement node) {
            this.message = message;
            this.node = node;
        }
    }

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
    public List<AnnotationResult> doAnnotate(PsiFile file) {
        final Process process = ProcessManager.createStylelintProcess(file);
        final String output = ProcessManager.getOutput(process);

        final List<AnnotationResult> results = new ArrayList<>(1);
        final AnnotationResult result = new AnnotationResult("First Annotation", file.getFirstChild());

        results.add(result);

        return results;
    }

    @Override
    public void apply(@NotNull PsiFile file, List<AnnotationResult> annotationResult, @NotNull AnnotationHolder holder) {
        annotationResult.forEach(result -> {
            final TextRange range = result.node.getTextRange();

            LOGGER.info("Apply");
        });
    }
}
