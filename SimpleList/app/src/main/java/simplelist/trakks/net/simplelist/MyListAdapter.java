package simplelist.trakks.net.simplelist;

import android.content.Context;
import android.graphics.Color;
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

    private int defaultPaintFlag = -1;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = view;
        ListItem p = (ListItem) getItem(i);
        if (v == null)
        {
            LayoutInflater vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.listitem_row, null);
        }

        if (p != null)
        {
            TextView text = (TextView) v.findViewById(R.id.textTextView);
            text.setText(p.getText());
            if (p.getArchived() != null)
            {
                if (defaultPaintFlag == -1) defaultPaintFlag = text.getPaintFlags();
                text.setPaintFlags(defaultPaintFlag | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else
            {
                if (defaultPaintFlag != -1)
                    text.setPaintFlags(defaultPaintFlag);
            }
            if (p.getScheduled() != null)
            {
                TextView scheduledText = (TextView) v.findViewById(R.id.scheduledTextView);
                String scheduleFormatted = formatSchedule(p.getScheduled());

                scheduledText.setText(scheduleFormatted);
                switch (scheduleFormatted)
                {
                    case TODAY:
                        scheduledText.setTextColor(Color.BLUE);
                        break;
                    case TOMORROW:
                        scheduledText.setTextColor(Color.BLACK);
                        break;
                    case YESTERDAY:
                        scheduledText.setTextColor(Color.LTGRAY);
                        break;
                    default:
                        scheduledText.setTextColor(Color.GRAY);
                        break;
                }
            }
        }

        return v;
    }

    private static final String TODAY = "today";
    private static final String YESTERDAY = "yesterday";
    private static final String TOMORROW = "tomorrow";

    private String formatSchedule(LocalDate schedule)
    {
        DateTime sched = schedule.toDateTime(new LocalTime());
        DateTime now = DateTime.now();
        Duration duration = new Duration(now, sched);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM");
        int days = (int) duration.getStandardDays();
        switch (days)
        {
            case -1:
                return YESTERDAY;
            case 0:
                return TODAY;
            case 1:
                return TOMORROW;
        }
        if (days >= 2 && days < 7)
        {
            return schedule.dayOfWeek().getAsText() + " next week";
        }
        //if (days >= 7 && days < 14)
        {
            return schedule.dayOfWeek().getAsText() + " " + schedule.toString(fmt);
        }
    }
}
