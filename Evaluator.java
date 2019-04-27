import java.util.ArrayList;
import java.util.regex.Pattern;


/*
<assign> -> <ID> = <expr>
<expr> -> <expr> + <term>
        | <term>
<term> -> <term> * <factor>
        | <factor>
<factor> -> (<expr>)
        | <id>
 */
public class Evaluator {

	public static Integer evalExpr(String expression) {
		ArrayList<String> tokens = Tokenizer.tokenize(expression); 
		return evalExprHelper(tokens, 0, tokens.size() - 1);
	}
	
	public static Integer evalExprT(ArrayList<String> tokens) {
		//ArrayList<String> tokens = Tokenizer.tokenize(expression); 
		return evalExprHelper(tokens, 0, tokens.size() - 1);
	}
	
	public static Integer modulus(Integer x, Integer y) {
		return x % y;
	}
	
	
	public static Integer exponent(Integer x, Integer y) {
		return (int) Math.pow(x, y);
	}
	
	public static Integer floorDecimal(Integer x, Integer y) {
		return Math.floorDiv(x, y);
	}
	
	public static void evalAssign(ArrayList<String> tokens) {
        //System.out.println("Eval Assign: " + start + " " + end);
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).equals("=")) {
				for(int j = i + 1; j < tokens.size(); j++) {
					temp.add(tokens.get(j));
				}
				for(int k = 0; k < temp.size(); k++) {
					if (Interpreter.variables.containsKey(temp.get(k))) {
						temp.set(k, Interpreter.variables.get(temp.get(k)).toString());
						// Evaluate token variables after the equal sign.
					}
				}
				Interpreter.variables.put(tokens.get(i - 1), evalExprT(temp));
				// i - 1 is before the variable. Whatever is before the equal sign is put in the array list. 
			}
		}
    }
	
	public static Integer evalExprHelper(ArrayList<String> tokens, int start, int end) {
		// <expr> -> <expr> + <term> | <term>
		//System.out.println("Eval ExprHelper: " + start + " " + end);
		if(start > end) {
			return null;
		}
		
		Integer i1 = evalTerm(tokens, start, end);
		if(i1 != null) {
			return i1;
		}
		for(int i = start + 1; i <= end; i++) {
			if(tokens.get(i).equals("+")) {
				i1 = evalExprHelper(tokens, start, i - 1);
				//System.out.println("Eval ExprHelper: i1 " + i1);
				if(i1 != null) {
					Integer i2 = evalTerm(tokens, i + 1, end);
					//System.out.println("Eval ExprHelper: i2 " + i2);
					if(i2 != null) {
						//System.out.println("Eval ExprHelper: i1 + i2 = " + (i1 + i2));
						return i1 + i2;
					}
				}
			} else if(tokens.get(i).equals("-")) {
				i1 = evalExprHelper(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalTerm(tokens, i + 1, end);
					if(i2 != null) {
						return i1 - i2;
					}
				}
			}
		}
		return evalTerm(tokens, start, end);
	}
	
	public static Integer evalTerm(ArrayList<String> tokens, int start, int end) {
		//tokens.forEach(System.out::println);
		//System.out.println("Eval Term: " + start + " " + end);
		// <term> -> <term> * <factor> | <factor>
		if (start > end) {
			return null;
		}
		Integer i1 = evalFactor(tokens, start, end);
		if(i1 != null) {
			return i1;
		}
		
		for(int i = start + 1; i <= end; i++) {
			if (tokens.get(i).equals("*")) {
				i1 = evalTerm(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalFactor(tokens, i + 1, end);
					if(i2 != null) {
						return i1 * i2;
					}
				}
			} else if(tokens.get(i).equals("/")) {
				i1 = evalTerm(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalFactor(tokens, i + 1, end);
					if(i2 != null) {
						return i1 / i2;
					}
				}
			} else if(tokens.get(i).equals("%")) {
				i1 = evalTerm(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalFactor(tokens, i + 1, end);
					if(i2 != null) {
						return modulus(i1, i2);
					}
				}
			} else if(tokens.get(i).equals("//")) {
				i1 = evalTerm(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalFactor(tokens, i + 1, end);
					if(i2 != null) {
						return floorDecimal(i1, i2);
					}
				}
			} else if(tokens.get(i).equals("**")) {
				i1 = evalTerm(tokens, start, i - 1);
				if(i1 != null) {
					Integer i2 = evalFactor(tokens, i + 1, end);
					if(i2 != null) {
						return exponent(i1, i2);
					}
				}
			}
		}
		
		return evalFactor(tokens, start, end);
	}
	
	public static Integer evalFactor(ArrayList<String> tokens, int start, int end) {
		//<factor> -> (<expr>) | <id>
		//System.out.println("Eval Factor: " + start + " " + end);
		if(start > end) {
			return null;
		}
		
		Integer i1 = evalID(tokens, start, end);
		
		if(i1 != null) {
			return i1;
		}
		
		if((tokens.get(start).equals("(") && tokens.get(end).equals(")") 
				&& evalExprHelper(tokens, start + 1, end - 1) != null)) {
			i1 = evalExprHelper(tokens, start + 1, end - 1);
			//System.out.println("Hello" + i1);
			if(i1 != null) {
				return i1;
			}
		}
		
		return evalID(tokens, start, end);
	}
	
	public static Integer evalID(ArrayList<String> tokens, int start, int end) {
		//System.out.println("Eval ID: " + start + " " + end);
		if(start != end) {
			return null;
		}
		
		String s = tokens.get(start);
		// The get() method is used to retrieve a particular variable value from a class.
		if(s.matches("\\d+")) {
			return Integer.parseInt(s);
			// parseInt() is string to integer
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(evalExpr("15**2")); // 225
		System.out.println(evalExpr("234")); // 234
		System.out.println(evalExpr("1 + 3"));	// 4
		System.out.println(evalExpr("4 * 3"));	// 12
		System.out.println(evalExpr("6 - 1"));	// 5
		System.out.println(evalExpr("9 / 3"));	// 3.0
		System.out.println(evalExpr("9 // 3")); // 3
		System.out.println(evalExpr("2 % 10"));	// 2
		System.out.println(evalExpr("15//7")); // 2
		System.out.println(evalExpr("(1 + 3)")); //4
		System.out.println(evalExpr("(1 + 3) * 3"));	// 12
		System.out.println(evalExpr("4 + 6 + (1 * 3 + 10)"));	// 23
		System.out.println(evalExpr("(1 + (2 + 1)) * 45"));	// 180
		System.out.println(evalExpr("(1 + (2 + 1)) * (78+3*15) +45"));	// 537
		System.out.println(evalExpr("(((((((((((((((((1+23)))))))))))))))))"));	// 24
		System.out.println(evalExpr("1 + 1 * 5")); //6
		System.out.println(evalExpr("(1 + 1 * 5)"));	// 6
		System.out.println(evalExpr("(1 + 7%2) * 45"));	// 90
		System.out.println(evalExpr("(1 + 8 // 3) * 45"));	// 135	
		System.out.println(evalExpr("(1 + (5-2)) * 45"));	// 180		
		System.out.println(evalExpr("(1 +")); // null
		System.out.println(evalExpr("1 + * 2")); // null
	}
}
