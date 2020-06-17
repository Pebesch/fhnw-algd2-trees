package ch.schmucki.avl;

//******************************************************************************
//  FHNW.ALGD2  -  Excercise 5 : AVL Trees                                     *
// --------------------------------------------------------------------------- *
//  vorgegebene Elemente                                                       *
//******************************************************************************


public class AVL_tree{

// ***** API *******************************************************************

    public AVL_tree(){
        m_root.R = null;
        m_root.U = m_root;
    }

    public AVL_tree(int[] sorted){
        this();
        m_root.R = buildTree(sorted, 0, sorted.length - 1, m_root);
        updateBalances(m_root.R);
    }

    public void show(){
        System.out.println();
        traverse(m_root.R, 0);
    }

    public boolean exists(int key){
        Node r = m_root.R;
        while (r != null){
            if (r.key == key)
                return true;
            r = key > r.key ? r.R : r.L;
        }
        return false;
    }

    public boolean insert(int key){
        // TODO : assignment 5.3
        SearchResult r = find(key);
        if(r.node != null) {
            // existiert bereits
            return false;
        }
        if(r.isLeftChild) {
            r.parent.L = new Node(key);
            r.parent.L.bal = 0;
            r.parent.L.U = r.parent;
            r.parent.bal--;
        } else {
            r.parent.R = new Node(key);
            r.parent.R.bal = 0;
            r.parent.R.U = r.parent;
            r.parent.bal++;
        }
        if (r.parent.bal != 0)
            updateIn(r.parent);
        return true;
    }



    public boolean remove(int key){
        return remove(key, null);
    }

    public boolean checkBalanceFactors(){
        return balanceInfoIsCorrect(m_root.R);
    }


// *****************************************************************************
// ***** auxiliaries ***********************************************************
// *****************************************************************************

    private Node buildTree(int[] a, int start, int end, Node parent){
        Node ret = null;
        if (start <= end){
            int M = (start + end) / 2;
            ret = new Node(a[M]);
            ret.U = parent;
            ret.bal = 0;
            ret.L = buildTree(a, start, M - 1, ret);
            ret.R = buildTree(a, M + 1, end, ret);
        }
        return ret;
    }

    // used to show a tree semigraphically
    private void traverse(Node root, int level){
        if (root != null){
            traverse(root.R, level + 1);
            for (int i = 0; i < level; ++i)
                System.out.print("        ");
            System.out.print("[");
            System.out.format("%1$03d ", root.key);
            if (root.bal != 0)
                System.out.format("%1$+2d", root.bal);
            else
                System.out.print("\u00B70");
            System.out.print("]");
            System.out.println();
            traverse(root.L, level + 1);
        }
    }


    private SearchResult find(int key){
        SearchResult res = new SearchResult(m_root, m_root.R, false);
        while (res.node != null){
            if (res.node.key == key)
                return res;
            res.parent = res.node;
            if (key > res.node.key){
                res.node = res.node.R;
                res.isLeftChild = false;
            } else{
                res.node = res.node.L;
                res.isLeftChild = true;
            }
        }
        return res;
    }


    // this method is used when a tree is generated from a sorted Array
    private int updateBalances(Node n){
        if (n != null){
            int left = updateBalances(n.L);
            int rght = updateBalances(n.R);
            n.bal = rght - left;
            return 1 + (left > rght ? left : rght);
        }else{
            return 0;
        }
    }


    // removes node using optimized search, if starting node is known (pointed by r)
    private boolean remove(int key, SearchResult r){
        if(r == null) {
            r = find(key);
        }
        // TODO : assignment 5.4
        if(r.node.L == null && r.node.R == null) {
            // p has no children
            // delete p
            // p was left son > f.bal +1, p was right son f.bal -1
            // |f.bal| = 2 > update out(f)
            r.node = null;
            if(r.isLeftChild) {
                r.parent.L = null;
                r.parent.bal += 1;
            } else {
                r.parent.R = null;
                r.parent.bal -= 1;
            }
            if(Math.abs(r.parent.bal) == 2) {
                updateOut(r.parent);
            }
        } else if(r.node.L != null && r.node.R != null) {
            // p has two children
            // searchNearest smallest r
            // replace p with r and call delete(r)
            SearchResult ns = searchNearestSmaller(r.node);
            r.node = ns.node;
            remove(0, r);
        } else {
            // p has one child
            // delete p, p's son will take it's place, bal p' = 0
            //update out(p')
            r.node = r.node.L != null ? r.node.L : r.node.R;
            r.node.bal = 0;
            updateOut(r.node);
        }
        return true;
    }


    // search removal-substitute for node p
    private SearchResult searchNearestSmaller(Node p){
        // TODO : assignment 5.2
        // Node is at least l of current node
        SearchResult s = new SearchResult(p, p.L, true);
        while (s.node.R != null) {
            // while we can go r
            s.parent = s.node;
            s.node = s.node.R;
            s.isLeftChild = false;
        }
        return s;
    }

    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateRight(Node p){
        // TODO : assignment 5.2
        /**
         *   c
         *  b
         * a
         * - b becomes new root
         * - c takes ownership of b's right child as left
         * - b takes ownership of c as it's right child
         *  b
         * a c
         */
        Node newRoot = p.L;
        int bal = newRoot.bal;
        if(p.U.L == p) {
            // Case we are working on the root
            p.U.L = newRoot;
        } else {
            p.U.R = newRoot; // b becomes new root
            p.L = newRoot.R; // c takes ownership of b's right child as left
            newRoot.R = p; // b takes ownership of c as it's right child
            p.U = newRoot; // b becomes c's parent
        }
        if(p.L != null) {
            p.L.U = p;
        }
        p.bal = newRoot.bal = 0;
        if(bal == 0) {
            p.bal = -1;
            newRoot.bal = 1;
        }
    }

    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateLeft(Node p){
        /**
         * a
         *  b
         *    c
         * - b becomes new root
         * - a takes ownership of b's left child as it's right child
         * - b takes ownership of a as it's left child
         *  b
         * a c
         */
        // TODO : assignment 5.2
        Node newRoot = p.R;
        int bal = newRoot.bal;
        if(p.U.R == p) {
            p.U.R = newRoot;
        } else {
            p.U.L = newRoot; // b becomes new root
            p.R = newRoot.L; // a takes ownership of b's left child as it's right child
            newRoot.L = p; // a takes ownership of b's left child as it's right child
            p.U = newRoot; // b becomes a's parent
        }
        if(p.R != null) {
            p.R.U = p;
        }
        p.bal = newRoot.bal = 0;
        if(bal == 0) {
            p.bal = 1;
            newRoot.bal = -1;
        }
    }


    private void updateIn(Node p){
        // TODO : assignment 5.3
        // Call this method if p did not have children before that
        Node f; // father
        while (p.U.U != p.U) {
            f = p.U; // Father is one up
            f.bal += (f.L == p ? -1 : 1); // Update parents balance
            if(p.bal == 0) {
                return; // Balance is zero, no problem
            } else if(f.bal * f.bal == 1) {
                p = f;
            } else {
                if(f.bal > 0) {
                    // at least left rotate
                    if(p.bal < 0) {
                        // right left rotate
                        Node l = p.L;
                        int bal = p.L.bal;
                        rotateRight(p);
                        rotateLeft(f);
                        f.bal = p.bal = l.bal = 0;
                        if(bal == 1) f.bal = -1;
                        else if (bal == -1) p.bal = -1;
                    } else {
                        rotateLeft(f);
                    }
                } else {
                    // at least right rotate
                    if(p.bal > 0) {
                        // left right rotate
                        Node r = p.R;
                        int bal = p.R.bal;
                        rotateLeft(p);
                        rotateRight(f);
                        f.bal = p.bal = r.bal = 0;
                        if(bal == 1) p.bal = -1;
                        else if (bal == -1) f.bal = 1;
                    } else {
                        rotateRight(f);
                    }
                }
            }


        }
    }


    private void updateOut(Node p){
        // TODO : assignment 5.4
        Node f;
        while (p.U.U != p.U) {
            f = p.U;

        }
    }


    // compute depth of a tree : for testing issues only
    private int getDepth(Node root){
        if (root==null){
            return 0;
        }else{
            return 1 + Math.max(getDepth(root.L), getDepth(root.R));
        }
    }


    //check balance of a certain node : for testing issues only
    private boolean balanceInfoIsCorrect (Node node){
        if (node==null){
            return true;
        }else{
            return (getDepth(node.R) - getDepth(node.L) == node.bal)
                    && balanceInfoIsCorrect(node.R) && balanceInfoIsCorrect(node.L);
        }
    }

// *****************************************************************************
// ***** attributes, constants & nested classes ********************************
// *****************************************************************************

    private Node m_root = new Node(Integer.MIN_VALUE);


    private static class Node{
        int key;
        int bal;
        Node L;
        Node R;
        Node U;
        Node(int key){  this.key = key;  }
    }


    private static class SearchResult{
        Node node;
        Node parent;
        boolean isLeftChild;

        SearchResult(Node parent, Node result, boolean left){
            this.node = result;
            this.parent = parent;
            this.isLeftChild = left;
        }
    }

}


