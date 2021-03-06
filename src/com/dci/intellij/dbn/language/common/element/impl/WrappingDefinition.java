package com.dci.intellij.dbn.language.common.element.impl;

public class WrappingDefinition {
    private TokenElementType beginElementType;
    private TokenElementType endElementType;

    public WrappingDefinition() {
    }

    public WrappingDefinition(TokenElementType beginElementType, TokenElementType endElementType) {
        this.beginElementType = beginElementType;
        this.endElementType = endElementType;
    }

    public TokenElementType getBeginElementType() {
        return beginElementType;
    }

    public TokenElementType getEndElementType() {
        return endElementType;
    }

    public void setBeginElementType(TokenElementType beginElementType) {
        this.beginElementType = beginElementType;
    }

    public void setEndElementType(TokenElementType endElementType) {
        this.endElementType = endElementType;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (obj instanceof WrappingDefinition) {
            WrappingDefinition definition = (WrappingDefinition) obj;
            return
                this.beginElementType.tokenType.equals(definition.beginElementType.tokenType) &&
                this.endElementType.tokenType.equals(definition.endElementType.tokenType);
        }
        return false;
    }
}
