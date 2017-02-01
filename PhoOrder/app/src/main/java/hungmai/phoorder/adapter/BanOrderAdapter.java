package hungmai.phoorder.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hungmai.phoorder.R;
import hungmai.phoorder.activity.OrderActivity;
import hungmai.phoorder.model.Ban;
import hungmai.phoorder.model.Bill;

/**
 * Created by Admin on 1/22/2017.
 */

public class BanOrderAdapter extends RecyclerView.Adapter<BanOrderAdapter.ViewHolder> {

    public List<Ban> ban_order_list = new ArrayList<Ban>();
//    public List<Bill> bill_ban_order_list = new ArrayList<Bill>();

    public Activity activity;

    public BanOrderAdapter(Activity activity){
        this.activity = activity;
    }

    public void setBanOrderList(List<Ban> ban_order_list){
        this.ban_order_list = ban_order_list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        long trang_thai = checkBanOrder(position);
//        holder.load(ban_order_list.get(position), trang_thai);
        holder.load(ban_order_list.get(position));
    }

    @Override
    public int getItemCount() {
        return ban_order_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button btnOrderItem;
        public ViewHolder(View v){
            super(v);
            btnOrderItem = (Button)v.findViewById(R.id.btnOrderItem);
        }

        public void load(final Ban ban){
            btnOrderItem.setBackgroundColor(Color.GRAY);
            btnOrderItem.setText("Bàn " + ban.ban_so);
            btnOrderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OrderActivity)activity).hien_thi_ban_order(ban);
                }
            });

            if (ban.trang_thai == 1){
                btnOrderItem.setBackgroundColor(Color.parseColor("#225378"));
            }else if (ban.trang_thai == 2){
                btnOrderItem.setBackgroundColor(Color.parseColor("#ff4081"));
            }
        }
    }
}
