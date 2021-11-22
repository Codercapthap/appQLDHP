package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sql.JDBCUtil;

public class NamHoc {
    public static String[] getDSNamHoc(String namBatDau) {      // Lấy ds năm học bắt đầu từ namBatDau
        String[] dsNH = null;
        try {
            Connection con = JDBCUtil.getConnection();

            Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "SELECT ds_nam_hoc " +
                    "FROM nam_hoc " +
                    "WHERE SUBSTRING(ds_nam_hoc, 1, 4) >= '" + namBatDau + "';";

            ResultSet rs = stm.executeQuery(sql);

            if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
                int count = 0;		// Lưu số lượng hàng trong ResultSet

                while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
                    count++;
                }

                dsNH = new String[count];		// Cấp phát mảng có số lượng phần tử là count

                rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

                count = 0;	// Dùng để lặp qua các phần tử trong mảng dsHP

                while(rs.next()) {
                    dsNH[count] = new String(rs.getString("ds_nam_hoc"));
                    count++;
                }
            } else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
                rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

                dsNH = new String[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

                rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

                int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsHP

                while(rs.next()) {
                    dsNH[i] = new String(rs.getString("ds_nam_hoc"));
                    i++;
                }
            }

            stm.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNH;
    }
}
