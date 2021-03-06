package com.dci.intellij.dbn.common.editor;

import com.dci.intellij.dbn.common.ProjectRef;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.vfs.file.DBContentVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EditorNotificationProvider<T extends EditorNotificationPanel> extends EditorNotifications.Provider<T> {
    protected ProjectRef projectRef;

    public EditorNotificationProvider(@NotNull Project project) {
        this.projectRef = ProjectRef.from(project);
    }

    public void updateEditorNotification(@Nullable DBContentVirtualFile databaseContentFile) {
        Dispatch.run(() -> {
            Project project = getProject();
            EditorNotifications notifications = EditorNotifications.getInstance(project);
            if (databaseContentFile ==  null) {
                notifications.updateAllNotifications();
            } else {
                notifications.updateNotifications(databaseContentFile.getMainDatabaseFile());
            }
        });
    }

    @NotNull
    protected Project getProject() {
        return projectRef.ensure();
    }
}
