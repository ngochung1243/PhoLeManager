package hungmai.phodocorder.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hungmai.phodocorder.R;
import hungmai.phodocorder.activity.DocOrderActivity;
import hungmai.phodocorder.model.MangVe;
import hungmai.phodocorder.model.MyBundle;

/**
 * Created by Admin on 1/30/2017.
 */

public class MangVeOrderAdapter extends RecyclerView.Adapter<MangVeOrderAdapter.ViewHolder> {
    public List<MangVe> mangve_list = new ArrayList<MangVe>();

    public Activity activity;

    public MangVeOrderAdapter(Activity activity){
        this.activity = activity;
    }

    public void setMangVeOrderList(List<MangVe> mangve_list){
        this.mangve_list = mangve_list;
        notifyDataSetChanged();
    }

    @Override
    public MangVeOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_order, parent, false);
        return new MangVeOrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MangVeOrderAdapter.ViewHolder holder, int position) {
        holder.load(position);
    }

    @Override
    public int getItemCount() {
        return mangve_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button btnOrderItem;
        public ViewHolder(View v){
            super(v);
            btnOrderItem = (Button)v.findViewById(R.id.btnOrderItem);
        }

        public void load(final int position){
            btnOrderItem.setBackgroundColor(Color.parseColor("#225378"));
            final MangVe mang_ve = mangve_list.get(position);

            btnOrderItem.setText("Mang về");
            btnOrderItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {((DocOrderActivity)activity).hien_thi_mang_ve(mang_ve);}
                });
        }
    }
}
