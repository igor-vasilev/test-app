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

    String push(Node node) {
        if (containsKey(node.getId())) {
            return "Selected node is already in the cache";
        }

        // unless node is adding to deleted parent
        if (isNodeAlive(node)) {

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

            fireModelChanged();
            return null;
        } else {
            return "Node cannot be added to removed parent";
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

    void removeNodes(List<Node> nodes) {
        nodes.forEach(n -> get(n.getId()).setDeleted(true));
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
