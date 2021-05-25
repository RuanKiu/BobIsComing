public class EntityQueueItem extends QueueItem 
{
  private Entity entity;
  public EntityQueueItem(double dist, Entity e)
  {
    super(dist);
    entity = e;
  }
  public Entity getEntity()
  {
    return entity;
  }
  public String toString()
  {
    return super.toString() + " " + entity;
  }
}

