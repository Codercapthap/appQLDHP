package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sql.JDBCUtil;

public class HocPhan {
	private String maHP, tenHP;
	private int soTC;

	public HocPhan(String ma, String ten, int tinChi) {
		maHP = new String(ma);
		tenHP = new String(ten);
		soTC = tinChi;
	}

	public static HocPhan getHPTheoMa(String ma) {
		HocPhan hp = null;
		try {
			Connection con = JDBCUtil.getConnection();

			Statement stm = con.createStatement();

			String sql = "SELECT ten_hoc_phan, so_tin_chi " +
					"FROM hoc_phan " +
					"WHERE ma_hoc_phan = '" + ma + "'";

			ResultSet rs = stm.executeQuery(sql);

			if(rs.next()) {
				hp = new HocPhan(ma, rs.getString("ten_hoc_phan"), rs.getInt("so_tin_chi"));
			} else {
				return null;
			}

			stm.close();
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hp;
	}

	public void setMaHP(String ma) {
		maHP = new String(ma);
	}
	
	public void setTenHP(String ten) {
		tenHP = new String(ten);
	}

	public void setSoTC(int tinChi) {
		soTC = tinChi;
	}

	public String getMaHP() {
		return maHP;
	}
	
	public String getTenHP() {
		return tenHP;
	}

	public int getSoTC() {
		return soTC;
	}
}
