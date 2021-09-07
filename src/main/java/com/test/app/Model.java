package com.test.app;

import java.util.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public abstract class Model extends LinkedHashMap<Integer, Node> implements TreeModel {
    Node root;
    private ArrayList<TreeModelListener> listeners = new ArrayList<>();

    Model() {
        init();
    }
    
    abstract void init();

    void reset() {
        clear();
        init();
        fireModelChanged();
    }

    // node's considered dead if its parent is deleted
    // otherwise it's alive
    boolean isNodeAlive(Node node) {
        Node parent = get(node.getParentId());
        return parent == null || !parent.isDeleted();
    }

    // below UI stuff for rendering the model

    void fireModelChanged() {
        listeners.forEach(l -> l.treeStructureChanged(new TreeModelEvent(this, new Object[] {root})));
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((Node)parent).get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((Node)parent).size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((Node)parent).indexOf(child);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((Node)node).isEmpty();
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}
