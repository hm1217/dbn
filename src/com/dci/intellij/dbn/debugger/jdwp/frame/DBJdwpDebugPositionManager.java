package com.dci.intellij.dbn.debugger.jdwp.frame;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.debugger.jdwp.process.DBJdwpDebugProcess;
import com.dci.intellij.dbn.execution.ExecutionInput;
import com.dci.intellij.dbn.execution.statement.StatementExecutionInput;
import com.dci.intellij.dbn.language.common.DBLanguagePsiFile;
import com.dci.intellij.dbn.language.common.psi.PsiUtil;
import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;

public class DBJdwpDebugPositionManager implements PositionManager {
    private DebugProcess process;

    public DBJdwpDebugPositionManager(@NotNull DebugProcess process) {
        this.process = process;
    }

    @Nullable
    @Override
    public SourcePosition getSourcePosition(@Nullable Location location) throws NoDataException {
        location = check(location);
        int lineNumber = location.lineNumber() - 1;

        String ownerName = getOwnerName(location);
        VirtualFile virtualFile = getDebugProcess().getVirtualFile(location);
        if (virtualFile != null) {
            PsiFile psiFile = PsiUtil.getPsiFile(getDebugProcess().getProject(), virtualFile);

            if (psiFile != null) {
                if (ownerName == null) {
                    ExecutionInput executionInput = getDebugProcess().getExecutionInput();
                    if (executionInput instanceof StatementExecutionInput) {
                        StatementExecutionInput statementExecutionInput = (StatementExecutionInput) executionInput;
                        lineNumber += statementExecutionInput.getExecutableLineNumber();
                    }
                }
                return SourcePosition.createFromLine(psiFile, lineNumber);
            }
        }

        return null;
    }

    public String getOwnerName(@Nullable Location location) {
        try {
            if (location != null) {
                String sourcePath = location.sourcePath();
                StringTokenizer tokenizer = new StringTokenizer(sourcePath, "\\.");
                String signature = tokenizer.nextToken();
                String programType = tokenizer.nextToken();
                return tokenizer.nextToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected DBJdwpDebugProcess getDebugProcess() {
        return process.getUserData(DBJdwpDebugProcess.KEY);
    }

    @NotNull
    @Override
    public List<ReferenceType> getAllClasses(@NotNull SourcePosition classPosition) throws NoDataException {
        check(classPosition);
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<Location> locationsOfLine(@NotNull ReferenceType type, @NotNull SourcePosition position) throws NoDataException {
        check(position);
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public ClassPrepareRequest createPrepareRequest(@NotNull ClassPrepareRequestor requestor, @NotNull SourcePosition position) throws NoDataException {
        check(position);
        return process.getRequestsManager().createClassPrepareRequest(requestor, "");
    }

    @NotNull Location check(@Nullable Location location) throws NoDataException {
        if (location == null || !location.declaringType().name().startsWith("$Oracle")) {
            throw NoDataException.INSTANCE;
        }
        return location;
    }

    void check(@NotNull SourcePosition position) throws NoDataException {
        PsiFile file = position.getFile();
        if (!(file instanceof DBLanguagePsiFile)) {
            throw NoDataException.INSTANCE;
        }
    }
}