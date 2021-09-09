package com.test.app;

import java.util.List;

public class DBModel extends Model {

    @Override
    void init() {
        this.root = new Node(
            new Node(),
            new Node(
                new Node(),
                new Node(
                    new Node(),
                    new Node(),
                    new Node(),
                    new Node(
                        new Node(),
                        new Node(),
                        new Node()
                    )
                ),
                new Node(
                    new Node(),
                    new Node(),
                    new Node()
                )
            ),
            new Node(),
            new Node(
                new Node(),
                new Node()
            )
        );
        deepAdd(root);
    }

    private void deepAdd(Node node) {
        int index = size() + 1;
        node.setId(index);
        node.setValue("Node " + index);
        put(index, node);
        node.forEach(child -> {
            child.setParentId(node.getId());
            deepAdd(child);
        });
    }

    void update(List<Node> nodes) {
        nodes.stream()
            .filter(this::isNodeAlive)
            .forEach(n -> {
                Node node = computeIfAbsent(n.getId(), id -> {
                    Node newNode = new Node();
                    newNode.setId(n.getId());
                    newNode.setParentId(n.getParentId());
                    Node parent = get(n.getParentId());
                    if (parent == null) {
                        return null;
                    }
                    parent.add(newNode);
                    return newNode;
                });
                if (node != null) {
                    node.setDeleted(n.isDeleted());
                    node.setValue(n.getValue());
                }
            });
        fireModelChanged();
    }
}
