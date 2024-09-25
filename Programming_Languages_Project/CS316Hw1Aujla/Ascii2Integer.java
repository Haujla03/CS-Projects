/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the ascii2int() method.
*/
public class Ascii2Integer
{
   /**
      A method that converts a binary tree of integer strings
      into a bnary tree of integers. This method assumes that
      all of the strings in the input tree contain properly
      formatted integers.
   */
   public static LinkedBTree<Integer> ascii2int(BTree<String> btree)
   {
     
     if(btree.isEmpty())                          // Lines 25-28 Is the Empty BTree case and returns a new btree of 
       {                                         // integers.
          return new LinkedBTree<Integer>();
       }
     
                                                                  // Lines 31-33 Creates a new BTree of integers, and converts
       LinkedBTree<Integer> newBTree = new LinkedBTree<Integer>(Integer.parseInt(btree.root()),   // btree of strings into btree
                                                (ascii2int(btree.left())),                       // of integers. 
                                                (ascii2int(btree.right())));
       
       return newBTree;
       
                                                                          // Line 35 Returns the New BTree.
   }


   // A simple test case for ascii2int().
   public static void main(String[] args)
   {
      BTree<String> btree24 =                                 // Lines 44-73 Are the Test Cases and prints the btree of strings, 
           new LinkedBTree<>("1",                            // btree of integers, creates a dot file, and creates a btree
               new LinkedBTree<>("12"),                     // png. 
               new LinkedBTree<>("123"));

      BTree<Integer> btree25 = ascii2int( btree24 );

      System.out.println( btree24 );
      BTree2dot.btree2dot(btree24, "btree24");
      BTree2png.btree2png("btree24");

      System.out.println( btree25 );
      BTree2dot.btree2dot(btree25, "btree25");
      BTree2png.btree2png("btree25");
      
      
       BTree<String> btree26 =
           new LinkedBTree<>("137",
               new LinkedBTree<>("57"),
               new LinkedBTree<>("8423"));

      BTree<Integer> btree27 = ascii2int( btree26 );

      System.out.println( btree26 );
      BTree2dot.btree2dot(btree26, "btree26");
      BTree2png.btree2png("btree26");

      System.out.println( btree27 );
      BTree2dot.btree2dot(btree27, "btree27");
      BTree2png.btree2png("btree27");
      
      
      
   }
}
