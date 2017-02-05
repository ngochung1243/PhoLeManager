package hungmai.phodocorder.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hungmai.phodocorder.R;
import hungmai.phodocorder.adapter.BanOrderAdapter;
import hungmai.phodocorder.adapter.MangVeOrderAdapter;
import hungmai.phodocorder.adapter.OrderAdapter;
import hungmai.phodocorder.helper.FirebaseHelper;
import hungmai.phodocorder.model.Ban;
import hungmai.phodocorder.model.Bill;
import hungmai.phodocorder.model.MangVe;
import hungmai.phodocorder.model.MyBundle;
import hungmai.phodocorder.model.Order;
import hungmai.phodocorder.model.OrderBusiness;

import static android.R.attr.order;

public class DocOrderActivity extends AppCompatActivity {

    BanOrderAdapter banOrderAdapter;
    MangVeOrderAdapter mangVeOrderAdapter;
    OrderAdapter orderAdapter;

    Button btnBan;
    Button btnMangVe;
    Button btnPhucVu;

    TextView tvOrderTitle;

    RecyclerView rcvOrder;
    RecyclerView rcvShowOrder;

    Ban ban_target = new Ban();
    MangVe mang_ve_target = new MangVe();
    Bill bill_target;

    boolean first_load = true;
    boolean hien_thi_ban = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_order);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Map();
        setListener();
        setAdapter();
        setRecyclerView();
        loadBill();

        setMangVeOrder();
        setBanOrder();
    }

    private void Map(){
        btnBan = (Button)findViewById(R.id.btnBan);
        btnMangVe = (Button)findViewById(R.id.btnMangVe);
        btnPhucVu = (Button)findViewById(R.id.btnPhucVu);

        tvOrderTitle = (TextView) findViewById(R.id.tvOrderTitle);

        rcvOrder = (RecyclerView) findViewById(R.id.rcvOrder);
        rcvShowOrder = (RecyclerView)findViewById(R.id.rcvShowOrder);
    }

    private void setListener(){
        btnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mang_ve_target = new MangVe();
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

        btnPhucVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taoPhucVuDialog();
            }
        });
    }

    private void setAdapter(){
        banOrderAdapter = new BanOrderAdapter(this);
        mangVeOrderAdapter = new MangVeOrderAdapter(this);
        orderAdapter = new OrderAdapter();
    }

    private void setRecyclerView(){
        rcvOrder.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvOrder.setLayoutManager(layoutManager);

        rcvShowOrder.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        rcvShowOrder.setLayoutManager(layoutManager2);
        rcvShowOrder.setAdapter(orderAdapter);
    }

    private void loadBill(){
        MyBundle.mOrderBusiness.loadBillBanOrder(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    private void setBanDaOrder(){
        MyBundle.mOrderBusiness.updateBanDaOrder(ban_target);
    }

    private void setMangVeDaOrder(){
        MyBundle.mOrderBusiness.updateMangVeDaOrder(mang_ve_target);
    }

    private void setBanOrder(){
        rcvOrder.setAdapter(banOrderAdapter);

        MyBundle.mOrderBusiness.loadBanOrder(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {
                if (MyBundle.mOrderBusiness.ban_order_list.size() > 0){
                    btnBan.setText("Bàn (" + MyBundle.mOrderBusiness.ban_order_list.size() + ")");
                    btnBan.setTextColor(Color.RED);
                }else {
                    btnBan.setText("Bàn");
                    btnBan.setTextColor(Color.WHITE);
                }

                banOrderAdapter.setBanOrderList(MyBundle.mOrderBusiness.ban_order_list);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), "error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMangVeOrder(){
        rcvOrder.setAdapter(mangVeOrderAdapter);
        MyBundle.mOrderBusiness.loadMangVeOrder(new OrderBusiness.OrderBusinessListener() {
            @Override
            public void onSuccess() {
                if (MyBundle.mOrderBusiness.mang_ve_order_list.size() > 0){
                    btnMangVe.setText("Mang về (" + MyBundle.mOrderBusiness.mang_ve_order_list.size() + ")");
                    btnMangVe.setTextColor(Color.RED);
                }else {
                    btnMangVe.setText("Mang về");
                    btnMangVe.setTextColor(Color.WHITE);
                }
                mangVeOrderAdapter.setMangVeOrderList(MyBundle.mOrderBusiness.mang_ve_order_list);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getApplicationContext(), "error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hien_thi_ban_order(Ban ban){

        tvOrderTitle.setText("Bàn " + ban.ban_so);
        ban_target = ban;

        bill_target = MyBundle.mOrderBusiness.loc_bill_ban(ban.ban_so);
        if (bill_target != null){
            MyBundle.mOrderBusiness.loadOrder(bill_target.id, new OrderBusiness.OrderListener() {
                @Override
                public void onSuccess(String bill_id) {
                    if (bill_target == null){
                        orderAdapter.clear();
                    }else if (bill_id.equals(bill_target.id)){
                        orderAdapter.setOrderList(MyBundle.mOrderBusiness.locMonChinh(), MyBundle.mOrderBusiness.locMonPhu(), MyBundle.mOrderBusiness.locNuocUong());
                    }
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(DocOrderActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            orderAdapter.clear();
        }
    }

    public void hien_thi_mang_ve(MangVe mang_ve){
        tvOrderTitle.setText("Mang về");
        this.mang_ve_target = mang_ve;
        bill_target = MyBundle.mOrderBusiness.loc_bill_mang_ve(this.mang_ve_target.id);
        if (bill_target != null){
            MyBundle.mOrderBusiness.loadOrder(bill_target.id, new OrderBusiness.OrderListener() {
                @Override
                public void onSuccess(String bill_id) {
                    if (bill_target == null){
                        orderAdapter.clear();
                    }else if (bill_id.equals(bill_target.id)){
                        orderAdapter.setOrderList(MyBundle.mOrderBusiness.locMonChinh(),MyBundle.mOrderBusiness.locMonPhu(), MyBundle.mOrderBusiness.locNuocUong());
                    }
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(DocOrderActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            orderAdapter.clear();
        }
    }

    private void taoPhucVuDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        MyBundle.mOrderBusiness.updateDaOrder();

                        if (hien_thi_ban){
                            setBanDaOrder();
                        }else {
                            setMangVeDaOrder();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn phục vụ order này?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
