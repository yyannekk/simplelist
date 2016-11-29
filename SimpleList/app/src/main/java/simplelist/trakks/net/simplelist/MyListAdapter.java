package simplelist.trakks.net.simplelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import simplelist.trakks.net.simplelist.model.ListItem;

import java.util.List;


public class MyListAdapter extends BaseAdapter
{
    private List<ListItem> items;
    private Context context;


    public MyListAdapter(Context context, List<ListItem> items)
    {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size() + 1;
    }

    @Override
    public Object getItem(int i)
    {
        if (i == items.size())
        {
            return new ListItem();
        }
        else
        {
            return items.get(i);
        }

    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    private boolean isNewPosition(int i)
    {
        System.out.println("test " + i + " == " + (getCount()-1));
        return i == getCount() - 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = view;

        if (v == null)
        {
            LayoutInflater vi = LayoutInflater.from(context);
            System.out.println("is new position " + i + "?:" + isNewPosition(i));
            if (isNewPosition(i))
            {
                System.out.println("inflate add_row...");
                v = vi.inflate(R.layout.listitem_add_row, null);
            }
            else
            {
                v = vi.inflate(R.layout.listitem_row, null);
            }

        }

        ListItem p = (ListItem) getItem(i);

        if (p != null)
        {
            if (!isNewPosition(i))
            {
                TextView text = (TextView) v.findViewById(R.id.textTextView);
                text.setText(p.getText());
            }
        }

        return v;
    }
}
