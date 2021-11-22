package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sql.JDBCUtil;

public class GDLogin extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton login;
	private JLabel username;
	private JTextField ID;
	private JLabel password;
	private JPasswordField pass;
	public GDLogin(){
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        userInterface();
        setResizable(false);
	}
	private void userInterface(){
        JPanel main_pan = new JPanel(new GridLayout(1, 2));
        
        JPanel left_pan = new JPanel(new BorderLayout());
        
        left_pan.setBackground(new Color(0, 80, 239));
        
        JLabel logo = new JLabel(new ImageIcon("img//books.png"));
        logo.setHorizontalTextPosition(JLabel.CENTER);
        logo.setVerticalTextPosition(JLabel.BOTTOM);
        logo.setForeground(Color.white);
        logo.setFont(new Font("Segoe UI", 0, 25));
        logo.setText("Quản lý điểm học phần");
        left_pan.add(logo);
        
        main_pan.add(left_pan);
        
        JPanel right_pan = new JPanel(new BorderLayout());
        right_pan.setBackground(Color.white);
        
        JPanel right_comp = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Đăng nhập");
        title.setPreferredSize(new Dimension(this.getWidth(), 70));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Segoe UI", 0, 30));
        right_comp.add(title, "North");
        
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        username = new JLabel("Tài khoản:");
        username.setFont(new Font("Segoe UI", 0, 14));
        username.setPreferredSize(new Dimension(200, 20));
        pan.add(username);
        ID = new JTextField();
        ID.setPreferredSize(new Dimension(200, 30));
        pan.add(ID);
        
        password = new JLabel("Mật khẩu:");
        password.setFont(new Font("Segoe UI", 0, 14));
        password.setPreferredSize(new Dimension(200, 20));
        pan.add(password);
        pass = new JPasswordField();
        pass.setPreferredSize(new Dimension(200, 30));
        pan.add(pass);
        
        right_comp.add(pan);
        
        right_pan.add(right_comp);
        
        JPanel pan_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pan_btn.setPreferredSize(new Dimension(this.getWidth(), 70));

        login = new JButton("Đăng nhập");
        login.setPreferredSize(new Dimension(120, 30));
        login.setFont(new Font("Segoe UI", 0, 17));
        login.setBackground(new Color(255, 255, 255));
        login.setForeground(new Color(0, 80, 239));
        login.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 239), 1, true));
        login.addActionListener(this);
        login.setFocusable(false);
        pan_btn.add(login);
        right_pan.add(pan_btn, "South");
        
        main_pan.add(right_pan);
        
        getContentPane().add(main_pan);
    }

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == login) {
			try {
				Connection con = JDBCUtil.getConnection();

				Statement stm = con.createStatement();
				
				String tk = ID.getText();
				String mk = new String(pass.getPassword());

				String sql = "select * from account "
						+ "where tai_khoan = '" + tk + "' and mat_khau = '" + mk + "';";

				ResultSet rs = stm.executeQuery(sql);
				
				if(rs.next()) {
					JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					String idAcc = rs.getString("id_account");
					this.dispose();
					if (idAcc.substring(0, 2).equals("gv")) {
						GDQuanLyCuaGV gdgv = new GDQuanLyCuaGV(idAcc);
						gdgv.showGUI();
					}
					else {
						GDQuanLyCuaSV gdsv = new GDQuanLyCuaSV(idAcc);
						gdsv.showGUI();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Đăng nhập thất bại", "Thông báo", JOptionPane.ERROR_MESSAGE);
				}
				stm.close();
				JDBCUtil.closeConnection(con);
			} catch (SQLException c) {
				c.printStackTrace();
			}
		}
	}
	public void visible() {
		this.setVisible(true);
	}
}
