package simplelist.trakks.net.simplelist;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DateTimeTest
{
    @Test
    public void addition_isCorrect() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        System.out.println(format.format(now));
    }
}