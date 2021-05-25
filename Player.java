public class Player extends Entity 
{
  private double angle;
  public Player(double x, double y)
  {
    super(x, y, "Player");
    angle = 0;
  }
  public Player()
  {
    this(0, 0);
  }
  public double getAngle()
  {
    return angle;
  }
  public void setAngle(double a)
  {
    angle = a;
  }
}
