package com.dci.intellij.dbn.language.common.psi;

import com.dci.intellij.dbn.common.property.Property;

public enum PsiResolveStatus implements Property{
    NEW,
    RESOLVING,
    RESOLVING_OBJECT_TYPE,
    CONNECTION_VALID,
    CONNECTION_ACTIVE;

    private final int index = Property.idx(this);

    @Override
    public int index() {
        return index;
    }
}
