/*
 Course: CS 31600
 Name: Harbir Aujla
 Email: aujla@pnw.edu
 Assignment: 2

*/

import tokenizer.*;
import tree.*;

/*
  This program parses the strings and retusn the abstract syntax trees (AST's) of the 
  string expressions. 
*/

/**
   E ::= Or ( '||' Or )*
  Or ::= Eq ( '&&' Eq )*
  Eq ::= R ( ( '==' | '!=' ) R )*
   R ::= A ( ( '<' | '<=' | '>' | '>=' ) A)*
   A ::= T ( [ '+' | '-' ] T )*
   T ::= F ( [ '*' | '/' | '%' ] F )*
   F ::= ( '+' | '-' | '!' ) F  // unary plus, minus, not operators
       | B [ '**' F ]           // binary exponentiation operator
   B ::= '(' E ')'
       | N
   N ::= N D
       | N
   D ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
*/
public class Hw2_Parse2AST
{
   public static Tree<Token> parse(String stringInE11)
   {
      // Tokenize the input string.
      final Tokenizer tokens = new Tokenizer(stringInE11);
      // Parse the list of tokens.
      final Tree<Token> ast = getE(tokens);
      // There shouldn't be any remaining tokens.
      if ( ! tokens.isEmpty() )
      {
         throw new ParseError("unexpected tokens, found " + tokens.peek());
      }
      return ast;
   }


   /**
      E ::= Or ( || Or )*
   */
   public static Tree<Token> getE(Tokenizer tokens)    // Lines 52-66 Get E method parses the Or string expressions
   {                                                  // and returns the ast trees. 
     Tree<Token> ast = getOr(tokens);                // Line 54 Creates the AST of Or tokens. 

      while ( ! tokens.isEmpty() && tokens.peek().lexeme.equals("||") ) // Line 56  checks if the 
                                                                    // tokens is empty or if it contatins || operator.
      {
         final Token tk = tokens.next();                             // Line 59  Consume the || operator
         final Tree<Token> subTree = getOr(tokens);              // Line 60-64 Recursion and creates ast tree  
         ast = new LinkedTree<Token>(tk,                         // with expression. 
                                ast,
                                subTree);
      }
      return ast;                                         // Line 65 Returns the ast. 
   }


   /**
      Or ::= Eq ( && Eq )*
   */
   private static Tree<Token> getOr(Tokenizer tokens)   // Lines 72-86 Get Or method parses the Eq string expressions
   {                                                    // and returns the ast trees. 

     Tree<Token> ast = getEq(tokens);                   // Line 75 Creates the AST of Eq tokens. 

      while ( ! tokens.isEmpty() && tokens.peek().lexeme.equals("&&"))  // Line 77  checks if the token is empty or                               
      {                                                                 // if it contatins the && operator. 
         final Token tk = tokens.next();                         // Line 79 Consume the && operator. 
         final Tree<Token> subTree = getEq(tokens);   // Lines 80-84 Recursion and creates the ast with the expression. 
         ast = new LinkedTree<Token>(tk,
                                ast,
                                subTree);
      }
      return ast;                                  // Line 85 Returns the AST.
   }


   /**
      Eq ::= R ( ( '==' | '!=' ) R )*
   */
    private static Tree<Token> getEq(Tokenizer tokens) // Lines 92-107 Get Eq method parses the ==, != string expressions
   {                                                   // and returns the ast trees. 

      Tree<Token> ast = getR(tokens);                 // Line 95 Creates AST of R tokens. 

      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("==")   // Line 97-98 checks if the token is empty or 
                                   || tokens.peek().lexeme.equals("!=") ) ) // if it contains the ==,!= operator. 
      {
         final Token tk = tokens.next();                             // Line 100 Consumes the == or != operator.
         final Tree<Token> subTree = getR(tokens);               // Lines 101-105 Recursion and creates the ast with 
         ast = new LinkedTree<Token>(tk,                        // the expression. 
                                ast,
                                subTree);
      }
      return ast;                                        // Line 106 Returns the AST. 
   }


   /**
      R ::= A ( ( '<' | '<=' | '>' | '>=' ) A)*
   */
   private static Tree<Token> getR(Tokenizer tokens)   // Lines 113-130 Get R method parses the <,<=,>,>= string expressions
   {                                                  // and returns the ast trees. 

       Tree<Token> ast = getA(tokens);                   // Line 116 Creates the AST of A tokens. 

      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("<")    // Lines 118-121 Checks if the token is empty  
                                   || tokens.peek().lexeme.equals("<=")   // or if it contains the <,<=,>,>= operators. 
                                   || tokens.peek().lexeme.equals(">")
                                   || tokens.peek().lexeme.equals(">=") ) )
      {
         final Token tk = tokens.next();                       // Line 123 Consumes the <,<=,>,>= operators
         final Tree<Token> subTree = getA(tokens);            // Lines 124-128 Recursion and creates the ast with the 
         ast = new LinkedTree<Token>(tk,                      // expression. 
                                ast,
                                subTree);
      }
      return ast;                                            // Line 129 Returns the AST. 
   }


   /**
      A ::= T ( [ '+' | '-' ] T )*
   */
   private static Tree<Token> getA(Tokenizer tokens)    // Lines 136-151 Get A method parses the +,- string expressions
   {                                                  // and returns the ast trees. 
      
     Tree<Token> ast = getT(tokens);                   // Line 139 Creates the AST of T tokens. 

      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("+")     // Lines 141-142 Checks if the token is empty
                                   || tokens.peek().lexeme.equals("-") ) ) // or if it contains the +,- operators. 
      {
         final Token tk = tokens.next();                          // Line 144 Consume the +,- operators
         final Tree<Token> subTree = getT(tokens);               // Lines 145-149 Recursion and creates ast with the 
         ast = new LinkedTree<Token>(tk,                        // expression. 
                                ast,
                                subTree);
      }
      return ast;                                // Line 150 Returns the AST. 
   }


   /**
      T ::= F ( [ '*' | '/' | ' %' ] F )*
   */
   private static Tree<Token> getT(Tokenizer tokens)   // Lines 157-173 Get T method parses the *,/,% string expressions
   {                                                  // and returns the ast trees. 

      Tree<Token> ast = getF(tokens);                // Line 160 Creates the AST of F tokens. 

      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("*")   // Line 162-164 Checks if the token is empty or
                                   || tokens.peek().lexeme.equals("/")   // if it contains *,/,% operators. 
                                   || tokens.peek().lexeme.equals("%") ) )
      {
         final Token tk = tokens.next();                            // Line 166 Consume the *,/,% operators. 
         final Tree<Token> subTree = getF(tokens);               // Line 167-171 Recursion and creates ast with the 
         ast = new LinkedTree<Token>(tk,                         // expression. 
                                ast,
                                subTree);
      }
      return ast;                                              // Line 172 Returns the AST. 
   }


   /**
      F ::= ( '+' | '-' | '!' ) F  // unary plus, minus, not operators
          | B [ '**' F ]           // binary exponentiation operator
   */
   private static Tree<Token> getF(Tokenizer tokens ) // Lines 180-205 Get F method parses the unary and exponentiate string expressions
   {                                                  // and returns the ast trees. 
       final Tree<Token> ast;
      if ( tokens.peek().lexeme.equals("-")              // Lines 183-185 Checks if the tokens is empty or if it contains
        || tokens.peek().lexeme.equals("+")             // -,+,! operators. 
        || tokens.peek().lexeme.equals("!") )
      {
         final Token tk = tokens.next();                 // Lines 187-188 Consumes the -,+,! operators and F tokens. 
         final Tree<Token> subTree = getF(tokens);
         ast = new LinkedTree<Token>(tk, subTree);        // Line 189 Creates ast with the unary expressions. 
      }
      else
      {                                                  // Lines 191-203 Consumes the B token and checks if token is empty
        Tree<Token> s = getB(tokens);                               // or if it contains ** operator. 
      if( ! tokens.isEmpty() && tokens.peek().lexeme.equals("**") )
      {  
         Token tk2 = tokens.next();                             // Line 196 Consumes the ** operator. 
         ast = new LinkedTree<Token>(tk2, s, getF(tokens));     // Line 197 Creates the ast with the exponentiate 
      }                                                        // expressions. 
     else
      {                                                      // Lines 199-204 Creates the ast with B tokens. 
         ast = s;
      }
    }
      return ast;                                      // Line 204 Returns the AST. 
   }


   /**
      B ::= '(' E ')'
          | N
   */
   private static Tree<Token> getB(Tokenizer tokens)   //Lines 212-235 Get B method parses the (E), N string expressions
   {                                                  // and returns the ast trees. 
     
      final Tree<Token> ast;                         // Line 215 Creates AST. 
     if (tokens.peek().lexeme.equals("E") )         // Line 216 Checks if the token matches "E". 
      {
         final Token tk = tokens.next();                 // Line 218 Consumes the E token. 
         final Tree<Token> subTree = getE(tokens);              
         ast = new LinkedTree<Token>(tk, subTree);     //  Line 219 Creates ast with the expression. 
      }
      else if ( tokens.match("(") )
      {
         ast = getE(tokens);                        // Line 222-229 Creates ast of E token. 
         if ( ! tokens.match(")") )
         {
            throw new ParseError("expected \")\", found " + tokens.peek());
         }
      }
      else
      {
         ast = getN(tokens);                 // Line 231-233 Creates ast of N token.
      }
      return ast;                           // Line 234 Returns the ast. 
   }


   /**
      N ::= D ( D )*
   */
   private static Tree<Token> getN(Tokenizer tokens)
   {
      int position = tokens.peek().position;
      int result = getD(tokens);
      while ( ! tokens.isEmpty()
           && Character.isDigit(tokens.peek().lexeme.charAt(0)) )
      {
         int d = getD(tokens);
         result = (10 * result) + d;
      }
      return new LinkedTree<>(new Token(result+"", position));
   }


   /**
      D ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
   */
   private static int getD(Tokenizer tokens)
   {
      final Token tk = tokens.next();

      if ( ! Character.isDigit(tk.lexeme.charAt(0)) )
         throw new TokenizeError("expected digit, found " + tk);

      return Integer.parseInt(tk.lexeme);
   }


   public static void main(String[] args)
   {
      String[] expressions = {
                              "65432100",                // Lines 269-348 Are the Test Cases for expressions and returns 
                              "0 || 1",                  // AST's and also creates png and dot files. 
                              "1 || 0 || 1",
                              "0 && 1",
                              "1 && 0 && 1",
                              "0 || 1",
                              "1 || 0 && 1 || 1",
                              "4 == 5",
                              "4 != 5",
                              " 1 == 2 != 3 == 4",
                              "4 < 5",
                              "4 > 5 < 6 == 7 != 0",
                              "2 + 3",
                              "2 + 3 * 4",
                              "6 == 2 * 3",
                              "!5",
                              "!0",
                              "!-15",
                              "-!250",
                              "-!0",
                              "2**29",
                              "(1 + 1)**(4 * 5)",
                              "(2<5)==(3-2)",
                              "1 + 5 || 0 == 2 - 1",
                              "(! 1+1) == 1",
                              "+(-5)",
                              "-(+5)",
                              "+-(-+5)",
                              "(+2 + -3)",
                              "-(4 + 3) / -2",
                              "-2 * -(3 + 4)",
                              "(2 + 3) * -(4 / 2)",
                              "-10 / (1 + -(2 * -3))",
                              "-(-(-2 + -3) * -4) + -5",
                              "-2 * -(-3 + -4) / +2",
                              "-10 / -(+(-1 - -2)) * -3",
                              "2 - (3 * 4) + 5",
                              "2 * (3 + 4) - 5",
                              "2 * (3 + 4) - -5",
                              "-(2 + 3 + 4) - 5",
                              // error cases
                              "2 @ 3",
                              "2 = 3",
                              "(2 + 3(",
                              "2 + -(3 * 4)) + 5",
                              "2 + -(-(3 * 4) + 5"
                              // test for short circuiting
                          //  "1 || (1 / 0)",
                          //  "0 && (1 / 0)",
                             };
      int i = 0;
      for (final String str : expressions)
      {
         try
         {
            System.out.print("Parsing exp "+i+": " + str);
            Tree<Token> ast = parse( str );
            System.out.println( " ... Parsed" );
            // Print the Tree using its toString() method.
            System.out.println( "AST = " + ast + "\n" );
            // Create dot and png files from the tree.
            final String baseName = String.format("Hw2_Exp_%02d_AST", i);
            Tree2dot.tree2dot(ast, baseName);
            Tree2png.tree2png(baseName);
         }
         catch (TokenizeError e)
         {
            System.out.println( " ... Failed (" + e + ")" );
         }
         catch (ParseError e)
         {
            System.out.println( " ... Failed (" + e + ")" );
         }
         ++i;
      }
   }
}
