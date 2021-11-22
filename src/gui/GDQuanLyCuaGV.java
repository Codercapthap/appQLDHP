package gui;

import logic.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import sql.JDBCUtil;

public class GDQuanLyCuaGV extends JFrame implements ActionListener {
    //-- panelMenu
    private JButton btn0, btn1, btn2, btn3, btn4;
    private Box hBox;
    private JPanel panelMenu;
    //

    //-- panel0
    private JPanel panelThongTin, panelIcon1, panelIcon2, panelIcon3, panelIcon, panelTrangChu, panel0;
    private JLabel lbTuaDe0, lbMSCB, lbHoTen, lbIcon1, lbTenIcon1, lbIcon2, lbTenIcon2, lbIcon3, lbTenIcon3;
    //

    //-- panel1
    private JLabel lbTuaDe1, lbNamHoc1, lbHocKy1, lbDanhSach1, lbChuThich1;
    private JComboBox<String> cBNamHoc1, cBHocKy1;
    private JTable table1;
    private JScrollPane scrollPane1;
    private JPanel panelCB1, panelTable1, panelXemDSHP, panel1;
    private final static int LIMIT_OF_ROWS_1 = 9;
    //

    //-- panel2
    private JLabel lbHP, lbTuaDe2, lbDS2, lbChuThich2, lbLoi, lbNamHoc2, lbHocKy2, lbNhomHocPhan2;
    private JComboBox<String> cBNamHoc2, cBHocKy2, cBNhomHocPhan2;
    private JButton btnTimKiem, btnLuu, btnThoat, btnNhap, btnXuat;
    private JPanel panelTimKiem, panelDS2, panelTable2, panelBtn, panelNoiDung2, panel2, panelCB2, panelFlowTb2, panelNhapDiem;
    private JTable table2;
    private JScrollPane scrollPane2;
    private JTextField tfTimKiem;
    private DefaultTableModel model2;        // Lưu model của table
    private TableRowSorter<DefaultTableModel> sorter;
    private final static int LIMIT_OF_ROWS_2 = 9;
    //

    //-- panel3
    private JPanel panelBieuDo, panel3, panelThongKe, panelCB3;
    private JButton btnXuatBieuDo;
    private Box vBox;
    private JLabel lbTuaDe3, lbNamHoc3, lbHocKy3, lbNhomHocPhan3, lbBieuDo;
    private JComboBox<String> cBNamHoc3, cBHocKy3, cBNhomHocPhan3;
    private JFreeChart barChart;
    //
    
    // Các font đc sử dụng cho các thành phần trong ứng dụng
    private Font f35 = new Font("Arial", Font.PLAIN, 35);
    private Font f20 = new Font("Arial", Font.PLAIN, 20);
    private Font f15 = new Font("Arial", Font.PLAIN, 15);

    private Color whiteColor = new Color(240, 240, 240);    // Màu trắng đc sử dụng trong ứng dụng
    private Color blueColor = new Color(102, 102, 255);     // Màu xanh đc sử dụng trong ứng dụng
    
    // CardLayout sẽ sắp xếp các giao diện chồng lên nhau nên mỗi lần chỉ hiển thị 1 giao diện và có thể đem bất kỳ 
    // giao diện nào ở dưới lên trên để đc hiển thị
    private CardLayout cardLayout = new CardLayout();

    private JPanel panelChinh;  // panelChinh sử dụng CardLayout chứa toàn bộ các panel của các tab trong menu
    private int panelShowed;    // Biến panelShowed lưu chỉ số của panel hiện tại đang đc chọn (bắt đầu từ 0)

    // Tham chiếu gv sẽ lưu đối tượng giảng viên đang đăng nhập vào ứng dụng để truy xuất đc các thông tin của giàng viên
    private GiangVien gv;

    public GDQuanLyCuaGV(String idAcount) {         // Cung cấp id tài khoản của giàng viên đăng nhập ứng dụng này
        this.setTitle("Nhập điểm");     // Đặt tiêu đề cho ứng dụng
        this.setSize(850, 650);        // Đặt kích thước cho ứng dụng
        this.setLocationRelativeTo(null);       // Khi ứng dụng hiển thị trên màn hình sẽ tự động đc canh giữa
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Chương trình sẽ kết thúc khi nhấn X để tắt ứng dụng

        // Lấy đối tượng giảng viên ứng với id tài khoản đăng nhập vào ứng dụng
        gv = GiangVien.getGVTheoIdAccount(idAcount);

        setUpPanelMenu();   // Thiết lập thanh menu
        setUpPanel0();      // Thiết lập giao diện chính
        setUpPanel1();      // Thiết lập giao diện xem danh sách các lớp học phần có giảng dạy của giảng viên
        setUpPanel2();      // Thiết lập giao diện xem sinh viên của 1 lớp học phần có giảng dạy của giảng viên
        setUpPanel3();      // Thiết lập giao diện xem thống kê điểm học phần của 1 lớp học phần có giảng dạy của giảng viên

        // Thêm các giao diện trên vào panelChinh đc gắn với các tên khác nhau
        panelChinh = new JPanel();
        panelChinh.setLayout(cardLayout);
        panelChinh.add(panel0, "gd0");
        panelChinh.add(panel1, "gd1");
        panelChinh.add(panel2, "gd2");
        panelChinh.add(panel3, "gd3");

        panelShowed = 0;        // Mặc định ban đầu là panel0 đc hiển thị
        btn0.setBackground(whiteColor);
        btn0.setForeground(blueColor);

        // Thêm menu và panelChinh vào frame
        this.add(panelMenu, BorderLayout.NORTH);
        this.add(panelChinh);
    }

    // Phương thức cho phép hiện giao diện ứng dụng
    public void showGUI() {
        this.setVisible(true);
    }

    // Thiết lập các thành phần cho thanh menu, lưu vào trong panelMenu
    private void setUpPanelMenu() {
        // -- Thiết lập hBox: gồm btn0, btn1, btn2, btn3 và btn4
        btn0 = new JButton(" Trang chủ ");
        btn1 = new JButton(" Lớp học phần ");
        btn2 = new JButton(" Nhập điểm ");
        btn3 = new JButton(" Thống kê ");
        btn4 = new JButton(" Đăng xuất ");

        btn0.setBackground(blueColor);
        btn1.setBackground(blueColor);
        btn2.setBackground(blueColor);
        btn3.setBackground(blueColor);
        btn4.setBackground(blueColor);

        btn0.setForeground(whiteColor);
        btn1.setForeground(whiteColor);
        btn2.setForeground(whiteColor);
        btn3.setForeground(whiteColor);
        btn4.setForeground(whiteColor);

        btn0.setFocusPainted(false);
        btn1.setFocusPainted(false);
        btn2.setFocusPainted(false);
        btn3.setFocusPainted(false);
        btn4.setFocusPainted(false);

        btn0.setFont(new Font("Arial", Font.PLAIN, 18));
        btn1.setFont(new Font("Arial", Font.PLAIN, 18));
        btn2.setFont(new Font("Arial", Font.PLAIN, 18));
        btn3.setFont(new Font("Arial", Font.PLAIN, 18));
        btn4.setFont(new Font("Arial", Font.PLAIN, 18));

        btn0.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn1.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn2.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn3.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn4.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        btn0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // Nếu btn0 đc nhấn
                cardLayout.show(panelChinh, "gd0");      // Giao diện panel0 sẽ đc hiển thị

                // Tô màu cho button đang đc chọn (button có chỉ số panelShowed) -> button đang đc chọn sẽ hiển thị là ko đc chọn
                repaintButton(panelShowed);

                panelShowed = 0;        // Cập nhật button đang đc chọn là btn0

                // Tô màu cho btn0  -> btn0 đang đc chọn
                btn0.setBackground(whiteColor);
                btn0.setForeground(blueColor);
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelChinh, "gd1");

                repaintButton(panelShowed);
                panelShowed = 1;
                btn1.setBackground(whiteColor);
                btn1.setForeground(blueColor);
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelChinh, "gd2");

                repaintButton(panelShowed);
                panelShowed = 2;
                btn2.setBackground(whiteColor);
                btn2.setForeground(blueColor);
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelChinh, "gd3");

                repaintButton(panelShowed);
                panelShowed = 3;
                btn3.setBackground(whiteColor);
                btn3.setForeground(blueColor);
            }
        });
        btn4.addActionListener(this);

        hBox = Box.createHorizontalBox();
        hBox.add(btn0);
        hBox.add(btn1);
        hBox.add(btn2);
        hBox.add(btn3);
        hBox.add(Box.createRigidArea(new Dimension(5, 50)));    // Tăng chiều cao cho hBox
        hBox.add(Box.createHorizontalGlue());  // Ở giữa vị trí này là khoảng trống, btn4 ở phía sau sẽ tiến sát về cuối
        hBox.add(btn4);
        // -- Kết thúc thiết lập hBox


        // -- Thiết lập panelMenu: gồm hBox
        panelMenu = new JPanel(new BorderLayout());
        panelMenu.add(hBox, BorderLayout.NORTH);
        panelMenu.setBackground(blueColor);
        // -- Kết thúc thiết lập panelMenu
    }

    // Tô màu cho 1 button có chỉ số là "btn" (bắt đầu từ 0) -> hiểu rằng button này đang ko đc chọn
    private void repaintButton(int btn) {
        if (btn == 0) {
            btn0.setBackground(blueColor);
            btn0.setForeground(whiteColor);
        } else if (btn == 1) {
            btn1.setBackground(blueColor);
            btn1.setForeground(whiteColor);
        } else if (btn == 2) {
            btn2.setBackground(blueColor);
            btn2.setForeground(whiteColor);
        } else if (btn == 3) {
            btn3.setBackground(blueColor);
            btn3.setForeground(whiteColor);
        }
    }

    // Thiết lập các thành phần cho giao diện chính, lưu vào trong panel0
    public void setUpPanel0() {
        // -- Thiết lập panelThongTin: gồm lbTuaDe0, lbMSCB và lbHoTen
        lbTuaDe0 = new JLabel("Quản lý điểm học phần");
        lbTuaDe0.setFont(new Font("Arial", Font.PLAIN, 35));
        lbTuaDe0.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lbTuaDe0.setHorizontalAlignment(JLabel.CENTER);

        lbMSCB = new JLabel("Mã số cán bộ: " + gv.getMSCB());
        lbMSCB.setFont(f20);
        lbMSCB.setBorder(BorderFactory.createEmptyBorder(30, 80, 15, 0));

        lbHoTen = new JLabel("Họ và tên: " + gv.getHoTen());
        lbHoTen.setFont(f20);
        lbHoTen.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        panelThongTin = new JPanel(new BorderLayout());
        panelThongTin.add(lbMSCB);
        panelThongTin.add(lbHoTen, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelThongTin


        // -- Thiết lập panelIcon1: gồm lbIcon1 và lbTenIcon1
        lbIcon1 = new JLabel(new ImageIcon("img//books.png"));
        lbIcon1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panelChinh, "gd1");

                repaintButton(panelShowed);
                panelShowed = 1;
                btn1.setBackground(whiteColor);
                btn1.setForeground(blueColor);
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        lbTenIcon1 = new JLabel("Xem danh sách lớp học phần");
        lbTenIcon1.setFont(f20);

        panelIcon1 = new JPanel(new BorderLayout());
        panelIcon1.add(lbIcon1);
        panelIcon1.add(lbTenIcon1, BorderLayout.SOUTH);
        panelIcon1.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 20));
        // -- Kết thúc thiết lập panelIcon1


        // -- Thiết lập panelIcon2: gồm lbIcon2 và lbTenIcon2
        lbIcon2 = new JLabel(new ImageIcon("img//scores.png"));
        lbIcon2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panelChinh, "gd2");

                repaintButton(panelShowed);
                panelShowed = 2;
                btn2.setBackground(whiteColor);
                btn2.setForeground(blueColor);
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        lbTenIcon2 = new JLabel("Nhập điểm học phần");
        lbTenIcon2.setFont(f20);

        panelIcon2 = new JPanel(new BorderLayout());
        panelIcon2.add(lbIcon2);
        panelIcon2.add(lbTenIcon2, BorderLayout.SOUTH);
        panelIcon2.setBorder(BorderFactory.createEmptyBorder(80, 20, 0, 20));
        // -- Kết thúc thiết lập panelIcon2


        // -- Thiết lập panelIcon3: gồm lbIcon3 và lbTenIcon3
        lbIcon3 = new JLabel(new ImageIcon("img//statistics.png"));
        lbIcon3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panelChinh, "gd3");

                repaintButton(panelShowed);
                panelShowed = 3;
                btn3.setBackground(whiteColor);
                btn3.setForeground(blueColor);
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        lbTenIcon3 = new JLabel("Xem thống kê điểm học phần");
        lbTenIcon3.setFont(f20);

        panelIcon3 = new JPanel(new BorderLayout());
        panelIcon3.add(lbIcon3);
        panelIcon3.add(lbTenIcon3, BorderLayout.SOUTH);
        panelIcon3.setBorder(BorderFactory.createEmptyBorder(80, 20, 0, 0));
        // -- Kết thúc thiết lập panelIcon3


        // -- Thiết lập panelIcon: gồm panelIcon1, panelIcon2 và panelIcon3
        panelIcon = new JPanel();
        panelIcon.add(panelIcon1);
        panelIcon.add(panelIcon2);
        panelIcon.add(panelIcon3);
        // -- Kết thúc thiết lập panelIcon


        // -- Thiết lập panelTrangChu: gồm lbTuaDe0, panelThongTin và panelIcon
        panelTrangChu = new JPanel(new BorderLayout());
        panelTrangChu.add(lbTuaDe0, BorderLayout.NORTH);
        panelTrangChu.add(panelThongTin);
        panelTrangChu.add(panelIcon, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelTrangChu


        // -- Thiết lập panel0: gồm panelTrangChu
        panel0 = new JPanel();
        panel0.add(panelTrangChu);
        // -- Kết thúc thiết lập panel0
    }

    // Thiết lập các thành phần cho giao diện "Xem lớp học phần", lưu vào trong panel1
    public void setUpPanel1() {
        // -- Thiết lập panelCB1: gồm lbNamHoc1, cBNamHoc1, lbHocKy1 và cBHocKy1
        lbNamHoc1 = new JLabel("Năm học:");
        lbNamHoc1.setFont(f20);
        lbNamHoc1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        lbHocKy1 = new JLabel("Học kỳ:");
        lbHocKy1.setFont(f20);
        lbHocKy1.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 5));

        // Lấy năm học từ csdl bắt đầu từ năm mà giảng viên bắt đầu giảng dạy
        cBNamHoc1 = new JComboBox<>(NamHoc.getDSNamHoc(gv.getNamGiangDay()));
        cBNamHoc1.setFont(f15);
        cBNamHoc1.setFocusable(false);
        cBNamHoc1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mỗi khi cBNamHoc1 được chọn 1 lựa chọn thì cập nhật lại table1 ứng với năm học đã chọn và học kỳ đang chọn
                setUpTable1();
            }
        });

        cBHocKy1 = new JComboBox<>(HocKy.getDSHocKy());
        cBHocKy1.setFont(f15);
        cBHocKy1.setFocusable(false);
        cBHocKy1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mỗi khi cBHocKy1 được chọn 1 lựa chọn thì cập nhật lại table1 ứng với học kỳ đã chọn và năm học đang chọn
                setUpTable1();
            }
        });

        panelCB1 = new JPanel();
        panelCB1.add(lbNamHoc1);
        panelCB1.add(cBNamHoc1);
        panelCB1.add(lbHocKy1);
        panelCB1.add(cBHocKy1);
        panelCB1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // -- Kết thúc thiết lập panelCB1


        // -- Thiết lập panelTable: gồm lbDanhSach1, scrollPane1 (chứa table1) và lbChuThich1
        lbDanhSach1 = new JLabel("Danh sách lớp học phần có giảng dạy");
        lbDanhSach1.setFont(f20);
        lbDanhSach1.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Ban đầu tạo table rỗng và bỏ vào bên trong scrollPane
        table1 = new JTable();
        scrollPane1 = new JScrollPane(table1);

        lbChuThich1 = new JLabel("Nhấn vào 1 hàng để chọn");
        lbChuThich1.setFont(f15);
        lbChuThich1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelTable1 = new JPanel(new BorderLayout());
        panelTable1.add(lbDanhSach1, BorderLayout.NORTH);
        panelTable1.add(scrollPane1);
        panelTable1.add(lbChuThich1, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelTable1


        // -- Thiết lập panelXemDSHP: gồm lbTuaDe1, panelCB1 và panelTable1
        lbTuaDe1 = new JLabel();
        lbTuaDe1.setText("Xem danh sách lớp học phần");
        lbTuaDe1.setFont(new Font("Arial", Font.PLAIN, 35));
        lbTuaDe1.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lbTuaDe1.setHorizontalAlignment(JLabel.CENTER);

        panelXemDSHP = new JPanel(new BorderLayout());
        panelXemDSHP.add(lbTuaDe1, BorderLayout.NORTH);
        panelXemDSHP.add(panelCB1);
        panelXemDSHP.add(panelTable1, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelXemDSHP


        // -- Thiết lập panel1: gồm panelXemDSHP
        panel1 = new JPanel();
        panel1.add(panelXemDSHP);
        // -- Kết thúc thiết lập panel1

        setUpTable1();  // Lấy nội dung cần thiết để hiện lên trong table1
    }

    // Lấy danh sách các lớp học phần mà giảng viên có giảng dạy tại năm học và học kỳ đang chọn và hiện vào bảng table1
    public void setUpTable1() {
        // Bỏ scrollPane hiện tại ra khỏi frame
        panelTable1.remove(scrollPane1);

        // Lấy năm học và học kỳ trong 2 comboBox
        String nh = cBNamHoc1.getItemAt(cBNamHoc1.getSelectedIndex());
        String hk = cBHocKy1.getItemAt(cBHocKy1.getSelectedIndex());
        lbDanhSach1.setText("Danh sách lớp học phần có giảng dạy ở học kỳ " + hk + ", năm học " + nh + ":");

        // Lấy danh sách nhóm học phần mà giảng viên này (được tham chiếu gv trỏ tới) có dạy trong nh và hk ở trên
        NhomHocPhan[] dsNHP = gv.getDSNhomHocPhan(nh, hk);

        int rows = dsNHP.length;
        // Tạo table rỗng có số hàng là số lượng học phần trong mảng dsHP
        // Nếu số hàng cần hiển thị ít hơn LIMIT_OF_ROWS thì vẫn hiển thị LIMIT_OF_ROWS hàng
        if (rows < LIMIT_OF_ROWS_1) {
            rows = LIMIT_OF_ROWS_1;
        }

        table1 = new JTable(rows, 4) {
            // Không cho sửa nội dung của bất kỳ ô nào trong table
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Đặt tên cho các cột trong table
        ((DefaultTableModel) table1.getModel()).setColumnIdentifiers(
                new String[]{"STT", "Mã Học Phần", "Ký hiệu", "Tên Học Phần", "Số Tín Chỉ"});

        table1.getTableHeader().setFont(f20);
        table1.setFont(f20);
        table1.setRowHeight(30);        // 1 hàng trong table sẽ có chiều cao là 30

        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);       // Tắt tính năng tự động đặt chiều rộng cho cột
        table1.getColumnModel().getColumn(0).setPreferredWidth(50); // Đặt chiều rộng cho cột thứ nhất trong table1
        table1.getColumnModel().getColumn(1).setPreferredWidth(150); // Đặt chiều rộng cho cột thứ hai trong table1
        table1.getColumnModel().getColumn(2).setPreferredWidth(100);
        table1.getColumnModel().getColumn(3).setPreferredWidth(310);
        table1.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Tô đường viền đen cho các hàng trong table1
        table1.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        table1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Đặt kích thước cho scrollPane1, mỗi hàng cao 30 -> chiều cao (LIMIT_OF_ROWS_1 * 30) sẽ gồm LIMIT_OF_ROWS hàng
        table1.setPreferredScrollableViewportSize(new Dimension(730, LIMIT_OF_ROWS_1 * 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Canh giữa nội dung trong cột thứ nhất
        table1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Cập nhật danh sách học phần trong dsHP vào table
        int row = 0;    // Biến row dùng để chạy vòng lặp
        for (NhomHocPhan nhp : dsNHP) {     // Lấy từng nhóm học phần trong mảng dsNHP
            table1.setValueAt(row + 1, row, 0);     // Gán số thứ tự là (row + 1) vào ô (row; 0) trong table1
            table1.setValueAt(nhp.getMaHP(), row, 1);   // Gán mã học phần là nhp.getMaHP() vào ô (row; 1) trong tabl1
            table1.setValueAt(nhp.getKyHieu(), row, 2);
            table1.setValueAt(HocPhan.getHPTheoMa(nhp.getMaHP()).getTenHP(), row, 3);
            table1.setValueAt(HocPhan.getHPTheoMa(nhp.getMaHP()).getSoTC(), row, 4);
            row++;
        }

        // Khí 1 hàng trong table1 đc nhấn -> 1 lớp học phần đc chọn -> chuyển sang giao diện "Nhập điểm" của lớp học phần đó
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.rowAtPoint(e.getPoint());	// Lấy chỉ số hàng đc nhấn
                if(table1.getValueAt(row, 1) != null) {		// Nếu hàng đc nhấn ko là hàng rỗng
                    cardLayout.show(panelChinh, "gd2");      // Chuyển sang giao diện "Nhập điểm" (ký hiệu là "gd2")

                    repaintButton(panelShowed);     // Tô màu lại cho button đang được nhấn hiện tại trờ thành ko đc nhấn
                    panelShowed = 2;        // Cập nhật button đc nhấn là button 2
                    btn2.setBackground(whiteColor);
                    btn2.setForeground(blueColor);

                    // Lấy năm học - học kỳ đang chọn ở giao diện "Xem lớp học phần" để gán vào 2 comboBox ở giao diện "Nhập điểm"
                    cBNamHoc2.setSelectedIndex(cBNamHoc1.getSelectedIndex());
                    cBHocKy2.setSelectedIndex(cBHocKy1.getSelectedIndex());

                    // Cập nhập danh sách các lớp học phần đế gán vào cBNhomHocPhan2 tương ứng với năm học, học kỳ và giảng viên đang xét
                    changeCBHP2();

                    // Để cBNhomHocPhan2 hiển thị mặc định là lớp học phần có chỉ số row
                    cBNhomHocPhan2.setSelectedIndex(row);

                    // Cập nhật lại nội dung cho table2 tương ứng với năm học, học kỳ và lớp học phần đang chọn
                    setUpTable2();

                    // Làm rỗng thanh tìm kiếm
                    tfTimKiem.setText("");
                }

            }
        });


        scrollPane1 = new JScrollPane(table1);  // Thêm table1 mới cập nhật vào scrollPane1
        panelTable1.add(scrollPane1);   // Thêm scrollPane1 hiện tại vào frame

        // Refresh lại frame để hiển thị dữ liệu mới cập nhật
        this.validate();
    }

    // Thiết lập các thành phần cho giao diện "Nhập điểm", lưu vào trong panel2
    public void setUpPanel2() {
        // -- Thiết lập panelCB2: gồm lbNamHoc2, cBNamHoc2, lbHocKy2, cBHocKy2, lbNhomHocPhan2 và cBNhomHocPhan2
        lbNamHoc2 = new JLabel("Năm học:");
        lbNamHoc2.setFont(f20);
        lbNamHoc2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        lbHocKy2 = new JLabel("Học kỳ:");
        lbHocKy2.setFont(f20);
        lbHocKy2.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 5));

        // Lấy năm học từ csdl bắt đầu từ năm mà giảng viên bắt đầu giảng dạy
        cBNamHoc2 = new JComboBox<>(NamHoc.getDSNamHoc(gv.getNamGiangDay()));
        cBNamHoc2.setFont(f15);
        cBNamHoc2.setFocusable(false);
        cBNamHoc2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // Mỗi khi cBNamHoc2 được chọn 1 lựa chọn
                // Cập nhập danh sách các lớp học phần đế gán vào cBNhomHocPhan2 tương ứng với năm học đã chọn và học kỳ đang chọn
                changeCBHP2();

                // Cập nhật lại table2 tương ứng với năm học, học kỳ và lớp học phần đang chọn
                setUpTable2();
            }
        });

        cBHocKy2 = new JComboBox<>(HocKy.getDSHocKy());
        cBHocKy2.setFont(f15);
        cBHocKy2.setFocusable(false);
        cBHocKy2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCBHP2();
                setUpTable2();
            }
        });

        lbNhomHocPhan2 = new JLabel("Lớp học phần:");
        lbNhomHocPhan2.setFont(f20);
        lbNhomHocPhan2.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 5));

        cBNhomHocPhan2 = new JComboBox<>();     // Ban đầu cho cBNhomHocPhan2 là rỗng

        panelCB2 = new JPanel();
        panelCB2.add(lbNamHoc2);
        panelCB2.add(cBNamHoc2);
        panelCB2.add(lbHocKy2);
        panelCB2.add(cBHocKy2);
        panelCB2.add(lbNhomHocPhan2);
        panelCB2.add(cBNhomHocPhan2);
        panelCB2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // -- Kết thúc thiết lập panelCB2


        // -- Thiết lập panelTimKiem: gồm tfTimKiem và btnTimKiem
        tfTimKiem = new JTextField(10);
        tfTimKiem.setFont(f15);

        btnTimKiem = new JButton("Tim");
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfTimKiem.getText().length() > 0) {     // Nếu nội dung trong thanh tìm kiếm có ít nhất 1 ký tự
                    // RowFilter.regexFilter(text) để tao ra 1 RowFilter dùng đề lọc ra các hàng trong table có chứa chuỗi text
                    // setRowFilter(1 RowFilter) để tiến hành gán 1 RowFilter dùng đề lọc các hàng trong table
                    sorter.setRowFilter(RowFilter.regexFilter(tfTimKiem.getText()));
                } else {
                    sorter.setRowFilter(null);    // Giữ nguyên các hàng trong table2, ko lọc hàng nào
                }
            }
        });

        panelTimKiem = new JPanel();
        panelTimKiem.add(tfTimKiem);
        panelTimKiem.add(btnTimKiem);
        // -- Kết thúc thiết lập panelTimKiem


        // -- Thiết lập panelDS2: gồm lbDS2 và panelTimKiem
        lbDS2 = new JLabel("Danh sách sinh viên:");
        lbDS2.setFont(f20);

        panelDS2 = new JPanel();
        panelDS2.setLayout(new BorderLayout());
        panelDS2.add(lbDS2, BorderLayout.WEST);
        panelDS2.add(panelTimKiem, BorderLayout.EAST);
        panelDS2.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        // -- Kết thúc thiết lập panelDS2


        // -- Thiết lập panelTable2: gồm panelDS2, scrollPane2 (chứa table2) và lbLoi
        table2 = new JTable();

        scrollPane2 = new JScrollPane(table2);

        lbLoi = new JLabel(" ");    // Ban đầu chưa có điểm nào bị lỗi vì các điểm ban đầu đc lấy từ csdl
        lbLoi.setFont(f15);
        lbLoi.setForeground(Color.RED);

        panelTable2 = new JPanel();
        panelTable2.setLayout(new BorderLayout());
        panelTable2.add(panelDS2, BorderLayout.NORTH);
        panelTable2.add(scrollPane2);
        panelTable2.add(lbLoi, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelTable2


        // -- Thiết lập panelFlowTb2: gồm panelTable2
        panelFlowTb2 = new JPanel();
        panelFlowTb2.add(panelTable2);
        // -- Kết thúc thiết lập panelTable2


        // -- Thiết lập panelBtn: gồm btnLuu, btnNhao và btnXuat
        btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(this);
        btnLuu.setFocusPainted(false);

        btnNhap = new JButton("Nhập excel");
        btnNhap.addActionListener(this);
        btnNhap.setFocusPainted(false);

        btnXuat = new JButton("Xuất excel");
        btnXuat.addActionListener(this);
        btnXuat.setFocusPainted(false);

        panelBtn = new JPanel();
        panelBtn.add(btnLuu);
        panelBtn.add(btnNhap);
        panelBtn.add(btnXuat);
        // -- Kết thúc thiết lập panelBtn


        // -- Thiết lập panelNoiDung2: gồm panelCB2, panelFlowTb2 và panelBtn
        panelNoiDung2 = new JPanel(new BorderLayout());
        panelNoiDung2.add(panelCB2, BorderLayout.NORTH);
        panelNoiDung2.add(panelFlowTb2);
        panelNoiDung2.add(panelBtn, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelNoiDung2


        // -- Thiết lập panelNhapDiem: gồm lbTuaDe2 và panelNoiDung2
        lbTuaDe2 = new JLabel("Nhập điểm học phần cho sinh viên");
        lbTuaDe2.setFont(f35);
        lbTuaDe2.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lbTuaDe2.setHorizontalAlignment(JLabel.CENTER);

        panelNhapDiem = new JPanel(new BorderLayout());
        panelNhapDiem.add(lbTuaDe2, BorderLayout.NORTH);
        panelNhapDiem.add(panelNoiDung2);
        // -- Kết thúc thiết lập panelNhapDiem


        // -- Thiết lập panel2: gồm panelNhapDiem
        panel2 = new JPanel();
        panel2.add(panelNhapDiem);
        // -- Kết thúc thiết lập panel2

        changeCBHP2();  // Cập nhập lại cBNhomHocPhan2 tương ứng với năm học và học kỳ đang chọn vì ban đầu chỉ khởi tạo là rỗng
        setUpTable2();  // Cập nhật lại table2 tương ứng với năm học, học kỳ và lớp học phần đang chọn
    }

    // Cập nhập danh sách các lớp học phần tương ứng với năm học đang chọn trong cBNamHoc2 và học kỳ đang chọn trong cBHocKy2
    private void changeCBHP2() {
        panelCB2.remove(cBNhomHocPhan2);    // Bỏ cBNhomHocPhan2 ra khỏi frame

        String nHBanDau = cBNamHoc2.getItemAt(cBNamHoc2.getSelectedIndex());    // Lấy năm học trong cBNamHoc2
        String hKBanDau = cBHocKy2.getItemAt(cBHocKy2.getSelectedIndex());      // Lấy học kỳ trong cBHọcKy2
        NhomHocPhan[] dsNHPBanDau = gv.getDSNhomHocPhan(nHBanDau, hKBanDau);    // Lấy danh sách lớp học phần tương ứng
        String[] dsTenNHPBanDau = new String[dsNHPBanDau.length];       // Định dạng lại các thông tin của 1 nhóm học phần
        int i = 0;
        for (NhomHocPhan nhp : dsNHPBanDau) {
            // 1 lưa chọn đc hiển thị trong cBNhomHocPhan2 = "Mã HP - Ký hiệu HP - Tên HP"
            dsTenNHPBanDau[i] = nhp.getMaHP() + " - " + nhp.getKyHieu() + " - " + HocPhan.getHPTheoMa(nhp.getMaHP()).getTenHP();
            i++;
        }

        // Gán các chuỗi đã đc định dạng vào cBNhomHocPhan2
        cBNhomHocPhan2 = new JComboBox<>(dsTenNHPBanDau);
        cBNhomHocPhan2.setFont(f15);
        cBNhomHocPhan2.setFocusable(false);
        cBNhomHocPhan2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Khi 1 nhóm học phần đc chọn từ cBNhomHocPhan2 thì cập nhật lại table2 tương ứng
                setUpTable2();
            }
        });

        panelCB2.add(cBNhomHocPhan2);       // Thêm cBNhomHocPhan2 vào frame lại
    }

    // Cập nhật lại danh sách các sinh viên của lớp học phần đang chọn trong cBNhomHocPhan2 và hiển thị trong bảng table2
    public void setUpTable2() {
        lbLoi.setText(" ");
        panelTable2.remove(scrollPane2);

        String nh = cBNamHoc2.getItemAt(cBNamHoc2.getSelectedIndex());
        String hk = cBHocKy2.getItemAt(cBHocKy2.getSelectedIndex());
        String nhp = cBNhomHocPhan2.getItemAt(cBNhomHocPhan2.getSelectedIndex());

        // Lấy danh sách các sinh viên và điểm học phần của các sinh viên tương ứng lớp học phần đang chọn trong cBNhomHocPhan2
        DiemHocPhan[] dsDiemHP = DiemHocPhan.getDSDiemHPTheoNhomHP(nhp.substring(0, nhp.indexOf(" ")), nh, hk,
                nhp.substring(nhp.indexOf("-") + 2, nhp.lastIndexOf("-") - 1));

        int rows = dsDiemHP.length;

        table2 = new JTable(rows > LIMIT_OF_ROWS_2 ? rows : LIMIT_OF_ROWS_2, 5) {
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho chỉnh sửa những ô ở cột 3 (cột điểm) và những hàng ko rỗng (hàng có chỉ số < dsDiemHP.length_
                if (column == 3 && row < rows) return true;
                else return false;
            }
        };

        model2 = (DefaultTableModel) table2.getModel();        // Lấy model của table

        model2.setColumnIdentifiers(new String[]{"STT", "MSSV", "Họ Tên Sinh Viên", "Điểm", ""});
        table2.getTableHeader().setFont(f20);
        table2.setFont(f20);
        table2.setRowHeight(30);

        table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table2.getColumnModel().getColumn(0).setPreferredWidth(50);
        table2.getColumnModel().getColumn(1).setPreferredWidth(150);
        table2.getColumnModel().getColumn(2).setPreferredWidth(250);
        table2.getColumnModel().getColumn(3).setPreferredWidth(100);

        hideColumnX();      // Ẩn cột kiểm tra các điểm ko hợp lệ

        table2.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        table2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        table2.setPreferredScrollableViewportSize(new Dimension(550, LIMIT_OF_ROWS_2 * 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table2.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table2.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table2.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table2.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Nhập dữ liệu cho table2
        int row = 0;
        for (DiemHocPhan dHP : dsDiemHP) {
            String mssv = dHP.getMSSV();
            table2.setValueAt(row + 1, row, 0);
            table2.setValueAt(mssv, row, 1);
            table2.setValueAt(SinhVien.getHoTenTheoMSSV(mssv), row, 2);
            // Nếu điểm == 11 thì điểm này chưa được giảng viên nhập điểm -> gán null vào ô điểm
            if (dHP.getDiem() == 11) table2.setValueAt(null, row, 3);
            else table2.setValueAt(dHP.getDiem(), row, 3);
            row++;
        }

        sorter = new TableRowSorter<DefaultTableModel>(model2);
        table2.setRowSorter(sorter);            // Gán đồi tượng TableRowSorter cho table2

        // Thiết lập cho scrollPane2 là chỉ hiển thị thanh trượt dọc khi cần và ko bao giờ hiển thị thanh trượt ngang
        scrollPane2 = new JScrollPane(table2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panelTable2.add(scrollPane2);
        this.validate();
    }

    // Ẩn cột cuối cùng của table2 (cột hiển thị các dấu X thể hiện các điểm ko hợp lệ trong table2)
    public void hideColumnX() {
        table2.getColumnModel().getColumn(4).setMinWidth(0);
        table2.getColumnModel().getColumn(4).setMaxWidth(0);
        table2.getColumnModel().getColumn(4).setPreferredWidth(0);
        table2.setPreferredScrollableViewportSize(new Dimension(550, LIMIT_OF_ROWS_2 * 30));
    }

    // Hiện cột cuối cùng của table2 (cột hiển thị các dấu X thể hiện các điểm ko hợp lệ trong table2)
    public void showColumnX() {
        table2.getColumnModel().getColumn(4).setMinWidth(30);
        table2.getColumnModel().getColumn(4).setMaxWidth(30);
        table2.getColumnModel().getColumn(4).setPreferredWidth(30);
        table2.setPreferredScrollableViewportSize(new Dimension(580, LIMIT_OF_ROWS_2 * 30));
    }

    // Thiết lập các thành phần cho giao diện "Thống kê", lưu vào trong panel3
    public void setUpPanel3() {
        // -- Thiết lập panelCB3: gồm lbNamHoc3, cBNamHoc3, lbHocKy3, cBHocKy3, lbNhomHocPhan3 và cBNhomHocPhan3
        lbNamHoc3 = new JLabel("Năm học:");
        lbNamHoc3.setFont(f20);
        lbNamHoc3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        lbHocKy3 = new JLabel("Học kỳ:");
        lbHocKy3.setFont(f20);
        lbHocKy3.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 5));

        // Lấy năm học từ csdl bắt đầu từ năm mà giảng viên bắt đầu giảng dạy
        cBNamHoc3 = new JComboBox<>(NamHoc.getDSNamHoc(gv.getNamGiangDay()));
        cBNamHoc3.setFont(f15);
        cBNamHoc3.setFocusable(false);
        cBNamHoc3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCBNHP3();      // Cập nhật lại cBNhomHocPhan3 tương ứng với năm học và học kỳ đang chọn
                setUpChart();       // Cập nhật lại biểu đồ tương ứng với lớp học phần đang chọn trong cBNhomHocPhan3
            }
        });

        cBHocKy3 = new JComboBox<>(HocKy.getDSHocKy());
        cBHocKy3.setFont(f15);
        cBHocKy3.setFocusable(false);
        cBHocKy3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCBNHP3();
                setUpChart();
            }
        });

        lbNhomHocPhan3 = new JLabel("Lớp học phần:");
        lbNhomHocPhan3.setFont(f20);
        lbNhomHocPhan3.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 5));

        cBNhomHocPhan3 = new JComboBox<>();

        panelCB3 = new JPanel();
        panelCB3.add(lbNamHoc3);
        panelCB3.add(cBNamHoc3);
        panelCB3.add(lbHocKy3);
        panelCB3.add(cBHocKy3);
        panelCB3.add(lbNhomHocPhan3);
        panelCB3.add(cBNhomHocPhan3);
        panelCB3.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // -- Kết thúc thiết lập panelCB3


        // -- Thiết lập vBox: gồm 'btnXuatBieuDo
        btnXuatBieuDo = new JButton("Xuất biểu đồ (png)");
        btnXuatBieuDo.setFont(f20);
        btnXuatBieuDo.addActionListener(this);

        vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalGlue());
        vBox.add(btnXuatBieuDo);
        // -- Kết thúc thiết lập vBox


        // -- Thiết lập panelBieuDo: gồm lbBieuDo và vBox
        lbBieuDo = new JLabel();
        lbBieuDo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        panelBieuDo = new JPanel(new BorderLayout());
        panelBieuDo.add(lbBieuDo);
        panelBieuDo.add(vBox, BorderLayout.EAST);
        panelBieuDo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        // -- Kết thúc thiết lập panelBieuDo


        // -- Thiết lập panelThongKe: gồm lbTuaDe3, panelCB3 và panelBieuDo
        lbTuaDe3 = new JLabel("Thống kê điểm học phần của sinh viên");
        lbTuaDe3.setFont(f35);
        lbTuaDe3.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lbTuaDe3.setHorizontalAlignment(JLabel.CENTER);

        panelThongKe = new JPanel(new BorderLayout());
        panelThongKe.add(lbTuaDe3, BorderLayout.NORTH);
        panelThongKe.add(panelCB3);
        panelThongKe.add(panelBieuDo, BorderLayout.SOUTH);
        // -- Kết thúc thiết lập panelThongKe


        // -- Thiết lập panel3: gồm panelThongKe
        panel3 = new JPanel();
        panel3.add(panelThongKe);
        // -- Kết thúc thiết lập panel3
        
        changeCBNHP3();      // Cập nhật cBNhomHocPhan3
        setUpChart();       // Cập nhật biểu đồ tương ứng với nhóm học phần đang chọn trong cBNhomHocPhan3
    }

    private void changeCBNHP3() {
        panelCB3.remove(cBNhomHocPhan3);

        String nHBanDau = cBNamHoc3.getItemAt(cBNamHoc3.getSelectedIndex());
        String hKBanDau = cBHocKy3.getItemAt(cBHocKy3.getSelectedIndex());
        NhomHocPhan[] dsNHPBanDau = gv.getDSNhomHocPhan(nHBanDau, hKBanDau);
        String[] dsTenNHPBanDau = new String[dsNHPBanDau.length];
        int i = 0;
        for (NhomHocPhan nhp : dsNHPBanDau) {
            dsTenNHPBanDau[i] = nhp.getMaHP() + " - " + nhp.getKyHieu() + " - " + HocPhan.getHPTheoMa(nhp.getMaHP()).getTenHP();
            i++;
        }

        cBNhomHocPhan3 = new JComboBox<>(dsTenNHPBanDau);
        cBNhomHocPhan3.setFont(f15);
        cBNhomHocPhan3.setFocusable(false);
        cBNhomHocPhan3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpChart();
            }
        });

        panelCB3.add(cBNhomHocPhan3);
        this.validate();
    }

    // Cập nhật lại biểu đồ tương ứng với nhóm học phần đang chọn trong cBNhomHocPhan3
    public void setUpChart() {
        // Lấy thông tin từ các comboBox để truy xuất danh sách điểm
        String nh = cBNamHoc3.getItemAt(cBNamHoc3.getSelectedIndex());
        String hk = cBHocKy3.getItemAt(cBHocKy3.getSelectedIndex());
        String nhp = cBNhomHocPhan3.getItemAt(cBNhomHocPhan3.getSelectedIndex());

        barChart = null;    // Tham chiếu barChart sẽ tham chiếu tới đối tượng biểu đồ

        try {
            // Viết câu lệnh sql để truy vấn về 1 bảng gồm 2 cột là điểm và số lượng sinh viên đạt đc, điểm sẽ đc làm tròn xuống
            // Vd về kết quả thu đc của câu truy vấn sql là:
            //  | diem | so_luong_sinh_vien |
            //  |   0  |         20         |
            //  |   1  |         5          |
            //  |  ... |        ...         |
            //  |  10  |         11         |
            String sql = "SELECT d.diem, COUNT(dhp.mssv) so_luong_sinh_vien " +
                    "FROM (SELECT * FROM diem_hoc_phan " +
                    "WHERE nam_hoc = '" + nh + "' AND hoc_ky = '" + hk + "' AND ma_hoc_phan = '" + nhp.substring(0, nhp.indexOf(" ")) + "' " +
                    "AND ky_hieu = '" + nhp.substring(nhp.indexOf("-") + 2, nhp.lastIndexOf("-") - 1) + "') dhp RIGHT JOIN " +
                    "(SELECT 0 diem UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION " +
                    "SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) d " +
                    "ON d.diem = floor(dhp.diem) " +
                    "GROUP BY d.diem ORDER BY d.diem";

            // Lớp JDBCCategoryDataset cho phép kết nối với 1 hệ quản trị csdl + thực hiện câu lệnh sql -> trả về kết quả
            // Tham chiếu barDataSet sẽ tham chiếu tới 1 bảng gồm 2 cột như trên -> dùng tham chiếu barDataSet để vẽ biểu ồ
            CategoryDataset barDataSet = new JDBCCategoryDataset(JDBCUtil.getConnection(), sql);

            // Sử dụng phương thức createBarChart() của lớp ChartFactory để thu đc 1 biểu đồ tương ứng với dữ liệu cung cấp vào
            barChart = ChartFactory.createBarChart("Điểm học phần", "Điểm", "Số lượng sinh viên", barDataSet,
                    PlotOrientation.VERTICAL, false, true, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(barChart != null) {      // Sau khi thu đc 1 biểu đồ
            //barChart.getCategoryPlot().getRenderer().setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("###")));
            //barChart.getCategoryPlot().getRenderer().setSeriesItemLabelsVisible(0, true);

            // Sử dụng phương thức createBufferedImage() để xuất biểu đồ ra ảnh
            // -> Gán ảnh làm nội dung của lbBieuDo
            lbBieuDo.setIcon(new ImageIcon(barChart.createBufferedImage(600, 380)));
        }

        this.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btn4) {     // Nếu button "Đăng xuất" đc nhấn
            // Hiện hộp thoại xác nhận
            int answer = JOptionPane.showConfirmDialog(this, "Đăng xuất khỏi ứng dụng?", "Xác nhận",JOptionPane.INFORMATION_MESSAGE);
            if(answer == JOptionPane.YES_OPTION){       // Nếu xác nhận là "Yes"
                this.dispose();     // Tắt giao diện đi nhưng ko kết thúc chương trình
                (new GDLogin()).setVisible(true);       // Hiện giao diện "Đăng nhập"
            }
        } else if (e.getSource() == btnLuu) {        // Khi btnLuu được nhấn
            boolean hopLe = true;       // Giả sữ các điểm đc nhập đã hợp lệ hết

            // Kiểm tra các điểm được nhập trong table trước khi lưu vào csdl
            for (int row = 0; row < table2.getRowCount(); row++) {
                table2.setValueAt(" ", row, 4);     // Ban đầu gán cho cột cuối cùng trong table2 là ko có dấu X

                // Nếu điểm của hàng thứ row có tồn tại thì mới lấy để kiểm tra
                if (table2.getValueAt(row, 3) != null && table2.getValueAt(row, 3).toString().length() > 0) {
                    String diemStr = table2.getValueAt(row, 3).toString();    // Lấy điểm tương ứng

                    try {
                        // Kiểm tra xem điểm được nhập có là 1 số thực hay không
                        float diem = Float.parseFloat(diemStr);

                        if (diem < 0 || diem > 10) {        // Nếu điểm được nhập < 0 hoặc > 10
                            throw new Exception();
                        }

                        char kyTuCuoi = diemStr.charAt(diemStr.length() - 1);

                        if (kyTuCuoi == 'd' || kyTuCuoi == 'f') {   // Nếu nội dung ô điểm là "...d" hoặc "...f"
                            throw new Exception();
                        }

                    } catch (Exception ex) {        // Khi điểm được nhập không hợp lệ
                        hopLe = false;      // Khẳng định tồn tại điểm ko hợp lệ

                        table2.setValueAt("X", row, 4);     // Gán cho cột cuối cùng, hàng thứ row có dấu X

                        lbLoi.setText("Điểm không hợp lệ");        // Thông báo
                    }
                }
            }

            if (hopLe) {        // Nếu biến hopLe vẫn == true -> các điểm được nhập đã hợp lệ hết
                lbLoi.setText(" ");     // Xóa thông báo "Điểm không hợp lệ"

                hideColumnX();      // Ẩn cột cuối cùng

                // Bắt đầu lưu dữ liệu trong table2 vào csdl
                for (int row = 0; row < table2.getRowCount(); row++) {      // Xét các hàng trong table2
                    Object mssv = table2.getValueAt(row, 1);       // Lấy dữ liệu trong ô mssv tại hàng row
                    if(mssv != null) {      // Nếu ô mssv ko rỗng
                        float diem = 11f;   // Ban đầu cho diem == 11 -> tương ứng với điểm của sinh viên này chưa đc nhập

                        // Nếu ô điểm ko rỗng
                        if(table2.getValueAt(row, 3) != null && table2.getValueAt(row, 3).toString().length() > 0) {
                            // Lấy dữ liệu trong ô điểm gán vào biến diem
                            diem = Float.parseFloat(table2.getValueAt(row, 3).toString());
                        }

                        // Lấy năm học và học kỳ
                        String nh = cBNamHoc2.getItemAt(cBNamHoc2.getSelectedIndex());
                        String hk = cBHocKy2.getItemAt(cBHocKy2.getSelectedIndex());

                        // Lấy nhóm học phần -> lấy mã học phần và ký hiệu của lớp học phần
                        String nhp = cBNhomHocPhan2.getItemAt(cBNhomHocPhan2.getSelectedIndex());
                        String maHP = nhp.substring(0, nhp.indexOf(" "));
                        String kyHieu = nhp.substring(nhp.indexOf("-") + 2, nhp.lastIndexOf("-") - 1);

                        try {
                            Connection con = JDBCUtil.getConnection();  // Tạo kết nối tới hệ quản trị csdl

                            Statement stm = con.createStatement();      // Tạo statement

                            // Viết câu lệnh sql để update điểm là diem cho sv có mssv đã lấy đc
                            String sqlLuu = "UPDATE diem_hoc_phan SET diem = " + diem +
                                    " WHERE mssv = '" + mssv.toString() + "' AND ma_hoc_phan = '" + maHP + "' AND" +
                                    " hoc_ky = '" + hk + "' AND nam_hoc = '" + nh + "' AND ky_hieu = '" + kyHieu +"';";

                            stm.executeUpdate(sqlLuu);      // Thực hiện câu lệnh sql   -> 1 điểm của 1 sv đc cập nhật

                            stm.close();
                            con.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }

                // Hiện thông báo lưu điểm thành công
                JOptionPane.showMessageDialog(null, "Lưu thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {        // Nếu tồn tại điểm ko hợp lệ -> ko lưu vào csdl
                showColumnX();      // Hiện cột cuối cùng để biết những điểm nào ko hợp lệ (đc đánh dấu X ở cột cuối cùng)
            }
        } else if(e.getSource() == btnNhap) {       // Nếu button "Nhập excel" đc nhấn
            // -> Cho phép chọn 1 file excel đang có trên máy tính, trong file excel có bảng điểm gồm các mssv và các điểm
            // -> Chương trình sẽ đọc các điểm trong file excel và hiển thị các điểm ứng với các mssv lên bảng table2

            // JFileChooser tạo giao diện đồ họa cho phép xem và lấy các file và thư mục đang có trên máy tính
            JFileChooser jFileChooser = new JFileChooser();

            int returnValue = jFileChooser.showOpenDialog(this);     // Mở hộp thoại Open để mở 1 file đang có

            // Sau khi tắt hộp thoại thì gọi phương thức getSelectedFile() để lấy đường dẫn lưu file đã đc chọn
            File openFile = jFileChooser.getSelectedFile();

            // Nếu có file đc chọn và nút Open đc nhấn
            if(openFile != null && returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    // Lấy file có đường dẫn đc lưu trong openFile để đọc file
                    FileInputStream in = new FileInputStream(new File(openFile.toString()));

                    Workbook wb = new XSSFWorkbook(in);     // Tạo 1 tham chiếu wb kiểu Workbook để tham chiếu tới file excel
                    Sheet sheet = wb.getSheetAt(0);     // Lấy trang tính đầu tiên của file excel

                    int startRowIndex = 0;      // Biến startRowIndex sẽ lưu chỉ số của hàng đầu tiên của bảng điểm
                    while (true) {
                        if(sheet.getRow(startRowIndex) != null) {       // Nếu hàng tại chỉ số startRowIndex có dữ liệu
                            Object firstCell = sheet.getRow(startRowIndex).getCell(0);      // Lấy ô đầu tiên

                            // Nếu ô đầu tiên khác rỗng và là chuỗi "STT" -> đây là hàng tiêu đề của bảng điểm
                            // -> đã tìm đc hàng đầu tiên cùa bảng điểm là bên dưới của hàng tiêu đề hiện tại
                            // -> thoát vòng lặp while
                            if(firstCell != null && firstCell.toString().equalsIgnoreCase("STT")) break;
                        }
                        startRowIndex++;       // Cập nhật biến chạy vòng lặp
                    }

                    // Sau khi thoát vòng lặp thì biến startRowIndex là chỉ số của hàng tiêu đề trong bảng điểm
                    // -> tăng 1 đơn vị sẽ là chỉ số của hàng đầu tiên trong bảng điểm
                    startRowIndex++;

                    while(true) {       // Duyệt các hàng trong bảng điểm cho tới hàng cuối cùng của bảng điểm
                        // Nếu ô đầu tiên (ô của cột "STT") là rỗng hoặc ko phải là 1 số thì hàng này ko hợp lệ
                        // -> đã đi qua hàng cuối cùng của bảng điểm, đã xét hết bảng điểm -> thoát vòng lặp
                        if(sheet.getRow(startRowIndex) == null || sheet.getRow(startRowIndex).getCell(0) == null
                        || sheet.getRow(startRowIndex).getCell(0).getCellType() != CellType.NUMERIC) {
                            break;
                        } else {    // Nếu hàng hiện tại là 1 hàng trong bảng điểm
                            // Lấy ô ở cột mssv
                            Object mssv = sheet.getRow(startRowIndex).getCell(1);
                            if(mssv != null) {      // Nếu ô mssv ko rỗng
                                // Lấy ô ở cột điểm
                                Object diem = sheet.getRow(startRowIndex).getCell(3);
                                //if(diem != null) {      // Nếu ô ở cột điểm ko rỗng -> hiển thị điểm của mssv này vào table2
                                    // Duyệt table2 để tìm hàng có mssv này
                                    for(int row = 0; (row <=  table2.getRowCount()) && (table2.getValueAt(row, 1) != null); row++) {
                                        if(table2.getValueAt(row, 1).toString().equals(mssv.toString())) {
                                            table2.setValueAt(diem, row, 3);    // Hiện điểm cho sv tìm đc
                                            break;      // Thoát vòng lặp for vì đã tìm đc sv có mssv cần tìm
                                        }
                                    }
                                //}
                            }
                        }
                        startRowIndex++;    // Xét tiếp hàng tiếp theo trong file excel
                    }
                    wb.close();
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        } else if(e.getSource() == btnXuat) {
            try {
                // JFileChooser tạo giao diện đồ họa cho phép xem các file và thư mục đang có trên máy tính
                JFileChooser jFileChooser = new JFileChooser();

                jFileChooser.showSaveDialog(this);      // Mở hộp thoại Save để lưu 1 file mới

                // Sau khi nhập tên file và nhấn Save thì gọi phương thức getSelectedFile() để lấy đường dẫn lưu file + tên file đó
                File saveFile = jFileChooser.getSelectedFile();

                if(saveFile != null) {
                    // Thêm phần mở rộng ".xlsx" vào cuối tên file
                    saveFile = new File(saveFile.toString() + ".xlsx");

                    Workbook wb = new XSSFWorkbook();       // Tạo 1 workbook trong 1 file excel

                    Sheet sheet = wb.createSheet("Sheet 1");    // Tạo 1 sheet trong 1 workbook

                    // Tạo 1 dòng hiện thông tin cơ bản của giảng viên
                    Row rowHeader = sheet.createRow(0);     // Tạo 1 hàng trong 1 sheet
                    Cell cellHeader = rowHeader.createCell(0);  // Tạo 1 ô trong 1 hàng
                    cellHeader.setCellValue("Họ tên: " + gv.getHoTen());        // Gán giá trị vào ô
                    cellHeader = rowHeader.createCell(2);
                    cellHeader.setCellValue("MSCB: " + gv.getMSCB());

                    // Tạo 1 dòng hiện năm học và học kỳ của bảng điểm đang xuất
                    rowHeader = sheet.createRow(1);
                    cellHeader = rowHeader.createCell(0);
                    cellHeader.setCellValue("Năm học: " + cBNamHoc2.getItemAt(cBNamHoc2.getSelectedIndex()));
                    cellHeader = rowHeader.createCell(2);
                    cellHeader.setCellValue("Học kỳ: " + cBHocKy2.getItemAt(cBHocKy2.getSelectedIndex()));

                    // Tạo 1 dòng dữ liệu hiện tên lớp học phần của bảng điểm đang xuất
                    rowHeader = sheet.createRow(2);
                    cellHeader = rowHeader.createCell(0);
                    cellHeader.setCellValue("Lớp học phần: " + cBNhomHocPhan2.getItemAt(cBNhomHocPhan2.getSelectedIndex()));

                    // Tạo 1 dòng dữ liệu hiện chuỗi "Danh sách sinh viên:"
                    rowHeader = sheet.createRow(4);
                    cellHeader = rowHeader.createCell(0);
                    cellHeader.setCellValue("Danh sách sinh viên:");

                    // Tạo 1 dòng dữ liệu hiện các tiêu đề của các cột trong bảng
                    rowHeader = sheet.createRow(5);
                    for(int i = 0; i < table2.getColumnCount(); i++) {
                        Cell cell = rowHeader.createCell(i);    // Tạo 1 cell thứ i trong row
                        cell.setCellValue(table2.getColumnName(i));     // Lấy tên cột thứ i trong bảng gán vào cell thứ i
                    }
                    // -> bên trên của hàng đầu tiên trong bảng điểm đã có tới 6 hàng khác

                    int rTable = 0;     // Lưu chỉ số của hàng đang xuất dữ liệu trong table2
                    
                    // Đọc các hàng dữ liệu trong bảng
                    for(rTable = 0; (rTable < table2.getRowCount()) && (table2.getValueAt(rTable, 0) != null); rTable++) {
                        // Đọc hàng có chỉ số rTable trong table2 nhưng lưu vào hàng có chỉ số rTable + 6 trong file excel
                        Row row = sheet.createRow(rTable + 6);  // Tạo 1 hàng trong file excel

                        for(int j = 0; j < table2.getColumnCount(); j++) {      // Đọc các ô trong 1 hàng trong table2
                            
                            Cell cell = row.createCell(j);      // Tạo 1 ô trong file excel
                            
                            Object value = table2.getValueAt(rTable, j);       // Đọc nội dung trong ô trong table2
                            
                            if(value != null) {     // Nếu ô này khác rỗng
                                // Xem kiểu dữ liệu của nội dung trong ô và gán nội dung vào ô trong file excel
                                if(value instanceof String) {
                                    if(j != 3) cell.setCellValue((String) value);
                                    else {
                                        try {
                                            cell.setCellValue(Math.round(Float.parseFloat(value.toString()) * 100) / 100.0);
                                        } catch (NumberFormatException nfe) {
                                            cell.setCellValue((String) value);
                                        }
                                    }
                                } else if(value instanceof Integer) {
                                    cell.setCellValue((Integer) value);
                                } else if(value instanceof Float) {
                                    // Làm tròn 1 giá trị kiểu float với 2 chữ số thập phân
                                    cell.setCellValue(Math.round(((Float) value) * 100) / 100.0);
                                }
                            }
                        }
                    }

                    //Ghi thông tin bên dưới bảng
                    Row rowFooter = sheet.createRow(rTable + 6);
                    Cell cellFooter = rowFooter.createCell(0);
                    cellFooter.setCellValue("Tổng cộng có " + rTable + " sinh viên");

                    // FileOutputStream cho phép ghi 1 đối tượng File vào máy tính tương ứng với đường dẫn của đối tượng
                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                    
                    wb.write(out);  // Ghi 1 file execl vào máy tính thông qua 1 đối tượng FileOutputStream
                    
                    wb.close();
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(e.getSource() == btnXuatBieuDo) {       // Nếu button "In biểu đồ" đc nhấn
            try {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showSaveDialog(this);      // Mở hộp thoại để lưu 1 ảnh mới
                
                File saveFile = jFileChooser.getSelectedFile();

                if(saveFile != null) {
                    // ImageIO là 1 lớp cho phép ghi 1 file ảnh vào máy tính bằng phương thức write()
                    ImageIO.write(barChart.createBufferedImage(900, 570), "png", new File(saveFile.toString() + ".png"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

