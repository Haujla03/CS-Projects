/*
   Course: CS 31600
   Name: Harbir Aujla
   Email: aujla@pnw.edu
   Assignment: 1

*/

import binarytree.*;

/**
   A program that tests the concatenateLeaves() method.
*/
public class ConcatenateLeaves
{
   /**
      Return a string that is the concatenation of the leaves,
      from the input binary tree, going from left to right.
      <p>
      How does this compare with an in-order, or pre-order,
      or post-order traversal of the binary tree?
   */
   public static <T> String concatenateLeaves(BTree<T> btree)
   {
       if (btree.isEmpty())                                     // Lines 25-26 Is the Empty BTree case and returns an
        return "";                                              // empty BTree. 
      
                                                             // Lines 29-33  If node is leaf node, print its data in  
    if (btree.isLeaf())
    {                                                       // string data type. 
        
        return btree.root().toString();
    }
      
    concatenateLeaves(btree.left());                     // Lines 35 & 38 Are the Concatenating leaves method for left        
                                                        // and right branches. 
    
    concatenateLeaves(btree.right());
     
      String result = concatenateLeaves(btree.left()) + " " + concatenateLeaves(btree.right());
                                                         // Line 40 Concatenates Leaves of the input binary tree from
                                                         // left to right. 

      return result;                                    // Line 44 Returns the result of the concatenated leaves. 
   }


   // A simple test case for concatenateLeaves().
   public static void main(String[] args)
   {
      BTree<String> btree =                                    // Lines 51-94 Are the Test cases that prints the BTrees,
           new BTreeLinked<>("#",                             //  the concatenated leaves, creates a dot file of btree,
               new BTreeLinked<>("#",                   //  creates a btree png, and prints out the pre-order, in-order
                   new BTreeLinked<>("mary"),                // and post order traversals. 
                   new BTreeLinked<>("had")),
               new BTreeLinked<>("#",
                   new BTreeLinked<>("a"),
                   new BTreeLinked<>("#",
                       new BTreeLinked<>("little"),
                       new BTreeLinked<>("lamb"))));

      String result = concatenateLeaves( btree );

      System.out.println( btree );
      System.out.println( result );
      BTree2dot.btree2dot(btree, "btree");
      BTree2png.btree2png("btree");

      System.out.println("PreOrder of the btree:"  +  " "   + btree.preOrder() );
      System.out.println("InOrder of the btree:"   +  " "   + btree.inOrder() );
      System.out.println("PostOrder of the btree:" +  " "   + btree.postOrder() );
      
      
       BTree<String> btreeD =
           new BTreeLinked<>("#",
               new BTreeLinked<>("#",
                   new BTreeLinked<>("London"),
                   new BTreeLinked<>("Bridge")),
               new BTreeLinked<>("#",
                   new BTreeLinked<>("is"),
                   new BTreeLinked<>("#",
                       new BTreeLinked<>("Falling"),
                       new BTreeLinked<>("Down"))));

      String result2 = concatenateLeaves( btreeD );

      System.out.println( btreeD );
      System.out.println( result2 );
      BTree2dot.btree2dot(btreeD, "btreeD");
      BTree2png.btree2png("btreeD");

      System.out.println("PreOrder of the btreeD:"  +  " "   + btreeD.preOrder() );
      System.out.println("InOrder of the btreeD:"   +  " "   + btreeD.inOrder() );
      System.out.println("PostOrder of the btreeD:" +  " "   + btreeD.postOrder() );
   }
}
