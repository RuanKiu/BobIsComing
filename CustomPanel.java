import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomPanel extends JPanel implements ActionListener, KeyListener, ComponentListener, MouseListener
{
  private Timer timer;
  private Engine engine; 
  private int resolution;
  private boolean forward, backward, left, right, rr, rl;
  private double shadeFactor;
  private double rotationSpeed;
  private double movementSpeed, strafeSpeed;
  private double sprint;
  private boolean showMap;
  private int mapSize;
  private Image flashlightHand, originalFlashlightHand;
  private ArrayList<Entity> entities;
  private static int COLUMN, ENTITY;
  private Color startShade;
  public CustomPanel()
  {
    timer = new Timer(15, this);
    engine = new Engine();
    resolution = 3;
    forward = backward = left = right = rr = rl = false;
    shadeFactor = 1.6;
    rotationSpeed = 1;
    movementSpeed = 0.05;
    strafeSpeed = 0.02;
    sprint = 1;
    showMap = false;
    startShade = Color.GRAY;
    mapSize = (int) Math.min(getWidth(), getHeight()) / 200;
    try
    {
      originalFlashlightHand = ImageIO.read(new File("flashlightHand.png"));
      flashlightHand = originalFlashlightHand.getScaledInstance(700 / 3, -1, Image.SCALE_SMOOTH);
    }
    catch (IOException e) {}
    entities = new ArrayList<>();
    Entity nightmareBob = new Entity(8, 8);
    Entity nightmareBob2 = new Entity(10, 20);
    entities.add(nightmareBob);
    entities.add(nightmareBob2);
    COLUMN = 0;
    ENTITY = 1;

    addKeyListener(this);
    addComponentListener(this);
    addMouseListener(this);

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
    if (right)
      engine.strafePlayer(strafeSpeed * sprint);
    if (left)
      engine.strafePlayer(-strafeSpeed * sprint);
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

  // Mouse handling
  public void mouseClicked(MouseEvent e) 
  {
    if (e.getY() > getHeight() - getHeight() / 3 && e.getX() > getWidth() / 2)
      if(startShade.equals(Color.DARK_GRAY)) {
        shadeFactor = 2;
        startShade = Color.GRAY;
      }
      else {
        shadeFactor = 5;
        startShade = Color.DARK_GRAY;
      }
  }

  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}



  // Component handling
  public void componentResized(ComponentEvent e)
  {
    engine.updateFOV(getWidth(), getHeight());
    mapSize = (int) Math.min(getWidth(), getHeight()) / 200;
    flashlightHand = originalFlashlightHand.getScaledInstance((int) ((getWidth() + getHeight()) / (2.4 * 2)), -1, Image.SCALE_SMOOTH);
  }
  public void componentMoved(ComponentEvent e) {}
  public void componentHidden(ComponentEvent e) {}
  public void componentShown(ComponentEvent e) {}

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    GradientPaint gp = new GradientPaint(0, (int) (getHeight() / 2), Color.BLACK, 0, getHeight(), startShade, false);
    g2.setPaint(gp);
    g2.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
    gp = new GradientPaint(0, 0, startShade, 0, getHeight() / 2, Color.BLACK, false);
    g2.setPaint(gp);
    g2.fillRect(0, 0, getWidth(), getHeight() / 2);

    ArrayList<double[]> toDraw = new ArrayList<>();

    double rayAngle = engine.getPlayer().getAngle() - (engine.getFOV() / 2.0); 
    double dist = engine.castRay(rayAngle);
    toDraw.add(0, new double[]{COLUMN, dist, 0});

    for (int i = 1; i < getWidth(); i += resolution)
    {
      rayAngle = engine.getPlayer().getAngle() - (engine.getFOV() / 2.0) + (i / (double) getWidth()) * engine.getFOV(); 
      dist = engine.castRay(rayAngle);

      boolean entered = false;
      int index = 0;
      while (index < toDraw.size() && !entered)
      {
        if (toDraw.get(index)[1] <= dist) // Does not check for other types
        {
          entered = true;
          toDraw.add(index + 1, new double[]{COLUMN, dist, i});
        }
        index++;
      }
      if (!entered)
        toDraw.add(0, new double[]{COLUMN, dist, i});

    }

    for (Entity e : entities)
    {
      dist = engine.getDistanceFromEntity(e);
      if (dist < engine.getRenderFar() && dist > 0.3 && engine.isEntityVisible(e))
      {
        boolean entered = false;
        double max = toDraw.get(0)[1];
        int index = 0;
        int remember = 0;
        while (index < toDraw.size() - 1 && !entered)
        {
          if (toDraw.get(index)[1] > max && max <= dist) // Does not check for other types
          {
            max = toDraw.get(index)[1];
            remember = index;
          }
          index++;
        }
        toDraw.add(remember + 1, new double[]{ENTITY, dist, engine.getAngleFromEntity(e)});

      }
    }
    int entCount = 0;
    // Drawing scene
    for (int i = toDraw.size() - 1; i >= 0; i--)
    {
      if ((int) (toDraw.get(i)[0] + 0.1) == COLUMN)
        drawColumn(g2, toDraw.get(i)[1], (int) (toDraw.get(i)[2] + 0.5));
      else if ((int) (toDraw.get(i)[0] + 0.1) == ENTITY)
      {
        if (entCount == 0)
          drawEntity(g2, toDraw.get(i)[1], toDraw.get(i)[2]);
        else 
          drawEntityColor(g2, toDraw.get(i)[1], toDraw.get(i)[2]);
        entCount++;
      }
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

    g2.drawImage(flashlightHand, getWidth() / 2, getHeight() - (flashlightHand.getHeight(null) - 10), null);

  }
  public void drawColumn(Graphics2D g2, double dist, int i)
  {
    double roof = getHeight() / 2.0 - getHeight() / dist;
    double floor = getHeight() - roof;
    if (dist * shadeFactor < 1)
      dist = 1;
    else
      dist *= shadeFactor;


    Color shade = new Color((int) (255 / dist), (int) (255 / dist), (int) (255 / dist));
    g2.setColor(shade);
    g2.fillRect(i, (int) roof, resolution, (int) (floor - roof));

  }
  public void drawEntity(Graphics2D g2, double dist, double angle)
  {

    double roof = getHeight() / 2.0 - getHeight() / dist;
    int y = (int) (getHeight() - roof - (30 / dist) * 46.5);
    int x = (int) ((0.5 * angle / (engine.getFOV() / 2) + 0.5) * getWidth() - (30 * 20) / dist);
    drawNightmareBob(g2, x, y, 30 / dist);

  }
  public void drawEntityColor(Graphics2D g2, double dist, double angle)
  {

    double roof = getHeight() / 2.0 - getHeight() / dist;
    int y = (int) (getHeight() - roof - (30 / dist) * 46.5);
    int x = (int) ((0.5 * angle / (engine.getFOV() / 2) + 0.5) * getWidth() - (30 * 20) / dist);
    drawNightmareBob2(g2, x, y, 30 / dist);

  }
  public void drawNightmareBob(Graphics2D g2, int x, int y, double size)
  {
    //head
    g2.setColor(Color.WHITE);
    g2.fillOval((int) (5 * size) + x,(int) (10 * size) + y,(int) (10 * size),(int) (10 * size));
    g2.setColor(Color.BLACK);
    g2.fillOval((int) (7.5 * size) + x,(int) (12 * size) + y,(int) (2 * size),(int) (2 * size));
    g2.fillOval((int) (10.5 * size) + x,(int) (12 * size) + y,(int) (2 * size),(int) (2 * size));
    g2.setColor(Color.BLACK);
    g2.fillOval((int) (8.5 * size) + x,(int) (15 * size) + y, (int) (3 * size), (int) (3 * size));


    //hair
    g2.setColor(Color.BLACK);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (10 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (9 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (8 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (7 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (6 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (11 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (12 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (13 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (14 * size) + x,(int) (5 * size) + y);

    // neck
    g2.setColor(Color.BLACK);
    g2.fillRect((int) (9 * size) + x,(int) (20 * size) + y,(int) (2 * size),(int) (2.5 * size));

    //body
    g2.setColor(Color.DARK_GRAY);
    g2.fillRect((int) (5 * size) + x,(int) (21 * size) + y,(int) (10 * size),(int) (18 * size));

    // left arm
    g2.setColor(Color.GRAY);
    g2.fillRect(x,(int) (21 * size) + y,(int) (5 * size),(int) (9 * size));

    // right arm
    g2.setColor(Color.GRAY);
    g2.fillRect((int) (15 * size) + x,(int) (21 * size) + y,(int) (5 * size),(int) (9 * size));

    //buttons
    g2.setColor(Color.GRAY);
    g2.fillOval((int) (9.5 * size) + x,(int) (22 * size) + y,(int) (size),(int) (size));
    g2.fillOval((int) (9.5 * size) + x,(int) (24 * size) + y,(int) (size),(int) (size));	
    g2.fillOval((int) (9.5 * size) + x,(int) (26 * size) + y,(int) (size),(int) (size));

    //legs
    g2.setColor(Color.BLACK);
    g2.fillRect((int) (7 * size) + x,(int) (39 * size) + y,(int) (3 * size), (int) (8 * size));
    g2.fillRect((int) (11 * size) + x,(int) (39 * size) + y,(int) (3 * size), (int) (8 * size));

    //shoes
    g2.setColor(Color.GRAY);
    g2.fillRect((int) (5 * size) + x,(int) (47 * size) + y,(int) (5 * size),(int) (2 * size));
    g2.fillRect((int) (11 * size) + x,(int) (47 * size) + y,(int) (5 * size),(int) (2 * size));

  }
  public void drawNightmareBob2(Graphics2D g2, int x, int y, double size)
  {
    //head
    g2.setColor(Color.YELLOW);
    g2.fillOval((int) (5 * size) + x,(int) (10 * size) + y,(int) (10 * size),(int) (10 * size));
    g2.setColor(Color.BLACK);
    g2.fillOval((int) (7.5 * size) + x,(int) (12 * size) + y,(int) (2 * size),(int) (2 * size));
    g2.fillOval((int) (10.5 * size) + x,(int) (12 * size) + y,(int) (2 * size),(int) (2 * size));
    g2.setColor(Color.BLACK);
    g2.fillOval((int) (8.5 * size) + x,(int) (15 * size) + y, (int) (3 * size), (int) (3 * size));


    //hair
    g2.setColor(Color.BLACK);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (10 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (9 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (8 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (7 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (6 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (11 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (12 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (13 * size) + x,(int) (5 * size) + y);
    g2.drawLine((int) (10 * size) + x,(int) (10 * size) + y,(int) (14 * size) + x,(int) (5 * size) + y);

    // neck
    g2.setColor(Color.BLACK);
    g2.fillRect((int) (9 * size) + x,(int) (20 * size) + y,(int) (2 * size),(int) (2.5 * size));

    //body
    g2.setColor(Color.RED);
    g2.fillRect((int) (5 * size) + x,(int) (21 * size) + y,(int) (10 * size),(int) (18 * size));

    // left arm
    g2.setColor(Color.GREEN);
    g2.fillRect(x,(int) (21 * size) + y,(int) (5 * size),(int) (9 * size));

    // right arm
    g2.setColor(Color.RED);
    g2.fillRect((int) (15 * size) + x,(int) (21 * size) + y,(int) (5 * size),(int) (9 * size));

    //buttons
    g2.setColor(Color.RED);
    g2.fillOval((int) (9.5 * size) + x,(int) (22 * size) + y,(int) (size),(int) (size));
    g2.fillOval((int) (9.5 * size) + x,(int) (24 * size) + y,(int) (size),(int) (size));	
    g2.fillOval((int) (9.5 * size) + x,(int) (26 * size) + y,(int) (size),(int) (size));

    //legs
    g2.setColor(Color.BLACK);
    g2.fillRect((int) (7 * size) + x,(int) (39 * size) + y,(int) (3 * size), (int) (8 * size));
    g2.fillRect((int) (11 * size) + x,(int) (39 * size) + y,(int) (3 * size), (int) (8 * size));

    //shoes
    g2.setColor(Color.RED);
    g2.fillRect((int) (5 * size) + x,(int) (47 * size) + y,(int) (5 * size),(int) (2 * size));
    g2.fillRect((int) (11 * size) + x,(int) (47 * size) + y,(int) (5 * size),(int) (2 * size));

  }
}

