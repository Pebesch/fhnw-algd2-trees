package ch.schmucki.binarytrees.searchtree;

public class TraverseDisplay implements ITraverseable {
    public void traverse(BinarySearchTreeImpl.Node root, int level){
        if (root != null){
            traverse(root.r, level + 1);
            for (int i = 0; i < level; ++i)
                System.out.print("    ");
            System.out.println(root.data);
            traverse(root.l, level + 1);
        }
    }
}
