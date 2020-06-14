package ch.schmucki.binarytrees.searchtree;

public class TraverseInOrderRight implements ITraverseable {

    @Override
    public void traverse(BinarySearchTreeImpl.Node root, int level) {
        if(root != null && root.data != null) {
            level++;
            traverse(root.r, level);
            System.out.println(root.data + " at level " + level + ".");
            traverse(root.l, level);
        }
    }
}
