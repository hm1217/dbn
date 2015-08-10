package com.dci.intellij.dbn.debugger.jdwp.frame;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.debugger.jdwp.process.DBJdwpDebugProcess;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.PositionManagerFactory;
import com.intellij.debugger.engine.DebugProcess;

public class DBJdwpDebugPositionManagerFactory extends PositionManagerFactory {
    @Nullable
    @Override
    public PositionManager createPositionManager(@NotNull DebugProcess process) {
        DBJdwpDebugProcess jdwpDebugProcess = process.getUserData(DBJdwpDebugProcess.KEY);
        return jdwpDebugProcess == null ? null : new DBJdwpDebugPositionManager(process);
    }
}
