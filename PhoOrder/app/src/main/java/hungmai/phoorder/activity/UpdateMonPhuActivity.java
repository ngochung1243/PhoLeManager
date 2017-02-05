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
import hungmai.phoorder.defaultvalue.GiaMonPhu;
import hungmai.phoorder.helper.DateTimeHelper;
import hungmai.phoorder.helper.FirebaseHelper;
import hungmai.phoorder.model.Order;

public class UpdateMonPhuActivity extends AppCompatActivity implements View.OnClickListener{

    public static String BILL_ID = "bill1";
    public static Order target_order = new Order();
    public static boolean isEdit = false;

    public static final int LOAI_DEFAULT = -1;
    public static final int LOAI_CHEN_THUONG = 0;
    public static final int LOAI_CHEN_BAP = 1;
    public static final int LOAI_CHEN_TRUNG = 2;
    public static final int LOAI_CHEN_2_TRUNG = 3;
    public static final int LOAI_CHEN_BANH = 4;
    public static final int LOAI_XI_QUACH_1_NGUOI = 5;
    public static final int LOAI_XI_QUACH_2_NGUOI = 6;
    public static final int LOAI_YAOURT_HU = 7;

    CheckBox cbChenTai;
    CheckBox cbChenNam;
    CheckBox cbChenGau;
    CheckBox cbChenGan;
    CheckBox cbChenVien;
    CheckBox cbChenVe;
    CheckBox cbChenBap;
    CheckBox cbChenTrung;
    CheckBox cbChen2Trung;
    CheckBox cbChenBanh;
    CheckBox cbYaourtHu;
    CheckBox cbXiQuach1Nguoi;
    CheckBox cbXiQuach2Nguoi;

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
    List<String> ten_mon_list = new ArrayList<String>();

    int so_luong = 0;

    int gia = 0;
    int gia_tong = 0;

    int loai_mon_phu = LOAI_DEFAULT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mon_phu_order);

        Map();
        setListener();

        loadOrder();
    }

    public void Map(){
        cbChenTai = (CheckBox)findViewById(R.id.cbChenTai);
        cbChenNam = (CheckBox)findViewById(R.id.cbChenNam);
        cbChenGau = (CheckBox)findViewById(R.id.cbChenGau);
        cbChenGan = (CheckBox)findViewById(R.id.cbChenGan);
        cbChenVien = (CheckBox)findViewById(R.id.cbChenVien);
        cbChenVe = (CheckBox)findViewById(R.id.cbChenVe);
        cbChenBap = (CheckBox)findViewById(R.id.cbChenBap);
        cbChenTrung = (CheckBox)findViewById(R.id.cbChenTrung);
        cbChen2Trung = (CheckBox)findViewById(R.id.cbChen2Trung);
        cbChenBanh = (CheckBox)findViewById(R.id.cbChenBanh);
        cbXiQuach1Nguoi = (CheckBox)findViewById(R.id.cbXiQuach1Nguoi);
        cbXiQuach2Nguoi = (CheckBox)findViewById(R.id.cbXiQuach2Nguoi);
        cbYaourtHu = (CheckBox)findViewById(R.id.cbYaourtHu);

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

    public void setListener(){
        cbChenTai.setOnClickListener(this);
        cbChenNam.setOnClickListener(this);
        cbChenGau.setOnClickListener(this);
        cbChenGan.setOnClickListener(this);
        cbChenVien.setOnClickListener(this);
        cbChenVe.setOnClickListener(this);
        cbChenBap.setOnClickListener(this);
        cbChenTrung.setOnClickListener(this);
        cbChen2Trung.setOnClickListener(this);
        cbChenBanh.setOnClickListener(this);
        cbXiQuach1Nguoi.setOnClickListener(this);
        cbXiQuach2Nguoi.setOnClickListener(this);
        cbYaourtHu.setOnClickListener(this);

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
        if (target_order.ten.size() != 0){
            checkLoai();
            loadTenPho();
            loadSoLuong();
            setGia();
            loadGhiChu();
        }
    }

    private void checkLoai(){
        for (String ten : target_order.ten){
            if (ten.equals("Bắp")){
                loai_mon_phu = LOAI_CHEN_BAP;
                return;
            }else if (ten.equals("Trứng")){
                loai_mon_phu = LOAI_CHEN_TRUNG;
                return;
            }
            else if (ten.equals("Bánh")) {
                loai_mon_phu = LOAI_CHEN_BANH;
                return;
            }else if (ten.equals("Xí quách 1 người")){
                loai_mon_phu = LOAI_XI_QUACH_1_NGUOI;
                return;
            }else if (ten.equals("Xí quách 2 người")){
                loai_mon_phu = LOAI_XI_QUACH_2_NGUOI;
                return;
            }else if (ten.equals("Yaourt hủ")){
                loai_mon_phu = LOAI_YAOURT_HU;
                return;
            }
        }

        loai_mon_phu = LOAI_CHEN_THUONG;
    }

    private void loadTenPho(){
        ten_mon_list = target_order.ten;
        for (String ten_mon : target_order.ten){
            if (ten_mon.equals("Tái")){
                cbChenTai.setChecked(true);
            }else if (ten_mon.equals("Nạm")){
                cbChenNam.setChecked(true);
            }else if (ten_mon.equals("Gầu")){
                cbChenGau.setChecked(true);
            }else if (ten_mon.equals("Gân")){
                cbChenGan.setChecked(true);
            }else if (ten_mon.equals("Viên")){
                cbChenVien.setChecked(true);
            }else if (ten_mon.equals("Vè")){
                cbChenVe.setChecked(true);
            }else if (ten_mon.equals("Bắp")){
                loai_mon_phu = LOAI_CHEN_BAP;
                cbChenBap.setChecked(true);
            }else if (ten_mon.equals("Trứng")){
                loai_mon_phu = LOAI_CHEN_TRUNG;
                cbChenTrung.setChecked(true);
            }else if (ten_mon.equals("2 Trứng")){
                loai_mon_phu = LOAI_CHEN_TRUNG;
                cbChen2Trung.setChecked(true);
            }else if (ten_mon.equals("Bánh")){
                loai_mon_phu = LOAI_CHEN_BANH;
                cbChenBanh.setChecked(true);
            }else if (ten_mon.equals("Xí quách 1 người")){
                loai_mon_phu = LOAI_XI_QUACH_1_NGUOI;
                cbXiQuach1Nguoi.setChecked(true);
            }else if (ten_mon.equals("Xí quách 2 người")){
                loai_mon_phu = LOAI_XI_QUACH_2_NGUOI;
                cbXiQuach2Nguoi.setChecked(true);
            }else if (ten_mon.equals("Yaourt hủ")){
                loai_mon_phu = LOAI_YAOURT_HU;
                cbYaourtHu.setChecked(true);
            }
        }

        setTenMon();
    }

    private void loadSoLuong(){
        so_luong = (int)target_order.so_luong_nho;

        edtSoLuong.setText(String.valueOf(so_luong));

        tvSoLuong.setText(String.valueOf(so_luong));
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

    private void updateTenMon(boolean isCheck, String mon_phu_ten){
        if (isCheck){
            ten_mon_list.add(mon_phu_ten);
        }else {
            ten_mon_list.remove(mon_phu_ten);
        }

        setGia();
        setTenMon();
    }

    private void updateChenThuong(boolean isCheck, String mon_phu_ten){

        if (isCheck){
            if (loai_mon_phu != LOAI_CHEN_THUONG){
                ten_mon_list.clear();
            }

            if (ten_mon_list.size() == 0){
                ten_mon_list.add("Chén");
            }

            setEnableVoiPhoThuong();

            loai_mon_phu = LOAI_CHEN_THUONG;
        }else if (ten_mon_list.size() == 2){
            loai_mon_phu = LOAI_DEFAULT;
            ten_mon_list.clear();
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void setEnableVoiPhoThuong(){
        cbChenBap.setChecked(false);
        cbChenTrung.setChecked(false);
        cbChenBanh.setChecked(false);
        cbXiQuach1Nguoi.setChecked(false);
        cbXiQuach2Nguoi.setChecked(false);
        cbYaourtHu.setChecked(false);
    }

    private void updateChenBap(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            ten_mon_list.add("Chén");
            loai_mon_phu = LOAI_CHEN_BAP;
            setUnCheckAll();
            cbChenBap.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateChenTrung(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            ten_mon_list.add("Chén");
            loai_mon_phu = LOAI_CHEN_TRUNG;
            setUnCheckAll();
            cbChenTrung.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateChen2Trung(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            ten_mon_list.add("Chén");
            loai_mon_phu = LOAI_CHEN_2_TRUNG;
            setUnCheckAll();
            cbChen2Trung.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateChenBanh(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            ten_mon_list.add("Chén");
            loai_mon_phu = LOAI_CHEN_BANH;
            setUnCheckAll();
            cbChenBanh.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateXiQuach1Nguoi(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            loai_mon_phu = LOAI_XI_QUACH_1_NGUOI;
            setUnCheckAll();
            cbXiQuach1Nguoi.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateXiQuach2Nguoi(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            loai_mon_phu = LOAI_XI_QUACH_2_NGUOI;
            setUnCheckAll();
            cbXiQuach2Nguoi.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void updateYaourtHu(boolean isCheck, String mon_phu_ten){
        ten_mon_list.clear();
        if (isCheck){
            loai_mon_phu = LOAI_YAOURT_HU;
            setUnCheckAll();
            cbYaourtHu.setChecked(true);
        }else{
            loai_mon_phu = LOAI_DEFAULT;
        }

        updateTenMon(isCheck, mon_phu_ten);
    }

    private void setUnCheckAll(){
        cbChenTai.setChecked(false);
        cbChenNam.setChecked(false);
        cbChenGau.setChecked(false);
        cbChenGan.setChecked(false);
        cbChenVien.setChecked(false);
        cbChenVe.setChecked(false);
        cbChenTrung.setChecked(false);
        cbChen2Trung.setChecked(false);
        cbChenBanh.setChecked(false);
        cbChenBap.setChecked(false);
        cbXiQuach1Nguoi.setChecked(false);
        cbXiQuach2Nguoi.setChecked(false);
        cbYaourtHu.setChecked(false);
    }

    private void checkGia(){
        switch (loai_mon_phu){
            case LOAI_CHEN_THUONG:
                gia = GiaMonPhu.CHEN_PHO;
                break;
            case LOAI_CHEN_BAP:
                gia = GiaMonPhu.CHEN_BAP;
                break;
            case LOAI_CHEN_TRUNG:
                gia = GiaMonPhu.CHEN_TRUNG;
                break;
            case LOAI_CHEN_2_TRUNG:
                gia = GiaMonPhu.CHEN_2_TRUNG;
                break;
            case LOAI_CHEN_BANH:
                gia = GiaMonPhu.CHEN_BANH;
                break;
            case LOAI_XI_QUACH_1_NGUOI:
                gia = GiaMonPhu.XI_QUACH_1_NGUOI;
                break;
            case LOAI_XI_QUACH_2_NGUOI:
                gia = GiaMonPhu.XI_QUACH_2_NGUOI;
                break;
            case LOAI_YAOURT_HU:
                gia = GiaMonPhu.YAOURT_HU;
                break;
            default:
                gia = 0;
        }
    }

    private void setTenMon(){
        tvTen.setText("");
        for (String mon_phu_name : ten_mon_list){
            tvTen.append(mon_phu_name + " ");
        }
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

    private void setOrder(){
        target_order.bill_id = BILL_ID;
        target_order.ten = ten_mon_list;
        target_order.loai = 1;
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
            case R.id.cbChenTai:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Tái");
                break;

            case R.id.cbChenNam:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Nạm");
                break;

            case R.id.cbChenGau:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Gầu");
                break;

            case R.id.cbChenGan:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Gân");
                break;

            case R.id.cbChenVien:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Viên");
                break;

            case R.id.cbChenVe:
                isCheck = ((CheckBox)v).isChecked();
                updateChenThuong(isCheck, "Vè");
                break;

            case R.id.cbChenBap:
                isCheck = ((CheckBox)v).isChecked();
                updateChenBap(isCheck, "Bắp");
                break;

            case R.id.cbChenTrung:
                isCheck = ((CheckBox)v).isChecked();
                updateChenTrung(isCheck, "Trứng");
                break;

            case R.id.cbChen2Trung:
                isCheck = ((CheckBox)v).isChecked();
                updateChen2Trung(isCheck, "2 Trứng");
                break;

            case R.id.cbChenBanh:
                isCheck = ((CheckBox)v).isChecked();
                updateChenBanh(isCheck, "Bánh");
                break;

            case R.id.cbXiQuach1Nguoi:
                isCheck = ((CheckBox)v).isChecked();
                updateXiQuach1Nguoi(isCheck, "Xí quách 1 người");
                break;

            case R.id.cbXiQuach2Nguoi:
                isCheck = ((CheckBox)v).isChecked();
                updateXiQuach2Nguoi(isCheck, "Xí quách 2 người");
                break;

            case R.id.cbYaourtHu:
                isCheck = ((CheckBox)v).isChecked();
                updateYaourtHu(isCheck, "Yaourt hủ");
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
