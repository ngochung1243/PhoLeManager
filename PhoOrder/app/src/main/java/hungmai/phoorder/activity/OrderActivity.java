package hungmai.phoorder.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hungmai.phoorder.R;
import hungmai.phoorder.adapter.BanOrderAdapter;
import hungmai.phoorder.adapter.MangVeOrderAdapter;
import hungmai.phoorder.adapter.OrderAdapter;
import hungmai.phoorder.custom_view.ExpandableHeightGridView;
import hungmai.phoorder.helper.DateTimeHelper;
import hungmai.phoorder.helper.FirebaseHelper;
import hungmai.phoorder.model.Ban;
import hungmai.phoorder.model.Bill;
import hungmai.phoorder.model.MangVe;
import hungmai.phoorder.model.MyBundle;
import hungmai.phoorder.model.Order;
import hungmai.phoorder.model.OrderBusiness;

public class OrderActivity extends AppCompatActivity {

    public static int THEM_ORDER = 1;
    public static int THANH_TOAN_ORDER = 2;

    BanOrderAdapter ban_adapter;
    MangVeOrderAdapter mangve_adapter;
    OrderAdapter mon_chinh_order_adapter;
    OrderAdapter mon_phu_order_adapter;
    OrderAdapter nuoc_uong_order_adapter;

    Button btnBan;
    Button btnMangVe;
    Button btnGuiOrder;
    Button btnThanhToan;

    RecyclerView rcvOrder;
    TextView tvBanSo;

    ExpandableHeightGridView gvShowMonChinhOrder;
    ExpandableHeightGridView gvShowMonPhuOrder;
    ExpandableHeightGridView gvShowMonNuocUongOrder;

    Ban ban_target = new Ban();
    MangVe mang_ve = new MangVe();
    Bill target_bill;

    boolean first_load = true;
    boolean hien_thi_ban = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ban_adapter  = new BanOrderAdapter(this);
        mangve_adapter  = new MangVeOrderAdapter(this);
        mon_chinh_order_adapter = new OrderAdapter(this, 0);
        mon_phu_order_adapter = new OrderAdapter(this, 1);
        nuoc_uong_order_adapter = new OrderAdapter(this, 2);

        Map();
        setListener();
        setRecyclerView();
        setGridView();

        setBanOrder();

        MyBundle.mOrderBusiness.loadBillBanOrder(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {

            }
        });

    }

    private void Map(){
        btnBan = (Button)findViewById(R.id.btnBan);
        btnMangVe = (Button)findViewById(R.id.btnMangVe);
        btnGuiOrder = (Button)findViewById(R.id.btnGuiOrder);
        btnThanhToan = (Button)findViewById(R.id.btnThanhToan);

        rcvOrder = (RecyclerView) findViewById(R.id.rcvOrder);

        tvBanSo = (TextView) findViewById(R.id.tvBanSo);

        gvShowMonChinhOrder = (ExpandableHeightGridView) findViewById(R.id.gvShowMonChinhOrder);
        gvShowMonPhuOrder = (ExpandableHeightGridView) findViewById(R.id.gvShowMonPhuOrder);
        gvShowMonNuocUongOrder = (ExpandableHeightGridView) findViewById(R.id.gvShowNuocUongOrder);
    }

    private void setListener(){
        btnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mang_ve = new MangVe();
                hien_thi_ban = true;
                setBanOrder();
            }
        });

        btnMangVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ban_target = new Ban();
                hien_thi_ban = false;
                setMangVeOrder();
            }
        });

        btnGuiOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hien_thi_ban){
                    guiBanOrder();
                }else {
                    guiMangVeOrder();
                }
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThanhToanView();
            }
        });
    }

    private void setRecyclerView(){
        rcvOrder.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvOrder.setLayoutManager(layoutManager);
    }

    private void setGridView(){
        gvShowMonChinhOrder.setAdapter(mon_chinh_order_adapter);
        gvShowMonChinhOrder.setExpanded(true);

        gvShowMonPhuOrder.setAdapter(mon_phu_order_adapter);
        gvShowMonPhuOrder.setExpanded(true);

        gvShowMonNuocUongOrder.setAdapter(nuoc_uong_order_adapter);
        gvShowMonNuocUongOrder.setExpanded(true);
    }

    private void guiBanOrder(){
        if (MyBundle.mOrderBusiness.checkOrder()){
            MyBundle.mOrderBusiness.updateDangOrder();
            if (ban_target.trang_thai == 2){
                ban_target.do_uu_tien = 1;
            }
            ban_target.trang_thai = 1;
            ban_target.thoi_gian_gui_order = DateTimeHelper.getTimeHienTai();
            MyBundle.mOrderBusiness.updateBan(ban_target);
        }
    }

    private void guiMangVeOrder(){
        if (MyBundle.mOrderBusiness.checkOrder()){
            MyBundle.mOrderBusiness.updateDangOrder();
            mang_ve.trang_thai = 1;
            mang_ve.thoi_gian_gui_order = DateTimeHelper.getTimeHienTai();
            MyBundle.mOrderBusiness.updateMangVe(mang_ve);
        }
    }

    private void setBanOrder(){
        rcvOrder.setAdapter(ban_adapter);

        MyBundle.mOrderBusiness.loadBan(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {
                ban_adapter.setBanOrderList(MyBundle.mOrderBusiness.ban_order_list);
                if (first_load){
                    ban_target = MyBundle.mOrderBusiness.ban_order_list.get(0);
                    hien_thi_ban_order(ban_target);
                    first_load = false;
                }

            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), "error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMangVeOrder(){
        rcvOrder.setAdapter(mangve_adapter);
        MyBundle.mOrderBusiness.loadMangVe(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {
                mangve_adapter.setMangVeOrderList(MyBundle.mOrderBusiness.mang_ve_list);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), "error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hien_thi_ban_order(Ban ban){

        tvBanSo.setText("Bàn " + ban.ban_so);
        ban_target = ban;

        target_bill = MyBundle.mOrderBusiness.loc_bill_ban(ban.ban_so);
        if (target_bill != null){
            MyBundle.mOrderBusiness.loadOrder(target_bill.id, new OrderBusiness.OrderListener() {
                @Override
                public void onSuccess(String bill_id) {
                    if (target_bill == null){
                        mon_chinh_order_adapter.setOrderList(new ArrayList<Order>());
                        mon_phu_order_adapter.setOrderList(new ArrayList<Order>());
                        nuoc_uong_order_adapter.setOrderList(new ArrayList<Order>());
                    }else if (bill_id.equals(target_bill.id)){
                        mon_chinh_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonChinh());
                        mon_phu_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonPhu());
                        nuoc_uong_order_adapter.setOrderList(MyBundle.mOrderBusiness.locNuocUong());
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }else {
            mon_chinh_order_adapter.setOrderList(new ArrayList<Order>());
            mon_phu_order_adapter.setOrderList(new ArrayList<Order>());
            nuoc_uong_order_adapter.setOrderList(new ArrayList<Order>());
        }
    }

    public void hien_thi_mang_ve(MangVe mang_ve){
        tvBanSo.setText("Mang về");
        this.mang_ve = mang_ve;
        target_bill = MyBundle.mOrderBusiness.loc_bill_mang_ve(this.mang_ve.id);
        if (target_bill != null){
            MyBundle.mOrderBusiness.loadOrder(target_bill.id, new OrderBusiness.OrderListener() {
                @Override
                public void onSuccess(String bill_id) {
                    if (target_bill == null){
                        mon_chinh_order_adapter.setOrderList(new ArrayList<Order>());
                        mon_phu_order_adapter.setOrderList(new ArrayList<Order>());
                        nuoc_uong_order_adapter.setOrderList(new ArrayList<Order>());
                    }else if (bill_id.equals(target_bill.id)){
                        mon_chinh_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonChinh());
                        mon_phu_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonPhu());
                        nuoc_uong_order_adapter.setOrderList(MyBundle.mOrderBusiness.locNuocUong());
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }else {
            mon_chinh_order_adapter.setOrderList(new ArrayList<Order>());
            mon_phu_order_adapter.setOrderList(new ArrayList<Order>());
            nuoc_uong_order_adapter.setOrderList(new ArrayList<Order>());
        }
    }

    public void themBill(){
        target_bill = new Bill();
        if (hien_thi_ban){
            target_bill.ban_so = ban_target.ban_so;
        }else {
            target_bill.mang_ve_id = mang_ve.id;
        }

        target_bill.ngay_order = DateTimeHelper.getNgayHienTai();
        target_bill.trang_thai = 0;

        FirebaseHelper.updateBill(target_bill);
    }

    private void checkTargetBill(){
        if (target_bill == null){
            themBill();
            MyBundle.mOrderBusiness.loadOrder(target_bill.id, new OrderBusiness.OrderListener() {
                @Override
                public void onSuccess(String bill_id) {
                    if (target_bill == null){
                        mon_chinh_order_adapter.setOrderList(new ArrayList<Order>());
                        mon_phu_order_adapter.setOrderList(new ArrayList<Order>());
                        nuoc_uong_order_adapter.setOrderList(new ArrayList<Order>());
                    }else {
                        if (bill_id.equals(target_bill.id)){
                            mon_chinh_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonChinh());
                            mon_phu_order_adapter.setOrderList(MyBundle.mOrderBusiness.locMonPhu());
                            nuoc_uong_order_adapter.setOrderList(MyBundle.mOrderBusiness.locNuocUong());
                        }
                    }

                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }

    public void startThemMonChinhOrderView(){
        checkTargetBill();

        UpdateMainOrderActivity.BILL_ID = target_bill.id;
        UpdateMainOrderActivity.target_order = new Order();
        UpdateMainOrderActivity.isEdit = false;
        Intent intent = new Intent(this, UpdateMainOrderActivity.class);
        startActivityForResult(intent, THEM_ORDER);
    }

    public void startEditMonChinhOrderView(Order order){
        UpdateMainOrderActivity.BILL_ID = target_bill.id;
        UpdateMainOrderActivity.target_order = order;
        UpdateMainOrderActivity.isEdit = true;
        Intent intent = new Intent(this, UpdateMainOrderActivity.class);
        startActivity(intent);
    }

    public void startThemMonPhuOrderView(){
        checkTargetBill();

        UpdateMonPhuActivity.BILL_ID = target_bill.id;
        UpdateMonPhuActivity.target_order = new Order();
        UpdateMonPhuActivity.isEdit = false;
        Intent intent = new Intent(this, UpdateMonPhuActivity.class);
        startActivityForResult(intent, THEM_ORDER);
    }

    public void startEditMonPhuOrderView(Order order){
        UpdateMonPhuActivity.BILL_ID = target_bill.id;
        UpdateMonPhuActivity.target_order = order;
        UpdateMonPhuActivity.isEdit = true;
        Intent intent = new Intent(this, UpdateMonPhuActivity.class);
        startActivity(intent);
    }

    public void startThemNuocUongOrderView(){
        checkTargetBill();

        UpdateNuocUongActivity.BILL_ID = target_bill.id;
        UpdateNuocUongActivity.target_order = new Order();
        UpdateNuocUongActivity.isEdit = false;
        Intent intent = new Intent(this, UpdateNuocUongActivity.class);
        startActivityForResult(intent, THEM_ORDER);
    }

    public void startEditNuocUongOrderView(Order order){
        UpdateNuocUongActivity.BILL_ID = target_bill.id;
        UpdateNuocUongActivity.target_order = order;
        UpdateNuocUongActivity.isEdit = true;
        Intent intent = new Intent(this, UpdateNuocUongActivity.class);
        startActivity(intent);
    }

    private void startThanhToanView(){
        ThanhToanActivity.thanh_toan_ban = hien_thi_ban;
        ThanhToanActivity.ban_target = ban_target;
        ThanhToanActivity.mang_ve_target = mang_ve;
        ThanhToanActivity.bill_target = target_bill;

        Intent intent = new Intent(this, ThanhToanActivity.class);
        startActivityForResult(intent, THANH_TOAN_ORDER);
    }

    public void xoaOrder(Order order){
        taoDialog(order);
    }

    private boolean checkTrangThaiBanDaPhucVuKhacOrder(Order order_xoa){
        for (Order order : MyBundle.mOrderBusiness.order_list){
            if (!order.id.equals(order_xoa.id)){
                if (order.trang_thai == 0){
                    return false;
                }
            }
        }

        return true;
    }

    private void taoDialog(final Order order){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        if (MyBundle.mOrderBusiness.order_list.size() == 1){
                            FirebaseHelper.xoaBill(target_bill);
                            target_bill = null;
                            if (hien_thi_ban){
                                ban_target.trang_thai = 0;
                                FirebaseHelper.updateBan(ban_target);
                            }else {
                                mang_ve.trang_thai = 0;
                                FirebaseHelper.updateMangVe(mang_ve);
                            }
                        }else {
                            if (checkTrangThaiBanDaPhucVuKhacOrder(order)){
                                if (hien_thi_ban){
                                    ban_target.trang_thai = 2;
                                    FirebaseHelper.updateBan(ban_target);
                                }else {
                                    mang_ve.trang_thai = 2;
                                    FirebaseHelper.updateMangVe(mang_ve);
                                }

                            }
                        }

                        FirebaseHelper.xoaOrder(order);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa order này?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == THANH_TOAN_ORDER && resultCode == 1){
            mon_chinh_order_adapter.clear();
            mon_phu_order_adapter.clear();
            nuoc_uong_order_adapter.clear();
        }
    }
}
