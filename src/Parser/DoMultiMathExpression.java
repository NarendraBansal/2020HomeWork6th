package Parser;

import java.util.ArrayList;

public class DoMultiMathExpression extends Expression 
{
	public String betweenOperator;
	private Expression left;
	private Expression right;
	private String op;
	
	public ArrayList <DoMultiMathExpression> list=new ArrayList<DoMultiMathExpression>();
	
	public DoMultiMathExpression(String betweenOperator,Expression left, String op, Expression right)
	{
		super("Do-Math Expression");
		this.betweenOperator=betweenOperator;
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	public String toString()
	{
		return super.toString() + "\n\t" + this.left.toString() + " "
				+ this.op + " " + this.right.toString();
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	public String getOp() {
		return op;
	}

	
	
}
