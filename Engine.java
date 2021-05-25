public class Engine 
{
  private Player player;
  private Map map;
  private double fovV, fovH;
  private double renderFar;
  private double increment;
  public Engine()
  {
    player = new Player(5, 5);
    map = new Map();
    map.loadMap("School.txt");
    fovV = Math.toRadians(25);
    fovH = Math.toRadians(45); 
    updateFOV(700, 700);
    renderFar = Math.min(map.getWidth(), map.getHeight()) / 2;
    increment = 0.01;
  }
  public Map getMap()
  {
    return map;
  }
  public Player getPlayer()
  {
    return player;
  }
  public double getFOV()
  {
    return fovH;
  }
  public double getRenderFar()
  {
    return renderFar;
  }
  public double castRay(double rayAngle)
  {
    double dist = 0;
    boolean hitWall = false;
    double stepX = Math.sin(rayAngle);
    double stepY = Math.cos(rayAngle);
    while (!hitWall && dist < renderFar)
    {
      dist += increment;
      double testX = player.x() + stepX * dist;
      double testY = player.y() + stepY * dist; 
      if (testX < 0 || testX >= map.getWidth() || testY < 0 || testY >= map.getHeight())
      {
        hitWall = true;
        dist = renderFar + 10;
      }
      else if (map.getMap()[(int) testY][(int) testX])
      {
        hitWall = true;
      }
    }
    return dist;
  }
  public double getDistanceFromEntity(Entity e)
  {
    return Math.sqrt(Math.pow(player.x() - e.x(), 2) + Math.pow(player.y() - e.y(), 2));
  }
  public double getAngleFromEntity(Entity e)
  {
    double stepX = Math.sin(player.getAngle());
    double stepY = Math.cos(player.getAngle());
    double entityAngle = Math.atan2(stepY, stepX) - Math.atan2((e.y() - player.y()), (e.x() - player.x()));
    if (entityAngle < -Math.PI) 
      entityAngle += Math.PI * 2;
    if (entityAngle > Math.PI)
      entityAngle -= Math.PI * 2;
    return entityAngle;
  }
  public boolean isEntityVisible(Entity e)
  {
    double entityAngle = getAngleFromEntity(e);
    double dist = getDistanceFromEntity(e);
    if (dist < 5)    
      return (Math.abs(entityAngle) < fovH / 1.3); 
    else
      return (Math.abs(entityAngle) < fovH / 2); 
  }
  public void movePlayer(double distance)
  {
    double stepX = Math.sin(player.getAngle()) * distance;
    double stepY = Math.cos(player.getAngle()) * distance;
    player.setX(player.x() + stepX);
    player.setY(player.y() + stepY);
  }
  public void strafePlayer(double distance)
  {
    double stepX = Math.cos(player.getAngle()) * distance;
    double stepY = -Math.sin(player.getAngle()) * distance;
    player.setX(player.x() + stepX);
    player.setY(player.y() + stepY);
  }
  public void rotatePlayer(double angle)
  {
    player.setAngle(player.getAngle() + angle);
  }
  public void updateFOV(double w, double h)
  {
    fovH = 2 * Math.atan(Math.tan(fovV / 2) * w / h);
  }
}

