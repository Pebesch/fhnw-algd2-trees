package ch.schmucki.avl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVL_treeTest {

    @org.junit.jupiter.api.Test
    void checkBalanceFactors() {
    }

    @Test
    void checkRightRotation() {
        AVL_tree tree = new AVL_tree();
        tree.insert(3);
        tree.insert(2);
        tree.insert(1);
    }

    @Test
    void checkLeftRotation() {
        AVL_tree tree = new AVL_tree();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
    }
}