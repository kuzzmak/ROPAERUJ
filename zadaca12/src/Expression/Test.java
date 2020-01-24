package Expression;

public class Test {

	public static void main(String[] args) {

		IExpression iff = new IF();
		((IF) iff).addOutput(new IF());
		
		IExpression right = new Terminal("Right", Action.RIGHT);
		
		((IF) iff).addOutput(right);
		System.out.println(iff);

	}

}
