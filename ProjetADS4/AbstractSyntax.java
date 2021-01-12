import java.util.ArrayList;
import java.awt.Graphics2D;

abstract class Expression {
	public abstract int eval(ValueEnvironment env) throws Exception;
}

class Int extends Expression {
	private int value;

	public Int (int n) {
		this.value = n;
	}

	public int eval (ValueEnvironment env) {
		return this.value;
	}
}

class Var extends Expression {
	private String name;

	public Var (String s) {
		this.name = s;
	}

	public int eval (ValueEnvironment env) throws Exception {
		return env.getValue(name);
	}
}

class Operation extends Expression {
	private Expression left, right;
	private String operateur;

	public Operation (Expression e1, Expression e2, String operateur) {
		this.left = e1;
		this.right = e2;
		this.operateur = operateur;
	}

	public int eval (ValueEnvironment env) throws Exception {
		switch (operateur){
			case "+": return (left.eval(env) + right.eval(env));
			case "-": return (left.eval(env) - right.eval(env));
			case "*": return (left.eval(env) * right.eval(env));
			case "/": return (left.eval(env) / right.eval(env));
		}
		throw new Exception ("Unknown operateur");
	}
}

class Program extends ArrayList<Instruction> {
	public void run(Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		for (Instruction inst : this) {
			inst.exec(g, varEnv, funcEnv);
		}
	}

	public void run (Graphics2D g) throws Exception {
		ValueEnvironment varEnv = new ValueEnvironment();
		FunctionEnvironment funcEnv = new FunctionEnvironment();
		this.addEnvironmentLists(varEnv, funcEnv);
		this.run(g, varEnv, funcEnv);
		this.removeEnvironmentLists(varEnv, funcEnv);
	}


	public static void addEnvironmentLists (ValueEnvironment varEnv, FunctionEnvironment funcEnv) {
		varEnv.addFirst(new ValueList());
		funcEnv.addFirst(new FunctionList());
	}

	public static void removeEnvironmentLists (ValueEnvironment varEnv, FunctionEnvironment funcEnv) {
		varEnv.remove();
		funcEnv.remove();
	}
}

abstract class Instruction {
	public abstract void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception;
}

class Declaration extends Instruction {
	private String name;
	private Expression e;
	private final boolean isConst;

	public Declaration (String name, Expression e, boolean b){
		this.name = name;
		this.e = e;
		this.isConst = b ;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		varEnv.addValue(name, e.eval(varEnv), isConst);
	}
}

class DeclarationFunction extends Instruction {
	private String name;
	private String [] args;
	private Program p;

	public DeclarationFunction (String s, ArrayList<String> var, Program p){
		this.name = s;
		this.args = new String [var.size()];
		for (int i = 0; i < var.size(); i++){
			this.args[i] = var.get(i);
		}
		this.p = p;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		funcEnv.addFunction(name, args, p, varEnv.clone(), funcEnv.clone());
	}
}

class Assignation extends Instruction {
	private String name;
	private Expression e;

	public Assignation (String name, Expression e){
		this.name = name;
		this.e = e;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		varEnv.changeValue(name, e.eval(varEnv));
	}
}

class DoFunction extends Instruction {
	String name;
	Expression [] args;

	public DoFunction (String s, ArrayList<Expression> var){
		this.name = s;
		this.args = new Expression [var.size()];
		for (int i = 0; i < var.size(); i++){
			this.args[i] = var.get(i);
		}
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		Function f = funcEnv.getFunction(name, args.length);
		ValueEnvironment functionVarEnv = f.getDeclarationVarEnv();
		FunctionEnvironment functionFuncEnv = f.getDeclarationFuncEnv();
		Program.addEnvironmentLists(functionVarEnv, functionFuncEnv);
		for (int i = 0; i < args.length; i++){
			functionVarEnv.addValue(f.getArgs(i), args[i].eval(varEnv), false);
		}
		f.getProgram().run(g, functionVarEnv, functionFuncEnv);
		Program.removeEnvironmentLists(functionVarEnv, functionFuncEnv);
	}
}

class Draw extends Instruction {
	private Figure f;

	public Draw (Figure f){
		this.f = f;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		f.draw(g, varEnv);
	}
}

class Bloc extends Instruction {
	private Program p;

	public Bloc (Program p){
		this.p = p;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		Program.addEnvironmentLists(varEnv, funcEnv);
		p.run(g, varEnv, funcEnv);
		Program.removeEnvironmentLists(varEnv, funcEnv);
	}
}

class Conditional extends Instruction {
	private Expression bool;
	private Instruction ifTrue;
	private Instruction ifFalse;

	public Conditional (Expression b, Instruction ifTrue, Instruction ifFalse){
		this.bool = b;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		if (this.bool.eval(varEnv) == 0){
			this.ifTrue.exec(g, varEnv, funcEnv);
		}else{
			this.ifFalse.exec(g, varEnv, funcEnv);
		}
	}
}

class While extends Instruction {
	private Expression e;
	private Program p;

	public While (Expression e, Program p){
		this.e = e;
		this.p = p;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		Program.addEnvironmentLists(varEnv, funcEnv);
		int nbLoop = e.eval(varEnv);
		for (int i = 0; i < nbLoop; i++){
			this.p.run(g, varEnv, funcEnv);
		}
		Program.removeEnvironmentLists(varEnv, funcEnv);
	}
}

class For extends Instruction {
	private String name;
	private Expression begin, end, step;
	private Program p;

	public For(String i, Expression a, Expression b, Expression c, Program p){
		this.name = i;
		this.begin = a;
		this.end = b;
		this.step = c;
		this.p = p;
	}

	public void exec (Graphics2D g, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		Program.addEnvironmentLists(varEnv, funcEnv);
		int stepValue = step.eval(varEnv);
		varEnv.addValue(name, begin.eval(varEnv), false);
		boolean stepPositif = (stepValue >= 0);
		while ((stepPositif && varEnv.getValue(name) < end.eval(varEnv)) || (!stepPositif && varEnv.getValue(name) > end.eval(varEnv))) {
			p.run(g, varEnv, funcEnv);
			varEnv.changeValue(name, varEnv.getValue(name) + stepValue);
		}
		Program.removeEnvironmentLists(varEnv, funcEnv);
	}

}
