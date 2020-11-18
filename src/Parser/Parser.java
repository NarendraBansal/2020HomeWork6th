package Parser;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Parser 
{
	private static ArrayList<Statement> theListOfStatements = new ArrayList<Statement>();
	
	public static ArrayList<Statement> getParsedStatements()
	{
		return Parser.theListOfStatements;
	}
	
	public static void display()
	{
		for(Statement s : theListOfStatements)
		{
			System.out.println(s);
		}
	}
	
	static ResolveExpression parseResolve(String name)
	{
		//parse this string into language objects
		//turn remember syntax into a ResolveStatement
		ResolveExpression rs = new ResolveExpression(name);
		return rs;
	}
	static DoMathExpression parseDoMath(String expression)
	{
		//do-math do-math a + 7 + 4 - doesn't work for this YET!
		//make the above work for HW
		
		//do-math a + 7 - will work for this
		// (resolve expression a) + (int_lit expression 7)
		//right now we are assuming only a single level of do-math
		String[] theParts = expression.split("\\s+");
		Expression left = Parser.parseExpression(theParts[1]);
		String math_op = theParts[2];
		Expression right = Parser.parseExpression(theParts[3]);
		
		//create and return an instance of DoMathExpression
		DoMathExpression theResult = new DoMathExpression(left, math_op, right);
		return theResult;
	}
	
	static DoMultiMathExpression parseDoMultiMath(String expression)
	{
		//do-math do-math a + 7 + 4 - doesn't work for this YET!
		//make the above work for HW
		
		//do-math a + 7 - will work for this
		// (resolve expression a) + (int_lit expression 7)
		//right now we are assuming only a single level of do-math
		String[] mathParts = expression.split("do-math");
		DoMultiMathExpression dme=null;
		ArrayList<DoMultiMathExpression> al = new ArrayList<DoMultiMathExpression>();
		for(String mp:mathParts) {
			String[] theParts = mp.split("\\s+");
			if(theParts.length>1) {
			Expression left = Parser.parseExpression(theParts[1]);
			String math_op = theParts[2];
			Expression right = Parser.parseExpression(theParts[3]);
			
			if(theParts.length>4) {
				dme = new DoMultiMathExpression(theParts[4],left, math_op, right);
			}
			else {
				dme = new DoMultiMathExpression(null,left, math_op, right);
			}
			al.add(dme);
			//create and return an instance of DoMathExpression
		}	
		}
		dme.list=al;
		
		return dme;
	}
	
	static LiteralExpression parseLiteral(String value)
	{
		//We ONLY have a single LiteralType - int literal
		return new Int_LiteralExpression(Integer.parseInt(value));
	}
	
	static RememberStatement parseRemember(String type, String name, Expression valueExpression)
	{
		//parse this string into language objects
		//turn remember syntax into a RememberStatement
		RememberStatement rs = new RememberStatement(type, name, valueExpression);
		return rs;
	}
	
	public static void parse(String filename)
	{
		try
		{
			Scanner input = new Scanner(new File(System.getProperty("user.dir") + 
					"/src/" + filename));
			//builds a single string that has the contents of the file
			String fileContents = "";
			while(input.hasNext())
			{
				fileContents += input.nextLine().trim();
			}
			
			String[] theProgramLines = fileContents.split(";");
			for(int i = 0; i < theProgramLines.length; i++)
			{
				parseStatement(theProgramLines[i]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("File Not Found!!!");
		}
	}
	
	static Expression parseExpression(String expression)
	{
		//determine which kind of expression this is, and parse it
		//right now we only have a single kind of expression (ResolveExpression)
		//Possible expressions types:
		// do-math, resolve, literal
		String[] theParts = expression.split("\\s+");
		
		StringTokenizer st = new StringTokenizer(expression, "do-math");
		int countTok=st.countTokens();
		
		if(countTok>1)
		{
			//must be a do-math expression
			return Parser.parseDoMultiMath(expression);
		}
		
		
		else if(Character.isDigit(theParts[0].charAt(0))) //does the value start with a number
		{
			//must a literal expression
			return Parser.parseLiteral(expression);
		}
		else
		{
			//must be a var name
			return Parser.parseResolve(expression);
		}
	}
	
	//parses the top level statements within our language
	static void parseStatement(String s)
	{
		//split the string on white space (1 or more spaces)
		String[] theParts = s.split("\\s+");
	
		
		if(theParts[0].equals("remember"))
		{
			int posOfEqualSign = s.indexOf('=');
			String everythingAfterTheEqualSign = s.substring(posOfEqualSign+1).trim();
	
			//parse a remember statement with type, name, and value
			theListOfStatements.add(Parser.parseRemember(theParts[1], 
					theParts[2], Parser.parseExpression(everythingAfterTheEqualSign)));
			
		
		}
	}
}
