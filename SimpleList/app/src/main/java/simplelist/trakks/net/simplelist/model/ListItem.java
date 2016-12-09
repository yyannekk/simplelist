package simplelist.trakks.net.simplelist.model;


import org.joda.time.LocalDate;

import java.util.Date;
import java.util.UUID;

public class ListItem
{
    private LocalDate scheduled;
    private LocalDate archived;
    private String text;

    public ListItem(UUID id, String text, LocalDate scheduled, LocalDate archived)
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

    public LocalDate getScheduled()
    {
        return scheduled;
    }

    public void setScheduled(LocalDate scheduled)
    {
        this.scheduled = scheduled;
    }

    public LocalDate getArchived()
    {
        return archived;
    }

    public void setArchived(LocalDate archived)
    {
        this.archived = archived;
    }

    public ListItem clone()
    {
        UUID id = this.id != null ? UUID.fromString(this.id.toString()) : null;
        return new ListItem(id, new String(text), scheduled, archived);
    }

    public void clear()
    {
        this.setText("");
        this.setArchived(null);
        this.setScheduled(null);
        this.setId(null);
    }
}
