package hungmai.phoorder.model;

import java.util.UUID;

/**
 * Created by Admin on 1/22/2017.
 */

public class Bill {
    public String id = UUID.randomUUID().toString();
    public long ban_so = 0;
    public String mang_ve_id = "";
    public long so_mon = 0;
    public String ngay_order = "";
    public long trang_thai = 0;
}
