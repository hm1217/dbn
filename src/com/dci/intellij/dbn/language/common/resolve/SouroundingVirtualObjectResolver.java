package com.dci.intellij.dbn.language.common.resolve;

import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;

public class SouroundingVirtualObjectResolver extends UnderlyingObjectResolver{
    private static final SouroundingVirtualObjectResolver INSTANCE = new SouroundingVirtualObjectResolver();

    public static SouroundingVirtualObjectResolver getInstance() {
        return INSTANCE;
    }

    private SouroundingVirtualObjectResolver() {
        super("VIRTUAL_OBJECT_RESOLVER");
    }

    @Override
    public DBObject resolve(IdentifierPsiElement identifierPsiElement) {
        DBObjectType objectType = identifierPsiElement.getObjectType();
        BasePsiElement virtualObjectPsiElement = identifierPsiElement.findEnclosingVirtualObjectPsiElement(objectType);
        if (virtualObjectPsiElement != null) {
            return virtualObjectPsiElement.resolveUnderlyingObject();
        }

        return null;
    }
}