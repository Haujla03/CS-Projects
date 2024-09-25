/*
  Course: CS 31600
  Name: Harbir Aujla
  Email: aujla@pnw.edu
  Assignment: 4
*/

package language5a;

import tree.*;
import tokenizer.*;

/*
 This program is a static analyzer that walks through the program trees and anaylzes the Fun, Lambda, and Apply expressions
 and looks for any syntatic errors caused by these expressions. 
 */

/**
   Analyze (check) an Abstract Syntax Tree (AST)
   that is defined by the following grammar.
   <pre>{@code
       Prog ::= Expr
              | '(' 'prog' (Fun | Expr)* Expr ')'

        Fun ::= '(' 'fun' VARIABLE Lambda ')'   // a function declaration

     Lambda ::= '(' 'lambda' VARIABLE* Expr ')' // formal parameters followed by function body

       Expr ::= Apply
              | If
              | While
              | Begin
              | Var
              | Set
              | Print
              | AExpr
              | BExpr
              | INTEGER
              | BOOLEAN
              | VARIABLE

      Apply ::= '(' 'apply' Expr Expr* ')'  // function value followed by actual parameters

         If ::= '(' 'if' Expr Expr Expr ')'

      While ::= '(' 'while' Expr Expr ')'

      Begin ::= '(' 'begin' Expr+ ')'

        Var ::= '(' 'var' VARIABLE Expr ')'

        Set ::= '(' 'set' VARIABLE Expr ')'

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

   public static Environment globalEnv; // Reference to the global environment object.
                                        // This is needed to implement lexical scope.

   /**
      The methods checkProg(), checkExpr(), checkAexpr(), checkBexpr(),
      and checkRexpr() are essentially a post-order traversal of the abstract
      syntax tree.

      @param tree  an expression {@link Tree} to be checked
      @return the {@link Value} of the expression {@link Tree}
   */
   public static void check(final Tree<Token> tree)
   {
      // Instantiate a global environment object.
      globalEnv = new Environment();

      checkProg( tree, globalEnv );
   }


   /**
      Check a program using the given {@link Environment}.

      @param tree  an expression {@link Tree} to be checked
      @param env   an {@link Environment} for local variables
      @return the {@link Value} of the expression {@link Tree}
   */
   public static void checkProg(final Tree<Token> tree,
                                final Environment env)
   {
      thereIsAnError = false;

      // Check which kind of Prog we have.
      if ( ! tree.root().lexeme.equals("prog") )
      {
         // Check the single expression.
         checkExpr( tree, env );
      }
      else
      {
         // Analyze each Fun and gather all the function
         // names into the global environment. Do this before
         // analyzing each function's lambda expression so
         // that we can allow function forward references
         // and mutually recursive functions.
         for (int i = 0; i < tree.subTrees().size()-1; ++i)
         {
            if ( tree.subTrees().get(i).root().lexeme.equals("fun") )
            {
               checkFun( tree.subTrees().get(i), env );
            }
         }

         // Analyze each Fun or Expr in the Prog. In this pass,
         // we analyze the body (the lambda expression) of each
         // function definition.
         for (int i = 0; i < tree.subTrees().size()-1; ++i)
         {
            if ( tree.subTrees().get(i).root().lexeme.equals("fun") )
            {
               final String functionName = tree.subTrees().get(i).subTrees().get(0).root().lexeme;
               if ( env.defined(functionName) )
               {
                  final Tree<Token> lambda = tree.subTrees().get(i).subTrees().get(1);
                  checkLambda(lambda, env);
               }
            }
            else
            {
               checkExpr( tree.subTrees().get(i), env );
            }
         }

         // Check the last expression.
         checkExpr( tree.subTrees().get(tree.subTrees().size()-1), env );
      }

  if (thereIsAnError)
      {
         throw new AnalysisError();
      }
   }


   /**
      Check a function definition.

      This method mutates the global environment object.
      The Value object put into the environment by this method
      has the tag "lambda" and its value field is a reference
      to the function's "lambda expression".

      @param tree  an expression {@link Tree} for a function definition
      @param env   an {@link Environment} to hold the function definition
   */
   public static void checkFun(Tree<Token> tree, Environment env)
   {
      // Line 179 Gets the function name.
       final String name = tree.subTrees().get(0).root().lexeme;
       
      // Lines 183-191 Checks that we have a proper name.
       
      if ( ! name.matches("^[a-zA-Z][a-zA-Z0-9]*") )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: improper function name: "
                            + name
                           // + " in var"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }
      
       

      // Lines 197-204 Checks if the function name has already been defined.
       
        if (  env.defined(name) )  // static check (semantic)
      {
            thereIsAnError = true;
            System.err.println("Syntax error: function already exsists : "
                               + name
                               + " at line " + tree.subTrees().get(0).root().line
                               + ", position "  + tree.subTrees().get(0).root().position);
     }

       
       

      // Lines 210-216 Checks that fun has two branches.
      if (  tree.subTrees().size() > 2  )
       {
          thereIsAnError = true;
          System.err.println("Syntax error: improper function definition:"
                            + name);
     
      }
      

      // Lines 220-226 Check that the second branch of fun is a lambda branch.
          if ( !tree.subTrees().get(1).root().lexeme.equals("lambda") )
          {
            thereIsAnError = true;
            System.err.println("Syntax error: bad function definition: "
                            + toString(tree));
            
         }
         
       
   
      // Line 232 Creates a lambda Value pointing to the lambda branch.
      
       final Value lambda = new Value (tree.subTrees().get(1));

      // Line 236 Adds the <function-name, lambda-value> pair to the global environment.
       
       env.add(name, lambda);

   }


   // Check a lambda-expression.
   public static void checkLambda(final Tree<Token> tree,
                                  final Environment env)
   {
      // Check this lambda-expression. This code is similar to
      // part of the evaluateApply method in the evaluator.    
      // Create a new environment object that is "nested"
      // in the global environment ("lexical scope").
      // Line 253 Gets the lambda expression tree.
      // Line 257 This environment is used to bind formal parameter
      // names to "dummy" actual parameter values.
     
     final Tree<Token> lambda = tree;


     
/*5*/ final Environment localEnv = new Environment(globalEnv, "Function Activation");

      // Lines 261-307 Bind, in the new environment object, a dummy value
      // to each formal parameter name.
/*6*/ for (int i = 0; i < tree.subTrees().size()-1; ++i) // iterate through the formal parameters
      {
         // Line 266 Retrieve, from within the lambda expression,
         // a formal parameter name.
          
         final String formalParamName = tree.subTrees().get(i).root().lexeme;

         // Lines 270-278 Checks that the parameter name is not a keyword.
           
      if ( isKeyword(formalParamName) )  // static check (syntax)
       {
         thereIsAnError = true;
         System.err.println("Syntax error: keyword as parameter name: "
                            + formalParamName
                            + " in "
                            + "lambda at line " + tree.subTrees().get(i).root().line
                            + ", position " + tree.subTrees().get(i).root().position);
       }

         // Lines 282-289 Checks that we have a proper parameter name.
      
         if ( ! formalParamName.matches("^[a-zA-Z][a-zA-Z0-9]*") )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: improper parameter name: "
                            + formalParamName
                            + " in lambda at line " + tree.subTrees().get(i).root().line
                            + ", position " + tree.subTrees().get(i).root().position);
      }

         // Line 293-300 Checks if the parameter name is being repeated.
         
         if (  env.definedLocal(formalParamName)  )  // static check (semantic)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: repeated parameter name: "
                            + formalParamName
                            + "lambda at line " + tree.subTrees().get(i).root().line
                            + ", position " + tree.subTrees().get(i).root().position);
      }

            // Line 305 Bind, in the new local environment object,
            // a dummy value, for example new Value(false),
            // to the formal parameter name.
          localEnv.add(formalParamName, new Value(false));

      }

      // Line 313 Checks the body of the lambda expression using the
      // new environment (which contains bindings of dummy
      // parameter values to the function's formal parameter names).

     checkExpr(tree.subTrees().get(tree.subTrees().size()-1), localEnv);
      
       // When this method returns, the local Environment
      // object that we created to hold the parameter bindings
      // becomes a garbage object (and will be garbage collected).
       
   }


   // Check an expression.
   private static void checkExpr(final Tree<Token> tree,
                                 final Environment env)
   {
      final String node = tree.root().lexeme;

      if ( node.equals("apply") )
      {
         checkApply( tree, env );
      }
      else if ( node.equals("if") )
      {
         checkIf( tree, env );
      }
      else if ( node.equals("while") )
      {
         checkWhile( tree, env );
      }
      else if ( node.equals("begin") )
      {
         checkBegin(tree, env);
      }
      else if ( node.equals("set") )
      {
         checkSet(tree, env);
      }
      else if ( node.equals("var") )
      {
         checkVar(tree, env);
      }
      else if ( node.equals("print") )
      {
         checkPrint(tree, env);
      }
      else if ( node.equals("&&")
             || node.equals("||")
             || node.equals("!") )
      {
         checkBexpr(tree, env);  // boolean expression
      }
      else if ( node.equals("==")
             || node.equals("!=") )
      {
         checkEqexpr(tree, env);  // equality operator
      }
      else if ( node.equals("<")
             || node.equals(">")
             || node.equals("<=")
             || node.equals(">=") )
      {
         checkRelexpr(tree, env);  // relational operator
      }
      else if ( node.equals("+")
             || node.equals("-")
             || node.equals("*")
             || node.equals("/")
             || node.equals("%")
             || node.equals("**") )
      {
         checkAexpr(tree, env);  // arithmetic expression
      }
      else if ( tree.subTrees().size() == 0 ) // a leaf node
      {
         if ( node.equals("true") || node.equals("false") )
         {

         }
         else if ( node.matches("^[-|+]*[0-9][0-9]*") )
         {

         }
         else if ( env.defined(node) )  // a defined variable
         {

         }
         else   // static check (semantic)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: undefined variable: "
                               + node
                               + " at line " + tree.root().line
                               + ", position " + tree.root().position);
         }
      }
      else   // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: invalid expression: "
                            + toString(tree));
      }
   }


   // Check an apply-expression.
   
   // checks 2 things if variable is defined, and correct number of arguements;
   private static void checkApply(final Tree<Token> tree,
                                  final Environment env)
   {
      // Lines 424-428 Checks all of apply's sub expressions to make
      // sure that they are syntactically correct.
       
     for(int i= 0; i < tree.subTrees().size(); i++)
     {
       checkExpr(tree.subTrees().get(i), env);
                 
     }

      // The next part only works for apply expressions that apply a
      // function name defined by a Fun term in the prog-expression.
    
      // The syntax for an apply-expression is
      //    '(' 'apply' Expr Expr* ')'
      // where the first Expr is an expression that evaluates to a function
      // value and the other Expr's evaluate to the actual parameters. We can
      // only check that the actual parameters agree with the formal parameters
      // in the special case where the first Expr is a variable name put into
      // the global environment by a Fun term. More complicated apply-expressions
      // need much more complex type checking algorithms.
      final String name = tree.subTrees().get(0).root().lexeme;
      if ( name.matches("^[a-zA-Z][a-zA-Z0-9]*")         // A valid variable name,
        && globalEnv.definedLocal(name)                  // that is in the global env,
        && globalEnv.lookUp(name).tag.equals("lambda") ) // and defines a function.
      {
         // Line 448 Gets a reference to the function's "lambda expression".

         Value lambda = env.lookUp(name);

         // Lines 453-458 Checks that the number of actual parameters (in apply)
         // is equal to the number of formal parameters (in the lambda).
        
        if(tree.subTrees().size() != lambda.valueL.subTrees().size() )
        {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguements : "
                               + toString(tree));
        }


      }
   }


   // Check an if-expression.
   private static void checkIf(final Tree<Token> tree,
                               final Environment env)
   {
      if ( 3 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: incorrect conditional expression: "
                            + toString(tree));
      }

      // Check the boolean condition.
      checkExpr( tree.subTrees().get(0), env );

      // Check each branch of the if-expression (if they exist).
      if ( 1 < tree.subTrees().size() )
      {
         checkExpr( tree.subTrees().get(1), env );
         if ( 2 < tree.subTrees().size() )
         {
            checkExpr( tree.subTrees().get(2), env );
         }
      }

      // Check any extra (improper) Exprs in the if-expression.
      for (int i = 3; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a while-loop expression.
   private static void checkWhile(final Tree<Token> tree,
                                  final Environment env)
   {
      if ( 2 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: incorrect while expression: "
                            + toString(tree));
      }

      // Check the boolean condition.
      checkExpr( tree.subTrees().get(0), env );

      // Check the body of the while-loop (if it exists).
      if ( 1 < tree.subTrees().size() )
      {
         checkExpr( tree.subTrees().get(1), env );
      }

      // Check any extra (improper) Exprs in the while-expression.
      for (int i = 2; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a begin expression.
   private static void checkBegin(final Tree<Token> tree,
                                  final Environment env)
   {
      // Create a new Environment object chained to (or "nested in")
      // the previous ("outer") environment object.
      final Environment newEnv = new Environment(env, "Local (begin)");

      // Check each sub expression in the begin
      // expression (using the new environment chain).
      for (int i = 0; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), newEnv );
      }

      // When this method returns, the local Environment
      // object that we created at the beginning of this method
      // becomes a garbage object (and will be garbage collected).
      // This is like "popping" a stack frame off of the call stack
      // in Java, C , or C++.
   }


   // Check a set expression.
   private static void checkSet(final Tree<Token> tree,
                                final Environment env)
   {
      if ( 2 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: wrong number of arguments: "
                            + toString(tree));
      }

      // get the variable
      final String variable = tree.subTrees().get(0).root().lexeme;

      // check that we have a proper variable
      if ( ! variable.matches("^[a-zA-Z][a-zA-Z0-9]*") )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: improper variable name: "
                            + variable
                            + " in set"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }

      // check that the variable name is not a keyword
      if ( isKeyword(variable) )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: keyword as variable name: "
                            + variable
                            + " in set"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }

      // get, and then check, the expression
      final Tree<Token> expr = tree.subTrees().get(1);
      checkExpr( expr, env );

      // check if this variable has already been declared
      if ( ! env.defined(variable) )  // static check (semantic)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: undefined variable: "
                            + variable
                            + " in set"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }

      // Check any extra (improper) Exprs in the set-expression.
      for (int i = 2; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a var expression.
   private static void checkVar(final Tree<Token> tree,
                                final Environment env)
   {
      if ( 2 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: wrong number of arguments: "
                            + toString(tree));
      }

      // get the variable
      final String variable = tree.subTrees().get(0).root().lexeme;

      // check that we have a proper variable
      if ( ! variable.matches("^[a-zA-Z][a-zA-Z0-9]*") )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: improper variable name: "
                            + variable
                            + " in var"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }

      // check that the variable name is not a keyword
      if ( isKeyword(variable) )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: keyword as variable name: "
                            + variable
                            + " in var"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }

      // get, and then check, the expression
      final Tree<Token> expr = tree.subTrees().get(1);
      checkExpr( expr, env );

      // check if this variable has already been declared
      // in the local environment
      if ( env.definedLocal(variable) )  // static check (semantic)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: variable already declared: "
                            + variable
                            + " in var"
                            + " at line " + tree.subTrees().get(0).root().line
                            + ", position " + tree.subTrees().get(0).root().position);
      }
      else
      {
         // declare the new, local, variable (as if it was a boolean)
         env.add(variable, new Value(false));
      }

      // Check any extra (improper) Exprs in the var-expression.
      for (int i = 2; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a print expression.
   private static void checkPrint(final Tree<Token> tree,
                                  final Environment env)
   {
      if ( 1 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: wrong number of arguments: "
                            + toString(tree));
      }

      checkExpr( tree.subTrees().get(0), env );

      // Check any extra (improper) Exprs in the print-expression.
      for (int i = 1; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a boolean expression.
   private static void checkBexpr(final Tree<Token> tree,
                                  final Environment env)
   {
      if ( 0 == tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: missing arguments: "
                            + toString(tree));
      }

      final String node = tree.root().lexeme;

      // check the first operand
      checkExpr( tree.subTrees().get(0), env );

      if ( node.equals("&&")
        || node.equals("||") )
      {
         if ( 2 > tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }

         // check the rest of the operands
         for (int i = 1; i < tree.subTrees().size(); ++i)
         {
            checkExpr( tree.subTrees().get(i), env );
         }
      }
      else if ( node.equals("!") )
      {
         if ( 1 != tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }
      }
   }


   // Check an equality expression (which is a boolean expression).
   private static void checkEqexpr(final Tree<Token> tree,
                                   final Environment env)
   {
      if ( 2 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: wrong number of arguments: "
                            + toString(tree));
      }

      // check each of the (first) two sub expressions
      checkExpr( tree.subTrees().get(0), env );
      checkExpr( tree.subTrees().get(1), env );

      // Check any extra (improper) Exprs in the equality expression.
      for (int i = 2; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check a relational expression (which is a boolean expression).
   private static void checkRelexpr(final Tree<Token> tree,
                                    final Environment env)
   {
      if ( 2 != tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: wrong number of arguments: "
                            + toString(tree));
      }

      checkExpr( tree.subTrees().get(0), env );
      checkExpr( tree.subTrees().get(1), env );

      // Check any extra (improper) Exprs in the relational expression.
      for (int i = 2; i < tree.subTrees().size(); ++i)
      {
         checkExpr( tree.subTrees().get(i), env );
      }
   }


   // Check an arithmetic expression.
   private static void checkAexpr(final Tree<Token> tree,
                                  final Environment env)
   {
      if ( 0 == tree.subTrees().size() )  // static check (syntax)
      {
         thereIsAnError = true;
         System.err.println("Syntax error: missing arguments: "
                            + toString(tree));
      }

      final String node = tree.root().lexeme;

      checkExpr( tree.subTrees().get(0), env );

      if ( tree.subTrees().size() >= 2 )
      {
         checkExpr( tree.subTrees().get(1), env );
      }

      if ( node.equals("+") )
      {
         for (int i = 2; i < tree.subTrees().size(); ++i)
         {
            checkExpr( tree.subTrees().get(i), env );
         }
      }
      else if ( node.equals("-") )
      {
         if ( 2 < tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }
      }
      else if ( node.equals("*") )
      {
         if ( 1 == tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }

         for (int i = 2; i < tree.subTrees().size(); ++i)
         {
            checkExpr( tree.subTrees().get(i), env );
         }
      }
      else if ( node.equals("/") )
      {
         if ( 2 != tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }
      }
      else if ( node.equals("%") )
      {
         if ( 2 != tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }
      }
      else if ( node.equals("**") )
      {
         if ( 2 != tree.subTrees().size() )  // static check (syntax)
         {
            thereIsAnError = true;
            System.err.println("Syntax error: wrong number of arguments: "
                               + toString(tree));
         }
      }
   }


   /**
      Returns true if the given string is one of the reserved keywords.
   */
   private static boolean isKeyword(final String word)
   {
      return keywords.contains(word);
   }

   private static final java.util.List<String> keywords
                   = java.util.Arrays.asList(
                       "prog",
                       "fun",
                       "lambda",
                       "apply",
                       "if",
                       "while",
                       "begin",
                       "var",
                       "set",
                       "print",
                       "true",
                       "false"
                     );

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
