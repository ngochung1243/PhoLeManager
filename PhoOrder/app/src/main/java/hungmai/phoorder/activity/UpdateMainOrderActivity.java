package hungmai.phoorder.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import hungmai.phoorder.defaultvalue.GiaMonChinh;
import hungmai.phoorder.helper.DateTimeHelper;
import hungmai.phoorder.helper.FirebaseHelper;
import hungmai.phoorder.model.Order;
import hungmai.phoorder.model.Pho;
import hungmai.phoorder.model.PhoBap;
import hungmai.phoorder.model.PhoEmBe;
import hungmai.phoorder.model.PhoGan;
import hungmai.phoorder.model.PhoGau;
import hungmai.phoorder.model.PhoNam;
import hungmai.phoorder.model.PhoTai;
import hungmai.phoorder.model.PhoTaiBam;
import hungmai.phoorder.model.PhoThapCam;
import hungmai.phoorder.model.PhoVe;
import hungmai.phoorder.model.PhoVien;
import hungmai.phoorder.model.PhoXiQuach;

/**
 * Created by Admin on 1/24/2017.
 */

public class UpdateMainOrderActivity extends AppCompatActivity implements View.OnClickListener{

    public static String BILL_ID = "bill1";
    public static Order target_order = new Order();
    public static boolean isEdit = false;

    CheckBox cbTai;
    CheckBox cbTaiBam;
    CheckBox cbNam;
    CheckBox cbGau;
    CheckBox cbGan;
    CheckBox cbVien;
    CheckBox cbVe;
    CheckBox cbThapCam;
    CheckBox cbBap;
    CheckBox cbEmBe;
    CheckBox cbXiQuach;

    EditText edtEmBe;
    EditText edtNho;
    EditText edtLon;
    EditText edtDacBiet;

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
    List<Pho> loai_pho_list = new ArrayList<Pho>();

    int so_luong_em_be = 0;
    int so_luong_nho = 0;
    int so_luong_lon = 0;
    int so_luong_dac_biet = 0;

    int gia_em_be = 0;
    int gia_nho = 0;
    int gia_lon = 0;
    int gia_dac_biet = 0;
    int gia_tong = 0;

    String thong_bao_xac_nhan = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mon_chinh_order);

        Map();
        setListener();

        loadOrder();
    }

    public void Map(){
        cbTai = (CheckBox)findViewById(R.id.cbTai);
        cbTaiBam = (CheckBox)findViewById(R.id.cbTaiBam);
        cbNam = (CheckBox)findViewById(R.id.cbNam);
        cbGau = (CheckBox)findViewById(R.id.cbGau);
        cbGan = (CheckBox)findViewById(R.id.cbGan);
        cbVien = (CheckBox)findViewById(R.id.cbVien);
        cbVe = (CheckBox)findViewById(R.id.cbVe);
        cbThapCam = (CheckBox)findViewById(R.id.cbThapCam);
        cbBap = (CheckBox)findViewById(R.id.cbBap);
        cbEmBe = (CheckBox)findViewById(R.id.cbEmBe);
        cbXiQuach = (CheckBox)findViewById(R.id.cbXiQuach);

        edtEmBe = (EditText)findViewById(R.id.edtEmBe);
        edtNho = (EditText)findViewById(R.id.edtNho);
        edtLon = (EditText)findViewById(R.id.edtLon);
        edtDacBiet = (EditText)findViewById(R.id.edtDacBiet);

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
            thong_bao_xac_nhan = "Bạn có muốn cập nhật order này?";
        }else {
            thong_bao_xac_nhan = "Bạn có muốn thêm order này?";
        }
        btnHuy = (Button)findViewById(R.id.btnHuy);
    }

    public void setListener(){
        cbTai.setOnClickListener(this);
        cbTaiBam.setOnClickListener(this);
        cbNam.setOnClickListener(this);
        cbGau.setOnClickListener(this);
        cbGan.setOnClickListener(this);
        cbVien.setOnClickListener(this);
        cbVe.setOnClickListener(this);
        cbThapCam.setOnClickListener(this);
        cbBap.setOnClickListener(this);
        cbEmBe.setOnClickListener(this);
        cbXiQuach.setOnClickListener(this);

        edtEmBe.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setSoLuong();
                    setGia();
                    return true;
                }
                return false;
            }
        });

        edtNho.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setSoLuong();
                    setGia();
                    return true;
                }
                return false;
            }
        });

        edtLon.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setSoLuong();
                    setGia();
                    return true;
                }
                return false;
            }
        });

        edtDacBiet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setSoLuong();
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
            loadTenPho();
            loadSoLuong();
            setGia();
            loadGhiChu();
        }

        setEnableEdt();
    }

    private void loadTenPho(){
        for (String pho_name : target_order.ten){
            if (pho_name.equals("Tái")){
                loai_pho_list.add(new PhoTai());
                cbTai.setChecked(true);
            }else if (pho_name.equals("Tái bằm")){
                loai_pho_list.add(new PhoTaiBam());
                cbTaiBam.setChecked(true);
            }else if (pho_name.equals("Nạm")){
                loai_pho_list.add(new PhoNam());
                cbNam.setChecked(true);
            }else if (pho_name.equals("Gầu")){
                loai_pho_list.add(new PhoGau());
                cbGau.setChecked(true);
            }else if (pho_name.equals("Gân")){
                loai_pho_list.add(new PhoGan());
                cbGan.setChecked(true);
            }else if (pho_name.equals("Viên")){
                loai_pho_list.add(new PhoVien());
                cbVien.setChecked(true);
            }else if (pho_name.equals("Vè")){
                loai_pho_list.add(new PhoVe());
                cbVe.setChecked(true);
            }else if (pho_name.equals("Thập cẩm")){
                loai_pho_list.add(new PhoThapCam());
                cbThapCam.setChecked(true);
            }else if (pho_name.equals("Bắp")){
                loai_pho_list.add(new PhoBap());
                cbBap.setChecked(true);
            }else if (pho_name.equals("Em bé")){
                loai_pho_list.add(new PhoEmBe());
                cbEmBe.setChecked(true);
            }else if (pho_name.equals("Xí quách")){
                loai_pho_list.add(new PhoXiQuach());
                cbXiQuach.setChecked(true);
            }
        }

        setTenMon();
    }

    private void loadSoLuong(){
        so_luong_em_be = (int)target_order.so_luong_em_be;
        so_luong_nho = (int)target_order.so_luong_nho;
        so_luong_lon = (int)target_order.so_luong_lon;
        so_luong_dac_biet = (int)target_order.so_luong_dac_biet;

        edtEmBe.setText(String.valueOf(so_luong_em_be));
        edtNho.setText(String.valueOf(so_luong_nho));
        edtLon.setText(String.valueOf(so_luong_lon));
        edtDacBiet.setText(String.valueOf(so_luong_dac_biet));

        loadSoLuongLenView();
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

    private void removePhoKhoiList(Pho pho){
        for (Pho check_pho : loai_pho_list){
            if (check_pho.do_uu_tien == pho.do_uu_tien){
                loai_pho_list.remove(check_pho);
                break;
            }
        }
    }

    private void updatePho(boolean isCheck, Pho pho){
        if (isCheck){
            loai_pho_list.add(pho);
        }else {
            removePhoKhoiList(pho);
        }
        Log.d("PHO", "List pho size: " + loai_pho_list.size());

        setEnableEdt();
        setSoLuong();
        setGia();
    }

    private void updatePhoThuong(boolean isCheck, Pho pho){
        removePhoKhoiList(new PhoThapCam());
        removePhoKhoiList(new PhoEmBe());
        updatePho(isCheck, pho);
        if (isCheck){
            setEnableVoiPhoThuong();
        }
        setTenMon();
    }

    private void setEnableVoiPhoThuong(){
        cbThapCam.setChecked(false);
        cbEmBe.setChecked(false);
    }

    private void updatePhoThapCam(boolean isCheck, PhoThapCam phoThapCam){
        loai_pho_list.clear();
        updatePho(isCheck, phoThapCam);
        if (isCheck){
            setEnableVoiPhoThapCam();
        }
        setTenMon();
    }

    private void setEnableVoiPhoThapCam(){
        cbTai.setChecked(false);
        cbTaiBam.setChecked(false);
        cbNam.setChecked(false);
        cbGau.setChecked(false);
        cbGan.setChecked(false);
        cbVien.setChecked(false);
        cbVe.setChecked(false);
        cbBap.setChecked(false);
        cbEmBe.setChecked(false);
        cbXiQuach.setChecked(false);
    }

    private void updatePhoBap(boolean isCheck, PhoBap phoBap){
        removePhoKhoiList(new PhoThapCam());
        removePhoKhoiList(new PhoEmBe());
        updatePho(isCheck, phoBap);
        if (isCheck){
            setEnableVoiPhoBap();
        }

        setTenMon();
    }

    private void setEnableVoiPhoBap(){
        cbThapCam.setChecked(false);
        cbEmBe.setChecked(false);
    }

    private void updatePhoEmBe(boolean isCheck, PhoEmBe phoEmBe){
        loai_pho_list.clear();
        updatePho(isCheck, phoEmBe);
        if (isCheck){
            setEnableVoiPhoEmBe();
        }

        setTenMon();
    }

    private void setEnableVoiPhoEmBe(){
        cbTai.setChecked(false);
        cbTaiBam.setChecked(false);
        cbNam.setChecked(false);
        cbGau.setChecked(false);
        cbGan.setChecked(false);
        cbVien.setChecked(false);
        cbVe.setChecked(false);
        cbThapCam.setChecked(false);

        cbBap.setChecked(false);
        cbXiQuach.setChecked(false);
    }

    private void updatePhoXiQuach(boolean isCheck, PhoXiQuach phoXiQuach){
        removePhoKhoiList(new PhoThapCam());
        removePhoKhoiList(new PhoEmBe());
        updatePho(isCheck, phoXiQuach);
        if (isCheck){
            setEnableVoiPhoXiQuach();
        }

        setTenMon();
    }

    private void setEnableVoiPhoXiQuach(){
        cbThapCam.setChecked(false);
        cbEmBe.setChecked(false);
    }

    private void formatMacDinhEdtSoLuong(){
        edtEmBe.setEnabled(true);
        edtNho.setEnabled(true);
        edtLon.setEnabled(true);
        edtDacBiet.setEnabled(true);
    }

    private void setEnableEdt(){
        formatMacDinhEdtSoLuong();
        if (loai_pho_list.size() == 0){
            edtEmBe.setEnabled(true);
            edtNho.setEnabled(false);
            edtLon.setEnabled(false);
            edtDacBiet.setEnabled(false);
        }else if (checkTatCaLaPhoThuong()){
            formatMacDinhEdtSoLuong();
        }else if (checkChiCoPhoThapCam()){
            edtEmBe.setEnabled(false);
            edtNho.setEnabled(false);
            edtLon.setEnabled(true);
            edtDacBiet.setEnabled(false);
        }else if (checkPhoBapKhongCoXiQuach()){
            formatMacDinhEdtSoLuong();
        }else if (checkPhoBapCoXiQuach()){
            edtEmBe.setEnabled(false);
            edtNho.setEnabled(false);
            edtLon.setEnabled(true);
            edtDacBiet.setEnabled(false);
        }else if (checkChiCoPhoEmBe()){
            edtEmBe.setEnabled(true);
            edtNho.setEnabled(false);
            edtLon.setEnabled(false);
            edtDacBiet.setEnabled(false);
        }else if (checkPhoXiQuachKhongCoBap()){
            edtEmBe.setEnabled(false);
            edtNho.setEnabled(false);
            edtLon.setEnabled(true);
            edtDacBiet.setEnabled(true);
        }
    }

    private void checkGia(){
        gia_em_be = GiaMonChinh.EM_BE;
        if (checkTatCaLaPhoThuong()){
            gia_nho = GiaMonChinh.PHO_NHO;
            gia_lon = GiaMonChinh.PHO_LON;
            gia_dac_biet = GiaMonChinh.PHO_DAC_BIET;
        }else if (checkChiCoPhoThapCam()){
            gia_lon = GiaMonChinh.THAP_CAM;
        }else if (checkPhoBapKhongCoXiQuach()){
            if (checkPhoBapCoTren2PhoThuong()){
                gia_nho = GiaMonChinh.BAP_TREN_2_THUONG_NHO;
                gia_lon = GiaMonChinh.BAP_TREN_2_THUONG_LON;
                gia_dac_biet = GiaMonChinh.BAP_TREN_2_THUONG_DAC_BIET;
            }else {
                gia_nho = GiaMonChinh.BAP_NHO;
                gia_lon = GiaMonChinh.BAP_LON;
                gia_dac_biet = GiaMonChinh.BAP_DAC_BIET;
            }
        }else if (checkPhoBapCoXiQuach()){
            gia_lon = GiaMonChinh.BAP_XI_QUACH_DAC_BIET;
        }else if (checkChiCoPhoEmBe()){
            gia_em_be = GiaMonChinh.EM_BE;
        }else if (checkPhoXiQuachKhongCoBap()){
            if (checkPhoXiQuachCoTren2PhoThuong()){
                gia_lon = GiaMonChinh.XI_QUACH_TREN_2_THUONG_LON;
                gia_dac_biet = GiaMonChinh.XI_QUACH_TREN_2_THUONG_DAC_BIET;
            }else {
                gia_lon = GiaMonChinh.XI_QUACH_LON;
                gia_dac_biet = GiaMonChinh.XI_QUACH_DAC_BIET;
            }
        }
    }

    private boolean checkTatCaLaPhoThuong(){
        for (int i = 0; i < loai_pho_list.size(); i++){
            if (loai_pho_list.get(i).loai_pho != 1){
                return false;
            }
        }

        return true;
    }

    private boolean checkChiCoPhoThapCam(){
        if (loai_pho_list.size() != 1){
            return false;
        }else if (loai_pho_list.get(0).loai_pho != 2){
            return false;
        }else {
            return true;
        }
    }

    private boolean checkPhoBapKhongCoXiQuach(){
        boolean coPhoBap = false;
        boolean coPhoXiQuach = false;
        for (Pho pho : loai_pho_list){
            if (pho.loai_pho == 3){
                coPhoBap = true;
            }
            if (pho.loai_pho == 5){
                coPhoXiQuach = true;
            }
        }

        if (coPhoBap && !coPhoXiQuach){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkPhoBapCoXiQuach(){
        boolean coPhoBap = false;
        boolean coPhoXiQuach = false;
        for (Pho pho : loai_pho_list){
            if (pho.loai_pho == 3){
                coPhoBap = true;
            }
            if (pho.loai_pho == 5){
                coPhoXiQuach = true;
            }
        }

        if (coPhoBap && coPhoXiQuach){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkChiCoPhoEmBe(){
        if (loai_pho_list.size() != 1){
            return false;
        }else if (loai_pho_list.get(0).loai_pho != 4){
            return false;
        }else {
            return true;
        }
    }

    private boolean checkPhoXiQuachKhongCoBap(){
        boolean coPhoBap = false;
        boolean coPhoXiQuach = false;
        for (Pho pho : loai_pho_list){
            if (pho.loai_pho == 3){
                coPhoBap = true;
            }
            if (pho.loai_pho == 5){
                coPhoXiQuach = true;
            }
        }

        if (coPhoXiQuach && !coPhoBap){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkPhoBapCoTren2PhoThuong(){
        boolean coPhoBap = false;
        int soPhoThuong = 0;
        for (Pho pho : loai_pho_list){
            if (pho.loai_pho == 3){
                coPhoBap = true;
            }

            if (pho.loai_pho == 1){
                soPhoThuong += 1;
            }
        }

        if (coPhoBap && soPhoThuong >= 2){
            return true;
        }else {
            return false;
        }
    }

    private boolean checkPhoXiQuachCoTren2PhoThuong(){
        boolean coPhoXiQuach = false;
        int soPhoThuong = 0;
        for (Pho pho : loai_pho_list){
            if (pho.loai_pho == 5){
                coPhoXiQuach = true;
            }

            if (pho.loai_pho == 1){
                soPhoThuong += 1;
            }
        }

        if (coPhoXiQuach && soPhoThuong >= 2){
            return true;
        }else {
            return false;
        }
    }

    private void setTenMon(){
        tvTen.setText("");
        for (Pho pho : loai_pho_list){
            tvTen.append(pho.ten + " ");
        }
    }

    private void setGia(){
        tvGia.setText("");
        checkGia();
        boolean isHas = false;
        if (loai_pho_list.size() > 0){
            if (!edtEmBe.getText().toString().equals("0") && !edtEmBe.getText().toString().equals("") && edtEmBe.isEnabled()){
                tvGia.append(edtEmBe.getText().toString() + "x" + gia_em_be + " ");
                isHas = true;
            }else{
                gia_em_be = 0;
            }

            if (!edtNho.getText().toString().equals("0") && !edtNho.getText().toString().equals("") && edtNho.isEnabled()){
                if (isHas){
                    tvGia.append("+ ");
                }
                tvGia.append(edtNho.getText().toString() + "x" + gia_nho + " ");
                isHas = true;
            }else{
                gia_nho = 0;
            }

            if (!edtLon.getText().toString().equals("0") && !edtLon.getText().toString().equals("") && edtLon.isEnabled()){
                if (isHas){
                    tvGia.append("+ ");
                }
                tvGia.append(edtLon.getText().toString() + "x" + gia_lon + " ");
                isHas = true;
            }else{
                gia_lon = 0;
            }

            if (!edtDacBiet.getText().toString().equals("0") && !edtDacBiet.getText().toString().equals("") && edtDacBiet.isEnabled()){
                if (isHas){
                    tvGia.append("+ ");
                }
                tvGia.append(edtDacBiet.getText().toString() + "x" + gia_dac_biet);
            }else {
                gia_dac_biet = 0;
            }

            gia_tong = gia_em_be * so_luong_em_be + gia_nho * so_luong_nho + gia_lon * so_luong_lon + gia_dac_biet * so_luong_dac_biet;
            tvGia.append(" = " + gia_tong + ".000");
        }else {
            gia_tong = 0;
            tvGia.append("");
        }
    }

    private void loadSoLuongLenView(){
        tvSoLuong.setText("");
        if (so_luong_em_be != 0){
            tvSoLuong.append(edtEmBe.getText().toString() + " Em bé ");
        }

        if (so_luong_nho != 0){
            tvSoLuong.append(edtNho.getText().toString() + " Nhỏ ");
        }

        if (so_luong_lon != 0){
            tvSoLuong.append(edtLon.getText().toString() + " Lớn ");
        }

        if (so_luong_dac_biet != 0){
            tvSoLuong.append(edtDacBiet.getText().toString() + " Đặc biệt ");
        }
    }

    private void setSoLuong(){
        if (!edtEmBe.getText().toString().equals("0") && !edtEmBe.getText().toString().equals("") && edtEmBe.isEnabled()){
            so_luong_em_be = Integer.parseInt(edtEmBe.getText().toString());
        }else{
            so_luong_em_be = 0;
        }

        if (!edtNho.getText().toString().equals("0") && !edtNho.getText().toString().equals("") && edtNho.isEnabled()){
            so_luong_nho = Integer.parseInt(edtNho.getText().toString());
        }else{
            so_luong_nho = 0;
        }

        if (!edtLon.getText().toString().equals("0") && !edtLon.getText().toString().equals("") && edtLon.isEnabled()){
            so_luong_lon = Integer.parseInt(edtLon.getText().toString());
        }else{
            so_luong_lon = 0;
        }

        if (!edtDacBiet.getText().toString().equals("0") && !edtDacBiet.getText().toString().equals("") && edtDacBiet.isEnabled()){
            so_luong_dac_biet = Integer.parseInt(edtDacBiet.getText().toString());
        }else{
            so_luong_dac_biet = 0;
        }

        loadSoLuongLenView();
    }

    private Order setOrder(){
        target_order.bill_id = BILL_ID;
        target_order.ten.clear();
        for (Pho pho : loai_pho_list){
            target_order.ten.add(pho.ten);
        }

        target_order.loai = 0;
        target_order.so_luong_em_be = so_luong_em_be;
        target_order.so_luong_nho = so_luong_nho;
        target_order.so_luong_lon = so_luong_lon;
        target_order.so_luong_dac_biet = so_luong_dac_biet;
        target_order.gia = gia_tong;
        if (!isEdit){
            target_order.thoi_gian_order = DateTimeHelper.getTimeHienTai();
        }
        target_order.ghi_chu = ghichu_list;
        target_order.trang_thai = 0;

        return target_order;
    }

    private void taoDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Order target_order = setOrder();
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
        builder.setMessage(thong_bao_xac_nhan).setPositiveButton("Yes", dialogClickListener)
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
            case R.id.cbTai:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoTai());
                break;

            case R.id.cbTaiBam:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoTaiBam());
                break;

            case R.id.cbNam:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoNam());
                break;

            case R.id.cbGau:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoGau());
                break;

            case R.id.cbGan:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoGan());
                break;

            case R.id.cbVien:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoVien());
                break;

            case R.id.cbVe:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThuong(isCheck, new PhoVe());
                break;

            case R.id.cbThapCam:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoThapCam(isCheck, new PhoThapCam());
                break;

            case R.id.cbBap:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoBap(isCheck, new PhoBap());
                break;

            case R.id.cbEmBe:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoEmBe(isCheck, new PhoEmBe());
                break;

            case R.id.cbXiQuach:
                isCheck = ((CheckBox)v).isChecked();
                updatePhoXiQuach(isCheck, new PhoXiQuach());
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
