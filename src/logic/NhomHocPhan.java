package logic;

public class NhomHocPhan {
    private String maHP, namHoc, hocKy, kyHieu, mscb;

    public NhomHocPhan(String maHocPhan, String nh, String hk, String kh, String msCanBo) {
        maHP = new String(maHocPhan);
        namHoc = new String(nh);
        hocKy = new String(hk);
        kyHieu = new String(kh);
        mscb = new String(msCanBo);
    }

    public String getMaHP() {
        return maHP;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public String getHocKy() {
        return hocKy;
    }

    public String getKyHieu() {
        return kyHieu;
    }

    public String getMscb() {
        return mscb;
    }
}
