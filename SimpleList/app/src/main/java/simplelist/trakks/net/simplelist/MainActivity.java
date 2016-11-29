package simplelist.trakks.net.simplelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import simplelist.trakks.net.simplelist.model.ListItem;
import simplelist.trakks.net.simplelist.model.Repository;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.itemListView);

        Repository repo = new Repository(this);
        List<ListItem> items = repo.getAll();
        items.add(new ListItem(UUID.randomUUID(),"sampleitem",null,null));
        items.add(new ListItem(UUID.randomUUID(),"sampleitem",null,null));
        MyListAdapter adapter = new MyListAdapter(this, items);
        list.setAdapter(adapter);
    }
}
