public class ColumnQueueItem extends QueueItem 
{
  private int column;
  public ColumnQueueItem(double dist, int column)
  {
    super(dist);
    this.column = column;
  }
  public int getColumn()
  {
    return column;
  }
  public String toString()
  {
    return super.toString() + " " + column;
  }
}

