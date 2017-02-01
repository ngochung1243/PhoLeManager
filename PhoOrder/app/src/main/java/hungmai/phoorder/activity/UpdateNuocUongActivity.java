package hungmai.phoorder.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hungmai.phoorder.R;
import hungmai.phoorder.defaultvalue.GiaNuocUong;
import hungmai.phoorder.helper.DateTimeHelper;
import hungmai.phoorder.helper.FirebaseHelper;
import hungmai.phoorder.model.Order;

public class UpdateNuocUongActivity extends AppCompatActivity implements View.OnClickListener{

    public static String BILL_ID = "bill1";
    public static Order target_order = new Order();
    public static boolean isEdit = false;

    CheckBox cbNuocEpThom;
    CheckBox cbNuocEpCaRot;
    CheckBox cbRauMa;
    CheckBox cbDauNanh;
    CheckBox cbYaourtDa;

    CheckBox cbCoDa;
    CheckBox cbKhongDa;
    CheckBox cb1Hu;
    CheckBox cb2Hu;

    EditText edtSoLuong;
    EditText edtGhiChu;

    LinearLayout layoutKeyboard;
    ScrollView scvOrderInput;

    TextView tvTen;
    TextView tvSoLuong;
    TextView tvGia;
    TextView tvGhiChu;

    Button btnDatMon;
    Button btnHuy;

    List<String> ghichu_list = new ArrayList<String>();
    String ten_nuoc_uong = "";
    String loai = "";

    int so_luong = 0;
    int gia = 0;
    int gia_tong = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nuoc_uong_order);

        Map();
        setListener();

//        testData();
        loadOrder();
    }

    private void testData(){
        String full_ten_nuoc_uong = "Yaourt đá - 1 hủ";
        target_order.ten.add(full_ten_nuoc_uong);
        target_order.so_luong_nho = 1;
        target_order.gia = 12;
        target_order.loai = 2;
        target_order.trang_thai = 0;
        target_order.ghi_chu.add("1");
        target_order.ghi_chu.add("Ít đá");
    }

    private void Map(){
        cbNuocEpThom = (CheckBox)findViewById(R.id.cbNuocEpThom);
        cbNuocEpCaRot = (CheckBox)findViewById(R.id.cbNuocEpCaRot);
        cbRauMa = (CheckBox)findViewById(R.id.cbRauMa);
        cbDauNanh = (CheckBox)findViewById(R.id.cbDauNanh);
        cbYaourtDa = (CheckBox)findViewById(R.id.cbYaourtDa);

        cbCoDa = (CheckBox)findViewById(R.id.cbCoDa);
        cbKhongDa = (CheckBox)findViewById(R.id.cbKhongDa);
        cb1Hu = (CheckBox)findViewById(R.id.cb1Hu);
        cb2Hu = (CheckBox)findViewById(R.id.cb2Hu);

        edtSoLuong = (EditText)findViewById(R.id.edtSoLuong);
        edtGhiChu = (EditText)findViewById(R.id.edtGhichu);

        layoutKeyboard = (LinearLayout)findViewById(R.id.layoutKeyboard);
        scvOrderInput = (ScrollView)findViewById(R.id.scvOrderInput);

        tvTen = (TextView) findViewById(R.id.tvTen);
        tvSoLuong = (TextView) findViewById(R.id.tvSoLuong);
        tvGia = (TextView) findViewById(R.id.tvGia);
        tvGhiChu = (TextView) findViewById(R.id.tvGhiChu);

        btnDatMon = (Button)findViewById(R.id.btnDatMon);
        if (isEdit){
            btnDatMon.setText("Cập nhật");
        }
        btnHuy = (Button)findViewById(R.id.btnHuy);
    }

    private void setListener(){
        cbNuocEpThom.setOnClickListener(this);
        cbNuocEpCaRot.setOnClickListener(this);
        cbRauMa.setOnClickListener(this);
        cbDauNanh.setOnClickListener(this);
        cbYaourtDa.setOnClickListener(this);

        cb1Hu.setOnClickListener(this);
        cb2Hu.setOnClickListener(this);
        cbCoDa.setOnClickListener(this);
        cbKhongDa.setOnClickListener(this);

        edtSoLuong.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String so_luong_string = edtSoLuong.getText().toString();
                    if (!so_luong_string.equals("")){
                        so_luong = Integer.parseInt(so_luong_string);
                        tvSoLuong.setText(String.valueOf(so_luong));
                    }else {
                        so_luong = 0;
                        tvSoLuong.setText("");
                    }

                    setGia();
                    return true;
                }
                return false;
            }
        });

        edtGhiChu.setOnClickListener(this);

        btnDatMon.setOnClickListener(this);
        btnHuy.setOnClickListener(this);
    }

    private void loadOrder(){
        loadTenNuocUong();
        loadSoLuong();
        setGia();
        loadGhiChu();
    }

    private void loadTenNuocUong(){
        if (target_order.ten.size() > 0){
            String full_ten_nuoc_uong = target_order.ten.get(0);
            if (full_ten_nuoc_uong.contains("Nước ép thơm")){
                ten_nuoc_uong = "Nước ép thơm";
                cbNuocEpThom.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("Nước ép cà rốt")){
                ten_nuoc_uong = "Nước ép cà rốt";
                cbNuocEpCaRot.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("Rau má")){
                ten_nuoc_uong = "Rau má";
                cbRauMa.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("Đậu nành")){
                ten_nuoc_uong = "Đậu nành";
                cbDauNanh.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("Yaourt đá")){
                ten_nuoc_uong = "Yaourt đá";
                cbYaourtDa.setChecked(true);
            }

            if (full_ten_nuoc_uong.contains("Có đá")){
                loai = "Có đá";
                cbCoDa.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("Không đá")){
                loai = "Không đá";
                cbKhongDa.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("1 hủ")){
                loai = "1 hủ";
                cb1Hu.setChecked(true);
            }else if (full_ten_nuoc_uong.contains("2 hủ")){
                loai = "2 hủ";
                cb2Hu.setChecked(true);
            }

            tvTen.setText(full_ten_nuoc_uong);
        }
    }

    private void loadSoLuong(){
        so_luong = (int)target_order.so_luong_nho;

        edtSoLuong.setText(String.valueOf(so_luong));

        tvSoLuong.setText(String.valueOf(so_luong));
    }

    private void setGia(){
        checkGia();
        if (gia == 0){
            gia_tong = 0;
            tvGia.setText("");
        }else {
            gia_tong = so_luong * gia;
            tvGia.setText(so_luong + "x" + gia + " = " + gia_tong + ".000");
        }
    }

    private void checkGia(){
        gia = 0;

        if (ten_nuoc_uong.equals("Nước ép thơm")){
            if (loai.equals("Có đá")){
                gia = GiaNuocUong.NUOC_EP_THOM_CO_DA;
            }else if (loai.equals("Không đá")){
                gia = GiaNuocUong.NUOC_EP_THOM_KHONG_DA;
            }
        }else if (ten_nuoc_uong.equals("Nước ép cà rốt")){
            if (loai.equals("Có đá")){
                gia = GiaNuocUong.NUOC_EP_CA_ROT_CO_DA;
            }else if (loai.equals("Không đá")){
                gia = GiaNuocUong.NUOC_EP_CA_ROT_KHONG_DA;
            }
        }else if (ten_nuoc_uong.equals("Rau má")){
            if (loai.equals("Có đá")){
                gia = GiaNuocUong.RAU_MA_CO_DA;
            }else if (loai.equals("Không đá")){
                gia = GiaNuocUong.RAU_MA_KHONG_DA;
            }
        }else if (ten_nuoc_uong.equals("Đậu nành")){
            if (loai.equals("Có đá")){
                gia = GiaNuocUong.DAU_NANH_CO_DA;
            }else if (loai.equals("Không đá")){
                gia = GiaNuocUong.DAU_NANH_KHONG_DA;
            }
        }else if (ten_nuoc_uong.equals("Yaourt đá")){
            if (loai.equals("1 hủ")){
                gia = GiaNuocUong.YAOURT_DA_1_HU;
            }else if (loai.equals("2 hủ")){
                gia = GiaNuocUong.YAOURT_DA_2_HU;
            }
        }
    }

    private void loadGhiChu(){
        ghichu_list = target_order.ghi_chu;
        if (ghichu_list == null){
            ghichu_list = new ArrayList<String>();
        }else {
            loadGhiChuLenView();
        }
    }

    public void keyboardReturn(View v){
        if (v.getId() == R.id.btnXoa){
            if (ghichu_list.size() != 0){
                ghichu_list.remove(ghichu_list.size() - 1);
                loadGhiChuLenView();
            }
        }else if (v.getId() == R.id.btnXuongDong){
            ghichu_list.add("\n");
            loadGhiChuLenView();
        }else if (v.getId() == R.id.btnDongKeyboard){
            layoutKeyboard.setVisibility(View.GONE);
        }else {
            ghichu_list.add(((Button)v).getText().toString());
            loadGhiChuLenView();
        }
    }

    private void loadGhiChuLenView(){
        edtGhiChu.setText(" ");
        for (String ghichu : ghichu_list){
            edtGhiChu.append(ghichu + " ");
        }
        tvGhiChu.setText(edtGhiChu.getText());
        edtGhiChu.append("_");
    }

    private void updateNuocUong(boolean isCheck, String ten_nuoc_uong, CheckBox cbCheck){
        if (isCheck){
            setUnCheckAll();
            this.loai = "";
            if (ten_nuoc_uong.equals("Yaourt đá")){
                setEnableYaourt();
            }else {
                setEnableNuocUongThuong();
            }
            cbCheck.setChecked(true);
            this.ten_nuoc_uong = ten_nuoc_uong;
        }else {
            setAllEnable();
            this.ten_nuoc_uong = "";
        }

        setGia();
        tvTen.setText(this.ten_nuoc_uong + " - " + loai);
    }

    private void updateLoai(boolean isCheck, String loai, CheckBox cbCheck) {
        if (isCheck) {
            setUnCheckAllLoai();

            cbCheck.setChecked(true);
            this.loai = loai;

        } else {
            this.loai = "";
        }

        tvTen.setText(this.ten_nuoc_uong + " - " + this.loai);
        setGia();
    }

    private void setUnCheckAll(){
        cbNuocEpThom.setChecked(false);
        cbNuocEpCaRot.setChecked(false);
        cbRauMa.setChecked(false);
        cbDauNanh.setChecked(false);
        cbYaourtDa.setChecked(false);

        cbCoDa.setChecked(false);
        cbKhongDa.setChecked(false);
        cb1Hu.setChecked(false);
        cb2Hu.setChecked(false);
    }

    private void setUnCheckAllLoai(){
        cbCoDa.setChecked(false);
        cbKhongDa.setChecked(false);
        cb1Hu.setChecked(false);
        cb2Hu.setChecked(false);
    }

    private void setEnableNuocUongThuong(){
        cbCoDa.setEnabled(true);
        cbKhongDa.setEnabled(true);
        cb1Hu.setEnabled(false);
        cb2Hu.setEnabled(false);
    }

    private void setEnableYaourt(){
        cbCoDa.setEnabled(false);
        cbKhongDa.setEnabled(false);
        cb1Hu.setEnabled(true);
        cb2Hu.setEnabled(true);
    }

    private void setAllEnable(){
        cbCoDa.setEnabled(true);
        cbKhongDa.setEnabled(true);
        cb1Hu.setEnabled(true);
        cb2Hu.setEnabled(true);
    }

    private void setOrder(){
        target_order.bill_id = BILL_ID;
        target_order.ten.clear();
        target_order.ten.add(ten_nuoc_uong + " - " + loai);
        target_order.loai = 2;
        target_order.so_luong_nho = so_luong;
        target_order.gia = gia_tong;
        if (isEdit){
            target_order.thoi_gian_order = DateTimeHelper.getTimeHienTai();
        }
        target_order.ghi_chu = ghichu_list;
        target_order.trang_thai = 0;
    }

    private void taoDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        setOrder();
                        FirebaseHelper.updateOrder(target_order);
                        setResult(0);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn thêm/cập nhật order này?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private boolean checkOrderDung(){
        if (gia_tong == 0){
            return false;
        }

        return true;
    }

    private void datmon(){
        if (checkOrderDung()){
            taoDialog();
        }else {
            Toast.makeText(this, "Bạn nhập order chưa đúng!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void huyOrder(){
        setResult(1);
        finish();
    }

    @Override
    public void onClick(View v) {
        boolean isCheck;
        switch (v.getId()){
            case R.id.cbNuocEpThom:
                isCheck = ((CheckBox)v).isChecked();
                updateNuocUong(isCheck, "Nước ép thơm", cbNuocEpThom);
                break;
            case R.id.cbNuocEpCaRot:
                isCheck = ((CheckBox)v).isChecked();
                updateNuocUong(isCheck, "Nước ép cà rốt", cbNuocEpCaRot);
                break;
            case R.id.cbRauMa:
                isCheck = ((CheckBox)v).isChecked();
                updateNuocUong(isCheck, "Rau má", cbRauMa);
                break;
            case R.id.cbDauNanh:
                isCheck = ((CheckBox)v).isChecked();
                updateNuocUong(isCheck, "Đậu nành", cbDauNanh);
                break;
            case R.id.cbYaourtDa:
                isCheck = ((CheckBox)v).isChecked();
                updateNuocUong(isCheck, "Yaourt đá", cbYaourtDa);
                break;
            case R.id.cbCoDa:
                isCheck = ((CheckBox)v).isChecked();
                updateLoai(isCheck, "Có đá", cbCoDa);
                break;
            case R.id.cbKhongDa:
                isCheck = ((CheckBox)v).isChecked();
                updateLoai(isCheck, "Không đá", cbKhongDa);
                break;
            case R.id.cb1Hu:
                isCheck = ((CheckBox)v).isChecked();
                updateLoai(isCheck, "1 hủ", cb1Hu);
                break;
            case R.id.cb2Hu:
                isCheck = ((CheckBox)v).isChecked();
                updateLoai(isCheck, "2 hủ", cb2Hu);
                break;
            case R.id.edtGhichu:
                layoutKeyboard.setVisibility(View.VISIBLE);
                scvOrderInput.post(new Runnable() {
                    @Override
                    public void run() {
                        scvOrderInput.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.btnDatMon:
                datmon();
                break;
            case R.id.btnHuy:
                huyOrder();
                break;
        }
    }
}
