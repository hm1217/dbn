package com.dci.intellij.dbn.editor.code.options.ui;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.dci.intellij.dbn.common.options.ui.ConfigurationEditorForm;
import com.dci.intellij.dbn.common.ui.DBNComboBox;
import com.dci.intellij.dbn.editor.code.options.CodeEditorChangesOption;
import com.dci.intellij.dbn.editor.code.options.CodeEditorConfirmationSettings;
import com.intellij.openapi.options.ConfigurationException;

public class CodeEditorConfirmationSettingsForm extends ConfigurationEditorForm<CodeEditorConfirmationSettings> {
    private JPanel mainPanel;
    private JCheckBox confirmSaveCheckBox;
    private JCheckBox confirmRevertCheckBox;
    private DBNComboBox<CodeEditorChangesOption> disconnectSessionComboBox;

    public CodeEditorConfirmationSettingsForm(CodeEditorConfirmationSettings settings) {
        super(settings);

        disconnectSessionComboBox.setValues(CodeEditorChangesOption.ASK, CodeEditorChangesOption.SAVE, CodeEditorChangesOption.DISCARD);
        updateBorderTitleForeground(mainPanel);
        resetFormChanges();
        registerComponent(mainPanel);
    }

    public JPanel getComponent() {
        return mainPanel;
    }

    public void applyFormChanges() throws ConfigurationException {
        CodeEditorConfirmationSettings settings = getConfiguration();
        settings.getSaveChangesOptionHandler().setConfirm(confirmSaveCheckBox.isSelected());
        settings.getRevertChangesOptionHandler().setConfirm(confirmRevertCheckBox.isSelected());
        settings.getExitOnChangesOptionHandler().setSelectedOption(disconnectSessionComboBox.getSelectedValue());
    }

    public void resetFormChanges() {
        CodeEditorConfirmationSettings settings = getConfiguration();
        confirmSaveCheckBox.setSelected(settings.getSaveChangesOptionHandler().isConfirm());
        confirmRevertCheckBox.setSelected(settings.getRevertChangesOptionHandler().isConfirm());
        disconnectSessionComboBox.setSelectedValue(settings.getExitOnChangesOptionHandler().getSelectedOption());
    }
}