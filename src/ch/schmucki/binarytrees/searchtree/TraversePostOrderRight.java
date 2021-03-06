package ch.schmucki.binarytrees.searchtree;

public class TraversePostOrderRight implements ITraverseable {

    @Override
    public void traverse(BinarySearchTreeImpl.Node root, int level) {
        if(root != null && root.data != null) {
            level++;
            traverse(root.r, level);
            traverse(root.l, level);
            System.out.println(root.data + " at level " + level + ".");
        }
    }
}
