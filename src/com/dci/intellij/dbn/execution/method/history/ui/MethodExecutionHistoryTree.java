package com.dci.intellij.dbn.execution.method.history.ui;

import com.dci.intellij.dbn.common.ui.tree.DBNTree;
import com.dci.intellij.dbn.execution.method.MethodExecutionInput;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.Collections;
import java.util.List;

public class MethodExecutionHistoryTree extends DBNTree implements Disposable {
    private MethodExecutionHistoryDialog dialog;
    private boolean grouped;
    private boolean debug;

    MethodExecutionHistoryTree(MethodExecutionHistoryDialog dialog, boolean grouped, boolean debug) {
        super(dialog.getProject(), createTreeModel(grouped, debug));
        this.dialog = dialog;
        this.grouped = grouped;
        this.debug = debug;
        setCellRenderer(new TreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        TreeUtil.expand(this, 4);

        getModel().addTreeModelListener(treeModelListener);
    }

    @NotNull
    private static TreeModel createTreeModel(boolean grouped, boolean debug) {
        return grouped ?
                new MethodExecutionHistoryGroupedTreeModel(Collections.emptyList(), debug) :
                new MethodExecutionHistorySimpleTreeModel(Collections.emptyList(), debug);
    }

    @Override
    public MethodExecutionHistoryTreeModel getModel() {
        return (MethodExecutionHistoryTreeModel) super.getModel();
    }

    void init(List<MethodExecutionInput> executionInputs, boolean grouped) {
        MethodExecutionInput selectedExecutionInput = getSelectedExecutionInput();
        MethodExecutionHistoryTreeModel model = grouped ?
                new MethodExecutionHistoryGroupedTreeModel(executionInputs, debug) :
                new MethodExecutionHistorySimpleTreeModel(executionInputs, debug);
        model.addTreeModelListener(treeModelListener);
        setModel(model);
        TreeUtil.expand(this, 4);
        this.grouped = grouped;
        setSelectedInput(selectedExecutionInput);
    }

    boolean isGrouped() {
        return grouped;
    }

    void setSelectedInput(MethodExecutionInput executionInput) {
        if (executionInput != null) {
            MethodExecutionHistoryTreeModel model = getModel();
            getSelectionModel().setSelectionPath(model.getTreePath(executionInput));
        }
    }

    @Nullable
    MethodExecutionInput getSelectedExecutionInput() {
        Object selection = getLastSelectedPathComponent();
        if (selection instanceof MethodExecutionHistoryTreeModel.MethodTreeNode) {
            MethodExecutionHistoryTreeModel.MethodTreeNode methodNode = (MethodExecutionHistoryTreeModel.MethodTreeNode) selection;
            return methodNode.getExecutionInput();
        }
        return null;
    }

    private class TreeCellRenderer extends ColoredTreeCellRenderer {
        @Override
        public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            try {
                MethodExecutionHistoryTreeNode node = (MethodExecutionHistoryTreeNode) value;
                setIcon(node.getIcon());
                append(node.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                if (node instanceof MethodExecutionHistoryTreeModel.MethodTreeNode) {
                    MethodExecutionHistoryTreeModel.MethodTreeNode methodTreeNode = (MethodExecutionHistoryTreeModel.MethodTreeNode) node;
                    short overload = methodTreeNode.getOverload();
                    if (overload > 0) {
                        append(" #" + overload, SimpleTextAttributes.GRAY_ATTRIBUTES);
                    }
                }
            } catch (ProcessCanceledException ignore) {}
        }
    }

    void removeSelectedEntries() {
        MethodExecutionHistoryTreeNode treeNode = (MethodExecutionHistoryTreeNode)
                getSelectionPath().getLastPathComponent();
        MethodExecutionHistoryTreeNode parentTreeNode = (MethodExecutionHistoryTreeNode) treeNode.getParent();
        while (parentTreeNode != null &&
                parentTreeNode.getChildCount() == 1 && 
                !parentTreeNode.isRoot()) {
            getSelectionModel().setSelectionPath(TreeUtil.getPathFromRoot(parentTreeNode));
            parentTreeNode = (MethodExecutionHistoryTreeNode) parentTreeNode.getParent();
        }
        TreeUtil.removeSelected(this);
    }

    /**********************************************************
     *                         Listeners                      *
     **********************************************************/

    private TreeModelListener treeModelListener = new TreeModelHandler() {
        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            dialog.setSaveButtonEnabled(true);
        }
    };
}
