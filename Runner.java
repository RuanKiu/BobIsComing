import javax.swing.JFrame;

public class Runner 
{
  public static void main(String[] args)
  {
    JFrame window = new JFrame();
    CustomPanel container = new CustomPanel();
    window.add(container);
    window.setSize(container.getSize());
    window.setVisible(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

