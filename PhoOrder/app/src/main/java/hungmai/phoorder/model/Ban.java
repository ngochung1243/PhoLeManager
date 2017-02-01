package hungmai.phoorder.model;

import java.util.UUID;

/**
 * Created by Admin on 1/22/2017.
 */

public class Ban {
    public String id = UUID.randomUUID().toString();
    public long ban_so = 0;
    public String thoi_gian_gui_order = "";
    public long do_uu_tien = 0;
    public long trang_thai = 0;
}
