package hungmai.phodocorder.helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hungmai.phodocorder.model.Ban;
import hungmai.phodocorder.model.Bill;
import hungmai.phodocorder.model.MangVe;
import hungmai.phodocorder.model.Order;

/**
 * Created by Admin on 1/30/2017.
 */

public class FirebaseHelper {
    static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void loadBanOrder(final FirebaseBanOrderListener banOrderListener){
        final DatabaseReference mapsrefrence = database.getReference().child("danh_sach_ban");
        Query query_ban_order = mapsrefrence.orderByChild("trang_thai").equalTo(1);
        query_ban_order.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ban> ban_list = new ArrayList<Ban>();
                if (dataSnapshot.hasChildren()){
                    Map snapshot = (Map)dataSnapshot.getValue();
                    snapshot.keySet();
                    for (Object entry : snapshot.entrySet()){
                        Map.Entry<String, Map<String, Object>> entry1 = (Map.Entry<String, Map<String, Object>>)entry;
                        Map<String, Object> ban_map = entry1.getValue();
                        Ban ban = new Ban();
                        ban.id = (String)ban_map.get("id");
                        ban.ban_so = (long)ban_map.get("ban_so");
                        ban.thoi_gian_gui_order = (String)ban_map.get("thoi_gian_gui_order");
                        ban.do_uu_tien = (long)ban_map.get("do_uu_tien");
                        ban.trang_thai = (long)ban_map.get("trang_thai");

                        ban_list.add(ban);
                    }
                }

                banOrderListener.onLoadBanOrderSuccess(ban_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                banOrderListener.onLoadBanOrderFailed(databaseError.getMessage());
            }
        });
    }

    public static void loadMangVeOrder(final FirebaseMangVeOrderListener mangVeListener){
        DatabaseReference mapsrefrence = database.getReference().child("mang_ve");
        Query query_mang_ve_order = mapsrefrence.orderByChild("trang_thai").equalTo(1);
        query_mang_ve_order.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MangVe> mang_ve_list = new ArrayList<MangVe>();
                if (dataSnapshot.hasChildren()){
                    Map snapshot = (Map)dataSnapshot.getValue();
                    snapshot.keySet();
                    for (Object entry : snapshot.entrySet()){
                        Map.Entry<String, Map<String, Object>> entry1 = (Map.Entry<String, Map<String, Object>>)entry;
                        Map<String, Object> mang_ve_map = entry1.getValue();
                        MangVe mang_ve = new MangVe();
                        mang_ve.id = (String)mang_ve_map.get("id");
                        mang_ve.trang_thai = (long)mang_ve_map.get("trang_thai");
                        mang_ve.thoi_gian_gui_order = (String)mang_ve_map.get("thoi_gian_gui_order");
                        mang_ve_list.add(mang_ve);
                    }
                }

                mangVeListener.onLoadMangVeOrderSuccess(mang_ve_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mangVeListener.onLoadMangVeOrderFailed(databaseError.getMessage());
            }
        });
    }

    public static void loadBillOrder(final FirebaseBillListener billListener){
        final DatabaseReference mapsrefrence = database.getReference().child("bill");
        Query query_bill_1 = mapsrefrence.orderByChild("trang_thai").equalTo(0);
        query_bill_1.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Bill> bill_list = new ArrayList<Bill>();
                if (dataSnapshot.hasChildren()){
                    Map snapshot = (Map)dataSnapshot.getValue();
                    snapshot.keySet();
                    for (Object entry : snapshot.entrySet()){
                        Map.Entry<String, Map<String, Object>> entry1 = (Map.Entry<String, Map<String, Object>>)entry;
                        Map<String, Object> bill_map = entry1.getValue();
                        Bill bill = new Bill();
                        bill.id = (String)bill_map.get("id");
                        bill.ban_so = (long)bill_map.get("ban_so");
                        bill.mang_ve_id = (String)bill_map.get("mang_ve_id");
                        bill.ngay_order = (String)bill_map.get("ngay_order");
                        bill.so_mon = (long)bill_map.get("so_mon");
                        bill.trang_thai = (long)bill_map.get("trang_thai");

                        bill_list.add(bill);
                    }
                }

                billListener.onLoadBillSuccess(bill_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                billListener.onLoadBillFailed(databaseError.getMessage());
            }
        });
    }

    public static void loadOrder(final String bill_id, final FirebaseOrderListener orderListener){
        DatabaseReference mapsrefrence = database.getReference().child("order");
        Query query_order = mapsrefrence.orderByChild("bill_id").equalTo(bill_id);
        query_order.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Order> order_list = new ArrayList<Order>();
                if (dataSnapshot.hasChildren()){
                    Map snapshot = (Map)dataSnapshot.getValue();
                    snapshot.keySet();
                    for (Object entry : snapshot.entrySet()){
                        Map.Entry<String, Map<String, Object>> entry1 = (Map.Entry<String, Map<String, Object>>)entry;
                        Map<String, Object> order_map = entry1.getValue();
                        Order order = new Order();
                        order.trang_thai = (long)order_map.get("trang_thai");
                        if (order.trang_thai == 1){
                            order.id = (String)order_map.get("id");
                            order.bill_id = (String)order_map.get("bill_id");
                            order.ten = (List<String>)order_map.get("ten");
                            order.so_luong_em_be = (long)order_map.get("so_luong_em_be");
                            order.so_luong_nho = (long)order_map.get("so_luong_nho");
                            order.so_luong_lon = (long)order_map.get("so_luong_lon");
                            order.so_luong_dac_biet = (long)order_map.get("so_luong_dac_biet");
                            order.gia = (long)order_map.get("gia");
                            order.loai = (long)order_map.get("loai");
                            order.thoi_gian_order = (String)order_map.get("thoi_gian_order");
                            order.ghi_chu = (List<String>)order_map.get("ghi_chu");

                            order_list.add(order);
                        }

                    }
                }

                orderListener.onLoadOrderSuccess(order_list, bill_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                orderListener.onLoadOrderFailed(databaseError.getMessage());
            }
        });
    }

    public static void updateBan(Ban ban){
        DatabaseReference mapsrefrence = database.getReference().child("danh_sach_ban").child(ban.id);
        mapsrefrence.setValue(ban);
    }

    public static void updateMangVe(MangVe mang_ve){
        DatabaseReference mapsrefrence = database.getReference().child("mang_ve").child(mang_ve.id);
        mapsrefrence.setValue(mang_ve);
    }

    public static void updateOrder(Order order){
        DatabaseReference mapsrefrence = database.getReference().child("order").child(order.id);
        mapsrefrence.setValue(order);
    }

    public interface FirebaseBanOrderListener{
        void onLoadBanOrderSuccess(List<Ban> ban_order_list);
        void onLoadBanOrderFailed(String error);
    }

    public interface FirebaseMangVeOrderListener {
        void onLoadMangVeOrderSuccess(List<MangVe>mang_ve_order_list);
        void onLoadMangVeOrderFailed(String error);
    }

    public interface FirebaseBillListener{
        void onLoadBillSuccess(List<Bill>bill_list);
        void onLoadBillFailed(String error);
    }

    public interface FirebaseOrderListener{
        void onLoadOrderSuccess(List<Order> order_list, String bill_id);
        void onLoadOrderFailed(String error);
    }
}
