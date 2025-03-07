package neverbuy.util.misc;

import java.util.Stack;

public interface ICalculator {
   default double eval(String expression) {
      Stack<Double> values = new Stack();
      Stack<Character> operators = new Stack();

      for(int i = 0; i < expression.length(); ++i) {
         char c = expression.charAt(i);
         if (!Character.isWhitespace(c)) {
            if (!Character.isDigit(c) && c != '.') {
               if (c == '(') {
                  operators.push(c);
               } else if (c == ')') {
                  while((Character)operators.peek() != '(') {
                     values.push(this.applyOp((Character)operators.pop(), (Double)values.pop(), (Double)values.pop()));
                  }

                  operators.pop();
               } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                  while(!operators.isEmpty() && this.hasPrecedence(c, (Character)operators.peek())) {
                     values.push(this.applyOp((Character)operators.pop(), (Double)values.pop(), (Double)values.pop()));
                  }

                  operators.push(c);
               }
            } else {
               StringBuilder sb;
               for(sb = new StringBuilder(); Character.isDigit(c) || c == '.'; c = expression.charAt(i)) {
                  sb.append(c);
                  ++i;
                  if (i >= expression.length()) {
                     break;
                  }
               }

               --i;
               values.push(Double.parseDouble(sb.toString()));
            }
         }
      }

      while(!operators.isEmpty()) {
         values.push(this.applyOp((Character)operators.pop(), (Double)values.pop(), (Double)values.pop()));
      }

      return (Double)values.pop();
   }

   private boolean hasPrecedence(char op1, char op2) {
      if (op2 != '(' && op2 != ')') {
         return op1 != '*' && op1 != '/' || op2 != '+' && op2 != '-';
      } else {
         return false;
      }
   }

   private double applyOp(char op, double b, double a) {
      double var10000;
      switch(op) {
      case '*':
         var10000 = a * b;
         break;
      case '+':
         var10000 = a + b;
         break;
      case ',':
      case '.':
      default:
         var10000 = 0.0D;
         break;
      case '-':
         var10000 = a - b;
         break;
      case '/':
         if (b == 0.0D) {
            throw new UnsupportedOperationException("Cannot divide by zero");
         }

         var10000 = a / b;
      }

      return var10000;
   }
}
