package ch.schmucki.avl;

//******************************************************************************
//  FHNW.ALGD2  -  Excercise 5 : Testing AVL Trees                             *
// --------------------------------------------------------------------------  *
//  version 1  (2011-10-19)                                               vtg  *
//  version 2  (2013-10-03)   new from scratch                                 *
//******************************************************************************

public class main_AVL{

    // operation modes
    static final boolean MASS_OPERATION = true;      // mass tests or detail tests switch
    static final boolean SHOW_TREES_INSERT = false;  // for detail mode only
    static final boolean SHOW_TREES_REMOVE = false;  // for detail mode only

    // values for mass tests
    static final int TREE_SIZE = 2500;
    static int[] set = new int[TREE_SIZE];

    // key arrays for detail tests
    static int[] ki = {16, 14,  0,  1, 17,  5,  9, 18,  8,  6, 10, 13,  4,  3,  2, 19, 12, 15,  7, 11};
    static int[] kd = {10, 17, 18, 13,  6, 16,  0,  1, 14, 19,  8,  7,  5,  3, 15,  9,  2,  4, 11, 12};

    // our test object...
    private static AVL_tree t;


    public static void main(String[] blah){

        t = new AVL_tree();

        //***** inserting nodes ******************************************************
        if (MASS_OPERATION){
            prepareKeySet();
            massInserterTest();
            System.out.println("-------------------------");
        }else{
            for (int i=0; i<ki.length; ++i){
                if (SHOW_TREES_INSERT){
                    System.out.print("insert: " + String.format("%3d", ki[i]) + " ");
                    System.out.println("-----------------------");
                }
                t.insert(ki[i]);
                if (SHOW_TREES_INSERT){
                    t.show();
                }
            }
            if (!SHOW_TREES_INSERT){
                t.show();
            }
            System.out.println(" > Initializer : balanced = " + t.checkBalanceFactors());
            System.out.println("----------------------------------------------------");
            System.out.println("----------------------------------------------------");
        }

        //***** deleting nodes *******************************************************
        if (MASS_OPERATION){
            prepareKeySet();
            massDeleterTest();
        }else{
            for (int i=0; i<kd.length; ++i){
                System.out.println("remove " + kd[i] + "  ");
                boolean res = t.remove(kd[i]);
                if (res){
                    if (SHOW_TREES_REMOVE){
                        t.show();
                    }
                }else{
                    System.out.println("  rejected");
                }
                System.out.println(" > Balance " + (t.checkBalanceFactors()  ? "ok"  :  "corrupted"));
                System.out.println("----------------------------------------------------");
            }
        }
    }

//******************************************************************************************
//***** auxiliaries ************************************************************************
//******************************************************************************************


    public static void prepareKeySet(){
        int index1, index2, swap;
        for (int i=0; i<set.length; ++i)
            set[i] = i;
        for (int i=0; i<set.length; ++i){
            index1 = (int)(Math.random() * set.length);
            index2 = (int)(Math.random() * set.length);
            swap = set[index1];
            set[index1] = set[index2];
            set[index2] = swap;
        }
    }


    public static void massInserterTest(){
        int successful = 0;
        int rejected   = 0;
        int bal_OK     = 0;
        int bal_Fail   = 0;
        boolean ok;
        for (int i=0; i<TREE_SIZE; ++i){
            ok = t.insert(set[i]);
            if (ok){
                ++successful;
            }else{
                ++rejected;
            }
            if (t.checkBalanceFactors()){
                ++bal_OK;
            }else{
                ++bal_Fail;
            }
        }
        System.out.println();
        System.out.println("Successful Inserts :" + String.format("%5d", successful));
        System.out.println("Rejected Inserts   :" + String.format("%5d", rejected));
        System.out.println("Balance is OK      :" + String.format("%5d", bal_OK));
        System.out.println("Balance failed     :" + String.format("%5d", bal_Fail));
    }


    public static void massDeleterTest(){
        int successful = 0;
        int rejected   = 0;
        int bal_OK     = 0;
        int bal_Fail   = 0;
        boolean ok = false;
        int key = 0;
        for (int i=0; i<TREE_SIZE; ++i){
            try{
                ok = t.remove(set[i]);
            }catch (Exception e){
                System.out.println("at index " + i + " , key = " + key);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                t.show();
            }
            if (ok){
                ++successful;
            }else{
                ++rejected;
            }
            if (t.checkBalanceFactors()){
                ++bal_OK;
            }else{
                ++bal_Fail;
            }
        }
        System.out.println();
        System.out.println("Successful Deletes :" + String.format("%5d", successful));
        System.out.println("Rejected Deletes   :" + String.format("%5d", rejected));
        System.out.println("Balance is OK      :" + String.format("%5d", bal_OK));
        System.out.println("Balance failed     :" + String.format("%5d", bal_Fail));
    }

}