import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;

public class CustomPanel extends JPanel implements ActionListener, KeyListener, ComponentListener
{
  private Timer timer;
  private Engine engine; 
  private int resolution;
  private boolean forward, backward, left, right, rr, rl;
  private double shadeFactor;
  private double rotationSpeed;
  private double movementSpeed;
  private double sprint;
  private boolean showMap;
  private int mapSize;
  public CustomPanel()
  {
    timer = new Timer(15, this);
    engine = new Engine();
    resolution = 3;
    forward = backward = left = right = rr = rl = false;
    shadeFactor = 1.6;
    rotationSpeed = 1;
    movementSpeed = 0.05;
    sprint = 1;
    showMap = false;
    mapSize = (int) Math.min(getWidth(), getHeight()) / 200;

    addKeyListener(this);
    addComponentListener(this);

    setVisible(true);
    setSize(700, 700);
    setBackground(Color.BLACK);
    setFocusable(true);
    requestFocusInWindow();

    timer.start();
  }

  // Action handling
  public void actionPerformed(ActionEvent e)
  {
    repaint();
    if (forward) 
      engine.movePlayer(movementSpeed * sprint);
    if (backward)
      engine.movePlayer(-movementSpeed * sprint);
    if (rr)
      engine.rotatePlayer(Math.toRadians(rotationSpeed));
    if (rl)
      engine.rotatePlayer(Math.toRadians(-rotationSpeed));
  }

  // Key handling
  public void keyPressed(KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_W: forward = true; break;
      case KeyEvent.VK_S: backward = true; break;
      case KeyEvent.VK_A: left = true; break;
      case KeyEvent.VK_D: right = true; break;
      case KeyEvent.VK_RIGHT: rr = true; break;
      case KeyEvent.VK_LEFT: rl = true; break;
      case KeyEvent.VK_KP_RIGHT: rr = true; break;
      case KeyEvent.VK_KP_LEFT: rl = true; break;
      case KeyEvent.VK_SHIFT: sprint = 2; break;
      case KeyEvent.VK_M: showMap = !showMap; break;
    }
  }
  public void keyReleased(KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_W: forward = false; break;
      case KeyEvent.VK_S: backward = false; break;
      case KeyEvent.VK_A: left = false; break;
      case KeyEvent.VK_D: right = false; break;
      case KeyEvent.VK_RIGHT: rr = false; break;
      case KeyEvent.VK_LEFT: rl = false; break;
      case KeyEvent.VK_KP_RIGHT: rr = false; break;
      case KeyEvent.VK_KP_LEFT: rl = false; break;
      case KeyEvent.VK_SHIFT: sprint = 1; break;
    }
  }

  public void keyTyped(KeyEvent e) {}
  
  // Component handling
  public void componentResized(ComponentEvent e)
  {
    engine.updateFOV(getWidth(), getHeight());
    mapSize = (int) Math.min(getWidth(), getHeight()) / 200;
  }
  public void componentMoved(ComponentEvent e) {}
  public void componentHidden(ComponentEvent e) {}
  public void componentShown(ComponentEvent e) {}

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    GradientPaint gp = new GradientPaint(0, (int) (getHeight() / 2), Color.BLACK, 0, getHeight(), Color.GRAY, false);
    g2.setPaint(gp);
    g2.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
    gp = new GradientPaint(0, 0, Color.GRAY, 0, getHeight() / 2, Color.BLACK, false);
    g2.setPaint(gp);
    g2.fillRect(0, 0, getWidth(), getHeight() / 2);

    for (int i = 0; i < getWidth(); i += resolution)
    {
      double rayAngle = engine.getPlayer().getAngle() - (engine.getFOV() / 2.0) + (i / (double) getWidth()) * engine.getFOV(); 
      double dist = engine.castRay(rayAngle);
      int roof = (int) (getHeight() / 2.0 - getHeight() / dist);
      int floor = getHeight() - roof;
      if (dist * shadeFactor < 1)
        dist = 1;
      else
        dist *= shadeFactor;

      Color shade = new Color((int) (255 / dist), (int) (255 / dist), (int) (255 / dist));
      g2.setColor(shade);
      g2.fillRect(i, roof, resolution, floor - roof);
    }
    if (showMap)
    {
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, engine.getMap().getWidth() * mapSize, engine.getMap().getHeight() * mapSize);
      g2.setColor(Color.BLACK);
      for (int r = 0; r < engine.getMap().getMap().length; r++)
      {
        for (int c = 0; c < engine.getMap().getMap()[r].length; c++)
        {
          if (engine.getMap().getMap()[r][c])
            g2.fillRect(c * mapSize, r * mapSize, mapSize, mapSize); 
        }
      }
      g2.setColor(Color.RED);
      g2.fillRect((int) (engine.getPlayer().x() * mapSize), (int) (engine.getPlayer().y() * mapSize), mapSize, mapSize);
    }
  }
}

