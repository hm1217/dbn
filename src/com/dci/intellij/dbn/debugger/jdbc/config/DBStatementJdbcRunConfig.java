package com.dci.intellij.dbn.debugger.jdbc.config;

import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.debugger.common.config.DBProgramRunConfigType;
import com.dci.intellij.dbn.debugger.common.config.DBStatementRunConfig;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;

public class DBStatementJdbcRunConfig extends DBStatementRunConfig {

    public DBStatementJdbcRunConfig(Project project, DBProgramRunConfigType configType, String name, boolean generic) {
        super(project, configType, name, generic);
    }

    @Override
    protected DBStatementJdbcRunConfigEditor createConfigurationEditor() {
        return new DBStatementJdbcRunConfigEditor(this);
    }

    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        return new DBStatementJdbcRunProfileState(env);
    }

    public void checkConfiguration() throws RuntimeConfigurationException {
/*        if (executionInput == null) {
            throw new RuntimeConfigurationError("No or invalid method selected. The database connection is down, obsolete or method has been dropped.");
        }

        if (executionInput.isObsolete()) {
            throw new RuntimeConfigurationError(
                    "Method " + executionInput.getMethodRef().getQualifiedName() + " could not be resolved. " +
                    "The database connection is down or method has been dropped.");
        }

        ConnectionHandler connectionHandler = getMethod().getConnectionHandler();
        if (!DatabaseFeature.DEBUGGING.isSupported(connectionHandler)){
            throw new RuntimeConfigurationError(
                    "Debugging is not supported for " + connectionHandler.getDatabaseType().getDisplayName() +" databases.");
        }*/
    }


    @Override
    public RunConfiguration clone() {
        DBStatementJdbcRunConfig runConfiguration = (DBStatementJdbcRunConfig) super.clone();
        runConfiguration.resetConfigurationEditor();
        return runConfiguration;
    }
}
