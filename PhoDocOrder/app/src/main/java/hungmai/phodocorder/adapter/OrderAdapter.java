package hungmai.phodocorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import hungmai.phodocorder.R;
import hungmai.phodocorder.model.Order;

/**
 * Created by Admin on 1/30/2017.
 */

public class OrderAdapter extends SectionedRecyclerViewAdapter<OrderAdapter.MyViewHolder> {

    List<Order> mon_chinh_order_list = new ArrayList<Order>();
    List<Order> mon_phu_order_list = new ArrayList<Order>();
    List<Order> nuoc_uong_order_list = new ArrayList<Order>();

    public void setOrderList(List<Order> mon_chinh_order_list, List<Order> mon_phu_order_list, List<Order> nuoc_uong_order_list){
        this.mon_chinh_order_list = mon_chinh_order_list;
        this.mon_phu_order_list = mon_phu_order_list;
        this.nuoc_uong_order_list = nuoc_uong_order_list;
        notifyDataSetChanged();
    }

    public void clear(){
        mon_chinh_order_list.clear();
        mon_phu_order_list.clear();
        nuoc_uong_order_list.clear();
    }

    @Override
    public int getSectionCount() {
        return 3;
    }

    @Override
    public int getItemCount(int section) {
        if (section == 0){
            return mon_chinh_order_list.size();
        }else if (section == 1){
            return mon_phu_order_list.size();
        }else {
            return nuoc_uong_order_list.size();
        }
    }

    @Override
    public void onBindHeaderViewHolder(MyViewHolder holder, int section) {
        switch (section){
            case 0:
                holder.setTitle("Món chính");
                break;
            case 1:
                holder.setTitle("Món phụ");
                break;
            case 2:
                holder.setTitle("Nước uống");
                break;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
        switch (section){
            case 0:
                holder.loadOrder(mon_chinh_order_list.get(relativePosition));
                break;
            case 1:
                holder.loadOrder(mon_phu_order_list.get(relativePosition));
                break;
            case 2:
                holder.loadOrder(nuoc_uong_order_list.get(relativePosition));
                break;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = 0;
        switch (viewType){
            case VIEW_TYPE_HEADER:
                layout = R.layout.item_header_show_order;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.item_content_show_order;
                break;
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvTen;
        TextView tvSoLuong;
        TextView tvGia;
        TextView tvGhiChu;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvTen = (TextView)itemView.findViewById(R.id.tvTen);
            tvSoLuong = (TextView)itemView.findViewById(R.id.tvSoLuong);
            tvGia = (TextView)itemView.findViewById(R.id.tvGia);
            tvGhiChu = (TextView)itemView.findViewById(R.id.tvGhiChu);

        }

        public void setTitle(String title){
            tvTitle.setText(title);
        }

        public void loadOrder(Order order){
            loadTen(order.ten);
            loadSoLuong(order);
            loadGia(order);
            loadGhiChu(order);
        }

        private void loadTen(List<String> ten_mon_list){
            tvTen.setText("Tên: ");
            for (String ten_mon : ten_mon_list){
                tvTen.append(ten_mon + " ");
            }
        }

        private void loadSoLuong(Order order){
            String so_luong = "";
            if (order.so_luong_em_be > 0){
                so_luong += order.so_luong_em_be + " em bé ";
            }

            if (order.so_luong_nho > 0){
                so_luong += order.so_luong_nho + " nhỏ ";
            }
            if (order.so_luong_lon > 0){
                so_luong += order.so_luong_lon + " lớn ";
            }
            if (order.so_luong_dac_biet > 0){
                so_luong += order.so_luong_dac_biet + " đặc biệt ";
            }
            tvSoLuong.setText("Số lượng: " + so_luong);
        }

        private void loadGia(Order order){
            tvGia.setText("Giá: " + order.gia);
        }

        private void loadGhiChu(Order order){
            tvGhiChu.setText("Ghi chú: ");
            if (order.ghi_chu != null){
                for (String ghichu : order.ghi_chu){
                    tvGhiChu.append(ghichu + " ");
                }
            }
        }
    }
}
