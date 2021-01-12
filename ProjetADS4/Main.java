import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {

    private static void initAndShow(String filename)
	{
        JFrame frame = new JFrame("ADS4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        frame.getContentPane().setPreferredSize(new Dimension(800,600));
        frame.getContentPane().add(new MyCanvas(filename));

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
	{
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run()
			{
                initAndShow(args[0]);
            }
        });
    }
}

@SuppressWarnings("serial")
class MyCanvas extends JComponent {

  String filename;

  public MyCanvas(String fname) {
    filename = fname;
  }

  @Override
  public void paintComponent(Graphics g){
    if (g instanceof Graphics2D)
    {
      Graphics2D g2d = (Graphics2D)g;

      // A compléter.
      // Appelez ici votre analyseur et interpréteur, en leur fournissant
      // l'objet g2d de type Graphics2D. Ils pourront ainsi appeler les fonctions
      // g2d.drawCircle, g2d.setColor, etc...
      //
      // Par exemple :
      //
	  try{
  	 	File input = new File(filename);
     	Reader reader = new FileReader(input);
     	Lexer lexer = new Lexer(reader);
      	LookAhead look = new LookAhead(lexer);
      	Parser parser = new Parser(look);
      	Program ast = parser.nontermProg();
      	ast.run(g2d);
	  }catch(Exception e){
		System.out.println(e);
	  }
    }
  }
}
