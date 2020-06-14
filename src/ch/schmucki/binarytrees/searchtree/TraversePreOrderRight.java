package ch.schmucki.binarytrees.searchtree;

public class TraversePreOrderRight implements ITraverseable {

    @Override
    public void traverse(BinarySearchTreeImpl.Node root, int level) {
        if(root != null && root.data != null) {
            level++;
            System.out.println(root.data + " at level " + level + ".");
            traverse(root.r, level);
            traverse(root.l, level);
        }
    }
}
