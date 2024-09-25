/*
 Course: CS 31600
 Name: Harbir Aujla
 Email: aujla@pnw.edu
 Assignment: 2

*/

import tokenizer.*;
import tree.*;

/*
  This program parses the strings and returns the parse-trees of the string expressions. 
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
public class Hw2_Parse2Syntax
{
   public static Tree<String> parse(String stringInE11)
   {
      // Tokenize the input string.
      final Tokenizer tokens = new Tokenizer(stringInE11);
      // Parse the list of tokens.
      final Tree<String> parseTree = getE(tokens);
      // There shouldn't be any remaining tokens.
      if ( ! tokens.isEmpty() )
      {
         throw new ParseError("unexpected tokens, found " + tokens.peek());
      }
      return parseTree;
   }


   /**
      Left recursion,

         E ::= E [ '||' Or ]

      converted to iteration,

         E ::= Or ( '||' Or )*
   */
   public static Tree<String> getE(Tokenizer tokens)    // Lines 57-73 Get E method parses the Or string expressions
   {                                                    // and returns the parse trees. 
     
     Tree<String> parse = new LinkedTree<String>("E", getOr(tokens)); // Line 60 Creates the parse trees of "E",Or tokens. 
    
     
     while ( ! tokens.isEmpty() && tokens.peek().lexeme.equals("||"))  // Line 56  checks if the tokens is empty or                              
      {                                                               // if it contains || operator. 
         final Token tk = tokens.next();                            // Line 65 Consume the || operator. 
         final Tree<String> subTree = getOr(tokens);               //Lines 66-71  Recursion and creates parse tree
         parse = new LinkedTree<String>("E",                       // with the expression. 
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                         
      }
      return parse;                                               // Line 72 Returns the Parse Tree. 
   }


   /**
      Left recursion,

         Or ::= Or [ '&&' Eq ]

      converted to iteration,

         Or ::= Eq ( '&&' Eq )*
   */
   private static Tree<String> getOr(Tokenizer tokens) // Lines 85-102 Get Or method parses the Eq string expressions
   {                                                  // // and returns the parse trees. 
     Tree<String> parse = new LinkedTree<String>("Or", getEq(tokens));  // Line 87 Creates the Parse Tree of "Or",Eq tokens. 

     
     
       while ( ! tokens.isEmpty() && tokens.peek().lexeme.equals("&&"))  // Line 91  checks if the token is empty or
                                                                        // if it conatins && operator. 
      {
         final Token tk = tokens.next();                          // line 94 Consume the && operator. 
         final Tree<String> subTree = getEq(tokens);              // Line 95-100 Recursion and creates parse trees with
         parse = new LinkedTree<String>("Or",                     // the expressions.  
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                           
     }
     return parse;                                         // Line 101 Returns the Parse Trees. 
   }


   /**
      Left recursion,

         Eq ::= Eq [ ( '==' | '!=' ) R ]

      converted to iteration,

         Eq ::= R ( ( '==' | '!=' ) R )*
   */
   private static Tree<String> getEq(Tokenizer tokens) // Lines 114-131 Get Eq method parses the ==, != string expressions
   {                                                  // and returns the parse trees. 
       
     Tree<String> parse = new LinkedTree<String>("Eq", getR(tokens)); // Line 117 Creates Parse Trees of "Eq",R tokens. 

     
     
       while ( ! tokens.isEmpty() && (tokens.peek().lexeme.equals("==")   // Line 121-122 checks if the token is empty or 
                                  || tokens.peek().lexeme.equals("!=") ) ) // if it contains ==, != operators. 
      {
         final Token tk = tokens.next();                         // Line 124 Consume the ==, != operators.
         final Tree<String> subTree = getR(tokens);             // Line 125-130 Recursion and creates the parse trees 
         parse = new LinkedTree<String>("Eq",                   // with the expressions. 
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                           
     }
     return parse;                                             // Line 131 Returns the Parse Trees. 
   }


   /**
      Left recursion,

         R ::= R [ ( '<' | '<=' | '>' | '>=' ) A ]

      converted to iteration,

         R ::= A ( ( '<' | '<=' | '>' | '>=' ) A)*
   */
   private static Tree<String> getR(Tokenizer tokens)  // Lines 144-163 Get R method parses the <,<=,>,>= string expressions
   {                                                  // and returns the parse trees. 

      Tree<String> parse = new LinkedTree<String>("R", getA(tokens));  // Line 147 Creates the Parse Trees of "R",A tokens. 
     
     
       while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("<")   // Lines 118-121 Checks if the token is empty  
                                    || tokens.peek().lexeme.equals("<=") // or if it contains <,<=,>,>= operators.
                                    || tokens.peek().lexeme.equals(">") 
                                    || tokens.peek().lexeme.equals(">=") ) )
      {
         final Token tk = tokens.next();                         // Line 155 Consume the <,<=,>,>= operators.
         final Tree<String> subTree = getA(tokens);             // Line 156-161 Recursion and creates the parse trees 
         parse = new LinkedTree<String>("R",                  // with the expressions. 
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                            
     }
     return parse;                                // Line 162 Returns the Parse Trees. 
   }


   /**
      Left recursion,

         A ::= A [ ( '+' | '-' ) T ]

      converted to iteration,

         A ::= T ( [ '+' | '-' ] T )*
   */
   private static Tree<String> getA(Tokenizer tokens)   // Lines 175-192 Get A method parses the +,- string expressions
   {                                                  // and returns the parse trees. 
      
      Tree<String> parse = new LinkedTree<String>("A", getT(tokens));  // Line 178 Creates the Parse Trees of "A",T tokens. 
     
     
       while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("+")   // Lines 141-142 Checks if the token is empty
                                    || tokens.peek().lexeme.equals("-") ) )  // or if it contains +,- operators. 
      {
         final Token tk = tokens.next();                              // Line 184 Consume the +,- operators.
         final Tree<String> subTree = getT(tokens);                  // Lines 185-190 Recursion and creates the parse trees
         parse = new LinkedTree<String>("A",                         // with the expressions. 
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                           
     }
     return parse;                                         // Line 191 Returns the Parse Trees. 
   }


   /**
      Left recursion,

         T ::= T [ ( '*' | '/' | '%' ) F ]

      converted to iteration,

         T ::= F ( [ '*' | '/' | '%' ] F )*
   */
   private static Tree<String> getT(Tokenizer tokens)   // Lines 157-173 Get T method parses the *,/,% string expressions
   {                                                    // and returns the parse trees. 

      Tree<String> parse = new LinkedTree<String>("T", getF(tokens));  // Line 207 Creates the Parse Trees of "T",F tokens. 
     
     
       while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("*")  // Line 210-212 Checks if the token is empty or
                                    || tokens.peek().lexeme.equals("/")  // if it contains *,/,% operators. 
                                    || tokens.peek().lexeme.equals("%") ) )
      {
         final Token tk = tokens.next();                          // Line 214  Consume the *,/,% operators.
         final Tree<String> subTree = getF(tokens);              // Line 215-220 Recursion and creates parse trees with
         parse = new LinkedTree<String>("T",                     // the expressions. 
                                        parse, 
                                        new LinkedTree<String>(tk.toString()),
                                        subTree);                                                            
     }
     return parse;                                          // Line 221 Returns the Parse Trees. 
   }


   /**
      F ::= ( '+' | '-' | '!' ) F  // unary plus, minus, not operators
          | B [ '**' F ]           // binary exponentiation operator
   */
   private static Tree<String> getF(Tokenizer tokens)  // Lines 229-262 Get F method parses the unary and exponentiate string expressions
   {                                                  // and returns the parse trees. 
      final Tree<String> parse;
      if ( tokens.peek().lexeme.equals("-")           // Lines 232-234 Checks if the tokens is empty or if it contains
        || tokens.peek().lexeme.equals("+")          // -,+,! operators. 
        || tokens.peek().lexeme.equals("!") )
      {
        final Token tk = tokens.next();                // Line 236-237 Consume the +,-,! operators and F tokens.
         final Tree<String> subTree = getF(tokens);
         parse = new LinkedTree<String>("F",                 // Line 238 Creates parse trees with the unary expressions. 
                                  new LinkedTree<String>(tk.toString()),
                                  subTree);
      }
      else  
      {
          Tree<String> s = getB(tokens);     // Lines 244-245 Consumes the B token and checks if token is empty or if it
        if( ! tokens.isEmpty() &&  tokens.peek().lexeme.equals("**") )   // contains ** operator. 
        {
         final Token tk1 = tokens.next();                 // Line 247 Consume the "**" operator. 
         final Tree<String> subTree = getF(tokens);
         parse = new LinkedTree<String>("F",               // Line 248-252 Creates the parse trees with the exponentiate expressions.
                                        new LinkedTree<String>(tk1.toString()),
                                        subTree);
      }
      else
      {
        parse = new LinkedTree<String>("F",                  //  // Lines 253-259 Creates the parse trees with B tokens. 
                                       new LinkedTree<String>(s.toString()));
       
      }
     }

      return parse;                             // Line 261 Returns the Parse Trees. 
   }


   /**
      B ::= '(' E ')'
          | N
   */
   private static Tree<String> getB(Tokenizer tokens)  //Lines 269-298 Get B method parses the (E), N string expressions
   {
                                                        // and returns the parse trees. 
      Tree<String> parse = new LinkedTree<String>("B");    // Line 272 Creates parse trees with "B".
                                  
     
     if ( tokens.peek().lexeme.equals("E") )    // Line 275 Checks if the token matches "E".    
      {
         final Token tk1 = tokens.next();               // Line 277 Consume the "E" token. 
         final Tree<String> subTree = getE(tokens);      // Line 279-881 Creates parse trees with expression. 
         parse = new LinkedTree<String>("B",               
                                  new LinkedTree<String>(tk1.toString()),
                                  subTree);
     }
      else if( tokens.match("(") )
      {
        parse = getE(tokens);         // Lines 284-290 Creates Parse Trees of (E) token. 
        if( ! tokens.match(")") )
        {
           throw new ParseError("expected \")\", found " + tokens.peek());
        }
      }
      else 
      {
        parse = new LinkedTree<String>("B",               // Lines 292-295 Creates Parse Trees of N tokens. 
                                       new LinkedTree<String>(getN(tokens).toString()));
      }
      return parse;                                // Line 296 Returns the Parse Trees. 
     
   }


   /**
      N ::= D ( D )*
   */
   private static Tree<String> getN(Tokenizer tokens)
   {
      int position = tokens.peek().position;
      int result = getD(tokens);
      while ( ! tokens.isEmpty()
           && Character.isDigit(tokens.peek().lexeme.charAt(0)) )
      {
         int d = getD(tokens);
         result = (10 * result) + d;
      }
      return new LinkedTree<>( new Token(result+"", position).toString() );
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
                              "65432100",                         // Lines 333-411 Are the Test Cases for expressions and returns 
                              "0 || 1",                          // Parse Trees and also creates png and dot files. 
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
            Tree<String> parseTree = parse( str );
            System.out.println( " ... Parsed" );
            // Print the Tree using its toString() method.
            System.out.println( "Parse Tree = " + parseTree + "\n" );
            // Create dot and png files from the tree.
            final String baseName = String.format("Hw2_Exp_%02d_Parse", i);
            Tree2dot.tree2dot(parseTree, baseName);
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
