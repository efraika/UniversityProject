import java.util.LinkedList;
import java.util.HashMap;

public class ValueEnvironment extends LinkedList<ValueList> {

	public ValueEnvironment (){}

	public void addValue (String s, int n, boolean isConst) throws Exception {
		this.peek().addValue(s, n, isConst);
	}

	public int getValue (String s) throws Exception {
		for (int i = 0; i < this.size(); i++){
			Value v = this.get(i).getValue(s);
			if (v != null){
				return v.getVal();
			}
		}
		throw new Exception ("Variable " + s + " doesn't exists");
	}

	public void changeValue (String s, int n) throws Exception {
		for (int i = 0; i < this.size(); i++){
			if (this.get(i).changeValue(s, n))
				return;
		}
		throw new Exception ("Variable " + s + " doesn't exists");
	}

	public ValueEnvironment clone (){
		ValueEnvironment copy = new ValueEnvironment ();
		for (ValueList vl : this){
			copy.add(vl.clone());
		}
		return copy;
	}
}

class ValueList extends HashMap<String, Value>{

	public ValueList (){}

	public void addValue (String s, int n, boolean isConst) throws Exception {
		if (!this.containsKey(s)){
			this.put(s, new Value(n, isConst));
		}else{
			throw new Exception ("Variable " + s + " already defined");
		}
	}

	public Value getValue (String s) {
		if (this.containsKey(s)){
			return this.get(s);
		}
		return null;
	}

	public boolean changeValue (String s, int n) throws Exception {
		if (!this.containsKey(s)) {
			return false;
		}
		if(this.get(s).isConst()){
			throw new Exception ("Variable " + s + " is a constant");
		}
		this.get(s).setVal(n);
		return true;
	}

	public ValueList clone (){
		ValueList copy = new ValueList();
		Object [] keys = this.keySet().toArray();
		for (int i = 0; i < keys.length; i++){
			copy.put((String)keys[i], this.get(keys[i]).clone());
		}
		return copy;
	}
}

class Value{

	private int value;
	private final boolean isConst;

	public Value(int n, boolean b){
		this.value = n;
 		this.isConst = b;
	}

	public int getVal(){
		return this.value;
	}

	public void setVal (int n){
		this.value = n;
	}

	public boolean isConst(){
		return this.isConst;
	}

	public Value clone (){
		return new Value (this.value, this.isConst);
	}
}
