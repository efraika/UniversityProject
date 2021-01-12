import java.util.LinkedList;
import java.util.HashMap;
import java.util.Objects;

public class FunctionEnvironment extends LinkedList<FunctionList> {

	public FunctionEnvironment (){}

	public void addFunction (String name, String [] args, Program p, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		this.peek().addFunction(name, args, p, varEnv, funcEnv);
	}

	public Function getFunction (String name, int nbArgs) throws Exception {
		for (int i = 0; i < this.size(); i++){
			Function f = this.get(i).getFunction(name, nbArgs);
			if (f != null){
				return f;
			}
		}
		throw new Exception ("Function " + name + " with " + nbArgs + " arguments doesn't exists");
	}
	
	public FunctionEnvironment clone (){
		FunctionEnvironment copy = new FunctionEnvironment();
		for (FunctionList fl : this){
			copy.add(fl.clone());
		}
		return copy;
	}
}

class FunctionList extends HashMap<Signature, Function> {

	public FunctionList (){}

	public void addFunction (String name, String [] args, Program p, ValueEnvironment varEnv, FunctionEnvironment funcEnv) throws Exception {
		Signature newFunctionSign = new Signature(name, args.length);
		if (!this.containsKey(newFunctionSign)){
			this.put(newFunctionSign, new Function (args, p, varEnv, funcEnv));
		}else{
			throw new Exception ("Function " + name + " with " + args.length + " arguments already exists");
		}
	}

	public Function getFunction (String name, int nbArgs){
		Signature sign = new Signature(name, nbArgs);
		if (this.containsKey(sign)){
			return this.get(sign);
		}
		return null;
	}

	public FunctionList clone (){
		FunctionList copy = new FunctionList();
		Object [] keys = this.keySet().toArray();
		for (Object k : keys){
			copy.put(((Signature)k).clone(), this.get(k).clone());
		}
		return copy;
	}
}

class Signature {
	private String name;
	private int nbArgs;

	public Signature (String s, int n){
		this.name = s;
		this.nbArgs = n;
	}

	public boolean equals(Object obj){
		if (obj == this){
			return true;
		}
		if (obj == null || !(obj instanceof Signature)){
			return false;
		}
		Signature s = (Signature) obj;
		return (this.name.equals(s.name) && this.nbArgs == s.nbArgs);
	}

	public int hashCode (){
		return Objects.hash(this.name, this.nbArgs);
	}
	
	public Signature clone (){
		return new Signature (this.name, this.nbArgs);
	}
}

class Function {
	private final String [] args;
	private final Program body;
	private final ValueEnvironment declarationVarEnv;
	private final FunctionEnvironment declarationFuncEnv;

	public Function (String [] args, Program p , ValueEnvironment varEnv, FunctionEnvironment funcEnv){
		this.args = args;
		this.body = p;
		this.declarationVarEnv = varEnv;
		this.declarationFuncEnv = funcEnv;
	}

	public String [] getArgs (){
		return this.args;
	}

	public Program getProgram (){
		return this.body;
	}

	public String getArgs (int n){
		return this.args[n];
	}

	public ValueEnvironment getDeclarationVarEnv (){
		return this.declarationVarEnv;
	}

	public FunctionEnvironment getDeclarationFuncEnv (){
		return this.declarationFuncEnv;
	}

	public Function clone (){
		return new Function (this.args, this.body, this.declarationVarEnv, this.declarationFuncEnv);
	}
}
