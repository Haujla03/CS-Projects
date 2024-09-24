/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the integer2Boolean() method.
*/
public class Integer2Boolean
{
   /**
      A method that converts a binary tree of integer values
      into a bnary tree of booleans, true for even integers
      and false for odd integers.
   */
   public static BTreeLinked<Boolean> integer2Boolean(BTree<Integer> btree)
   {
      if ( btree.isEmpty() )
      {                                                   // Lines 23-26 Is the Empty BTree case and returns an empty 
         return new BTreeLinked<Boolean>();              // btree of booleans. 
      }

     
   BTreeLinked<Boolean> newBTree = new BTreeLinked<Boolean>(btree.root() % 2 == 0,
                                   (integer2Boolean(btree.left())),                      // Lines 29-31 Creates a New BTree
                                   (integer2Boolean(btree.right())));         // of booleans with true for even integers
                                                                             //  and false for odd integers. 
                                                     
      return newBTree;
                                                                           // Line 34 Returns the New BTree. 
  
       

   }


   // A simple test case for integer2Boolean().
   public static void main(String[] args)
   {
      BTree<Integer> btreeB =                             // Lines 45-80 Are the Test Cases that prints the btree, the 
           new LinkedBTree<>(1,                          // converted btree, creates a dot file, and creates a btree 
               new LinkedBTree<>(12),                   // png.  
               new LinkedBTree<>(123));

      BTree<Boolean> btreeC = integer2Boolean( btreeB );

      System.out.println( btreeB );
      BTree2dot.btree2dot(btreeB, "btreeB");
      BTree2png.btree2png("btreeB");

      System.out.println( btreeC );
      BTree2dot.btree2dot(btreeC, "btreeC");
      BTree2png.btree2png("btreeC");
      
      BTree<Integer> btreeG =
           new LinkedBTree<>(36,
               new LinkedBTree<>(368,
                   new LinkedBTree<>(88),
                   new LinkedBTree<>(136)),
               new LinkedBTree<>(13567,
                   new LinkedBTree<>(784),
                   new LinkedBTree<>(4,
                       new LinkedBTree<>(682),
                       new LinkedBTree<>(871))));
      
      
      BTree<Boolean> btreeH = integer2Boolean( btreeG );
      
      System.out.println( btreeG );
      BTree2dot.btree2dot(btreeG, "btreeG");
      BTree2png.btree2png("btreeG");

      System.out.println( btreeH );
      BTree2dot.btree2dot(btreeH, "btreeH");
      BTree2png.btree2png("btreeH");
      
      
   }
}
