package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.intellij.openapi.actionSystem.Anchor;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;

public class ConnectionSelectActionGroup extends DefaultActionGroup {
    private static ConnectionSelectAction NO_CONNECTION = new ConnectionSelectAction(null);

    public ConnectionSelectActionGroup(Project project) {
        add(new ConnectionSettingsAction(), new Constraints(Anchor.FIRST, null));
        addSeparator();
        add(NO_CONNECTION);

        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();

        for (ConnectionHandler virtualConnectionHandler : connectionBundle.getVirtualConnections()) {
            ConnectionSelectAction connectionAction = new ConnectionSelectAction(virtualConnectionHandler);
            add(connectionAction);
        }

        if (connectionBundle.getConnectionHandlers().size() > 0) {
            addSeparator();
            for (ConnectionHandler connectionHandler : connectionBundle.getConnectionHandlers()) {
                ConnectionSelectAction connectionAction = new ConnectionSelectAction(connectionHandler);
                add(connectionAction);
            }
        }
    }
}
