package com.dci.intellij.dbn.language.common.element.impl;

import com.dci.intellij.dbn.language.common.element.ElementTypeBundle;
import com.dci.intellij.dbn.language.common.element.lookup.QualifiedIdentifierElementTypeLookupCache;
import com.dci.intellij.dbn.language.common.element.parser.impl.QualifiedIdentifierElementTypeParser;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeDefinitionException;
import com.dci.intellij.dbn.language.common.psi.QualifiedIdentifierPsiElement;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class QualifiedIdentifierElementType extends ElementTypeBase {
    private TokenElementType separatorToken;
    private final List<LeafElementType[]> variants = new ArrayList<>();
    private Set<DBObjectType> objectTypeCache = EnumSet.noneOf(DBObjectType.class);
    private int maxLength;

    public QualifiedIdentifierElementType(ElementTypeBundle bundle, ElementTypeBase parent, String id, Element def) throws ElementTypeDefinitionException {
        super(bundle, parent, id, def);
        List children = def.getChildren();
        for (Object o : children) {
            Element child = (Element) o;
            List<LeafElementType[]> childVariants = createVariants(child);
            for (LeafElementType[] childVariant : childVariants) {
                for (LeafElementType leafElementType : childVariant) {
                    if (leafElementType instanceof IdentifierElementType) {
                        IdentifierElementType identifierElementType = (IdentifierElementType) leafElementType;
                        objectTypeCache.add(identifierElementType.getObjectType());
                    }
                }
            }
            variants.addAll(childVariants);
        }
        String separatorId = def.getAttributeValue("separator");
        separatorToken = new TokenElementType(bundle, this, separatorId, TokenElementType.SEPARATOR);
    }

    @Override
    public QualifiedIdentifierElementTypeLookupCache createLookupCache() {
        return new QualifiedIdentifierElementTypeLookupCache(this);
    }

    @NotNull
    @Override
    public QualifiedIdentifierElementTypeParser createParser() {
        return new QualifiedIdentifierElementTypeParser(this);
    }

    public List<LeafElementType[]> getVariants() {
        return variants;
    }

    private List<LeafElementType[]> createVariants(Element element) throws ElementTypeDefinitionException {
        List<LeafElementType[]> variants = new ArrayList<>();

        List children = element.getChildren();
        LeafElementType[] leafElementTypes = new LeafElementType[children.size()];
        boolean[] optional = new boolean[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            String type = child.getName();
            leafElementTypes[i] = (LeafElementType) getElementBundle().resolveElementDefinition(child, type, this);
            optional[i] = getBooleanAttribute(child, "optional");
            leafElementTypes[i].setOptional(optional[i]);
        }
        variants.add(leafElementTypes);
        if (maxLength < leafElementTypes.length) maxLength = leafElementTypes.length;

        int lastIndex = leafElementTypes.length - 1;
        int leftIndex = 0;
        int rightIndex = lastIndex;

        while (optional[rightIndex]) {
            LeafElementType[] variant = new LeafElementType[rightIndex];
            System.arraycopy(leafElementTypes, 0, variant, 0, variant.length);
            variants.add(variant);
            rightIndex--;
        }

        while (optional[leftIndex]) {
            rightIndex = lastIndex;
            LeafElementType[] variant = new LeafElementType[lastIndex - leftIndex];
            System.arraycopy(leafElementTypes, leftIndex +1, variant, 0, variant.length);
            variants.add(variant);

            while (optional[rightIndex]) {
                variant = new LeafElementType[rightIndex - leftIndex -1];
                System.arraycopy(leafElementTypes, leftIndex +1, variant, 0, variant.length);
                variants.add(variant);
                rightIndex--;
            }
            leftIndex++;
        }

        return variants;
    }

    @Override
    public PsiElement createPsiElement(ASTNode astNode) {
        /*SortedSet<QualifiedIdentifierVariant> parseVariants = getParseVariants();
        if (parseVariants != null) {
            astNode.putUserData(PARSE_VARIANTS_KEY, parseVariants);
        }*/
        return new QualifiedIdentifierPsiElement(astNode, this);
    }

    @Override
    public String getDebugName() {
        return "identifier sequence (" + getId() + ")";
    }

    /*private SortedSet<QualifiedIdentifierVariant> parseVariants;
    private SortedSet<QualifiedIdentifierVariant> getParseVariants() {
        return parseVariants;
    }
    private void setParseVariants(SortedSet<QualifiedIdentifierVariant> parseVariants) {
        this.parseVariants = parseVariants;
    }*/

    @Override
    public boolean isLeaf() {
        return false;
    }

    public TokenElementType getSeparatorToken() {
        return separatorToken;
    }

    public boolean containsObjectType(DBObjectType objectType) {
        return objectTypeCache.contains(objectType);
    }

}
