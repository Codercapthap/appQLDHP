package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sql.JDBCUtil;

public class SinhVien {
	private String mssv, hoTen, idAccount, namHocTap;
	
	public SinhVien(String ms, String ten, String idAcc, String namHoc) {
		mssv = new String(ms);
		hoTen = new String(ten);
		idAccount = new String(idAcc);
		namHocTap =  new String(namHoc);
	}

	public static SinhVien getSVTheoIdAccount(String id) {
		String ms = null, ten = null, namHT = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement();

			String sql = "SELECT mssv, ho_ten " +
					"FROM sinh_vien " +
					"WHERE id_account = '" + id + "';";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.next()) {
				ms = rs.getString("mssv");
				ten = rs.getString("ho_ten");
				namHT = "20" + ms.substring(1, 3);
			} else {
				return null;
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new SinhVien(ms, ten, id, namHT);
	}
	
	public String getNamHocTap() {
		return namHocTap;
	}
	
	public String getMSSV() {
		return mssv;
	}
	
	public String getHoTenSV() {
		return hoTen;
	}

	public HocPhan[] getDSHocPhan(String nh, String hk) {
		HocPhan[] dsHP = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT DISTINCT h.ma_hoc_phan ma_hp, h.ten_hoc_phan ten_hp, h.so_tin_chi so_tc " +
					     "FROM diem_hoc_phan d JOIN hoc_phan h ON d.ma_hoc_phan = h.ma_hoc_phan " +
						 "WHERE d.mssv = '" + mssv + "' AND d.nam_hoc = '" + nh + "' AND d.hoc_ky = '" + hk + "'";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
				int count = 0;		// Lưu số lượng hàng trong ResultSet

				while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
					count++;
				}

				dsHP = new HocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

				rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

				count = 0;	// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsHP[count] = new HocPhan(rs.getString("ma_hp"), rs.getString("ten_hp"), rs.getInt("so_tc"));
					count++;
				}
			} else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
				rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

				dsHP = new HocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

				rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

				int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsHP[i] = new HocPhan(rs.getString("ma_hp"), rs.getString("ten_hp"), rs.getInt("so_tc"));
					i++;
				}
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsHP;
	}
	
	public HocPhan[] getDSHocPhanTichLuy(String nh, String hk) {
		HocPhan[] dsHP = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT DISTINCT h.ma_hoc_phan ma_hp, h.ten_hoc_phan ten_hp, h.so_tin_chi so_tc " +
				     "FROM diem_hoc_phan d JOIN hoc_phan h ON d.ma_hoc_phan = h.ma_hoc_phan " +
					 "WHERE d.mssv = '"  + mssv + "' AND d.nam_hoc <= '" + nh + "' except " + 
				     "(select distinct h.ma_hoc_phan ma_hp, h.ten_hoc_phan ten_hp, h.so_tin_chi so_tc " +
					 "from diem_hoc_phan d join hoc_phan h on d.ma_hoc_phan = h.ma_hoc_phan " +
				     "where d.mssv = '" + mssv + "' and d.nam_hoc = '" + nh + "' and hoc_ky > + '" + hk + "')";
			ResultSet rs = stm.executeQuery(sql);

			if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {	// Nếu ResultSet chỉ có thể duyệt 1 chiều từ trên xuống
				int count = 0;		// Lưu số lượng hàng trong ResultSet

				while(rs.next()) {		// Duyệt xuống từng hàng để đếm số lượng hàng
					count++;
				}

				dsHP = new HocPhan[count];		// Cấp phát mảng có số lượng phần tử là count

				rs = stm.executeQuery(sql);		// Thực thi lại câu sql để lấy lại ResultSet để duyệt từ trên xuống

				count = 0;	// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsHP[count] = new HocPhan(rs.getString("ma_hp"), rs.getString("ten_hp"), rs.getInt("so_tc"));
					count++;
				}
			} else { // Nếu JDBC Driver hỗ trợ ResultSet duyệt 2 chiều
				rs.last();		// Nhảy xuống hàng sau hàng cuối cùng -> có số thứ tự bằng số lượng hàng của ResultSet

				dsHP = new HocPhan[rs.getRow()];	// Cấp phát mảng có số lượng phần tử là số thứ tự của hàng hiện tại

				rs.beforeFirst();	// Nhảy lên hàng trước hàng đầu tiên đề duyệt từng hàng từ trên xuống

				int i = 0;		// Dùng để lặp qua các phần tử trong mảng dsHP

				while(rs.next()) {
					dsHP[i] = new HocPhan(rs.getString("ma_hp"), rs.getString("ten_hp"), rs.getInt("so_tc"));
					i++;
				}
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsHP;
	}

	public static String getHoTenTheoMSSV(String mssv) {
		String ten = null;

		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement();

			String sql = "SELECT ho_ten FROM sinh_vien WHERE mssv = '" + mssv + "'";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.next()) {
				ten = rs.getString("ho_ten");
			} else {
				return null;
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ten;
	}
}
