/*
Course: CS 31600
Name: Harbir Aujla
Email: aujla@pnw.edu
Assignment: 3

*/

package language4a;

import tree.*;
import tokenizer.*;


/*
 This program is a static analyzer that walks through the program trees and anaylzes the AST's generated by the
 programs and it doesn't needs to fully evaluate the programs.
 */


/**
   Analyze (check) an Abstract Syntax Tree (AST)
   that is defined by the following grammar.
   <pre>{@code
       Prog ::= Expr
              | '(' 'prog' Expr+ ')'

       Expr ::= If
              | While
              | Begin
              | Set
              | Var
              | Print
              | AExp
              | BExp
              | INTEGER
              | BOOLEAN
              | VARIABLE

         If ::= '(' 'if' Exp Exp Exp ')'

      While ::= '(' 'while' Exp Exp ')'

      Begin ::= '(' 'begin' Expr+ ')'

        Set ::= '(' 'set' VARIABLE Expr ')'

        Var ::= '(' 'var' VARIABLE Expr ')'

      Print ::= '(' 'print' Expr ')'

      BExpr ::= '(' '||'  Expr Expr+ ')'
              | '(' '&&'  Expr Expr+ ')'
              | '(' '!'   Expr ')'
              | '('  EqOp Expr Expr ')'
              | '(' RelOp Expr Expr ')'

       EqOp ::= '==' | '!='
      RelOp ::= '<' | '>' | '<=' | '>='

      AExpr ::= '(' '+' Expr Expr* ')'
              | '(' '-' Expr Expr? ')'
              | '(' '*' Expr Expr+ ')'
              | '(' '/' Expr Expr  ')'
              | '(' '%' Expr Expr  ')'
              | '(' '**' Expr Expr ')'

    INTEGER ::= [-|+][0-9]+
    BOOLEAN ::= 'true' | 'false'
   VARIABLE ::= [a-zA-Z][a-zA-Z0-9]*
   }</pre>
*/
public class AnalyzeAST
{
   public static boolean thereIsAnError = false;

   /**
      The methods checkProg(), checkExpr(), checkAexpr(), checkBexpr(),
      and checkRexpr() are essentially a post-order traversal of the abstract
      syntax tree.

      @param tree  an expression {@link Tree} to be checked
      @return the {@link Value} of the expression {@link Tree}
   */
   public static Value check(final Tree<Token> tree)
   {
      // Instantiate a global environment object.
      final Environment env = new Environment();

      final Value value = checkProg( tree, env );

      return value;
   }


   /**
      Check a program using the given {@link Environment}.

      @param tree  an expression {@link Tree} to be checked
      @param env   an {@link Environment} for local variables
      @return the {@link Value} of the expression {@link Tree}
   */
   public static Value checkProg(final Tree<Token> tree,
                                 final Environment env)
   {
      thereIsAnError = false;

      final Value result;  // a blank final

      // Check which kind of Prog we have.
      if ( ! tree.root().lexeme.equals("prog") )
      {
         // Check the single expression.
         result = checkExpr( tree, env );
      }
      else
      {
         // Check each Expr in the Prog.
         for (int i = 0; i < tree.subTrees().size() - 1; ++i)
         {
            checkExpr( tree.subTrees().get(i), env );
         }
         // Check the last Expr in the Prog and use it as the type.
         result = checkExpr( tree.subTrees().get(tree.subTrees().size()-1), env );
      }

      if (thereIsAnError)
      {
         throw new AnalysisError();
      }
      return result;
   }


   // Check an expression.
   private static Value checkExpr(final Tree<Token> tree,
                                  final Environment env)
   {
      final Value result;  // a blank final

      final String node = tree.root().lexeme;

      if ( node.equals("if") )
      {
         result = checkIf( tree, env );
      }
      else if ( node.equals("while") )
      {
         result = checkWhile( tree, env );
      }
      else if ( node.equals("begin") )
      {
         result = checkBegin(tree, env);
      }
      else if ( node.equals("set") )
      {
         result = checkSet(tree, env);
      }
      else if ( node.equals("var") )
      {
         result = checkVar(tree, env);
      }
      else if ( node.equals("print") )
      {
         result = checkPrint(tree, env);
      }
      else if ( node.equals("&&")
             || node.equals("||")
             || node.equals("!") )
      {
         result = checkBexpr(tree, env);  // boolean expression
      }
      else if ( node.equals("==")
             || node.equals("!=") )
      {
         result = checkEqexpr(tree, env);  // equality operator
      }
      else if ( node.equals("<")
             || node.equals(">")
             || node.equals("<=")
             || node.equals(">=") )
      {
         result = checkRelexpr(tree, env);  // relational operator
      }
      else if ( node.equals("+")
             || node.equals("-")
             || node.equals("*")
             || node.equals("/")
             || node.equals("%")
             || node.equals("**") )
      {
         result = checkAexpr(tree, env);  // arithmetic expression
      }
      else if ( tree.subTrees().size() == 0 ) // a leaf node
      {
         if ( node.equals("true") || node.equals("false") )
         {
            result = new Value( node.equals("true") );
         }
         else if ( node.matches("^[-|+]*[0-9][0-9]*") )
         {
            result = new Value( Integer.parseInt( node ) );
         }
         else if ( env.defined(node) )  // a defined variable
         {
            result = env.lookUp( node );
         }
         else  // runtime check
         {
            thereIsAnError = true;
            System.out.println("Syntax error: undefined variable: " + node);
            result = new Value( true );
         }
      }
      else
      {
         thereIsAnError = true;
         System.out.println("Syntax error: invalid expression: " + toString(tree));
         result = new Value( true );
      }
      return result;
   }


   // Check an if-expression.
   /*
     The if expression anaylzer checks the if-statements and looks to see if there are any concerns regarding
     the if expression like having many arguments or having less arguments, also having two different return types.
     
   */
   private static Value checkIf(final Tree<Token> tree,
                                final Environment env)
   {
    
     final Value value;
     
     final Value conditionalExpr = checkExpr(tree.subTrees().get(0), env);
    /* Value valueL = checkExpr( tree.subTrees().get(1), env );
      Value valueR = checkExpr( tree.subTrees().get(2), env );
     */
     
    
     
     if(tree.subTrees().size() > 3) 
     {
       System.out.println("Syntax error: incorrect conditional expression: " + toString(tree));
       thereIsAnError = true;
       
     }
     if(tree.subTrees().size() < 2)
     {
       System.out.println("Syntax error: incorrect conditional expression:" + toString(tree));
       thereIsAnError = true;
     }
     if(tree.subTrees().size() < 1)
     {
       System.out.println("Syntax error: incorrect conditional expression:" + toString(tree));
       thereIsAnError = true;
     }
     
   /* if(  valueL.tag.equals(Value.INT_TAG) && !valueR.tag.equals(Value.INT_TAG) )
     {
       System.out.println("Syntax error: incorrect conditional expression:" + toString(tree));
       thereIsAnError = true;
     }
     */  
    
     
     
   return new Value(false);

   }


   // Check a while-loop expression.
    /*
     The while expression anaylzer checks the while-statements and looks to see if there are any concerns regarding
     the while expression like having many arguments or having less arguments, also having two different return types.
     
   */
   private static Value checkWhile(final Tree<Token> tree,
                                   final Environment env)
   {


      final Value value;
       
       final Value whileExpr = checkExpr(tree.subTrees().get(0), env);
       
     
       
       if(tree.subTrees().size() < 2)
       {
         System.out.println("Syntax error: incorrect while expression:" + toString(tree));
         thereIsAnError = true;
       }
      
     
      // Always return false for a while-loop expression.
      return new Value(false);
   }


   // Check a begin expression.
    /*
     The begin expression anaylzer checks the begin environments and looks to see if there are any concerns regarding
     the begin expressions, and returns the local environment objects.
     
   */
   private static Value checkBegin(final Tree<Token> tree,
                                   final Environment env)
   {
      // Create a new Environment object chained to (or "nested in")
      // the previous ("outer") environment object.
      final Environment newEnv = new Environment(env);
      
      
     for(int i = 0; i < tree.subTrees().size()-1; i++)
     {
       checkExpr( tree.subTrees().get(i), newEnv );
     }
     
     final Value result = checkExpr(tree.subTrees().get(tree.subTrees().size()-1), newEnv);
     

      
       return result;

   }


   // Check a set expression.
    /*
     The set expression anaylzer checks the set-statements and looks to see if there are any concerns regarding
     the set expression like having undefined variable or wrong type for variable
     
   */
   private static Value checkSet(final Tree<Token> tree,
                                 final Environment env)
   {
     
     final String variable = tree.subTrees().get(0).root().lexeme;
     
     if ( ! env.defined(variable) )
      {
         System.out.println("Syntax Error: undefined variable: " + variable);
         thereIsAnError = true;
     }

     
     if( env.defined(variable) ) 
     {
       
       System.out.println("Type error: wrong type for variable:" + variable);
       thereIsAnError = true;
       
     }
     
     
     return new Value(false);
    
   }


   // Check a var expression.
    /*
     The var expression anaylzer checks the var-statements and looks to see if there are any concerns regarding
     the var expression like having wrong number of arguments or the variable is already declared.
     
   */
   private static Value checkVar(final Tree<Token> tree,
                                 final Environment env)
   {
    
   final String variable = tree.subTrees().get(0).root().lexeme;

      if( !env.definedLocal(variable) )
         {
           
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree) );
           thereIsAnError = true;
         }
   
       if ( env.definedLocal(variable) )
      {
         System.out.println("Syntax error: variable already declared: " + variable);
         thereIsAnError = true;
      }
    
      
    
      final Tree<Token> expr = tree.subTrees().get(1);
     final Value result = checkExpr( expr, env );
    return result;
    
   }


   // Check a print expression.
    /*
     The print expression anaylzer checks the print-statements and looks to see if there are any concerns regarding
     the print expression like having wrong number of arguments.
   */
   private static Value checkPrint(final Tree<Token> tree,
                                   final Environment env)
   {
      
       final Value result = checkExpr( tree.subTrees().get(0), env );
      if ( 1 != tree.subTrees().size() )  // runtime check
      {
        System.out.println("Syntax Error: wrong number of arguments: "
                             + toString(tree));
        thereIsAnError = true;
      }
       if ( 2 != tree.subTrees().size() )  // runtime check
      {
        System.out.println("Syntax Error: wrong number of arguments: "
                             + toString(tree));
        thereIsAnError = true;
      }
        if ( 3 != tree.subTrees().size() )  // runtime check
      {
        System.out.println("Syntax Error: wrong number of arguments: "
                             + toString(tree));
        thereIsAnError = true;
      }
        
      return result;

       
   }


   // Check a boolean expression.
    /*
     The boolean expression anaylzer checks the boolean-statements and looks to see if there are any concerns regarding
     the boolean expression like having a type error of not a boolean expression.
     
   */
   private static Value checkBexpr(final Tree<Token> tree,
                                   final Environment env)
   {
      boolean result = false;

      final String node = tree.root().lexeme;

      Value value = checkExpr( tree.subTrees().get(0), env);
      
     Value valueL = checkExpr( tree.subTrees().get(0), env );
      Value valueR = checkExpr( tree.subTrees().get(1), env);

      result = value.valueB;

      if ( node.equals("&&") )
      {
         if(tree.subTrees().size() != 1)
         {
           
           System.out.println("Type error: not a boolean expression:" + tree.subTrees().get(1) );
           thereIsAnError = true;
         }
         
      }
      else if ( node.equals("||") )
      {
         if(tree.subTrees().size() != 1)
         {
          
           System.out.println("Type error: not a boolean expression:" + tree.subTrees().get(1) );
           thereIsAnError = true;
         }
      }
      else if ( node.equals("!") )
      {
        
         if(tree.subTrees().size() != 1 )
         {
           
           System.out.println("Type error: not a boolean expression:" + toString(tree) );
           thereIsAnError = true;
         }
         else
         {
           if(tree.subTrees().size() == 0)
           {
           
           System.out.println("Type error: not a boolean expression:" + tree.subTrees().get(1) );
           thereIsAnError = true;
         }
         }
      }
      
      


      return new Value(false); // boolean expressions always return a boolean
   }


   // Check an equality expression (which is a boolean expression).
    /*
     The equality expression anaylzer checks the equality-statements and looks to see if there are any concerns regarding
     the equality expression like having wrong number of arguments or not a boolean expression.
     
   */
   private static Value checkEqexpr(final Tree<Token> tree,
                                    final Environment env)
   {
     
     boolean result = false;

      final String op = tree.root().lexeme;

      final Value valueL = checkExpr( tree.subTrees().get(0), env );
      final Value valueR = checkExpr( tree.subTrees().get(1), env );

      if ( op.equals("==") )
      {
        if(tree.subTrees().size() != 2 || tree.subTrees().size() >=3 )
         {
         
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree) );
           thereIsAnError = true;
         }
         if (  valueL.tag.equals(Value.INT_TAG) && !valueR.tag.equals(Value.INT_TAG) )
         {
            System.out.println("Type Error: not a boolean expression:"  + toString(tree));
            result = false;
            thereIsAnError = true;
         }
         else if ( ! valueL.tag.equals(Value.INT_TAG) )
         {
            final int resultL = valueL.valueI;
            final int resultR = valueR.valueI;
            result = resultL == resultR;
         }
         else // boolean data type
         {
            final boolean resultL = valueL.valueB;
            final boolean resultR = valueR.valueB;
            result = resultL == resultR;
         }
      }
      else // the '!=' operator
      {
         if ( ! valueL.tag.equals(valueR.tag) )
         {
            result = true;
         }
         else if ( valueL.tag.equals(Value.INT_TAG) )
         {
            final int resultL = valueL.valueI;
            final int resultR = valueR.valueI;
            result = resultL != resultR;
         }
         else // boolean data type
         {
            final boolean resultL = valueL.valueB;
            final boolean resultR = valueR.valueB;
            result = resultL != resultR;
         }
      }

   
    
      return new Value(true); // equality expressions always return a boolean
   }


   // Check a relational expression (which is a boolean expression).
    /*
     The relational expression anaylzer checks the relational-statements and looks to see if there are any concerns regarding
     the relational expression like having wrong number of arguments.
     
   */
   private static Value checkRelexpr(final Tree<Token> tree,
                                     final Environment env)
   {

      boolean result = false;
      
      final String node = tree.root().lexeme;
      
      final Value Relexpr = checkExpr(tree.subTrees().get(0), env);
     
       if ( node.equals("<") )
      {
          if(tree.subTrees().size() >= 2)
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals(">") )
      {
           if(tree.subTrees().size() >= 2)
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals("<=") )
      {
          if(tree.subTrees().size() >= 2)
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals(">=") )
      {
          if(tree.subTrees().size() >= 2)
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }



      return new Value(false); // relational expressions always return a boolean
   }


   // Check an arithmetic expression.
    /*
     The arithmetic expression anaylzer checks the arithmetic-statements and looks to see if there are any concerns regarding
     the arithmetic expression like having wrong number of arguments or not an integer expression.
     
   */
   private static Value checkAexpr(final Tree<Token> tree,
                                   final Environment env)
   {
       
     int result = 0;

      final String node = tree.root().lexeme;

      final Value valueL = checkExpr( tree.subTrees().get(0), env );
      final Value valueR = checkExpr( tree.subTrees().get(1), env);
    

      final int resultL = valueL.valueI;
            int resultR = 0;

      if ( node.equals("+") )
      {
        
          if ( tree.subTrees().size() <= 1 || tree.subTrees().size() >= 3)
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
          } 
          if( valueL.tag.equals(Value.INT_TAG) && !valueR.tag.equals(Value.INT_TAG) )
          {
            System.out.println("Type error: not an integer expression:" + " " + valueR.tag.equals(Value.INT_TAG));
           thereIsAnError = true;
          }
          
      }
      else if ( node.equals("-") )
      {
         if ( tree.subTrees().size() <= 1 || tree.subTrees().size() >= 3 )
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals("*") )
      {
         if ( tree.subTrees().size() <= 1 || tree.subTrees().size() >= 3 )
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
         
         if( !valueL.tag.equals(Value.INT_TAG) && valueR.tag.equals(Value.INT_TAG))
         {
           System.out.println("Type error: not an integer expression:" + " " + !valueL.tag.equals(Value.INT_TAG));
           thereIsAnError = true;
         }
      }
      else if ( node.equals("/") )
      {
         if ( tree.subTrees().size() <= 1 || tree.subTrees().size() >= 3 )
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals("%") )
      {
         if ( tree.subTrees().size() <= 1 || tree.subTrees().size() >= 3 )
         {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
         }
      }
      else if ( node.equals("**") )
      { 
          if ( tree.subTrees().size() != 2 )
          {
           System.out.println("Syntax error: wrong number of arguments:" + toString(tree));
           thereIsAnError = true;
           
             }
      
          }
    
    
        return new Value(0); // arithmetic expressions always return an integer
   }


   /**
      Convert a {@code Tree} of {@link Token}s into a {@link String}.
      <p>
      This method does a pre-order traversal of the {@link Tree}.
      <p>
      This method is used by the analysis error messages.

      @param tree  {@link Tree} of {@link Token}s to convert into a {@link String}
      @return a {@link String} representation of this {@code Tree} of {@link Token}s
   */
   public static String toString(final Tree<Token> tree)
   {
      String result = "";
      if ( tree.isLeaf() )
      {
         result += tree.root().lexeme;
      }
      else
      {
         result += "(" + tree.root().lexeme;
         for ( final Tree<Token> subTree : tree.subTrees() )
         {
            result += " " + toString(subTree);  // recursion
         }
         result += ")";
      }
      return result;
   }
}
