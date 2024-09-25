/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the addStringBTrees() and addIntegerBTrees() methods.
*/

/***class Node
{                                     // Class Node is Defined later in the program, in the Public class BTrees. 
    int data;
    Node left, right;
     
    public Node(int data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }}
*/
public class AddBTrees
{
   /**
      Return a binary tree that is the "sum" of the two
      input trees of type String.
      <p>
      The sum of two empty trees is the empty tree.
      <p>
      The sum of an empty tree and a tree of size 1 (a leaf tree)
      is the leaf tree.
      <p>
      The sum of two trees of size 1 (two leaf trees) is a tree
      of size 1 (a leaf tree) whose data element is the sum of
      the data elements from the two leaf trees.
      <p>
      The sum of two binary trees is defined recursively using the
      above three cases.
      <p>
      Notice that the result tree has a node at any position where
      at least one of the two input trees has a node. You might say
      that the result tree has the shape of the "union" of the two
      input trees (compare with the compareBTrees() method).
   */
   public static BTreeAbstract<String> addStringBTrees(BTree<String> btree1,
                                                       BTree<String> btree2)
   {
        if (btree1.isEmpty() && btree2.isEmpty())                    // Lines 52-55 Is the Empty Btree case and returns
        {                                                           // an empty Btree of strings. 
            return new EmptyBTree<String>();
        }
        
        
        
       if( btree1.isEmpty())                            // Lines 59-71 Is the case of a sum of an empty tree and a leaf 
        {                                                       // tree and returns a leaf tree. 
          return new BTreeNode<String>(btree2.root(),
                             addStringBTrees(new EmptyBTree<String>(), btree2.left()),
                             addStringBTrees(new EmptyBTree<String>(), btree2.right()));
        }
  
      else if(btree2.isEmpty())
        {
          return new BTreeNode<String>(btree2.root(),
                             addStringBTrees(new EmptyBTree<String>(), btree2.left()),
                             addStringBTrees(new EmptyBTree<String>(), btree2.right()));
        }

       else
       {                                                             // Lines 74-78 Is the sum of two leaf trees and returns
         return new BTreeNode<String>(btree1.root() + btree2.root(),                //  a leaf tree with the sum of the elements of
                                addStringBTrees(btree1.left(), btree2.left()),      // the two leaf trees. 
                                addStringBTrees(btree1.right(), btree2.right()));
       }
  


  }


   /**
      Return a binary tree that is the "sum" of the two
      input trees of type Integer.
      <p>
      This method is define similarly to addStringBTrees().
   */
   // Define addIntegerBTrees()...
   
   public static BTreeAbstract<Integer> addIntegerBTrees(BTree<Integer> btree1,
                                                         BTree<Integer> btree2)
   {
     
       if(btree1.isEmpty() && btree2.isEmpty())               // Lines 97-100 Is the empty btree case and returns an empty
       {                                                      // btree of integers. 
          return new EmptyBTree<Integer>();
       }
     
       
       
       
      if( btree1.isEmpty())                                 // Lines 105-117 Is the sum of an empty btree and a leaf tree
        {                                                   // and returns a leaf tree. 
          return new BTreeNode<Integer>(btree2.root(),
                             addIntegerBTrees(new EmptyBTree<Integer>(), btree2.left()),
                             addIntegerBTrees(new EmptyBTree<Integer>(), btree2.right()));
        }
  
      else if(btree2.isEmpty())
        {
          return new BTreeNode<Integer>(btree2.root(),
                             addIntegerBTrees(new EmptyBTree<Integer>(), btree2.left()),
                             addIntegerBTrees(new EmptyBTree<Integer>(), btree2.right()));
        }

       else
       {
         return new BTreeNode<Integer>(btree1.root() + btree2.root(),
                                addIntegerBTrees(btree1.left(), btree2.left()),
                                addIntegerBTrees(btree1.right(), btree2.right()));
       }
  
                                                                    // Lines 120-124 Is the sum of two leaf trees and returns
                                                                   // a leaf tree with the sum of the elements of the two
                                                                   // leaf trees. 
     
   }
   

   
   
   
class Node                                               // Lines 136-145 Is the Class Node.
{
    int data;
    Node left, right;
     
    public Node(int data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }}







   // Simple test cases for addStringBTrees() and addIntegerBTrees().
   public static void main(String[] args)
   {
      BTree<Integer> btree12 =                            // Lines 156-244 Are the Test Cases and prints the btrees,
           new LinkedBTree<>(1,                        // the sum of btrees, creates a dot file, and creates a btree
               new LinkedBTree<>(12),                   // png. 
               new LinkedBTree<>(123));

      BTree<Integer> btree13 =
           new LinkedBTree<>(4,
               new LinkedBTree<>(4065),
               new LinkedBTree<>(485));

      BTree<Integer> btree14 = addIntegerBTrees(btree12, btree13);

      System.out.println( btree12 );
      System.out.println( btree13 );
      System.out.println( btree14 );
      BTree2dot.btree2dot(btree12, "btree12");
      BTree2png.btree2png("btree12");
      BTree2dot.btree2dot(btree13, "btree13");
      BTree2png.btree2png("btree13");
      BTree2dot.btree2dot(btree14, "btree14");
      BTree2png.btree2png("btree14");


      BTree<String> btree15 =
           new LinkedBTree<>("abc",
               new LinkedBTree<>("abc"),
               new LinkedBTree<>("abc"));

      BTree<String> btree16 =
           new LinkedBTree<>("x",
               new LinkedBTree<>("xy"),
               new LinkedBTree<>("xyz"));

      BTree<String> btree17 = addStringBTrees(btree15, btree16);

      System.out.println( btree15 );
      System.out.println( btree16 );
      System.out.println( btree17 );
      BTree2dot.btree2dot(btree15, "btree15");
      BTree2png.btree2png("btree15");
      BTree2dot.btree2dot(btree16, "btree16");
      BTree2png.btree2png("btree16");
      BTree2dot.btree2dot(btree17, "btree17");
      BTree2png.btree2png("btree17");
      
      BTree<Integer> btree18 =                            
           new LinkedBTree<>(172,                        
               new LinkedBTree<>(12),                   
               new LinkedBTree<>(3579));

      BTree<Integer> btree19 =
           new LinkedBTree<>(4,
               new LinkedBTree<>(4237),
               new LinkedBTree<>(362));

      BTree<Integer> btree20 = addIntegerBTrees(btree18, btree19);

      System.out.println( btree18 );
      System.out.println( btree19 );
      System.out.println( btree20 );
      BTree2dot.btree2dot(btree18, "btree18");
      BTree2png.btree2png("btree18");
      BTree2dot.btree2dot(btree19, "btree19");
      BTree2png.btree2png("btree19");
      BTree2dot.btree2dot(btree20, "btree20");
      BTree2png.btree2png("btree20");


      BTree<String> btree21 =
           new LinkedBTree<>("ace",
               new LinkedBTree<>("cel"),
               new LinkedBTree<>("aceled"));

      BTree<String> btree22 =
           new LinkedBTree<>("xls",
               new LinkedBTree<>("xlsy"),
               new LinkedBTree<>("xlsyz"));

      BTree<String> btree23 = addStringBTrees(btree21, btree22);

      System.out.println( btree21 );
      System.out.println( btree22 );
      System.out.println( btree23 );
      BTree2dot.btree2dot(btree21, "btree21");
      BTree2png.btree2png("btree21");
      BTree2dot.btree2dot(btree22, "btree22");
      BTree2png.btree2png("btree22");
      BTree2dot.btree2dot(btree23, "btree23");
      BTree2png.btree2png("btree23");
      
   }
}
