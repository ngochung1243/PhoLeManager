package hungmai.phoorder.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.Inflater;

import hungmai.phoorder.R;
import hungmai.phoorder.adapter.OrderThanhToanAdapter;
import hungmai.phoorder.model.Ban;
import hungmai.phoorder.model.Bill;
import hungmai.phoorder.model.MangVe;
import hungmai.phoorder.model.MyBundle;

import static hungmai.phoorder.R.id.edtSoLuong;
import static hungmai.phoorder.R.id.tvSoLuong;

public class ThanhToanActivity extends AppCompatActivity {

    public static Bill bill_target;
    public static Ban ban_target;
    public static MangVe mang_ve_target;
    public static boolean thanh_toan_ban;

    Button btnTinhTien;
    Button btnThanhToan;

    TextView tvTitle;
    TextView tvTongTien;

    RecyclerView rcvOrderList;

    OrderThanhToanAdapter orderThanhToanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        Map();
        setListener();
        setRecyclerView();

        setTitle();
        setTongTien();
    }

    private void Map(){
        btnTinhTien = (Button)findViewById(R.id.btnTinhTien);
        btnThanhToan = (Button)findViewById(R.id.btnThanhToan);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTongTien = (TextView) findViewById(R.id.tvTongTien);

        rcvOrderList = (RecyclerView) findViewById(R.id.rcvOrderList);
    }

    private void setListener(){
        btnTinhTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinhTien();
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thanhtoan();
            }
        });
    }

    private void setRecyclerView(){
        rcvOrderList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvOrderList.setLayoutManager(layoutManager);

        orderThanhToanAdapter = new OrderThanhToanAdapter();
        rcvOrderList.setAdapter(orderThanhToanAdapter);

        orderThanhToanAdapter.setOrderList(MyBundle.mOrderBusiness.order_list);
    }

    private void setTitle(){
        if (thanh_toan_ban){
            tvTitle.setText("Thanh toán - Bàn " + ban_target.ban_so);
        }else {
            tvTitle.setText("Thanh toán - Mang về");
        }
    }

    private void setTongTien(){
        tvTongTien.setText(String.valueOf(MyBundle.mOrderBusiness.tinhTongTien()));
    }

    private void tinhTien(){
        taoTinhTienThoiDialog();
    }

    private void taoTinhTienThoiDialog(){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_tinh_tien_thoi);
        dialog.setTitle("Tính tiền thối");

        final EditText edtTienKhach = (EditText) dialog.findViewById(R.id.edtTienKhach);
        TextView tvTongTien = (TextView) dialog.findViewById(R.id.tvTongTien);
        final TextView tvTienThoi = (TextView) dialog.findViewById(R.id.tvTienThoi);

        final int tongTien = MyBundle.mOrderBusiness.tinhTongTien();
        tvTongTien.setText(String.valueOf(tongTien));

        edtTienKhach.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    int tienKhach = Integer.parseInt(edtTienKhach.getText().toString());
                    int tienThoi = tienKhach - tongTien;
                    tvTienThoi.setText(String.valueOf(tienThoi));

                    return true;
                }
                return false;
            }
        });
        dialog.show();
    }

    private void thanhtoan(){
        MyBundle.mOrderBusiness.updateBillDaThanhToan(bill_target);
        if (thanh_toan_ban){
            MyBundle.mOrderBusiness.updateBanDaThanhToan(ban_target);
        }else {
            MyBundle.mOrderBusiness.updateMangVeDaThanhToan(mang_ve_target);
        }
        MyBundle.mOrderBusiness.order_list.clear();
        setResult(1);
        finish();
    }
}
