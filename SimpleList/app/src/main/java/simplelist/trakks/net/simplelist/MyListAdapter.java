package simplelist.trakks.net.simplelist;

import android.content.Context;
import android.graphics.Paint;
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
        return items.size();
    }

    @Override
    public Object getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        System.out.println("update view " + view);
        View v = view;
        ListItem p = (ListItem) getItem(i);
        if (v == null)
        {
            LayoutInflater vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.listitem_row, null);


            if (p != null)
            {
                TextView text = (TextView) v.findViewById(R.id.textTextView);
                text.setText(p.getText());
                System.out.println("cross out?" + (p.getArchived() != null));
                if (p.getArchived() != null)
                {
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }
        else
        {
            if (p != null)
            {
                if (p.getArchived() != null)
                {
                    TextView text = (TextView) v.findViewById(R.id.textTextView);
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }

        return v;
    }
}
