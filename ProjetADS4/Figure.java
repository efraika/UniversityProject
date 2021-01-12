import java.awt.Graphics2D;
import java.awt.Color;

abstract class Figure {
	protected int red;
	protected int green;
	protected int blue;
	protected boolean empty;

	public Figure (String color, boolean empty){
		this.red = Integer.parseInt(color.substring(1, 3), 16);
		this.green = Integer.parseInt(color.substring(3, 5), 16);
		this.blue = Integer.parseInt(color.substring(5, 7), 16);
		this.empty = empty;
	}

	public abstract void draw (Graphics2D g, ValueEnvironment env) throws Exception;
}

class Rect extends Figure {
	protected Expression x, y, width, height;
	private boolean empty;

	public Rect (String color, Expression x, Expression y, Expression width, Expression height, boolean empty){
		super(color, empty);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw (Graphics2D g, ValueEnvironment env) throws Exception {
		g.setColor(new Color(this.red, this.green, this.blue));
		if (this.empty){
			g.drawRect(this.x.eval(env), this.y.eval(env), this.width.eval(env), this.height.eval(env));
		}else{
			g.fillRect(this.x.eval(env), this.y.eval(env), this.width.eval(env), this.height.eval(env));
		}
	}
}

class Circle extends Figure {
	protected Expression x, y, radius;

	public Circle (String color, Expression x, Expression y, Expression radius, boolean empty){
		super(color, empty);
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public void draw (Graphics2D g, ValueEnvironment env) throws Exception {
		g.setColor(new Color(this.red, this.green, this.blue));
		if (this.empty){
			g.drawOval(this.x.eval(env), this.y.eval(env), this.radius.eval(env), this.radius.eval(env));
		}else{
			g.fillOval(this.x.eval(env), this.y.eval(env), this.radius.eval(env), this.radius.eval(env));
		}
	}
}
