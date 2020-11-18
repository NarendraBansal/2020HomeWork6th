package Interpreter;

import java.util.ArrayList;

import Parser.*;

public class SpyderInterpreter 
{
	private static VariableEnvironment theEnv = new VariableEnvironment();
	private static ArrayList<String> theOutput = new ArrayList<String>();
	
	public static void displayResults()
	{
		System.out.println("Current Variable Environment");
		SpyderInterpreter.theEnv.display();
		for(String s : SpyderInterpreter.theOutput)
		{
			System.out.println(s);
		}
	}
	
	public static void interpret(ArrayList<Statement> theStatements)
	{
		for(Statement s : theStatements)
		{
			if(s instanceof RememberStatement)
			{
				//interpret a remember statement
				SpyderInterpreter.interpretRememberStatement((RememberStatement)s);
			}
		}
	}
	
	//determines if a String contains all digits (numbers)
	private static boolean isInteger(String s)
	{
		for(int i = 0; i < s.length(); i++)
		{
			if(!Character.isDigit(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	private static int interpretLiteralExpression(LiteralExpression le)
	{
		if(le instanceof Int_LiteralExpression)
		{
			return ((Int_LiteralExpression) le).getValue();
		}
		throw new RuntimeException("Not a valid literal type...");
	}
	static String prevBetweenOp=null;
	private static int interpretDoMultiMathExpression(DoMultiMathExpression dmexp)
	{
		ArrayList<DoMultiMathExpression> list = dmexp.list;
		int finalResult=0;
		
		for(DoMultiMathExpression dme:list) {
			String betweenOperator=dme.betweenOperator;
			int currentResult=0;
			Expression left = dme.getLeft();
			int leftValue = SpyderInterpreter.getExpressionValue(left);
			Expression right = dme.getRight();
			int rightValue = SpyderInterpreter.getExpressionValue(right);
			String math_op = dme.getOp();
			
			if(math_op.equals("+"))
			{
				currentResult= leftValue + rightValue;
			}
			else if(math_op.equals("-"))
			{
				currentResult= leftValue - rightValue;
			}
			else if(math_op.equals("*"))
			{
				currentResult= leftValue * rightValue;
			}
			else if(math_op.equals("/"))
			{
				currentResult= leftValue / rightValue;
			}
			else if(math_op.equals("%"))
			{
				currentResult= leftValue % rightValue;
			}
			if(prevBetweenOp!=null) {
				betweenOperator=prevBetweenOp;
			}
			if(betweenOperator!=null) {
				if(betweenOperator.equals("+"))
				{
					finalResult= finalResult + currentResult;
				}
				else if(betweenOperator.equals("-"))
				{
					finalResult= finalResult - currentResult;
				}
				else if(betweenOperator.equals("*"))
				{
					finalResult= finalResult * currentResult;
				}
				else if(betweenOperator.equals("/"))
				{
					finalResult= finalResult / currentResult;
				}
				else if(betweenOperator.equals("%"))
				{
					finalResult= finalResult % currentResult;
				}
				prevBetweenOp=betweenOperator;
			}
			//throw new RuntimeException("Not a valid math operator: " + math_op);
		}
		return finalResult;
		
	}
	
	private static int interpretDoMathExpression(DoMathExpression dme)
	{
		Expression left = dme.getLeft();
		int leftValue = SpyderInterpreter.getExpressionValue(left);
		Expression right = dme.getRight();
		int rightValue = SpyderInterpreter.getExpressionValue(right);
		String math_op = dme.getOp();
		
		if(math_op.equals("+"))
		{
			return leftValue + rightValue;
		}
		else if(math_op.equals("-"))
		{
			return leftValue - rightValue;
		}
		else if(math_op.equals("*"))
		{
			return leftValue * rightValue;
		}
		else if(math_op.equals("/"))
		{
			return leftValue / rightValue;
		}
		else if(math_op.equals("%"))
		{
			return leftValue % rightValue;
		}
		throw new RuntimeException("Not a valid math operator: " + math_op);
	}
	
	private static int interpretResolveExpression(ResolveExpression rs)
	{
		
		//only look up the variable in the env if it is not a LITERAL
		//Literal Types: int
		//this try/catch attempts to convert a string to an int and if it fails it
		//looks the string up as a variable name
		try
		{
			//tries to treat it as a int literal
			return Integer.parseInt(rs.getName());	
		}
		catch(Exception e)
		{
			try
			{
				//if not a literal, look it up in our environment
				return SpyderInterpreter.theEnv.getValue(rs.getName());
			}
			catch(Exception e2)
			{
				throw new RuntimeException("Variable " + rs.getName() + " NOT FOUND!");
			}
		}
	}
	
	private static int getExpressionValue(Expression e)
	{
		if(e instanceof ResolveExpression)
		{
			return SpyderInterpreter.interpretResolveExpression((ResolveExpression)e);
		}
		else if(e instanceof LiteralExpression)
		{
			return SpyderInterpreter.interpretLiteralExpression((LiteralExpression) e);
		}
		else if(e instanceof DoMathExpression)
		{
			return SpyderInterpreter.interpretDoMathExpression((DoMathExpression) e);
		}
		else if(e instanceof DoMultiMathExpression)
		{
			return SpyderInterpreter.interpretDoMultiMathExpression((DoMultiMathExpression) e);
		}
		throw new RuntimeException("Not a known expression type: " + e.getExpressionType());
	}
	
	private static void interpretRememberStatement(RememberStatement rs)
	{
		//we need to resolve this expression before we can actually remember anything
		Expression valueExpression = rs.getValueExpression();
		int answer = SpyderInterpreter.getExpressionValue(valueExpression);
		
		SpyderInterpreter.theEnv.addVariable(rs.getName(), answer);
		SpyderInterpreter.theOutput.add("<HIDDEN> Added " + rs.getName() + " = " + answer + " to the variable environment.");
	}
}
