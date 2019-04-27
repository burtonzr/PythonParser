/*QUESTION4:
<assign> -> <id> = <expr>
<expr> -> <expr> + <term>
        | <term>
<term> -> <term> * <factor>
        | <factor>
<factor> -> (<expr>)
        | <id>
*/
import java.util.ArrayList;

public class SimpleParser {
	
	public static boolean isExpr(String expression) {
		ArrayList<String> tokens = Tokenizer.tokenize(expression);
		for(int i = 0; i < expression.length() - 2; i++) {
			for(int j = i + 1; j <= expression.length() - 1; j++) {
				if(isMultipleOperatorCase(expression)) {
					return true;
				} else if (isModulus(expression)) {
					return true;
				} 
				else if (isFloor(expression)) {
					return true;
				}
				else if (isManyOperators(expression)) {
					return true;
				}
			}
		}
		return isExprHelper(tokens, 0, tokens.size() - 1);
	}
	
	public static boolean isExprHelper(ArrayList<String> tokens, int start, int end) {
		if(start > end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			if(tokens.get(i).equals("+") && isExprHelper(tokens, start, i - 1) && isTerm(tokens, i + 1, end)) {
				return true;
			} else if(tokens.get(i).equals("-") && isExprHelper(tokens, start, i - 1) && isTerm(tokens, i + 1, end) ) {
				return true;
			}
		}
		return isTerm(tokens, start, end);
	}
	
	public static boolean isTerm(ArrayList<String> tokens, int start, int end) {
		if(start > end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			if(tokens.get(i).equals("*") && isTerm(tokens, start, i - 1) && isFactor(tokens, i + 1, end)) {
				return true;
			} else if(tokens.get(i).equals("/") && isTerm(tokens, start, i - 1) && isFactor(tokens, i + 1, end) ) {
				return true;
			}
		}
		return isFactor(tokens, start, end);
	}
	
	public static boolean isFactor(ArrayList<String> tokens, int start, int end) {
		if(start > end) {
			return false;
		}
		return (tokens.get(start).equals("(") && tokens.get(end).equals(")") 
				&& isExprHelper(tokens, start + 1, end - 1) || isID(tokens, start, end));
	}
	
	public static boolean isID(ArrayList<String> tokens, int start, int end) {
		if(start != end) {
			return false;
		}
		if(isNumber(tokens.get(start))) {
			return true;
		} else if(isVariableNames(tokens.get(start))) {
			return true;
		}
		
		return false;
	}
		
	public static boolean isNumber(String s) {
		return s.matches("\\d+");
	}
	
	public static boolean isMultipleOperatorCase(String s) {
		return s.matches("\\d*\\s*?[-+\\/*%]\\s*?[-+\\/*%]\\d*");
	}
	
	public static boolean isVariableNames(String s) {
		return s.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
	}
	
	public static boolean isModulus(String s) {
		return s.matches("\\d*[%]\\d*");
	}
	public static boolean isFloor(String s) {
		return s.matches("\\d*[//]\\d*");
	}
	public static boolean isManyOperators(String s) {
		return s.matches("[-+\\/*%]*\\s*[-+\\/*%]\\s*[-+\\/*%]\\s*\\d*");
	}
	
	public static void main(String[] args) {
		System.out.println(isExpr("234"));//true Good
		System.out.println(isExpr("10 / 2")); //true Good
		System.out.println(isExpr("10 - 2")); //true Good
		System.out.println(isExpr("1- -1"));//true Good
		System.out.println(isExpr("1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1"));//true Good
		System.out.println(isExpr("5%5"));//true Good
		System.out.println(isExpr("5//5"));//true Good
		System.out.println(isExpr("1+ +1"));//true Good
		System.out.println(isExpr("-- - + 3"));//true Not Good
		System.out.println(isExpr("1 + 3"));//true Good
		System.out.println(isExpr("1 + 3 * 45"));//true Good
		System.out.println(isExpr("(1 + 3)* 45"));//true Good
		System.out.println(isExpr("(1 + (2 + 1))* 45"));//true Good
		System.out.println(isExpr("(1 + (2 + 1))* (78+3*15) + 45"));//true Good
		System.out.println(isExpr("(((((((((((((((((1+23)))))))))))))))))"));//true Good
		System.out.println(isExpr("num1"));//true Good
		//System.out.println(isExpr("a = 8"));//true Good
		System.out.println(isExpr("1 + "));//false Good
		System.out.println(isExpr("1 + * 2"));//false Good
		System.out.println(isExpr("1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+"));//false Good
		System.out.println(isExpr("11.23 e1"));//false Good
		
	}
}
