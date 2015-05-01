package com.dci.intellij.dbn.common.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.common.Colors;
import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.DisposableProjectComponent;

public class DBNCollapsiblePanel<P extends DisposableProjectComponent> extends DBNFormImpl<P>{
    private JLabel collapseExpandLabel;
    private JPanel contentPanel;
    private JPanel mainPanel;
    private boolean expanded = false;

    @Override
    public JComponent getComponent() {
        return mainPanel;
    }

    public DBNCollapsiblePanel(@NotNull P parentComponent, JComponent contentComponent, String title, boolean expanded) {
        super(parentComponent);
        this.expanded = expanded;
        contentPanel.add(contentComponent, BorderLayout.CENTER);
        contentPanel.setVisible(this.expanded);
        collapseExpandLabel.setText(title);
        collapseExpandLabel.setIcon(this.expanded ? Icons.COMMON_DOWN : Icons.COMMON_RIGHT);
        collapseExpandLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        collapseExpandLabel.setForeground(Colors.HINT_COLOR);

        collapseExpandLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    DBNCollapsiblePanel.this.expanded = !DBNCollapsiblePanel.this.expanded;
                    contentPanel.setVisible(DBNCollapsiblePanel.this.expanded);
                    collapseExpandLabel.setIcon(DBNCollapsiblePanel.this.expanded ? Icons.COMMON_DOWN : Icons.COMMON_RIGHT);
                }
            }
        });
    }
}