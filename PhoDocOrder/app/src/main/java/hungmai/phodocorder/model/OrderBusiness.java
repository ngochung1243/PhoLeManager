package hungmai.phodocorder.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hungmai.phodocorder.helper.FirebaseHelper;

import static android.R.attr.format;

/**
 * Created by Admin on 1/22/2017.
 */

public class OrderBusiness {
    public List<Ban> ban_order_list = new ArrayList<Ban>();
    public List<MangVe> mang_ve_order_list = new ArrayList<MangVe>();
    public List<Bill> bill_order_list = new ArrayList<Bill>();
    public List<Order> order_list = new ArrayList<Order>();

    public String target_bill_id = "";

    public void loadBanOrder(final OrderBusinessListener orderBusinessListener){
        FirebaseHelper.loadBanOrder(new FirebaseHelper.FirebaseBanOrderListener() {
            @Override
            public void onLoadBanOrderSuccess(List<Ban> ban_list) {
                ban_order_list = ban_list;
                sort_ban_order();
                orderBusinessListener.onSuccess();
            }

            @Override
            public void onLoadBanOrderFailed(String error) {
                orderBusinessListener.onFailed(error);
            }
        });
    }

    public void loadMangVeOrder(final OrderBusinessListener orderBusinessListener){
        FirebaseHelper.loadMangVeOrder(new FirebaseHelper.FirebaseMangVeOrderListener() {
            @Override
            public void onLoadMangVeOrderSuccess(List<MangVe> mang_ve_order_list) {
                OrderBusiness.this.mang_ve_order_list = mang_ve_order_list;
                sort_mang_ve();
                orderBusinessListener.onSuccess();
            }

            @Override
            public void onLoadMangVeOrderFailed(String error) {
                orderBusinessListener.onFailed(error);
            }
        });
    }

    public void loadBillBanOrder(final OrderBusinessListener orderBusinessListener){
        FirebaseHelper.loadBillOrder(new FirebaseHelper.FirebaseBillListener() {
            @Override
            public void onLoadBillSuccess(List<Bill> bill_list) {
                bill_order_list = bill_list;
                orderBusinessListener.onSuccess();
            }

            @Override
            public void onLoadBillFailed(String error) {
                orderBusinessListener.onFailed(error);
            }
        });
    }

    public void loadOrder(String bill_id, final OrderListener orderListener){
        FirebaseHelper.loadOrder(bill_id, new FirebaseHelper.FirebaseOrderListener() {
            @Override
            public void onLoadOrderSuccess(List<Order> order_list, String bill_id) {
                OrderBusiness.this.order_list = order_list;
                sort_order();
                orderListener.onSuccess(bill_id);
            }

            @Override
            public void onLoadOrderFailed(String error) {
                orderListener.onFailed(error);
            }
        });
    }

    public void updateDaOrder(){
        for (Order order : order_list){
            order.trang_thai = 2;
            FirebaseHelper.updateOrder(order);
        }
    }

    public void updateBanDaOrder(Ban ban){
        ban.trang_thai = 2;
        ban.do_uu_tien = 0;
        FirebaseHelper.updateBan(ban);
    }

    public void updateMangVeDaOrder(MangVe mang_ve){
        mang_ve.trang_thai = 2;
        FirebaseHelper.updateMangVe(mang_ve);
    }

    public List<Order> locMonChinh(){
        List<Order> monchinh_list = new ArrayList<Order>();

        for (Order order : order_list){
            if (order.loai == 0){
                monchinh_list.add(order);
            }
        }

        return monchinh_list;
    }

    public List<Order> locMonPhu(){
        List<Order> monphu_list = new ArrayList<Order>();

        for (Order order : order_list){
            if (order.loai == 1){
                monphu_list.add(order);
            }
        }

        return monphu_list;
    }

    public List<Order> locNuocUong(){
        List<Order> nuocuong_list = new ArrayList<Order>();

        for (Order order : order_list){
            if (order.loai == 2){
                nuocuong_list.add(order);
            }
        }

        return nuocuong_list;
    }

    private void sort_ban_order(){
        Collections.sort(ban_order_list, new Comparator<Ban>() {
            @Override
            public int compare(Ban o1, Ban o2) {
                if (o1.do_uu_tien < o2.do_uu_tien){
                    return 1;
                }else if (o1.do_uu_tien > o2.do_uu_tien){
                    return -1;
                }else{
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    try {
                        Date d1 = format.parse(o1.thoi_gian_gui_order);
                        Date d2 = format.parse(o2.thoi_gian_gui_order);
                        int result = d1.compareTo(d2);
                        return result;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            }
        });
    }

    public Bill loc_bill_ban(long ban_so){
        for (Bill bill : bill_order_list){
            if (bill.ban_so == ban_so && bill.trang_thai == 0){
                return bill;
            }
        }

        return null;
    }

    public Bill loc_bill_mang_ve(String mang_ve_id){
        for (Bill bill : bill_order_list){
            if (bill.mang_ve_id.equals(mang_ve_id)){
                return bill;
            }
        }

        return null;
    }

    public void sort_mang_ve(){
        Collections.sort(mang_ve_order_list, new Comparator<MangVe>() {
            @Override
            public int compare(MangVe o1, MangVe o2) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date d1 = format.parse(o1.thoi_gian_gui_order);
                    Date d2 = format.parse(o2.thoi_gian_gui_order);
                    int result = d1.compareTo(d2);
                    return result;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
    }

    public void sort_order(){
        Collections.sort(order_list, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if (o1.loai > o2.loai){
                    return -1;
                }else if (o1.loai < o2.loai){
                    return 1;
                }else {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    try {
                        Date d1 = format.parse(o1.thoi_gian_order);
                        Date d2 = format.parse(o2.thoi_gian_order);
                        int result = d1.compareTo(d2);
                        return result;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            }
        });
    }

    public interface OrderBusinessListener{
        void onSuccess();
        void onFailed(String error);
    }

    public interface OrderListener{
        void onSuccess(String bill_id);
        void onFailed(String error);
    }
}
