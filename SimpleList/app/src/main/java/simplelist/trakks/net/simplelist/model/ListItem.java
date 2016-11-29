package simplelist.trakks.net.simplelist.model;


import java.util.Date;
import java.util.UUID;

public class ListItem
{
    private Date scheduled;
    private Date archived;
    private String text;

    public ListItem(UUID id, String text, Date scheduled, Date archived)
    {
        this.scheduled = scheduled;
        this.archived = archived;
        this.text = text;
        this.id = id;
    }

    private UUID id;

    public ListItem()
    {

    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getScheduled()
    {
        return scheduled;
    }

    public void setScheduled(Date scheduled)
    {
        this.scheduled = scheduled;
    }

    public Date getArchived()
    {
        return archived;
    }

    public void setArchived(Date archived)
    {
        this.archived = archived;
    }

}
