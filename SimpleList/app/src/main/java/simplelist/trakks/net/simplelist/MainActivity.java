package simplelist.trakks.net.simplelist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import simplelist.trakks.net.simplelist.model.ListItem;
import simplelist.trakks.net.simplelist.model.Repository;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private EditText itemText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        final Repository repo = new Repository(this);
//        for (ListItem item : repo.getAll())
//        {
//            repo.delete(item.getId());
//        }

        final ListView listView = (ListView) findViewById(R.id.itemListView);


        final List<ListItem> items = repo.getAll();
//        items.add(new ListItem(UUID.randomUUID(),"sampleitem",null,null));
//        items.add(new ListItem(UUID.randomUUID(),"sampleitem",null,null));
        final MyListAdapter adapter = new MyListAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                ListItem item = (ListItem) adapter.getItem(i);
                item.setArchived(Calendar.getInstance().getTime());
                repo.update(item);
                adapter.notifyDataSetChanged();
                //listView.invalidateViews();
            }
        });

        itemText = (EditText) findViewById(R.id.addItemText);
        Button btn = (Button) findViewById(R.id.addItemButton);

        itemText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                System.out.println("focus changed");
                System.out.println(view);
                System.out.println(hasFocus);
                if(view.getId() == R.id.addItemText && !hasFocus) {

                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ListItem li = new ListItem();
                li.setText(itemText.getText().toString());
                itemText.setText("");
                itemText.clearFocus();

                System.out.println("clear..");
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);

                repo.insert(li);
                items.add(li);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
