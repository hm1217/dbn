package com.dci.intellij.dbn.connection.info.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.dci.intellij.dbn.common.ui.DBNHeaderForm;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.DatabaseType;
import com.dci.intellij.dbn.connection.config.ConnectionDatabaseSettings;
import com.dci.intellij.dbn.connection.info.ConnectionInfo;

public class ConnectionInfoForm extends DBNFormImpl{
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel setupPanel;
    private JLabel infoProductNameLabel;
    private JLabel infoProductVersionLabel;
    private JLabel infoDriverNameLabel;
    private JLabel infoDriverVersionLabel;
    private JLabel infoConnectionUrlLabel;
    private JLabel infoUserNameLabel;
    private JLabel setupNameLabel;
    private JLabel setupDescriptionLabel;
    private JLabel setupDriverLibraryLabel;
    private JLabel setupDriverLabel;
    private JLabel setupUrlLabel;
    private JPanel metaDataPanel;
    private JPanel detailsPanel;
    private JLabel statusMessageLabel;
    private JLabel setupDatabaseTypeLabel;

    public ConnectionInfoForm(ConnectionHandler connectionHandler, boolean showHeader) {
        initHeaderPanel(connectionHandler, showHeader);
        initSetupPanel(connectionHandler);
        initInfoPanel(connectionHandler);
    }
    private void initHeaderPanel(ConnectionHandler connectionHandler, boolean showHeader) {
        if (showHeader) {
            DBNHeaderForm headerForm = new DBNHeaderForm();
            headerForm.setTitle(connectionHandler.getName());
            headerForm.setIcon(connectionHandler.getIcon());
            headerForm.setBackground(connectionHandler.getEnvironmentType().getColor());
            headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        } else {
            headerPanel.setVisible(false);
        }
    }

    private void initInfoPanel(ConnectionHandler connectionHandler) {
        try {
            Connection connection = connectionHandler.getStandaloneConnection();
            ConnectionInfo connectionInfo = new ConnectionInfo(connection.getMetaData());

            infoProductNameLabel.setText(connectionInfo.getProductName());
            infoProductVersionLabel.setText(connectionInfo.getProductVersion());
            infoDriverNameLabel.setText(connectionInfo.getDriverName());
            infoDriverVersionLabel.setText(connectionInfo.getDriverVersion());
            infoConnectionUrlLabel.setText(connectionInfo.getUrl());
            infoUserNameLabel.setText(connectionInfo.getUserName());

            statusMessageLabel.setText("Conection successful");
            statusMessageLabel.setIcon(Icons.EXEC_MESSAGES_INFO);

        } catch (SQLException e) {
            infoProductNameLabel.setText("-");
            infoProductVersionLabel.setText("-");
            infoDriverNameLabel.setText("-");
            infoDriverVersionLabel.setText("-");
            infoConnectionUrlLabel.setText("-");
            infoUserNameLabel.setText("-");
            statusMessageLabel.setText("Connection error: " + e.getMessage());
            statusMessageLabel.setIcon(Icons.EXEC_MESSAGES_ERROR);
        }

        updateBorderTitleForeground(detailsPanel);
    }

    private void initSetupPanel(ConnectionHandler connectionHandler) {
        setupNameLabel.setText(connectionHandler.getName());
        setupDescriptionLabel.setText(connectionHandler.getDescription());
        ConnectionDatabaseSettings databaseSettings = connectionHandler.getSettings().getDatabaseSettings();
        setupDriverLibraryLabel.setText(databaseSettings.getDriverLibrary());
        setupDriverLabel.setText(databaseSettings.getDriver());
        setupUrlLabel.setText(databaseSettings.getDatabaseUrl());
        DatabaseType databaseType = connectionHandler.getDatabaseType();
        setupDatabaseTypeLabel.setText(databaseType.getName());
        setupDatabaseTypeLabel.setIcon(databaseType.getIcon());
        updateBorderTitleForeground(setupPanel);
    }

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
