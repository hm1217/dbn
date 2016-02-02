package com.dci.intellij.dbn.database.sqlite.adapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dci.intellij.dbn.database.common.util.ResultSetAdapter;

public class SqliteResultSetAdapter<T extends ResultSetElement> extends ResultSetAdapter{
    private List<T> elements = new ArrayList<T>();
    private int cursor = -1;


    @Override
    public boolean next() throws SQLException {
        cursor++;
        return cursor < elements.size();
    }

    public T getCurrentElement() {
        return elements.get(cursor);
    }

    public void addElement(T element) {
        elements.add(element);
    }

    public List<T> getElements() {
        Collections.sort(elements);
        return elements;
    }

    public void reset() {
        cursor = -1;
    }

    public T getElement(String name) {
        for (T element : elements) {
            if (element.getName().equalsIgnoreCase(name)) {
                return element;
            }
        }
        return null;
    }

    public static String toFlag(boolean value) {
        return value ? "Y" : "N";
    }
}