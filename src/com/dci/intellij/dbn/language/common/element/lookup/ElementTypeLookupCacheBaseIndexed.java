package com.dci.intellij.dbn.language.common.element.lookup;

import com.dci.intellij.dbn.language.common.SharedTokenTypeBundle;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.ElementType;
import com.dci.intellij.dbn.language.common.element.ElementTypeBundle;
import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.LeafElementType;
import com.dci.intellij.dbn.language.common.element.impl.WrappingDefinition;
import gnu.trove.THashSet;

import java.util.Set;

public abstract class ElementTypeLookupCacheBaseIndexed<T extends ElementType> extends ElementTypeLookupCacheBase<T> {

    protected Set<LeafElementType> allPossibleLeafs;
    protected Set<LeafElementType> firstPossibleLeafs;
    protected Set<LeafElementType> firstRequiredLeafs;
    protected Set<TokenType> allPossibleTokens;
    protected Set<TokenType> firstPossibleTokens;
    protected Set<TokenType> firstRequiredTokens;
    private Boolean startsWithIdentifier;

    public ElementTypeLookupCacheBaseIndexed(T elementType) {
        super(elementType);
        if (!elementType.isLeaf()) {
            allPossibleLeafs = new THashSet<LeafElementType>();
            firstPossibleLeafs = new THashSet<LeafElementType>();
            firstRequiredLeafs = new THashSet<LeafElementType>();
            allPossibleTokens = new THashSet<TokenType>();
            firstPossibleTokens = new THashSet<TokenType>();
            firstRequiredTokens = new THashSet<TokenType>();
        }
    }

    @Override
    public boolean isFirstPossibleToken(TokenType tokenType) {
        return firstPossibleTokens.contains(tokenType);
    }

    @Override
    public boolean isFirstRequiredToken(TokenType tokenType) {
        return firstRequiredTokens.contains(tokenType);
    }

    private ElementTypeBundle getBundle() {
        return elementType.getElementBundle();
    }


    public boolean containsToken(TokenType tokenType) {
        return allPossibleTokens != null && allPossibleTokens.contains(tokenType);
    }

    @Override
    public boolean containsLeaf(LeafElementType elementType) {
        return allPossibleLeafs != null && allPossibleLeafs.contains(elementType);
    }

    @Override
    public Set<TokenType> getFirstRequiredTokens() {
        return firstRequiredTokens;
    }

    public Set<TokenType> getFirstPossibleTokens() {
        return firstPossibleTokens;
    }

    public Set<LeafElementType> getFirstRequiredLeafs() {
        return firstRequiredLeafs;
    }
    @Override
    public Set<LeafElementType> getFirstPossibleLeafs() {
        return firstPossibleLeafs;
    }



    public boolean couldStartWithLeaf(LeafElementType leafElementType) {
        return firstPossibleLeafs.contains(leafElementType);
    }

    public boolean couldStartWithToken(TokenType tokenType) {
        return firstPossibleTokens.contains(tokenType);
    }

    public boolean shouldStartWithLeaf(LeafElementType leafElementType) {
        return firstRequiredLeafs.contains(leafElementType);
    }

    public void registerLeaf(LeafElementType leaf, ElementType source) {
        boolean initAllElements = initAllElements(leaf);
        boolean initAsFirstPossibleLeaf = initAsFirstPossibleLeaf(leaf, source);
        boolean initAsFirstRequiredLeaf = initAsFirstRequiredLeaf(leaf, source);

        // register first possible leafs
        ElementTypeLookupCache lookupCache = leaf.getLookupCache();
        if (initAsFirstPossibleLeaf) {
            firstPossibleLeafs.add(leaf);
            lookupCache.collectFirstPossibleTokens(firstPossibleTokens);
        }

        // register first required leafs
        if (initAsFirstRequiredLeaf) {
            firstRequiredLeafs.add(leaf);
            lookupCache.collectFirstPossibleTokens(firstRequiredTokens);
        }

        if (initAllElements) {
            // register all possible leafs
            allPossibleLeafs.add(leaf);

            // register all possible tokens
            if (leaf instanceof IdentifierElementType) {
                SharedTokenTypeBundle sharedTokenTypes = getSharedTokenTypes();
                allPossibleTokens.add(sharedTokenTypes.getIdentifier());
                allPossibleTokens.add(sharedTokenTypes.getQuotedIdentifier());
            } else {
                allPossibleTokens.add(leaf.getTokenType());
            }
        }

        if (initAsFirstPossibleLeaf || initAsFirstRequiredLeaf || initAllElements) {
            // walk the tree up
            registerLeafInParent(leaf);
        }
    }

    abstract boolean initAsFirstPossibleLeaf(LeafElementType leaf, ElementType source);
    abstract boolean initAsFirstRequiredLeaf(LeafElementType leaf, ElementType source);
    private boolean initAllElements(LeafElementType leafElementType) {
        return leafElementType != elementType && !allPossibleLeafs.contains(leafElementType);
    }

    protected void registerLeafInParent(LeafElementType leaf) {
        super.registerLeaf(leaf, null);
    }

    public boolean startsWithIdentifier() {
        if (startsWithIdentifier == null) {
            synchronized (this) {
                if (startsWithIdentifier == null) {
                    startsWithIdentifier = startsWithIdentifier(null);
                }
            }
        }
        return startsWithIdentifier;
    }

    protected boolean isWrapperBeginLeaf(LeafElementType leaf) {
        WrappingDefinition wrapping = elementType.getWrapping();
        if (wrapping != null) {
            if (wrapping.getBeginElementType() == leaf) {
                return true;
            }
        }
        return false;
    }
}