package hungmai.phoorder.model;

import java.util.UUID;

import hungmai.phoorder.helper.DateTimeHelper;

/**
 * Created by Admin on 1/22/2017.
 */

public class MangVe {
    public String id = UUID.randomUUID().toString();
    public long trang_thai = 0;
    public String thoi_gian_gui_order = "";
}
