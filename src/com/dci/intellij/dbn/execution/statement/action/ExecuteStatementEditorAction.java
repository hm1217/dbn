package com.dci.intellij.dbn.execution.statement.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.action.DumbAwareProjectAction;
import com.dci.intellij.dbn.common.action.Lookup;
import com.dci.intellij.dbn.common.util.EditorUtil;
import com.dci.intellij.dbn.debugger.DatabaseDebuggerManager;
import com.dci.intellij.dbn.execution.statement.StatementExecutionManager;
import com.dci.intellij.dbn.language.common.DBLanguagePsiFile;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class ExecuteStatementEditorAction extends DumbAwareProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        Editor editor = Lookup.getEditor(e);
        if (editor != null) {
            FileEditor fileEditor = EditorUtil.getFileEditor(editor);
            if (fileEditor != null) {
                StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
                executionManager.executeStatementAtCursor(fileEditor);
            }
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(isEnabled(e));
        presentation.setIcon(Icons.STMT_EXECUTION_RUN);
        presentation.setText("Execute Statement");
        presentation.setVisible(isVisible(e));
    }

    private static boolean isEnabled(AnActionEvent e) {
        Project project = Lookup.getProject(e);
        Editor editor = Lookup.getEditor(e);
        if (project == null || editor == null) {
            return false;
        } else {
            PsiFile psiFile = PsiUtil.getPsiFile(project, editor.getDocument());
            return psiFile instanceof DBLanguagePsiFile;
        }
    }

    public static boolean isVisible(AnActionEvent e) {
        VirtualFile virtualFile = Lookup.getVirtualFile(e);
        return !DatabaseDebuggerManager.isDebugConsole(virtualFile);
    }
}
