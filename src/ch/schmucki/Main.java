package ch.schmucki;

import ch.schmucki.binarytrees.searchtree.BinarySearchTreeImpl;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("Test");
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        BinarySearchTreeImpl impl = new BinarySearchTreeImpl(arr);
        impl.show();
    }
}
