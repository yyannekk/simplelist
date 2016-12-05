package simplelist.trakks.net.simplelist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.joda.time.LocalDate;
import simplelist.trakks.net.simplelist.model.ListItem;
import simplelist.trakks.net.simplelist.model.Repository;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private EditText itemText;
    private Repository repo;
    private MyListAdapter adapter;
    private List<ListItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        repo = new Repository(this);

        final ListView listView = (ListView) findViewById(R.id.itemListView);


        dataItems = repo.getAll();
        adapter = new MyListAdapter(this, dataItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                ListItem item = (ListItem) adapter.getItem(i);
                item.setArchived(LocalDate.now());
                repo.update(item);
                adapter.notifyDataSetChanged();
            }
        });

        itemText = (EditText) findViewById(R.id.addItemText);
        Button btn = (Button) findViewById(R.id.addItemButton);

        itemText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if (view.getId() == R.id.addItemText && !hasFocus)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        final ListItem listItem = new ListItem();

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //should be in editText changed....
                listItem.setText(itemText.getText().toString());

                ListItem item = listItem.clone();

                listItem.setText("");
                listItem.setArchived(null);
                listItem.setScheduled(null);

                itemText.setText("");
                itemText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);

                insert(item);
                refreshList();
            }
        });

        Button schedButton = (Button) findViewById(R.id.scheduleButton);

        final Calendar calendar = Calendar.getInstance();

        final Context context = this;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                LocalDate date = LocalDate.now().plusDays(1);
                String nameOfDay = date.dayOfWeek().getAsText();
                LocalDate scheduled = new LocalDate(year,monthOfYear+1,dayOfMonth);
                System.out.println(scheduled);

                listItem.setScheduled(scheduled);
//                updateLabel();
            }
        };

        schedButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                // TODO Auto-generated method stub
                LocalDate now = LocalDate.now();
//                System.out.println(String.format("jdk  year=%d, month=%d, day=%d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
//                System.out.println(String.format("joda year=%d, month=%d, day=%d",now.getYear(),now.getMonthOfYear(),now.getDayOfMonth()));
                new DatePickerDialog(context, date, now.getYear(), now.getMonthOfYear()-1,
                        now.getDayOfMonth() + 1).show();
            }
        });
    }

    private void insert(ListItem item)
    {
        repo.insert(item);
        dataItems.add(item);
    }

    private void removeAll(List<ListItem> items)
    {
        dataItems.removeAll(items);
        for (ListItem listItem : items)
        {
            repo.delete(listItem.getId());
        }
    }

    public void onRemoveArchivedClicked(MenuItem mi)
    {
        System.out.println("remove archived");
        List<ListItem> toDelete = new LinkedList<>();
        for (ListItem dataItem : dataItems)
        {
            if (dataItem.getArchived() != null)
            {
                System.out.println(dataItem);
                toDelete.add(dataItem);
            }
        }

        removeAll(toDelete);
        refreshList();
    }

    private void refreshList()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu_main; this adds dataItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
