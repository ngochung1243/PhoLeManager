package hungmai.phoorder.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hungmai.phoorder.R;
import hungmai.phoorder.activity.OrderActivity;
import hungmai.phoorder.model.Order;

import static android.R.attr.order;
import static hungmai.phoorder.R.id.btnXoaOrder;
import static hungmai.phoorder.R.id.tvTen;

/**
 * Created by Admin on 1/22/2017.
 */

public class OrderAdapter extends BaseAdapter {

    List<Order> order_list = new ArrayList<Order>();
    Activity activity;
    int loai_order;

    public OrderAdapter(Activity activity, int loai_order){
        this.activity = activity;
        this.loai_order = loai_order;
    }

    public void setOrderList(List<Order> order_list){
        this.order_list.clear();
        this.order_list.addAll(order_list);
        this.notifyDataSetChanged();
    }

    public void clear(){
        order_list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return order_list.size() + 1;
    }

    @Override
    public Order getItem(int position) {
        return order_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (position == order_list.size()){
            v = inflater.inflate(R.layout.item_show_order_cong, parent, false);
        }else {
            v = inflater.inflate(R.layout.item_show_order, parent, false);
        }

        if (position < order_list.size()){
            TextView tvTen;
            TextView tvSoLuong;
            TextView tvGia;
            LinearLayout layout;
            Button btnXoaOrder;

            tvTen = (TextView) v.findViewById(R.id.tvTen);
            tvSoLuong = (TextView) v.findViewById(R.id.tvSoLuong);
            tvGia = (TextView) v.findViewById(R.id.tvGia);
            layout = (LinearLayout) v.findViewById(R.id.layout);
            btnXoaOrder = (Button) v.findViewById(R.id.btnXoaOrder);

            final Order order = order_list.get(position);

            if (order.trang_thai != 2){
                if (order.trang_thai == 0) {
                    layout.setBackgroundColor(Color.GRAY);
                }else {
                    layout.setBackgroundColor(Color.parseColor("#225378"));
                }

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (loai_order){
                            case 0:
                                ((OrderActivity)activity).startEditMonChinhOrderView(order);
                                break;
                            case 1:
                                ((OrderActivity)activity).startEditMonPhuOrderView(order);
                                break;
                            case 2:
                                ((OrderActivity)activity).startEditNuocUongOrderView(order);
                                break;
                        }

                    }
                });

                btnXoaOrder.setVisibility(View.VISIBLE);
                btnXoaOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OrderActivity)activity).xoaOrder(order);
                    }
                });
            }else if (order.trang_thai == 2){
                layout.setBackgroundColor(Color.parseColor("#ff4081"));
                btnXoaOrder.setVisibility(View.GONE);
            }

            tvTen.setText("Tên: ");
            if (loai_order == 0){
                tvTen.append("Phở ");
            }
            for (String pho_ten : order.ten){
                tvTen.append(pho_ten + " ");
            }

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
            tvGia.setText("Giá: " + order.gia);
        }else {
            LinearLayout layoutThemOrder = (LinearLayout)v.findViewById(R.id.layoutThemOrder);
            layoutThemOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (loai_order){
                        case 0:
                            ((OrderActivity)activity).startThemMonChinhOrderView();
                            break;
                        case 1:
                            ((OrderActivity)activity).startThemMonPhuOrderView();
                            break;
                        case 2:
                            ((OrderActivity)activity).startThemNuocUongOrderView();
                            break;
                    }

                }
            });
        }

        return v;
    }
}
