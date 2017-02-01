package hungmai.phoorder.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hungmai.phoorder.helper.FirebaseHelper;

/**
 * Created by Admin on 1/22/2017.
 */

public class OrderBusiness {
    public List<Ban> ban_order_list = new ArrayList<Ban>();
    public List<MangVe> mang_ve_list = new ArrayList<MangVe>();
    public List<Bill> bill_order_list = new ArrayList<Bill>();
    public List<Order> order_list = new ArrayList<Order>();

    public String target_bill_id = "";

    public void loadBan(final OrderBusinessListener orderBusinessListener){
        FirebaseHelper.loadBan(new FirebaseHelper.FirebaseBanListener() {
            @Override
            public void onLoadBanSuccess(List<Ban> ban_list) {
                ban_order_list = ban_list;
                sort_ban();
                orderBusinessListener.onSuccess();
            }

            @Override
            public void onLoadBanFailed(String error) {
                orderBusinessListener.onFailed(error);
            }
        });
    }

    public void loadMangVe(final OrderBusinessListener orderBusinessListener){
        FirebaseHelper.loadMangVe(new FirebaseHelper.FirebaseMangVeListener() {
            @Override
            public void onLoadMangVeSuccess(List<MangVe> mangve_list) {
                mang_ve_list = mangve_list;
                sort_mang_ve();
                orderBusinessListener.onSuccess();
            }

            @Override
            public void onLoadMangVeFailed(String error) {
                orderBusinessListener.onFailed(error);
            }
        });
    }

    public void themMangVe(){
        MangVe mangVe = new MangVe();
        FirebaseHelper.themMangVe(mangVe);
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

    public void updateDangOrder(){
        for (Order order : order_list){
            if (order.trang_thai == 0){
                order.trang_thai = 1;
                FirebaseHelper.updateOrder(order);
            }
        }
    }

    public void updateBan(Ban ban){
        FirebaseHelper.updateBan(ban);
    }

    public void updateBanDaThanhToan(Ban ban){
        ban.trang_thai = 0;
        FirebaseHelper.updateBan(ban);
    }

    public void updateMangVe(MangVe mang_ve){
        FirebaseHelper.updateMangVe(mang_ve);
    }

    public void updateMangVeDaThanhToan(MangVe mang_ve){
        mang_ve.trang_thai = 3;
        FirebaseHelper.updateMangVe(mang_ve);
    }

    public void updateBillDaThanhToan(Bill bill){
        bill.trang_thai = 1;
        FirebaseHelper.updateBill(bill);
    }

    public boolean checkOrder(){
        for (Order order : order_list){
            if (order.trang_thai == 0){
                return true;
            }
        }

        return false;
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

    private void sort_ban(){
        Collections.sort(ban_order_list, new Comparator<Ban>() {
            @Override
            public int compare(Ban o1, Ban o2) {
                if (o1.ban_so < o2.ban_so){
                    return -1;
                }else {
                    return 1;
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
        Collections.sort(mang_ve_list, new Comparator<MangVe>() {
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
                    return 1;
                }else if (o1.loai < o2.loai){
                    return -1;
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

    public int tinhTongTien(){
        int tong_tien = 0;
        for (Order order : order_list){
            tong_tien += order.gia;
        }

        return tong_tien;
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
