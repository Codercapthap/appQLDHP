package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sql.JDBCUtil;

public class GiangVien {
	private String mscb, hoTen, idAccount, namGiangDay;
	
	public GiangVien(String ms, String ten, String idAcc, String namDay) {
		mscb = new String(ms);
		hoTen = new String(ten);
		idAccount = new String(idAcc);
		namGiangDay =  new String(namDay);
	}

	public static GiangVien getGVTheoIdAccount(String id) {
		String ms = null, ten = null, namGD = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement();

			String sql = "SELECT mscb, ho_ten, nam_bat_dau_giang_day " +
					"FROM giang_vien " +
					"WHERE id_account = '" + id + "';";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.next()) {
				ms = rs.getString("mscb");
				ten = rs.getString("ho_ten");
				namGD = rs.getString("nam_bat_dau_giang_day");
			} else {
				return null;
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new GiangVien(ms, ten, id, namGD);
	}

	public NhomHocPhan[] getDSNhomHocPhan(String nh, String hk) {
		NhomHocPhan[] dsNHP = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT DISTINCT ma_hoc_phan, ky_hieu " +
						 "FROM nhom_hoc_phan " +
					     "WHERE mscb = '" + mscb + "' AND nam_hoc = '" + nh + "' AND hoc_ky = '" + hk + "';";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
				int count = 0;		// Lưu số lượng hàng trong ResultSet

				while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
					count++;
				}

				dsNHP = new NhomHocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

				rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

				count = 0;	// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsNHP[count] = new NhomHocPhan(rs.getString("ma_hoc_phan"), nh, hk, rs.getString("ky_hieu"), mscb);
					count++;
				}
			} else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
				rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

				dsNHP = new NhomHocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

				rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

				int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsNHP[i] = new NhomHocPhan(rs.getString("ma_hoc_phan"), nh, hk, rs.getString("ky_hieu"), mscb);
					i++;
				}
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsNHP;
	}

	public String getMSCB() {
		return mscb;
	}
	
	public String getHoTen() {
		return hoTen;
	}

	public String getNamGiangDay() {
		return namGiangDay;
	}

	public void setMSCB(String ms) {
		mscb = new String(ms);
	}
	
	public void setHoTen(String ten) {
		hoTen = new String(ten);
	}

	public void setNamGiangDay(String nam) {
		namGiangDay = new String(nam);
	}
}
