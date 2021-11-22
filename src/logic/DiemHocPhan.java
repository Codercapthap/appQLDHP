package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sql.JDBCUtil;

public class DiemHocPhan {
    private String mssv, maHP, namHoc, hocKy, kyHieu;
    private float diem;

    public DiemHocPhan(String maSoSV, String maHocPhan, String nh, String hk, float diemHP, String kh) {
        mssv = new String(maSoSV);
        maHP = new String(maHocPhan);
        namHoc = new String(nh);
        hocKy = new String(hk);
        diem = diemHP;
        kyHieu = new String(kh);
       // soTC = soTinChi;
    }

    public static DiemHocPhan[] getDSDiemHPTheoNhomHP(String maHP, String nh, String hk, String kh) {
        DiemHocPhan[] dsDiemHP = null;

        try {
            Connection con = JDBCUtil.getConnection();

            Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT s.mssv mssv, d.diem diem " +
                    "FROM diem_hoc_phan d JOIN sinh_vien s ON d.mssv = s.mssv " +
                    "WHERE d.ma_hoc_phan = '" + maHP + "' AND " +
                    "d.nam_hoc = '" + nh + "' AND d.hoc_ky = '" + hk + "' AND d.ky_hieu = '" + kh + "'";

            ResultSet rs = stm.executeQuery(sql);

            if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
                int count = 0;		// Lưu số lượng hàng trong ResultSet

                while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
                    count++;
                }

                dsDiemHP = new DiemHocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

                rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

                count = 0;	// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[count] = new DiemHocPhan(rs.getString("mssv"), maHP, nh, hk, rs.getFloat("diem"), kh);
                    count++;
                }
            } else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
                rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

                dsDiemHP = new DiemHocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

                rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

                int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[i] = new DiemHocPhan(rs.getString("mssv"), maHP, nh, hk, rs.getFloat("diem"), kh);
                    i++;
                }
            }

            stm.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDiemHP;
    }
    
    public static DiemHocPhan[] getDSDiemHPTheoMSSV(String mssv,String nh,String hk) {
    	DiemHocPhan[] dsDiemHP = null;

        try {
            Connection con = JDBCUtil.getConnection();

            Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "Select ma_hoc_phan, diem, ky_hieu "
            		+ "	from diem_hoc_phan"
            		+ "	where mssv = '" + mssv + "' and nam_hoc = '" + nh
            		+ "' and hoc_ky = '" + hk + "';";

            ResultSet rs = stm.executeQuery(sql);

            if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
                int count = 0;		// Lưu số lượng hàng trong ResultSet

                while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
                    count++;
                }

                dsDiemHP = new DiemHocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

                rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

                count = 0;	// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[count] = new DiemHocPhan(mssv, rs.getString("ma_hoc_phan"), nh, hk, rs.getFloat("diem"), rs.getString("ky_hieu"));
                    count++;
                }
            } else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
                rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

                dsDiemHP = new DiemHocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

                rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

                int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[i] = new DiemHocPhan(mssv, rs.getString("ma_hoc_phan"), nh, hk, rs.getFloat("diem"), rs.getString("ky_hieu"));
                    i++;
                }
            }

            stm.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDiemHP;
    }

    public static DiemHocPhan[] getDSDiemHPTichLuy(String mssv, String nh, String hk) {
    	DiemHocPhan[] dsDiemHP = null;

        try {
            Connection con = JDBCUtil.getConnection();

            Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "(Select ma_hoc_phan, diem, ky_hieu "
            		+ "	from diem_hoc_phan"
            		+ "	where mssv = '" + mssv + "' and nam_hoc <= '" + nh + "') except"
            		+ "(Select ma_hoc_phan, diem, ky_hieu "
                    + "	from diem_hoc_phan"
                    + "	where mssv = '" + mssv + "' and nam_hoc = '" + nh
            		+ "' and hoc_ky > '" + hk + "');";

            ResultSet rs = stm.executeQuery(sql);

            if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
                int count = 0;		// Lưu số lượng hàng trong ResultSet

                while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
                    count++;
                }

                dsDiemHP = new DiemHocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

                rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

                count = 0;	// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[count] = new DiemHocPhan(mssv, rs.getString("ma_hoc_phan"), nh, hk, rs.getFloat("diem"), rs.getString("ky_hieu"));
                    count++;
                }
            } else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
                rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

                dsDiemHP = new DiemHocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

                rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

                int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsDiemHP

                while(rs.next()) {
                    dsDiemHP[i] = new DiemHocPhan(mssv, rs.getString("ma_hoc_phan"), nh, hk, rs.getFloat("diem"), rs.getString("ky_hieu"));
                    i++;
                }
            }

            stm.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDiemHP;
    }
    
    public String getMSSV() {
        return mssv;
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

    public Float getDiem() {
        return diem;
    }
    
    public String getKyHieu() {
        return kyHieu;
    }

}
