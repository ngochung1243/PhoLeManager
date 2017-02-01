package hungmai.phoorder.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import hungmai.phoorder.R;
import hungmai.phoorder.model.Order;

/**
 * Created by Admin on 1/31/2017.
 */

public class OrderThanhToanAdapter extends RecyclerView.Adapter<OrderThanhToanAdapter.MyHolder> {

    List<Order> order_list = new ArrayList<Order>();

    public void setOrderList(List<Order> order_list){
        this.order_list = order_list;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_order_thanh_toan, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.loadOrder(order_list.get(position));
    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView tvTen;
        TextView tvSoLuong;
        TextView tvGia;

        LinearLayout layoutOrder;

        public MyHolder(View itemView) {
            super(itemView);
            tvTen = (TextView)itemView.findViewById(R.id.tvTen);
            tvSoLuong = (TextView)itemView.findViewById(R.id.tvSoLuong);
            tvGia = (TextView)itemView.findViewById(R.id.tvGia);

            layoutOrder = (LinearLayout) itemView.findViewById(R.id.layoutOrder);
        }

        public void loadOrder(Order order){
            loadTen(order.ten);
            loadSoLuong(order);
            loadGia(order);

            if (order.trang_thai == 1){
                layoutOrder.setBackgroundColor(Color.parseColor("#225379"));
            }else if (order.trang_thai == 2){
                layoutOrder.setBackgroundColor(Color.parseColor("#ff4081"));
            }
        }

        private void loadTen(List<String> ten_mon_list){
            tvTen.setText("");
            for (String ten_mon : ten_mon_list){
                tvTen.append(ten_mon + " ");
            }
        }

        private void loadSoLuong(Order order){
            String so_luong = "";
            if (order.so_luong_nho > 0){
                so_luong += order.so_luong_nho + " nhỏ ";
            }
            if (order.so_luong_lon > 0){
                so_luong += order.so_luong_lon + " lớn ";
            }
            if (order.so_luong_dac_biet > 0){
                so_luong += order.so_luong_dac_biet + " đặc biệt ";
            }
            tvSoLuong.setText(so_luong);
        }

        private void loadGia(Order order){
            tvGia.setText(String.valueOf(order.gia));
        }
    }
}
