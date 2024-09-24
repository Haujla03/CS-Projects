/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the Compare BTrees method.
*/
public class CompareBTrees
{
   /**
      Return a binary tree that is the comparison of the two input trees.
      <p>
      The comparison of two empty trees is the empty tree.
      <p>
      The comparison of an empty tree and a leaf tree is the empty tree.
      <p>
      The comparison of two leaf trees, btree1 and btree2, is a leaf tree
      of type Boolean holding the result
          btree1.root().equals(btree2.root())
      <p>
      The comparison of two binary trees is defined recursively using the
      above three cases.
      <p>
      Another way to describe the result tree is that a node of the result
      tree holds true if both input trees have that node and their contents
      are equal, and a node of the result tree holds false if both input
      trees have that node and their contents are not equal. So the result
      tree has nodes only at those positions where both input trees have
      a node. You might say that the result tree has the shape of the
      "intersection" of the two input trees (compare this with the
      addStringBTrees() method).
   */
   public static <T> BTreeAbstract<Boolean> compareBTrees(BTree<T> btree1,
                                                          BTree<T> btree2)
   {
        if (btree1.isEmpty() || btree2.isEmpty())                 // Lines 42-45 Is the test case for Empty BTree and 
        {                                                        // returns an empty btree of booleans. 
            return new EmptyBTree<Boolean>();               
        }
        
        
 BTreeNode<Boolean> newBTree = new BTreeNode<Boolean>(        // Lines 48-51 Creates a New BTree with root of btree 1 equals 
                  (btree1.root().equals(btree2.root())),              // root of btree 2. 
                       (compareBTrees(btree1.left(), btree2.left())),
                       (compareBTrees(btree1.right(), btree2.right())));
            
        
        return newBTree;                                         // Line 54 Returns the New BTree. 
              
              
 
      

   }


   // Simple test case for compareBTrees().
   public static void main(String[] args)
   {
      BTree<Integer> btree4 =                                  // Lines 66-125 Are the Test cases that prints the two btrees, 
           new LinkedBTree<>(10,                               // and prints out the compared btrees, creates dot files,
               new LinkedBTree<>(20,                          // and creates btree png. 
                   new LinkedBTree<>(40),
                   new LinkedBTree<>()), // empty tree
               new LinkedBTree<>(30,
                   new LinkedBTree<>(50),
                   new LinkedBTree<>(60)));

      BTree<Integer> btree5 =
           new LinkedBTree<>(10,
               new LinkedBTree<>(30,
                   new LinkedBTree<>(),  // empty tree
                   new LinkedBTree<>(40)),
               new LinkedBTree<>(20,
                   new LinkedBTree<>(50),
                   new LinkedBTree<>())); // empty tree

      BTree<Boolean> btree3 = compareBTrees(btree4, btree5);

      System.out.println( btree4 );
      System.out.println( btree5);
      System.out.println( btree3);
      BTree2dot.btree2dot(btree4, "btree4");
      BTree2png.btree2png("btree4");
      BTree2dot.btree2dot(btree5, "btree5");
      BTree2png.btree2png("btree5");
      BTree2dot.btree2dot(btree3, "btree3");
      BTree2png.btree2png("btree3");
      
      
       BTree<Integer> btree6 =                                
           new LinkedBTree<>(15,
               new LinkedBTree<>(95,
                   new LinkedBTree<>(),
                   new LinkedBTree<>(84)), // empty tree
               new LinkedBTree<>(42,
                   new LinkedBTree<>(79),
                   new LinkedBTree<>(57)));

      BTree<Integer> btree7 =
           new LinkedBTree<>(15,
               new LinkedBTree<>(62,
                   new LinkedBTree<>(84),  // empty tree
                   new LinkedBTree<>()),
               new LinkedBTree<>(42,
                   new LinkedBTree<>(79),
                   new LinkedBTree<>())); // empty tree

      BTree<Boolean> btree8 = compareBTrees(btree6, btree7);

      System.out.println( btree6 );
      System.out.println( btree7);
      System.out.println( btree8);
      BTree2dot.btree2dot(btree6, "btree6");
      BTree2png.btree2png("btree6");
      BTree2dot.btree2dot(btree7, "btree7");
      BTree2png.btree2png("btree7");
      BTree2dot.btree2dot(btree8, "btree8");
      BTree2png.btree2png("btree8");
   }
}
