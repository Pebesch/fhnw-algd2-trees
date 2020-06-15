package ch.schmucki.avl;


//******************************************************************************
//  FHNW.ALGD2  -  Excercise 5 : AVL Trees                                     *
// --------------------------------------------------------------------------  *
//  version 1                                                             vtg  *
//  version 2                                                                  *
//******************************************************************************

//

public class AVL_tree_Solution{

// ***** API *******************************************************************

    public AVL_tree_Solution(){
        m_root.R = null;
        m_root.U = m_root;
    }

    public AVL_tree_Solution(int[] sorted){
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
        SearchResult r = find(key);
        if (r.node != null)
            return false;
        if (r.isLeftChild){
            r.parent.L = new Node(key);
            r.parent.L.bal = 0;
            r.parent.L.U = r.parent;
            --r.parent.bal;
        } else{
            r.parent.R = new Node(key);
            r.parent.R.bal = 0;
            r.parent.R.U = r.parent;
            ++r.parent.bal;
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
        if (r == null)
            r = find(key);
        if (r.node == null)
            return false;
        if (r.node.L == null && r.node.R == null){        // case 1 : node has no children
            if (r.isLeftChild){
                r.parent.L = null;
                ++r.parent.bal;
            } else{
                r.parent.R = null;
                --r.parent.bal;
            }
            if (r.parent.bal * r.parent.bal != 1){          // parent.bal changed to 0 or 2
                updateOut(r.parent);
            }
        }else if (r.node.L == null || r.node.R == null){  // case 2 : node has 1 child
            if (r.node.L != null){
                r.node.key = r.node.L.key;
                r.node.L = null;
            } else{
                r.node.key = r.node.R.key;
                r.node.R = null;
            }
            r.node.bal = 0;
            updateOut(r.node);
        }else{                                            // case 3 : node has 2 children
            Node n = r.node;
            r = searchNearestSmaller(r.node);
            n.key = r.node.key;
            remove(0, r);
        }
        return true;
    }


    // search removal-substitute for node p
    private SearchResult searchNearestSmaller(Node p){
        SearchResult s = new SearchResult(p, p.L, true);
        while (s.node.R != null){
            s.parent = s.node;
            s.node = s.node.R;
            s.isLeftChild = false;
        }
        return s;
    }


    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateRight(Node p){
        Node s = p.L;
        int bal = s.bal;    // save old balance
        if (p.U.L == p)
            p.U.L = s;
        else
            p.U.R = s;
        p.L = s.R;
        s.R = p;
        s.U = p.U;
        p.U = s;
        if (p.L != null)
            p.L.U = p;
        p.bal = s.bal = 0;  // update balances
        if (bal==0){
            p.bal = -1;
            s.bal =  1;
        }
    }


    // trying to raise an empty sub-tree will cause a nullpointer exception
    private void rotateLeft(Node p){
        Node s = p.R;
        int bal = s.bal;    // save old balance
        if (p.U.L == p)
            p.U.L = s;
        else
            p.U.R = s;
        p.R = s.L;
        s.L = p;
        s.U = p.U;
        p.U = s;
        if (p.R != null)
            p.R.U = p;
        p.bal = s.bal = 0;  // update balances
        if (bal==0){
            p.bal =  1;
            s.bal = -1;
        }
    }


    private void updateIn(Node p){
        Node f;
        while (p.U.U != p.U){ // while p is not root
            f = p.U;
            f.bal += (f.L == p  ?  -1  :  1);  // update parent's balance
            if (f.bal == 0){
                return;
            } else if (f.bal * f.bal == 1){ // -1 or +1
                p = f;        // cycle up with p's parent
            } else{         // severe inbalance : rotate
                if (f.bal < 0){
                    if (p.bal > 0){
                        Node r = p.R;
                        int bal = p.R.bal;
                        rotateLeft(p);
                        rotateRight(f);
                        f.bal = p.bal = r.bal = 0;
                        if (bal==1)      p.bal = -1;
                        else if(bal==-1) f.bal = 1;
                    }else{
                        rotateRight(f);
                    }
                }else{
                    if (p.bal < 0){
                        Node l = p.L;
                        int bal = p.L.bal;
                        rotateRight(p);
                        rotateLeft(f);
                        f.bal = p.bal = l.bal = 0;
                        if (bal==1)      f.bal = -1;
                        else if(bal==-1) p.bal = 1;
                    }else{
                        rotateLeft(f);
                    }
                }
                return;
            }
        }
    }


    private void updateOut(Node p){
        while (p.U != p){
            Node f = p.U;
            if (p.bal == 0){ //****************************** height of tree(p) diminished by 1
                f.bal += (f.L == p  ?  1  :  -1);  // update parent's balance
                p = f;
            }else if (p.bal * p.bal == 1){ //**************** no change of height: terminate
                return;
            }else{ //**************************************** severe inbalance : rotate
                if (p.bal < 0){
                    if (p.L.bal > 0){               // double rotate LR
                        Node lr = p.L.R;
                        Node l = p.L;
                        int bal = lr.bal;
                        rotateLeft(p.L);
                        rotateRight(p);
                        p.bal = l.bal = lr.bal = 0;
                        if (bal==1)      l.bal = -1;
                        else if(bal==-1) p.bal = 1;
                    }else{                          // rotate R
                        rotateRight(p);
                    }
                }else{
                    if (p.R.bal < 0){               // double rotate RL
                        Node rl = p.R.L;
                        Node r = p.R;
                        int bal = rl.bal;
                        rotateRight(p.R);
                        rotateLeft(p);
                        p.bal = r.bal = rl.bal = 0;
                        if (bal==1)      p.bal = -1;
                        else if(bal==-1) r.bal = 1;
                    }else{                          // rotate L
                        rotateLeft(p);
                    }
                }
                p = p.U;
            }
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



