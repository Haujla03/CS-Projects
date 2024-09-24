/*
 Course: CS 31600
 Name: Harbir Aujla
 Email: aujla@pnw.edu
 Assignment: 2

*/

import tokenizer.*;

/*
   This program parses the strings and returns the value of the string expressions.  
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
public class Hw2_Parse2Value
{
   public static int eval(String str)
   {
      // Tokenize the input string.
      final Tokenizer tokens = new Tokenizer(str);
      // Parse the list of tokens.
      final int result = getE(tokens);
      // There shouldn't be any remaining tokens.
      if ( ! tokens.isEmpty() )
      {
         throw new ParseError("unexpected tokens, found " + tokens.peek());
      }
      return result;
   }


   /**
      E ::= Or ( || Or )*

      This is not a short-circuited "or" operation.
   */
   public static int getE(Tokenizer tokens)               // Lines 52-77 Get E method parses the Or string expressions
   {                                                     // and returns the evaluated strings. 
    
    final int s1= getOr(tokens);                       // Line 55 Consumes the first Or token. 
    
    if( !tokens.isEmpty() && tokens.peek().lexeme.equals("||") )  // Line 57 checks if the tokens is empty or if it
    {                                                            // contains "or" operator. 
      
      final Token tkn = tokens.next();               // Line 57-58 Consumes the "||" token and the second "Or" token.
      final int s2 = getE(tokens);
     
      if( (s1==0) && (s2==0))                      // Line 63-70 Evaluates the Or expression and returns 0 if true
      {                                            // and 1 if false. 
        return 0;
      }
      else
      {
        return 1;
      }
    }
      return s1;                                // Line 72 Returns the Evaluated result. 
   }


   /**
      Or ::= Eq ( && Eq )*

      This is not a short-circuited "and" operation.
   */
   private static int getOr(Tokenizer tokens)                  // Lines 81-102  Get Or method parses the Eq expression 
   {                                                          // and returns the evaluated strings. 
      int p = getEq(tokens);                                 // Line 83 Consumes the first Eq token. 
      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("&&") ) )
      {
                                                     // Line 84 checks if the tokens is empty or if it "&&" operator.
        final Token tkn = tokens.next();
        int secondVal = getEq(tokens);               // Lines 87-88 Consumes the "&&" token and the second "Eq" token.
        
         if ( ((p !=0 ) &&  (secondVal !=0) ) )     // Lines 90-97 Evaluates the && expression and returns 1 if false,
         {
            p = 1;                                 // 0 if true. 
         }
         else
         {
           p = 0;
         }
      }
      return p;                                 // Line 99 Returns the Evaluted Result. 
   }


   /**
      Eq ::= R ( ( '==' | '!=' ) R )*
   */
   private static int getEq(Tokenizer tokens)      // Lines 106-141 Get Eq method parses the == and != expression and
   {                                              // returns the evaluated strings. 

      int c = getR(tokens);                                              // Line 109 Consumes the first R token. 
      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("==")
                                   || tokens.peek().lexeme.equals("!=") ) )
      {
                                           // Line 110-111 checks if the tokens is empty or if it "==" or "!=" operator.
        final Token tkn = tokens.next();                    // Lines 114-115 Consumes the == or != token and the second 
        int secondVal = getR(tokens);                      // R token. 
         if ( tkn.lexeme.equals("==") )
         {
           if( c == secondVal)
           {                                             // Lines 116-126 Checks if tkn matches == and if the R tokens 
            c = 1;                                      // are equal. If equal then returns 1, if not returns 0. 
           }
           else
           {
            c = 0;
           }
         }
         else if ( tkn.lexeme.equals("!=") )           // Lines 127-137 Checks if tkn matches != and if the R tokens
         {                                            // are not equal. If they do not equal then returns 0, if it does
           if( c == secondVal)                       // equal it returns 1. 
           {
            c = 0;
           }
           else
           {
            c =  1;
           }
         }
      }
      return c;                                    // Line 139 Returns the evaluated result. 

   }


   /**
      R ::= A ( ( '<' | '<=' | '>' | '>=' ) A)*
   */
   private static int getR(Tokenizer tokens)     // Lines 147-208 Get R method parses the <,<=,>,>= expression and
   {                                            // returns the evaluated strings. 
       
        int b = getA(tokens);                // Line 150-155 Consumes the first A token. Checks if the token is empty or  
      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("<")       // if it contains the <,<=,>,>= operator.  
                                   || tokens.peek().lexeme.equals("<=")
                                   || tokens.peek().lexeme.equals(">")
                                   || tokens.peek().lexeme.equals(">=") ) )
      {
        
        final Token tkn = tokens.next();               // Lines 157-158 Consumes the operators and second A token.
        int secondVal = getA(tokens);
         if ( tkn.lexeme.equals("<") )           // Lines 159-169 Checks if tkn matches < and if the A tokens satisfies
         {                                      // the expression. If < then it returns 1, if not returns 0. 
            if(b < secondVal)
            {
              b = 1;
            }
            else
            {
             b = 0;
            }
         }
         else if ( tkn.lexeme.equals("<=") )   // Lines 170-180 Checks if tkn matches <= and if the A tokens satisfies
         {                                    // the expression. If <= then it returns 1, if not returns 0. 
            if( b <= secondVal)
            {
              b = 1;
            }
            else
            {
              b = 0;
            }
         }
         else if ( tkn.lexeme.equals(">") )     // Lines 181-191 Checks if tkn matches > and if the A tokens satisfies
         {                                     // the expression. If > then it returns 1, if not returns 0. 
           if(b > secondVal)
           {
             b = 1;
           }
           else
           {
             b = 0;
           }
         }
         else if (tkn.lexeme.equals(">=") )   // Lines 192-202 Checks if tkn matches >= and if the A tokens satisfies
         {                                   // the expression. If >= then it returns 1, if not returns 0. 
           if( b >= secondVal)
           {
             b = 1;
           }
           else
           {
             b = 0;
           }
         }
           
      }
      return b;                             // Line 205 Returns the evaluated result. 

     
   }


   /**
      A ::= T ( [ '+' | '-' ] T )*
   */
   private static int getA(Tokenizer tokens)        // Lines 214-231 Get A method parses the + and - expressions and
   {                                               // returns the evaluted strings. 

       int v = getT(tokens);                  // Lines 217-219 Consumes the first T token. Checks if the tokens is 
      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("+")   // empty or if it contatins + or - operators. 
                                   || tokens.peek().lexeme.equals("-") ) )
      {
         if ( tokens.match("+") )                          // Lines 221-228 Checks if tokens matches + and adds the
         {                                               // T tokens. Also, Checks if token matches - and subtracts the 
            v += getT(tokens);                           // T tokens. 
         }
         else if ( tokens.match("-") )
         {
            v -= getT(tokens);
         }
      }
      return v;                                      // Line 230 Returns the evaluated result. 
   }


   /**
      T ::= F ( [ '*' | '/' | '%' ] F )*
   */
   private static int getT(Tokenizer tokens)          // Lines 237-264 Get T method parses the *,/,% expressions and 
   {                                                 // and returns the evaluated strings. 

        int e = getF(tokens);             // Lines 240-243 Consumes the first F token. Checks if the token is empty or
      while ( ! tokens.isEmpty() && ( tokens.peek().lexeme.equals("*")       // contains the *, /, % operators. 
                                   || tokens.peek().lexeme.equals("/")
                                   || tokens.peek().lexeme.equals("%") ) )
      {
         final Token tkn = tokens.next();                  //Lines 245-246 Consumes the *,/,% operators and the second
         int secondVal = getF(tokens);                    // F token. 
         
         if ( tkn.lexeme.equals("*") )                   // Lines 248-259 Checks if tokens equals *,/, or % operators
         {                                               // and evalutes the expression. 
           e = e * secondVal;
         }
         else if ( tkn.lexeme.equals("/") )
         {
            e =  e / secondVal;
         }
         else if ( tkn.lexeme.equals("%") )
         {
           e = e % secondVal;
         }
      }
      return e;                                     // Line 261 Returns the evaluated result. 

      
   }


   /**
      F ::= ( '+' | '-' | '!' ) F  // unary plus, minus, not operators
          | B [ '**' F ]           // binary exponentiation operator   
   */
   private static int getF(Tokenizer tokens) //Lines 271-305 Get F method parses the unary operators and exponentiation
   {                                               // expressions and returns the evaluated strings. 
    
     if ( tokens.match("+") )                      // Lines 274-292 Checks if token matches +,-,! unary operators and
      {                                           // evaluates the corresponding expressions. 
         return getF(tokens);
      }
      else if ( tokens.match("-") )
      {
         return - getF(tokens);
      }
      else if( tokens.match("!") )
      {
        if(getF(tokens) == 0)
        {
          return 1;
        }
        else
        {
          return 0;
        }
      }
      else                                     //Lines 293-304 Consumes the B token and checks if token is empty or if 
      {                                       // contains ** operator. 
         int s = getB(tokens);
         if( ! tokens.isEmpty() && tokens.match("**")  )
         { 
         return (int)Math.pow(s, getF(tokens));              // Line 298 Returns the evaluated exponentiate result.  
         }
         else
         {
           return s;                                   // Line 302 Returns B token. 
         }
     }   
 }


   /**
      B ::= '(' E ')'
          | N
   */
   private static int getB(Tokenizer tokens)         // Lines 312-328 Get B method parses the (E) and N expressions
   {                                                // and returns the evaluated strings. 
     final int e;
     if ( tokens.match("(") )                   // Lines 315-322 Checks if tokens match "(" and consumes the E token.
      {
         e = getE(tokens);
         if ( ! tokens.match(")") )
         {
            throw new ParseError("expected \")\", found " + tokens.peek());
         }
      }
      else
      {                                           // Lines 323-326 Consumes the N tokens. 
         e = getN(tokens);
      }
      return e;                               // Line 327 Returns the Evaluated result. 
   }


   /**
      N ::= D ( D )*
   */
   private static int getN(Tokenizer tokens)
   {
      int result = getD(tokens);
      while ( ! tokens.isEmpty()
           && Character.isDigit(tokens.peek().lexeme.charAt(0)) )
      {
         int d = getD(tokens);
         result = (10 * result) + d;
      }
      return result;
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
      String[] expressions = {                             // Lines 363-436 Are the Test Cases for expressions and 
                              "65432100",                  // prints the expression and the evaluated results. 
                              "0 || 1",
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
                              "(2<5) == (3-2)",
                              "1 + 5 || 0 == 2 - 1",
                              "(! 1+1)==1",
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
            final int r = eval( str );
            System.out.println( " ... Parsed" );
            // Print the result.
            System.out.println( r + " = " + str + "\n" );
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
