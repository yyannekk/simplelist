package simplelist.trakks.net.simplelist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
                if (p.getArchived() != null)
                {
                    text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                if (p.getScheduled() != null)
                {
                    TextView scheduledText = (TextView) v.findViewById(R.id.scheduledTextView);
//                    DateFormat format = new SimpleDateFormat("dd/MM ");
                    scheduledText.setText(formatSchedule(p.getScheduled()));
                }
            }
        }
        else
        {
            //TODO refactor
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

    private String formatSchedule(LocalDate schedule)
    {
        DateTime sched = schedule.toDateTime(new LocalTime());
        DateTime now = DateTime.now();
        Duration duration = new Duration(now, sched);
        System.out.println("sched: " + sched);
        System.out.println("now:   " + now);
        System.out.println("duration: " + duration);
        System.out.println("days: " + duration.getStandardDays());

        DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM");

        switch ((int) duration.getStandardDays())
        {
            case -1:
                return "yesterday";
            case 0:
                return "today";
            case 1:
                return "tomorrow";
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return schedule.dayOfWeek().getAsText();
            default:
                return schedule.toString(fmt);
        }


    }
}
