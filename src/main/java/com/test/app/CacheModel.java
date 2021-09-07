package com.test.app;

import javax.swing.event.TreeModelEvent;
import java.util.stream.Collectors;
import java.util.*;

public class CacheModel extends Model {
    private int nextId;

    @Override
    void init() {
        // dummy root, not visible to user
        root = new Node();
        nextId = 0;
    }

    void push(Node node) {
        // unless node is adding to deleted parent
        if (isNodeAlive(node)) {

            Node hit = get(node.getId());
            if (hit != null) {
                // node is already in the cache
                // update its state
                hit.setDeleted(false);
                hit.setValue(node.getValue());
            } else {
                // add node to cache
            
                // search for possible children and transfer ownership from root to the new node
                Iterator<Node> children = root.iterator();
                while (children.hasNext()) {
                    Node child = children.next();
                    if (child.getParentId() == node.getId()) {
                        node.add(child);
                        children.remove();
                    }
                }

                // add to root if parent isn't found in the cache
                getOrDefault(node.getParentId(), root)
                    .add(node);
            
                put(node.getId(), node);
            }
            fireModelChanged();
        }
    }

    void createNode(Node parent, String value) {
        Node node = new Node(--nextId, parent.getId(), value, false);
        parent.add(node);
        put(nextId, node);
        fireModelChanged();
    }

    void deleteNode(Node node) {
        node.setDeleted(true);
        fireModelChanged();
    }

    void setNodeValue(Node node, String newValue) {
        node.setValue(newValue);
        fireModelChanged();
    }

    List<Node> makeCopy() {
        return values().stream()
            // make db's life easy by discarding all nodes from a deleted branch except its root
            .filter(this::isNodeAlive)
            // copy node
            .map(Node::new)
            .collect(Collectors.toList());
    }
}
