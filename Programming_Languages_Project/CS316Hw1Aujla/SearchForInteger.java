/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

import java.util.List;
import java.util.ArrayList;

/**
   A program that tests the searchForInteger() method.
*/
public class SearchForInteger
{
   /**
      Search a binary tree of integers for a target integer
      and return a list of all the addresses in the tree
      where the target integer is located.
      <p>
      An "address" is a string made up of the characters 'r',
      'L' and 'R'. The root of a tree has address "r". If a
      node in the tree has address adr, then that node's left
      child has address adr+"L" and that node's right child
      has address adr+"R" (notice how that is a recursive
      definition of an address).
   */
   private static List<String> searchForInteger(int target,
                                                BTree<Integer> btree)
   {
      return searchForInteger(target, btree, "r");
   }


   /**
      Search a binary tree of integers for a target integer
      and return a list of all the addresses in the tree
      where the target integer is located.
   */
   private static List<String> searchForInteger(int target,
                                                BTree<Integer> btree,
                                                String rootAddress)
   {
     
     List<String> newList = new ArrayList<>();           // Line 48 Creates a New Array List. 
      
      if ( btree.isEmpty() )                           // Lines 50-53 Checks if the btree is empty and returns an empty
      {                                               // array list. 
         return newList;
      }
      
      if(btree.root() == target)                    // Lines 55-58 Checks if the root of the btree is equal to the target
      {                                             // value and adds the root address to the array list. 
        newList.add(rootAddress);
      }
      
      newList.addAll(searchForInteger(target, btree.left(), rootAddress + "L"));    // Lines 60-61 Adds the branche's addresses
      newList.addAll(searchForInteger(target, btree.right(), rootAddress + "R"));  // to the nodes in a recursive method. 
                     
      
      return newList;                                                 // Line 64 Returns the New Array List. 
    


   }


   // Test the searchForInteger() method.
   public static void main(String[] args)
   {
      BTree<Integer> btree9 =                                  // Lines 74-133 Is the Test Case that prints out the Btree,
           new BTreeLinked<>(2,                              // the addressess, creates a dot file, and creates a list 
               new BTreeLinked<>(0,                         // png. 
                   new BTreeLinked<>(0,
                       new BTreeLinked<>(0,
                           new BTreeLinked<>(0),
                           new BTreeLinked<>(0)),
                       new BTreeLinked<>(1,
                           new BTreeLinked<>(0),
                           new BTreeLinked<>(0))),
                   new BTreeLinked<>(0,
                       new BTreeLinked<>(0),
                       new BTreeLinked<>(0))),
               new BTreeLinked<>(0,
                   new BTreeLinked<>(0),
                   new BTreeLinked<>(0,
                       new BTreeLinked<>(0,
                           new BTreeLinked<>(0,
                               new BTreeLinked<>(0,
                                   new BTreeLinked<>(0),
                                   new BTreeLinked<>(0)),
                               new BTreeLinked<>(1,
                                   new BTreeLinked<>(0),
                                   new BTreeLinked<>(0))),
                           new BTreeLinked<>(0,
                               new BTreeLinked<>(0,
                                   new BTreeLinked<>(2),
                                   new BTreeLinked<>(0)),
                               new BTreeLinked<>(0,
                                   new BTreeLinked<>(0),
                                   new BTreeLinked<>(1)))),
                       new BTreeLinked<>(0))));

      System.out.println( btree9 );
      BTree2dot.btree2dot(btree9, "btree9");
      BTree2png.btree2png("btree9");
      List<String> addresses = searchForInteger(1, btree9);
      System.out.println( addresses );
      List<String> addresses2 = searchForInteger(2, btree9);
      System.out.println( addresses2 );
      
      
      BTree<Integer> btree10 =
           new LinkedBTree<>(15,
               new LinkedBTree<>(0,
                   new LinkedBTree<>(84),  // empty tree
                   new LinkedBTree<>(0)),
               new LinkedBTree<>(84,
                   new LinkedBTree<>(0),
                   new LinkedBTree<>(15))); // empty tree
      
      
      
      System.out.println( btree10 );
      BTree2dot.btree2dot(btree10, "btree10");
      BTree2png.btree2png("btree10");
      List<String> addresses3 = searchForInteger(15, btree10);
      System.out.println( addresses3 );
      List<String> addresses4 = searchForInteger(84, btree10);
      System.out.println( addresses4 );
      
      
      
   }
}
