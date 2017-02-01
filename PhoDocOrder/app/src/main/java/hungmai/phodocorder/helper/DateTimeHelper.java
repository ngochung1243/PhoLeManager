package hungmai.phodocorder.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 1/22/2017.
 */

public class DateTimeHelper {
    public static String getNgayHienTai(){
        String ngay_hien_tai = "";

        Calendar now = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ngay_hien_tai = sdf.format(new Date());

        return ngay_hien_tai;
    }

    public static String getTimeHienTai(){
        String thoi_gian_hien_tai = "";

        Calendar now = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        thoi_gian_hien_tai = sdf.format(new Date());

        return thoi_gian_hien_tai;
    }
}
