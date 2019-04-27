import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
	
	public static ArrayList<String> tokenize(String s){		
		Pattern allRegexPattern = Pattern.compile("[0-9.a-zA-Z_]+|\\+|\\*{2}|\\*|=|\\(|\\)|\\/{2}|\\/|-|%");

		ArrayList<String> tokens = new ArrayList<String>();
		Matcher matchString = allRegexPattern.matcher(s);		
		while (matchString.find()){
			String temp = matchString.group();
			if(temp != null) {
				tokens.add(temp);
			}
		}
		return tokens; //returning all the tokens in an array
	}
	
	public static void main(String[] args) {
		tokenize("123+56*num1").forEach(System.out::println);
		System.out.println("------");
		tokenize("(1+ 23) * 9").forEach(System.out::println);
		System.out.println("------");
		tokenize("aa1= (14 - 3) *2/a23").forEach(System.out::println);
		System.out.println("------");
		tokenize("123+abc+123abc").forEach(System.out::println);
    	System.out.println("------");
		tokenize("123+56").forEach(System.out::println);
    	System.out.println("------");
		tokenize("123+56*num1").forEach(System.out::println);
    	System.out.println("------");
		tokenize("(1+ 23) * 9").forEach(System.out::println);
    	System.out.println("------");
		tokenize("aa1= (14 - 3) *2/a23").forEach(System.out::println);
    	System.out.println("------");
    	tokenize("(1 + (2 + 1)) * (78+3*15) +45").forEach(System.out::println);
    	System.out.println("------");
		tokenize("a+b").forEach(System.out::println);
		System.out.println("------");
		tokenize("test=2").forEach(System.out::println);
		System.out.println("------");
		tokenize("major_test=2.2").forEach(System.out::println);
		System.out.println("------");
		tokenize("another_test=.2").forEach(System.out::println);
		System.out.println("------");
		tokenize("justAVariable").forEach(System.out::println);
		System.out.println("------");
		tokenize("test_123 = 5").forEach(System.out::println);
		System.out.println("------");
		tokenize("2=1").forEach(System.out::println);
		System.out.println("------");
		tokenize("a                              = b").forEach(System.out::println);
		System.out.println("------");
		tokenize("3 3").forEach(System.out::println);
		System.out.println("------");
		tokenize("3=3").forEach(System.out::println);
		System.out.println("------");
		tokenize("3/3-3").forEach(System.out::println);
	}
}
