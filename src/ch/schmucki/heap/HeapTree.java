package ch.schmucki.heap;

public class HeapTree<T extends Comparable> {
    private Element<T> root;
    private int size;

    public HeapTree() {
        this.root = new Element(Integer.MAX_VALUE, null);
    }

    static class Element<T extends Comparable> {
        int priority;
        T data;
        Element<T> L, R, UP;

        public Element(int priority, T data) {
            this.priority = priority;
            this.data = data;
        }
    }

    /** API **/

    // Returns element with highest priority, without deleting it.
    public T get() {
        return root.R.data;
    }

    public int size() {
        return size;
    }

    // Returns element with highest priority and also deletes it.
    public T remove() {
        // Copy top element
        Element<T> result = new Element<>(1, root.R.data);

        // Replace top element with lowest Element
        root.R = findLast();
        size--;

        // Bubble down Element to correct position
        bubbleDown(root.R);

        return result.data;
    }

    public void insert(int priority, T data) {
        size++;
    }

    public void show() {

    }

    /** Helper Methods **/

    private Element<T> find(int position) {
        Element current = root.R;
        int mask = Integer.highestOneBit(position);
        while (mask > 1) {
            mask >>>= 1;
            if ((position & mask) == 0) {
                current = current.L;
            } else {
                current = current.R;
            }
        }
        return current;
    }

    private Element<T> findLast() {
        return find(size() - 1);
    }

    private Element<T> bubbleDown(Element<T> element) {
        Element<T> current = element;
        while(current.data.compareTo(current.L.data) <= 0|| current.data.compareTo(current.R.data) <= 0) {
            if(current.data.compareTo(current.L.data) <= 0) {
                swapElements(current, current.L, true);
            }
            if(current.data.compareTo(current.R.data) <= 0) {
                swapElements(current, current.R, false);
            }
        }
        return current;
    }

    private void swapElements(Element<T> parent, Element<T> child, boolean isLeft) {
        Element<T> temp;
        if(isLeft) {
            temp = child.L;
            child.L = parent;
            parent.L = temp;
        } else {
            temp = child.R;
            child.R = parent;
            parent.R = temp;
        }

    }
}
