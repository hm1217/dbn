package com.dci.intellij.dbn.connection.action;

import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.intellij.openapi.project.DumbAwareAction;

public abstract class AbstractConnectionAction extends DumbAwareAction {
    private ConnectionHandlerRef connectionHandlerRef;

    public AbstractConnectionAction(String text, @NotNull ConnectionHandler connectionHandler) {
        this(text, null, connectionHandler);

    }
    public AbstractConnectionAction(String text, Icon icon, @NotNull ConnectionHandler connectionHandler) {
        this(text, null, icon, connectionHandler);
    }
    public AbstractConnectionAction(String text, String description, Icon icon, @NotNull ConnectionHandler connectionHandler) {
        super(text, description, icon);
        this.connectionHandlerRef = connectionHandler.getRef();
    }

    @NotNull
    public ConnectionHandler getConnectionHandler() {
        return connectionHandlerRef.get();
    }
}