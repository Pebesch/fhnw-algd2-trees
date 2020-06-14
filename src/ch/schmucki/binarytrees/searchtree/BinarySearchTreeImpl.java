package ch.schmucki.binarytrees.searchtree;

public class BinarySearchTreeImpl<T extends Comparable<T>> {
    private Node<T> root;
    private ITraverseable strategy = new TraverseInOrderLeft();
    // Ist seine Lösung mit einem Dummy Element?

    public BinarySearchTreeImpl(){
        root = null;
    }

    public BinarySearchTreeImpl(int[] sorted){
        root = buildTree(sorted, 0, sorted.length - 1);
    }

    public void show(){
        System.out.println();
        strategy.traverse(root, 0);
    }

    static class Node<T> {
        T data;
        Node<T> l;
        Node<T> r;
        public Node(T data) {
            this.data = data;
        }
    }

    public Node<T> search(Node<T> root, T value) {
        Node<T> current = root;
        if(current.data == null) throw new IllegalStateException("Root Node is empty");
        if(current.data.compareTo(value) == 0) return current; // Match
        else if(current.data.compareTo(value) == -1) search(current.l, value); // Value is smaller
        else if(current.data.compareTo(value) == 1) search(current.r, value); // Value is bigger
        return null;
    }

    public boolean insert(Node<T> root, T value) {
        if(root != null) {
            if(root.data.compareTo(value) == -1) {
                insert(root.l, value);
            } else {
                insert(root.r, value);
            }
        } else {
            root = new Node(value);
            return true;
        }
        return false;
    }

    public boolean exists(T value) {
        Node<T> currentRoot = root;
        while(currentRoot != null && currentRoot.data.compareTo(value) != 0)
        {
            if(currentRoot.data.compareTo(value) == -1)
            {
                currentRoot = currentRoot.l;
            } else {
                currentRoot = currentRoot.r;
            }
        }
        return currentRoot == null ? true : false;
    }

    private Node buildTree(int[] a, int start, int end){
        Node ret = null;
        if (start <= end){
            int M = (start + end) / 2;
            ret = new Node(a[M]);
            ret.l = buildTree(a, start, M - 1);
            ret.r = buildTree(a, M + 1, end);
        }
        return ret;
    }

}
