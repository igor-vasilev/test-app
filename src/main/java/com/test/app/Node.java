package com.test.app;

import java.util.Arrays;
import java.util.ArrayList;

public class Node extends ArrayList<Node> {
    private int id;
    private int parentId;
    private String value;
    private boolean deleted;

    Node(int id, int parentId, String value, boolean deleted) {
        this.id = id;
        this.parentId = parentId;
        this.value = value;
        this.deleted = deleted;
    }
    
    // copy constructor
    Node(Node node) {
        this(node.id, node.parentId, node.value, node.deleted);
    }

    Node(Node... children) {
        super(Arrays.asList(children));
    }

    void setId(int id) {
        this.id = id;
    }

    int getId() {
        return id;
    }

    void setValue(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }

    void setParentId(int parentId) {
        this.parentId = parentId;
    }

    int getParentId() {
        return parentId;
    }

    void setDeleted(boolean deleted) {
        if (this.deleted != deleted) {
            this.deleted = deleted;
            if (deleted) {
                forEach(child -> child.setDeleted(true));
            }
        }
    }

    boolean isDeleted() {
        return deleted;
    }


    @Override
    public String toString() {
        return deleted?("(" + value + ")"):value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return ((Node)o).id == id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }
    
}

