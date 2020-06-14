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
        return false;
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
        // TODO : assignment 5.4
        return false;
    }


    // search removal-substitute for node p
    private SearchResult searchNearestSmaller(Node p){
        // TODO : assignment 5.2
        return null;
    }


    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateRight(Node p){
        // TODO : assignment 5.2
    }


    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateLeft(Node p){
        // TODO : assignment 5.2
    }


    private void updateIn(Node p){
        // TODO : assignment 5.3
    }


    private void updateOut(Node p){
        // TODO : assignment 5.4
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


