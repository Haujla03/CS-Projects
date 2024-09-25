/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the isBalanced() method.
*/
public class IsBalanced
{
   /**
      This method defines a balanced binary tree as one whose
      left branch has the same size as the right branch.
   */
   public static <T> boolean isBalanced(BTree<T> btree)
   {
      if(btree.isEmpty())                                // Lines 22-25 Is the Empty BTree case and returns true for 
      {                                                 // a balanced btree. 
        return true;
      }
     else                                                    // Lines 26-29 Returns the size of the left and right branch
     {                                                       // of the binary tree and returns true if branches equal 
      return  btree.left().size() == btree.right().size();   // and false if branches does not equal. 
     }
       

   }


   // A simple test case for isBalanced().
   public static void main(String[] args)
   {
      BTree<Integer> btreeA =                                 // Lines 38-56 Are the Test Cases that prints out the 
           new LinkedBTree<>(1,                              // btree, whether the btree is balanced, creates a dot file
               new LinkedBTree<>(12),                       // of btree, and creates a btree png. 
               new LinkedBTree<>(123));

      System.out.println( btreeA );
      System.out.println("isBalanced : " + isBalanced(btreeA) );
      BTree2dot.btree2dot(btreeA, "btreeA");
      BTree2png.btree2png("btreeA");
    
      BTree<Integer> btree2 =
           new LinkedBTree<>(1,
               new LinkedBTree<>(),
               new LinkedBTree<>(3518));
      
      System.out.println( btree2 );
      System.out.println("isBalanced : " + isBalanced(btree2) );
      BTree2dot.btree2dot(btree2, "btree2");
      BTree2png.btree2png("btree2");

   
   }
}
