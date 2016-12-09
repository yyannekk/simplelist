package simplelist.trakks.net.simplelist.model;


import android.content.Context;
import simplelist.trakks.net.simplelist.DBHelper;

import java.util.List;
import java.util.UUID;

public class Repository
{
    private DBHelper db;

    public Repository(Context context)
    {
        db = new DBHelper(context);
    }

    public UUID insert(ListItem item)
    {
        if (item.getId() == null)
        {
            item.setId(UUID.randomUUID());
        }
        db.create(item.getId().toString(), item.getText(), item.getScheduled(), item.getArchived());
        return item.getId();
    }

    public void update(ListItem item)
    {
        if (item.getId() == null) throw new IllegalArgumentException("Item has no id");
        db.update(item.getId().toString(), item.getText(), item.getScheduled(), item.getArchived());
    }

    public void delete(UUID id)
    {
        db.delete(id.toString());
    }

    public List<ListItem> getAll()
    {
        return db.getAll();
    }
}
