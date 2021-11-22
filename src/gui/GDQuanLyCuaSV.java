package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gui.GDLogin;
import logic.DiemHocPhan;
import logic.HocKy;
import logic.HocPhan;
import logic.NamHoc;
import logic.SinhVien;

public class GDQuanLyCuaSV extends JFrame implements ActionListener {
    private JPanel panelChinh;

    //-- panelMenu
    private JButton btnDX;
    private Box hBox;
    private JPanel panelMenu;
    //

    //-- panel1
    private JLabel lbTuaDe, lbNamHoc, lbHocKy, lbDanhSach, lbDiemTBHK, lbDiemTBTL, lbSoTCTL, lbSoTCTLHK, lbMSSV, lbHoTen;
    private JComboBox<String> cBNamHoc, cBHocKy;
    private JTable table;
    private JButton btnXuat;
    private JScrollPane scrollPane1;
    private JPanel panelCB1, paneltable, panelXemDSHP, panel1, panelDiemTB, panelDiemTL, panelDiem, panelThongTin, panelTuaDe;
    // Giới hạn số lượng hàng mà scrollPane hiển thị
    private final static int LIMIT_OF_ROWS = 9;
    //
    
    private Font f20 = new Font("Sans-serif", Font.PLAIN, 20);
    private Font f15 = new Font("Sans-serif", Font.PLAIN, 15);

    private SinhVien sv;

    public GDQuanLyCuaSV(String idAcount) {
        this.setTitle("Xem điểm");
        this.setSize(850, 690);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        sv = SinhVien.getSVTheoIdAccount(idAcount);

        setUpPanelMenu();
        setUpPanel1();

        panelChinh = new JPanel();
        panelChinh.add(panel1, "giao diện xem điểm");

        this.add(panelMenu, BorderLayout.NORTH);
        this.add(panelChinh);

        // -- Kết thúc thiết lập frame
    }

    public void showGUI() {
        this.setVisible(true);
    }

    public void setUpPanelMenu() {

        btnDX = new JButton(" Đăng xuất ");

        btnDX.setBackground(new Color(102, 102, 255));

        btnDX.setForeground(new Color(240, 255, 255));

        btnDX.setFocusPainted(false);

        btnDX.setFont(new Font("Arial", Font.PLAIN, 18));

        btnDX.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 15));
        btnDX.addActionListener(this);

        hBox = Box.createHorizontalBox();
        hBox.add(Box.createRigidArea(new Dimension(5, 50)));
        hBox.add(Box.createHorizontalGlue());
        hBox.add(btnDX);

        panelMenu = new JPanel(new BorderLayout());
        panelMenu.add(hBox, BorderLayout.NORTH);
        panelMenu.setBackground(new Color(102, 102, 255));
    }

    public void setUpPanel1() {
    	
        // -- Thiết lập panelTuaDe: chứa tựa đề, MSSV và họ tên
    	panelThongTin = new JPanel();
        lbMSSV = new JLabel("Mã số sinh viên: " + sv.getMSSV());
        lbMSSV.setFont(f20);
        lbMSSV.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        lbHoTen = new JLabel("Họ và tên: " + sv.getHoTenSV());
        lbHoTen.setFont(f20);
        lbHoTen.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        panelThongTin.add(lbMSSV);
        panelThongTin.add(lbHoTen);
        panelTuaDe = new JPanel(new BorderLayout());
        lbTuaDe = new JLabel();
        lbTuaDe.setText("Xem danh sách điểm học phần");
        lbTuaDe.setFont(new Font("Sans-serif", Font.PLAIN, 35));
        lbTuaDe.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lbTuaDe.setHorizontalAlignment(JLabel.CENTER);
        panelTuaDe.add(lbTuaDe, BorderLayout.NORTH);
        panelTuaDe.add(panelThongTin);

        // -- Kết thúc thiết lập panelThongTin


        // -- Thiết lập panelCB: chứa lbNamHoc, cBNamHoc, lbHocKy, cBHocKy và btnLietKe

        lbNamHoc = new JLabel("Năm học:");
        lbNamHoc.setFont(f20);
        lbNamHoc.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        lbHocKy = new JLabel("Học kỳ:");
        lbHocKy.setFont(f20);
        lbHocKy.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 5));
        btnXuat = new JButton("Xuất Excel");
        btnXuat.addActionListener(this);
        btnXuat.setFont(f15);
        btnXuat.setFocusPainted(false);
		JLabel lbEmpty = new JLabel();
		lbEmpty.setBorder(BorderFactory.createEmptyBorder(0, 90, 0, 0));
        
        // Lấy năm học từ csdl bắt đầu từ năm mà sinh viên bắt đầu học
        cBNamHoc = new JComboBox<>(NamHoc.getDSNamHoc(sv.getNamHocTap()));
        cBNamHoc.setFont(f15);
        cBNamHoc.setFocusable(false);
        cBNamHoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUptable();
            }
        });

        cBHocKy = new JComboBox<>(HocKy.getDSHocKy());
        cBHocKy.setFont(f15);
        cBHocKy.setFocusable(false);
        cBHocKy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUptable();
            }
        });
        panelCB1 = new JPanel();
        panelCB1.add(lbNamHoc);
        panelCB1.add(cBNamHoc);
        panelCB1.add(lbHocKy);
        panelCB1.add(cBHocKy);
        panelCB1.add(lbEmpty);
        panelCB1.add(btnXuat);
        panelCB1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // -- Kết thúc thiết lập panelCB


        // -- Thiết lập panelTable

        lbDanhSach = new JLabel("Danh sách điểm học phần");
        lbDanhSach.setFont(f20);
        lbDanhSach.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Ban đầu tạo table rỗng và bỏ vào bên trong scrollPane
        table = new JTable();
        scrollPane1 = new JScrollPane(table);

		panelDiem = new JPanel();
        paneltable = new JPanel();
        paneltable.setLayout(new BorderLayout());
        paneltable.add(lbDanhSach, BorderLayout.NORTH);
        paneltable.add(scrollPane1);
        paneltable.add(panelDiem, BorderLayout.SOUTH);

        // -- Kết thúc thiết lập panelTable


        // -- Thiết lập panel: chứa panelThongTin, panelCB và panelTable

        panelXemDSHP = new JPanel(new BorderLayout());
        panelXemDSHP.add(panelTuaDe, BorderLayout.NORTH);
        panelXemDSHP.add(panelCB1);
        panelXemDSHP.add(paneltable, BorderLayout.SOUTH);

        // -- Kết thúc thiết lập panel

        panel1 = new JPanel();
        panel1.add(panelXemDSHP);
        setUptable();
    }


    public void setUptable() {
        // Bỏ scrollPane hiện tại ra khỏi frame
        paneltable.remove(scrollPane1);

        // Lấy năm học và học kỳ trong 2 comboBox
        String nh = cBNamHoc.getItemAt(cBNamHoc.getSelectedIndex());
        String hk = cBHocKy.getItemAt(cBHocKy.getSelectedIndex());
        lbDanhSach.setText("Danh sách điểm học phần có học tập ở học kỳ " + hk + ", năm học " + nh + ":");
        
        //Lấy danh sách điểm học phần trong học kì
        DiemHocPhan[] dsDiemHP = DiemHocPhan.getDSDiemHPTheoMSSV(sv.getMSSV()
				, nh, hk);
        
        // Lấy danh sách học phần mà sinh viên có học trong học kì
        HocPhan[] dsHP = sv.getDSHocPhan(nh, hk);

        int rows = dsHP.length;
        // Tạo table rỗng có số hàng là số lượng học phần trong mảng dsHP
        // Nếu số hàng cần hiển thị ít hơn LIMIT_OF_ROWS thì vẫn hiển thị LIMIT_OF_ROWS hàng
        if(rows < LIMIT_OF_ROWS) {
			rows = LIMIT_OF_ROWS;
		}
		//không cho phép sửa đổi nội dung table
		table = new JTable(rows, 5) {
			public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
	    
		((DefaultTableModel) table.getModel()).setColumnIdentifiers(new String[] {"STT", "Mã học phần", "Tên học phần", "Nhóm", "Số tín chỉ", "Điểm số", "Điểm chữ"});

		table.getTableHeader().setFont(f20);
		table.setFont(f20);
		table.setRowHeight(30);	
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(45); 
		table.getColumnModel().getColumn(1).setPreferredWidth(130); 
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);  
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(80); 
		table.getColumnModel().getColumn(6).setPreferredWidth(95); 

		table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		//thiết lập kích thước cho table
		table.setPreferredScrollableViewportSize(new Dimension(720, LIMIT_OF_ROWS * 30));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Canh giữa nội dung trong cột thứ nhất
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        // Cập nhật danh sách học phần trong dsHP vào table
        int row = 0;
        for (HocPhan hp : dsHP) {
            table.setValueAt(row + 1, row, 0);
            table.setValueAt(hp.getMaHP(), row, 1);
            table.setValueAt(hp.getTenHP(), row, 2);
            table.setValueAt(hp.getSoTC(), row, 4);
            row++;
        }

		//In ra điểm học phần và nhóm học phần
        row = 0;
		for (DiemHocPhan dHP : dsDiemHP) {
            table.setValueAt(dHP.getKyHieu(), row, 3);
			for (int i = 0; i < dsHP.length; ++i) {
				if (dHP.getMaHP().equals(dsHP[i].getMaHP())) {
					if(dHP.getDiem() == 11) table.setValueAt(null, row, 5);
					else table.setValueAt(dHP.getDiem(), row, 5);
					row++;
					break;
				}
			}
		}

		//In ra điểm chữ học phần
		row = 0;
		for (DiemHocPhan dHP : dsDiemHP) {
			for (int i = 0; i < dsHP.length; ++i) {
				if (dHP.getMaHP().equals(dsHP[i].getMaHP())) {
					float diem = dHP.getDiem();
					if(diem == 11f) table.setValueAt(null, row, 5);
					else table.setValueAt(diemHeChu(diem), row, 6);
					row++;
					break;
				}
			}
		}

		
		paneltable.remove(panelDiem);
		float diemTBHocKy = diemTB(dsDiemHP, dsHP);
		float diemTBTL = diemTBTL(nh, hk);
		int tongTCTL = tinChiTL(nh, hk);
		int TCTLHocKy = tongSoTCTL(dsDiemHP, dsHP);
		
		//Điểm trung bình học kỳ và tín chỉ tích lũy học kì
		lbDiemTBHK = new JLabel();
		lbDiemTBHK.setFont(f20);
		lbSoTCTLHK = new JLabel();
		lbSoTCTLHK.setFont(f20);
		//nếu học kỳ đó có môn có điểm thì mới hiển thị ra màn hình
		if (diemTBHocKy != 11) {
			lbDiemTBHK.setText("Điểm trung bình học kỳ: " + diemTBHocKy);
			lbSoTCTLHK.setText("Tổng tín chỉ tích lũy học kỳ: " + TCTLHocKy);
		}
		
		//Điểm trung bình tích lũy và tổng tín chỉ tích lũy
		lbDiemTBTL = new JLabel("Điểm trung bình tích lũy: " + diemTBTL);
		lbDiemTBTL.setFont(f20);
		lbSoTCTL = new JLabel("Tổng tín chỉ tích lũy: " + tongTCTL);
		lbSoTCTL.setFont(f20);
		
		//thiết lập panelDiem
		panelDiem = new JPanel(new BorderLayout());
		panelDiemTB = new JPanel(new BorderLayout());
		panelDiemTL = new JPanel(new BorderLayout());
		panelDiemTB.add(lbSoTCTLHK, BorderLayout.WEST);
		panelDiemTB.add(lbDiemTBHK, BorderLayout.EAST);
		panelDiemTL.add(lbSoTCTL, BorderLayout.WEST);
		panelDiemTL.add(lbDiemTBTL, BorderLayout.EAST);
		panelDiem.add(panelDiemTB, BorderLayout.NORTH);
		panelDiem.add(panelDiemTL, BorderLayout.SOUTH);
		
		
        scrollPane1 = new JScrollPane(table);
        
        // Thêm scrollPane hiện tại vào frame
        paneltable.add(scrollPane1);
		paneltable.add(panelDiem, BorderLayout.SOUTH);

        // Refresh lại frame để hiển thị dữ liệu mới cập nhật
        this.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnDX) {
            int answer =JOptionPane.showConfirmDialog(this, "Đăng xuất khỏi ứng dụng?", "Xác nhận",JOptionPane.INFORMATION_MESSAGE);
            if(answer == JOptionPane.YES_OPTION){
                this.dispose();
                (new GDLogin()).setVisible(true);
            }
        }
        else if(e.getSource() == btnXuat) {
            try {
                // JFileChooser tạo giao diện đồ họa cho phép xem các file và thư mục đang có trên máy tính
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showSaveDialog(this);      // Mở hộp thoại để lưu file mới
                // Sau khi nhập tên file và nhấn Save thì gọi phương thức getSelectedFile() để lấy đường dẫn lưu file + tên file đó
                File saveFile = jFileChooser.getSelectedFile();

                if(saveFile != null) {
                    saveFile = new File(saveFile.toString() + ".xlsx");
                    Workbook wb = new XSSFWorkbook();       // Tạo 1 workbook
                    Sheet sheet = wb.createSheet("Sheet 1");    // Tạo 1 sheet trong workbook
                    
                    //Tạo các dòng dữ liệu thông tin sinh viên và học kì
                    Row rowHeader = sheet.createRow(0); 
                    Cell cellHeader = rowHeader.createCell(0);
                    cellHeader.setCellValue("Họ tên: " + sv.getHoTenSV());
                    cellHeader = rowHeader.createCell(2);
                    cellHeader.setCellValue("MSSV: " + sv.getMSSV());
                    cellHeader = rowHeader.createCell(4);
                    cellHeader.setCellValue("Năm học: " + cBNamHoc.getItemAt(cBNamHoc.getSelectedIndex()));
                    cellHeader = rowHeader.createCell(6);
                    cellHeader.setCellValue("Học kỳ: " + cBHocKy.getItemAt(cBHocKy.getSelectedIndex()));
                    
                    rowHeader = sheet.createRow(2);     // Tạo 1 hàng chứa tiêu đề các cột trong sheet
                    for(int i = 0; i < table.getColumnCount(); i++) {
                        Cell cell = rowHeader.createCell(i);    // Tạo 1 cell thứ i trong row
                        cell.setCellValue(table.getColumnName(i));     // Lấy tên cột thứ i trong bảng
                    }
                    
                    int rTable = 0;
                    // Đọc các hàng dữ liệu còn lại trong bảng
                    for(rTable = 0; (rTable < table.getRowCount()) &&
                            (table.getValueAt(rTable, 0) != null); rTable++) {
                        Row row = sheet.createRow(rTable + 3);
                        for(int j = 0; j < table.getColumnCount(); j++) {
                            Cell cell = row.createCell(j);
                            Object value = table.getValueAt(rTable, j);
                            if(value != null) {
                                if(value instanceof String)
                                    cell.setCellValue((String) value);
                                else if(value instanceof Integer)
                                    cell.setCellValue((Integer) value);
                                else if(value instanceof Float)
                                    cell.setCellValue(Math.round(((Float) value) * 100) / 100.0);
                            }
                        }
                    }

                    //Ghi thông tin bên dưới bảng
                    Row rowFooter = sheet.createRow(rTable + 3);
                    Cell cellFooter = rowFooter.createCell(0);
                    cellFooter.setCellValue(lbDiemTBHK.getText());
                    rowFooter = sheet.createRow(rTable + 4);
                    cellFooter = rowFooter.createCell(0);
                    cellFooter.setCellValue(lbDiemTBTL.getText());
                    rowFooter = sheet.createRow(rTable + 5);
                    cellFooter = rowFooter.createCell(0);
                    cellFooter.setCellValue(lbSoTCTL.getText());
                    rowFooter = sheet.createRow(rTable + 6);
                    cellFooter = rowFooter.createCell(0);
                    cellFooter.setCellValue(lbSoTCTLHK.getText());
                    // FileOutputStream cho phép ghi 1 đối tượng File vào đường dẫn của đối tượng
                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);  // Ghi 1 file excel vào máy tính thông qua 1 đối tượng FileOutputStream
                    out.close();
                    wb.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private String diemHeChu(float diem) {
    	if (diem >= 9.0f && diem <= 10.0f) {
			return "A";
		}
		else if (diem >= 8.0f && diem <= 8.9f) {
			return "B+";
		}
		else if (diem >= 7.0f && diem <= 7.9f) {
			return "B";
		}
		else if (diem >= 6.5f && diem <= 6.9f) {
			return "C+";
		}
		else if (diem >= 5.5f && diem <= 6.4f) {
			return "C";
		}
		else if (diem >= 5.0f && diem <= 5.4f) {
			return "D+";
		}
		else if (diem >= 4.0f && diem <= 4.9f) {
			return "D";
		}
		else return "F";
	}
    
    private float diemTBTL(String nh, String hk) {
		//lấy danh sách điểm học phần và học phần từ cơ sở dữ liệu từ thời điểm đang chọn trở về trước
		DiemHocPhan dsTongDiemHP[] = DiemHocPhan.getDSDiemHPTichLuy(sv.getMSSV(), nh, hk);
		HocPhan dsTongHP[] = sv.getDSHocPhanTichLuy(nh, hk);
		float diemTL = diemTB(dsTongDiemHP, dsTongHP);
		return diemTL;
	}
    
    private int tinChiTL(String nh, String hk) {
		//lấy danh sách điểm học phần và học phần từ cơ sở dữ liệu từ thời điểm đang chọn trở về trước
		DiemHocPhan dsTongDiemHP[] = DiemHocPhan.getDSDiemHPTichLuy(sv.getMSSV(), nh, hk);
		HocPhan dsTongHP[] = sv.getDSHocPhanTichLuy(nh, hk);
		int TCTL = tongSoTCTL(dsTongDiemHP, dsTongHP);
		return TCTL;
	}
	private float diemTB(DiemHocPhan dsDiemHP[], HocPhan dHP[]) {
		float diemTB = 0;
		int tongSoTC = 0;
		boolean coDiem = false;
		//nếu toàn bộ học phần đều chưa có điểm thì trả về 11 (không hiển thị)
		for (DiemHocPhan d1 : dsDiemHP) {
			if (d1.getDiem() != 11) {
				coDiem = true;
				break;
			}
		}
		if (coDiem == false) {
			return 11;
		}
		
		//tính điểm học phần
		for (DiemHocPhan d2 : dsDiemHP) {
			for (HocPhan hp1 : dHP) {
				if(d2.getMaHP().equals(hp1.getMaHP())) {
					if (d2.getDiem() == 11) {
						continue;
					}
					else if (d2.getDiem() >= 9.0f && d2.getDiem() <= 10.0f) {
						diemTB += 4*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 8.0f && d2.getDiem() <= 8.9f) {
						diemTB += 3.5*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 7.0f && d2.getDiem() <= 7.9f) {
						diemTB += 3.0*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 6.5f && d2.getDiem() <= 6.9f) {
						diemTB += 2.5*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 5.5f && d2.getDiem() <= 6.4f) {
						diemTB += 2.0*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 5.0f && d2.getDiem() <= 5.4f) {
						diemTB += 1.5*hp1.getSoTC();
					}
					else if (d2.getDiem() >= 4.0f && d2.getDiem() <= 4.9f) {
						diemTB += 1.0*hp1.getSoTC();
					}
					tongSoTC += hp1.getSoTC();
					break;
				}
			}
		}
			
		diemTB /= tongSoTC;
		return (float)Math.round(diemTB*100)/100;
	}
	
	//tính tổng số tín chỉ
	private int tongSoTCTL(DiemHocPhan dsDiemHP[], HocPhan dHP[]) {
		int tongSoTC = 0;
		for (DiemHocPhan d1 : dsDiemHP) {
			if (d1.getDiem() != 11f && d1.getDiem() >= 4.0f) {
				for (HocPhan hp1 : dHP) {
					if(d1.getMaHP().equals(hp1.getMaHP())) {
						tongSoTC += hp1.getSoTC();
						break;
					}
				}
			}
		}
		return tongSoTC;
	}
}

