package simplelist.trakks.net.simplelist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.joda.time.LocalDate;
import simplelist.trakks.net.simplelist.model.ListItem;
import simplelist.trakks.net.simplelist.model.Repository;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private EditText itemText;
    private Repository repo;
    private MyListAdapter adapter;
    private List<ListItem> dataItems;
    private ListView listView;

    private Button addItemButton;
    private ViewGroup scheduleBar;

    private void hideItemActions()
    {
        scheduleBar.setVisibility(View.GONE);
        addItemButton.setVisibility(View.GONE);
    }

    private void showItemActions()
    {
        scheduleBar.setVisibility(View.VISIBLE);
        addItemButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final Context context = this;

        final ListItem listItem = new ListItem();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        repo = new Repository(this);

        listView = (ListView) findViewById(R.id.itemListView);

        scheduleBar = (ViewGroup) findViewById(R.id.scheduleBar);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                LocalDate scheduled = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                listItem.setScheduled(scheduled);
            }
        };

        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
        Button scheduleNextWeek = (Button) findViewById(R.id.scheduleNextWeekButton);
        Button scheduleNextDay = (Button) findViewById(R.id.scheduleTomorrowButton);

        scheduleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LocalDate now = LocalDate.now();
                new DatePickerDialog(context, date, now.getYear(), now.getMonthOfYear() - 1,
                        now.getDayOfMonth() + 1).show();
            }
        });

        scheduleNextDay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LocalDate now = LocalDate.now();
                listItem.setScheduled(now.plusDays(1));
            }
        });

        scheduleNextWeek.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LocalDate now = LocalDate.now();
                LocalDate nextFriday = now.plusWeeks(1).dayOfWeek().setCopy(5);
                listItem.setScheduled(nextFriday);
            }
        });


        addItemButton = (Button) findViewById(R.id.addItemButton);

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


        itemText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if (view.getId() == R.id.addItemText)
                {
                    if (!hasFocus)
                    {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        hideItemActions();
                    }
                    else
                    {
                        showItemActions();
                    }
                }
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //should be in editText changed....
                listItem.setText(itemText.getText().toString());

                addItemUnderConstruction(listItem.clone());

                listItem.clear();

                itemText.setText("");
                itemText.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemText.getWindowToken(), 0);

                refreshList();
            }
        });
    }

    private void addItemUnderConstruction(ListItem item)
    {
        insert(item);
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
        List<ListItem> toDelete = new LinkedList<>();
        for (ListItem dataItem : dataItems)
        {
            if (dataItem.getArchived() != null)
            {
                toDelete.add(dataItem);
            }
        }

        removeAll(toDelete);
        listView.invalidateViews();
        refreshList();
    }

    private void refreshList()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
