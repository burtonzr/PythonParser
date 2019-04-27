import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
	Evaluator eval = new Evaluator();
	String[] tokens;
	public static HashMap<String, Integer> variables = new HashMap<>();
	
	public static boolean isVariable(String s) {
		Pattern p = Pattern.compile("^[A-Za-z][\\_]*[\\w]*");
		Matcher m = p.matcher(s);
		boolean bool = m.matches();
		return bool;

	}
	public static boolean forLoop(String s) {
		return s.matches("\\s*for\\s+[a-zA-Z_$][a-zA-Z_$0-9]*\\s+in\\s+range\\(\\d*\\):");
	}
	
	public static void interpret(String s) {
		ArrayList<String> tokens = Tokenizer.tokenize(s);
			
			if (forLoop(s)) {
				@SuppressWarnings("unused")
				String tempVar = "";
				Integer times = 0;
				for (int i = 0; i < tokens.size(); i++) {
					if (tokens.get(i).equals("for")) {
						tempVar = tokens.get(i + 1);
					}
					if (tokens.get(i).equals("range")) {
						times = Integer.parseInt(tokens.get(i + 2));
					}
				}
				@SuppressWarnings("resource")
				Scanner scan = new Scanner(System.in);
				String nextLine;
				System.out.print("...");
				nextLine = scan.nextLine();
				if (!nextLine.startsWith("	")) {
					System.out.print("Indentation Error: expected an indented block.");
				}	
				if (nextLine.trim().startsWith("print")) {
					nextLine = nextLine.replace("(", "");
					nextLine = nextLine.replace(")", "");
					nextLine = nextLine.replace("print", "");
					
					for (int i = 0; i < times; i++) {	
						if (nextLine.startsWith("\"") || nextLine.endsWith("\"")) {
							System.out.print(nextLine.replace("\"", ""));
						} else if (!nextLine.startsWith("\"")) {
							if (!SimpleParser.isExpr(nextLine)) {
								System.out.print("Syntax error!");
								break;
							} else {
								System.out.print(Evaluator.evalExpr(nextLine));
							}
						}
						System.out.print("\n");

					}
					System.out.print("\n");
				}
					
			} else if(isVariable(tokens.get(0)) || SimpleParser.isExpr(s)) {
				Evaluator.evalAssign(tokens);
				for(int i = 0; i < tokens.size(); i++) {
					if (variables.containsKey(tokens.get(i))) {
						tokens.set(i, variables.get(tokens.get(i)).toString());
						// Get tokens and replace them with the value of the variable to evaluate them. 
					}
				}
				Integer result = Evaluator.evalExprT(tokens);
				if (result != null) {
					System.out.println(result.toString());
				} else if(tokens.get(0).equals("print")) {
					System.out.println(Evaluator.evalExpr(s.substring(5, s.length())));
					// Since print is 5 characters in length, the substring method only evaluates
					// characters after the print. 
				}
			} else {
				System.out.println("Syntax error!");
			}	
		}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String userInput;
		
		while(true) {
			System.out.println(">>>");
			userInput = scan.nextLine().trim();
			if(userInput.equals("q")) {
				break;
			} else if(userInput.equals("quit()")) {
				System.exit(0);
			} else if(userInput.equals("locals()")) {
				
			} else if (!userInput.equals("")) {
				interpret(userInput);
			}
			userInput = null;
		}
	}
	
}
