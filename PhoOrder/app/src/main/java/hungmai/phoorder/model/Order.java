package hungmai.phoorder.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.R.attr.id;

/**
 * Created by Admin on 1/21/2017.
 */

public class Order {
    public String id = UUID.randomUUID().toString();
    public List<String> ten = new ArrayList<String>();
    public String bill_id = "";
    public long so_luong_nho = 0;
    public long so_luong_lon = 0;
    public long so_luong_dac_biet = 0;
    public long gia = 0;
    public long loai = 0;
    public String thoi_gian_order = "";
    public List<String> ghi_chu = new ArrayList<String>();
    public long trang_thai = 0;
}
