package whiteboard;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;


import java.awt.BorderLayout;//边界布局
import java.awt.BasicStroke;//基本画笔
import java.awt.event.ActionListener;//动作事件 
import java.awt.event.ActionEvent; //动作监听
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
//
//import com.mr.util.FrameGetShape;//获取图形的接口
//import com.mr.util.ShapeWindow;//选择图形的界面
//import com.mr.util.Shapes;//表示可以选择的图形
import java.awt.AlphaComposite;//透明效果
import java.awt.Font;//字体类
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;

//按钮组
//按钮类
//可以显示状态的按钮
//工具栏
//小对话框
import javax.swing.*;
import java.awt.*;

public class DrawingBoard extends JFrame {// implements FrameGetShape{
		BufferedImage image = new BufferedImage( 570, 390, BufferedImage.TYPE_INT_BGR);//图片大小 类型
		Graphics gs = image.getGraphics();//获得图像的绘制图像
		Graphics2D g = (Graphics2D) gs;//将绘制图像转换为Graphics2D;
		DrawPictureCanvas canvas = new DrawPictureCanvas();//创建画布对象
		
		//鼠标绘制点横纵坐标
		int x1;
		int y1;
		int x2;
		int y2;
		int x = -1;
		int y = -1;
		
		private JToolBar toolBar;

		private JButton btnNew;
		private JButton btnOpen;
		private JButton btnSave;
		private JButton btnSaveas;
		
		private JButton btnDraw;
		private JButton btnErase;
		private JButton btnShapes;
		private JPopupMenu shapesMenu;
		private JMenuItem itemLine;
		private JMenuItem itemCircle;
		private JMenuItem itemOval;
		private JMenuItem itemSquare;
		private JMenuItem itemRectangle;
		private JButton btnClear;
		private JButton btnFill;
		
		private JButton btnPixelSize;
		private JPopupMenu pixelsizeMenu;
		private JButton btnBc;
		private JButton btnFc;
		private JTextPane textPane;

		Color foregroundColor = Color.BLACK;// 前景色
		Color backgroundColor = Color.WHITE;// 背景色
		private String keyword = "pencil"; // 状态
		private int pixel_size = 1; // 线粗
		private boolean fill = false; // 是否填充
		
		public  DrawingBoard() {
			setResizable(false);
			setBounds( 500, 100, 574, 460);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("DrawingBoard");
			init();
			addListener();
		}
		
		private  void addListener() {
			//画板 鼠标点击事件监听
			canvas.addMouseListener(new MouseAdapter(){
				public void mousePressed(final MouseEvent e)
				{
					x1 = e.getX();
					y1 = e.getY();
					System.out.println("Pressed");
					System.out.println(x1);
					System.out.println(y1);
				}
			});
			
			//画板 鼠标释放事件监听
			canvas.addMouseListener(new MouseAdapter(){
				public void mouseReleased(final MouseEvent e){
					x = -1;
					y = -1;
					x2 = e.getX();
					y2 = e.getY();
					if(keyword=="line")
					{
						g.setColor(foregroundColor);
						System.out.println("bc:"+backgroundColor+" fc:"+foregroundColor+" px:"+pixel_size);
						g.drawLine(x1, y1, x2, y2);
						canvas.repaint();
					}
					else
					{
						int xx1 = x1;
						int yy1 = y1;
						int width = x2 - x1;
						int height = y2 - y1;
						g.setColor(foregroundColor);
						if(x2-x1>0&&y2-y1>0)
						{
							xx1 = x1;
							yy1 = y1;
							width = x2 - x1;
							height = y2 - y1;
						}
						else if(x2-x1>0&&y2-y1<0)
						{
							xx1 = x1;
							yy1 = y2;
							width = x2 - x1;
							height = y1 - y2;
						}
						else if(x2-x1<0&&y2-y1<0)
						{
							xx1 = x2;
							yy1 = y2;
							width = x1 - x2;
							height = y1 - y2;
						}
						else if(x2-x1<0&&y2-y1>0)
						{
							xx1 = x2;
							yy1 = y1;
							width = x1 - x2;
							height = y2 - y1;
						}
						if(keyword=="oval")
						{
							g.drawOval(xx1, yy1, width, height);
							if(fill)
							{
								g.setColor(backgroundColor);
								g.fillOval(xx1, yy1, width, height);
							}
						}
						else if(keyword=="circle")
						{
							g.drawOval(xx1, yy1, width, width);
							if(fill)
							{
								g.setColor(backgroundColor);
								g.fillOval(xx1, yy1, width, width);
							}
						}
						if(keyword=="rectangle")
						{
							g.drawRect(xx1, yy1, width, height);
							if(fill)
							{
								g.setColor(backgroundColor);
								g.fillRect(xx1, yy1, width, height);
							}
						}
						if(keyword=="square")
						{
							g.drawRect(xx1, yy1, width, width);
							if(fill)
							{
								g.setColor(backgroundColor);
								g.fillRect(xx1, yy1, width, width);
							}
						}
						canvas.repaint();
					}
				}
			});
			
			//画板 鼠标事件监听
			canvas.addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseDragged(final MouseEvent e) //拖拽鼠标
				{
					if(x > 0 && y > 0){
						if(keyword=="rubber")
						{
							g.setColor(backgroundColor);
							g.fillOval(x, y, pixel_size, pixel_size);
						}
						else if(keyword=="pencil")
						{
							g.setColor(foregroundColor);
							g.drawLine(x, y, e.getX(), e.getY());
						}
					}
					x = e.getX();
					y = e.getY();
					canvas.repaint();
				}
			
				//画板 鼠标移动事件监听
				public void mouseMoved(final MouseEvent event){//当鼠标移动时
					if(keyword=="rubber")
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));					
					}
					else if(keyword=="pencil")
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));//十字架
					}
					
				}
			});
			
		
		toolBar.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));//默认光标
			}
		});
		

		
		// “新建” 按钮监听
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("new");
			}
		});
		
		// “打开” 按钮监听
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("open");
			}
		});
		
		// “保存” 按钮监听
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("save");
			}
		});
		
		// “另存为” 按钮监听
		btnSaveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("save as");
			}
		});
		
		// “铅笔” 按钮监听
		btnDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "pencil";
				textPane.setText(keyword);
			}
		});
		
		// “橡皮” 按钮监听
		btnErase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "rubber";
				textPane.setText(keyword);
			}
		});
		
		// “形状” 按钮监听
		btnShapes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapesMenu.show(btnShapes,0+btnShapes.getWidth(),0);
				textPane.setText(keyword);
			}
		});
		
		// “形状-直线” 按钮监听
		itemLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "line";
				textPane.setText(keyword);
			}
		});
		
		// “形状-椭圆” 按钮监听
		itemOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "oval";
				textPane.setText(keyword);
			}
		});
		
		// “形状-圆形” 按钮监听
		itemCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "circle";
				textPane.setText(keyword);
			}
		});
		
		// “形状-矩形” 按钮监听
		itemRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "rectangle";
				textPane.setText(keyword);
			}
		});
		
		// “形状-正方形” 按钮监听
		itemSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "square";
				textPane.setText(keyword);
			}
		});
		
		// “清除” 按钮监听
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);
				g.setColor(foregroundColor);
				canvas.repaint();
			}
		});
		
		// “填充” 按钮监听
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fill)
				{
					fill=false;
					btnFill.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/notfill_16.png")));
					btnFill.setToolTipText("not fill in color");
				}
				else
				{
					fill=true;
					btnFill.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/fill_16.png")));
					btnFill.setToolTipText("fill in color");
				}
			}
		});
		
		
		// “背景颜色” 按钮监听
		btnBc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JColorChooser();
				Color bgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
				if(bgColor != null){
					backgroundColor = bgColor;//如果选择颜色非空就把它赋值给背景颜色
				}
				//把按钮也设置为这种颜色
				btnBc.setBackground(backgroundColor);
			}
		});
		
		// “前景颜色” 按钮监听
		btnFc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JColorChooser();
				Color fgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
				if(fgColor != null){
					foregroundColor = fgColor;//如果选择颜色非空就把它赋值给背景颜色
				}
				//把按钮也设置为这种颜色
				btnFc.setBackground(foregroundColor);
			}
		});
		
		// “线粗” 按钮监听
		btnPixelSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				pixelsizeMenu.show(btnPixelSize,0+btnPixelSize.getWidth(),0);
			}
		});
		
		}
		
		public void init()
		{
			g.setColor(backgroundColor);//设置画笔颜色
			g.fillRect(0, 0, 570, 390);//用画笔填充
			g.setColor(foregroundColor);//设置画笔颜色
			canvas.setImage(image);//设置画布颜色
			Container s = getContentPane();
			s.add(canvas);
			
			// 左侧菜单
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.setBackground(Color.WHITE);
			getContentPane().add(toolBar, BorderLayout.WEST);
			toolBar.addSeparator();
			
			// “新建” 按钮
			btnNew = new JButton();
			btnNew.setBackground(Color.WHITE);
			btnNew.setToolTipText("New file");
			btnNew.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnNew_16.png")));
			toolBar.add(btnNew);
			// “打开” 按钮
			btnOpen = new JButton();
			btnOpen.setBackground(Color.WHITE);
			btnOpen.setToolTipText("Open file");
			btnOpen.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnOpen_16.png")));
			toolBar.add(btnOpen);
			// “保存” 按钮
			btnSave = new JButton();
			btnSave.setBackground(Color.WHITE);
			btnSave.setToolTipText("Save file");
			btnSave.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSave_16.png")));
			toolBar.add(btnSave);
			// “另存为” 按钮
			btnSaveas = new JButton();
			btnSaveas.setBackground(Color.WHITE);
			btnSaveas.setToolTipText("Save file as");
			btnSaveas.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSaveas_16.png")));
			toolBar.add(btnSaveas);
			// 分界
			toolBar.addSeparator();
			// “铅笔” 按钮（自由绘画）
			btnDraw = new JButton();
			btnDraw.setBackground(Color.WHITE);
			btnDraw.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnDraw_16.png")));
			btnDraw.setToolTipText("Pencil");
			toolBar.add(btnDraw);
			// “橡皮” 按钮
			btnErase = new JButton();
			btnErase.setBackground(Color.WHITE);
			btnErase.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnEraser_16.png")));
			btnErase.setToolTipText("Rubber");
			toolBar.add(btnErase);
			
			// “形状” 菜单
			shapesMenu = new JPopupMenu();
			shapesMenu.setBackground(Color.WHITE);
			// “直线” 按钮
			itemLine = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/line_16.png")));
			itemLine.setToolTipText("Line");
			itemLine.setBackground(Color.WHITE);
			shapesMenu.add(itemLine);
			// “圆形” 按钮
			itemCircle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/circle_16.png")));
			itemCircle.setToolTipText("Circle");
			itemCircle.setBackground(Color.WHITE);
			shapesMenu.add(itemCircle);
			// “椭圆” 按钮
			itemOval = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/oval_16.png")));
			itemOval.setToolTipText("Oval");
			itemOval.setBackground(Color.WHITE);
			shapesMenu.add(itemOval);
			// “正方形” 按钮
			itemSquare = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/square_16.png")));
			itemSquare.setToolTipText("Square");
			itemSquare.setBackground(Color.WHITE);
			shapesMenu.add(itemSquare);
			// “矩形” 按钮
			itemRectangle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/rectangle_16.png")));
			itemRectangle.setToolTipText("Rectangle");
			itemRectangle.setBackground(Color.WHITE);
			shapesMenu.add(itemRectangle);
			// “形状” 按钮
			btnShapes = new JButton();
			btnShapes.setBackground(Color.WHITE);
			btnShapes.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnShapes_16.png")));
			btnShapes.setToolTipText("Shapes");
			toolBar.add(btnShapes);
			toolBar.addSeparator();
			// “线粗” 菜单
			pixelsizeMenu = new JPopupMenu();
			pixelsizeMenu.setBackground(Color.WHITE);
			String[] strlistPixelsize = { "  1px", "  3px", "  5px", "  7px", "  9px", "12px"};
			int[] intarrayPixelsize = { 1, 3, 5, 7, 9, 12 };
			for(int i=0; i < strlistPixelsize.length; i++) 
			{
				final Integer inneri = new Integer(i);
				JMenuItem itemPixelsize = new JMenuItem(strlistPixelsize[i]);
				itemPixelsize.setBackground(Color.WHITE);
				itemPixelsize.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) 
					{
						pixel_size = intarrayPixelsize[inneri];
						btnPixelSize.setText(strlistPixelsize[inneri]);
						BasicStroke bStroke = new BasicStroke(pixel_size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
						g.setStroke(bStroke);
					}
				});
				pixelsizeMenu.add(itemPixelsize);
			}

			// “清除” 按钮
			btnClear = new JButton();
			btnClear.setToolTipText("Clear");
			btnClear.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/clear_16.png")));
			btnClear.setBackground(Color.WHITE);
			toolBar.add(btnClear);
			// “填充” 按钮
			btnFill = new JButton();
			btnFill.setBackground(Color.WHITE);
			btnFill.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/notfill_16.png")));
			toolBar.add(btnFill);
			// “线粗” 按钮
			btnPixelSize = new JButton();
			btnPixelSize.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
			btnPixelSize.setToolTipText("Pixel size");
			btnPixelSize.setText("  1px");
			btnPixelSize.setBackground(Color.WHITE);
			toolBar.add(btnPixelSize);
			// 分界
			toolBar.addSeparator();
			// “背景颜色” 按钮
			btnBc = new JButton();
			btnBc.setBackground(backgroundColor);
			btnBc.setToolTipText("Background Color");
			toolBar.add(btnBc);
			// “前景颜色” 按钮
			btnFc = new JButton();
			btnFc.setBackground(foregroundColor);
			btnBc.setToolTipText("Foreground Color");
			toolBar.add(btnFc);
			
			textPane = new JTextPane();
			getContentPane().add(textPane, BorderLayout.SOUTH);
			textPane.setEditable(false);
			textPane.setText("pencil");
		}
		
		
		public static void main(String[] args) {
			new DrawingBoard().setVisible(true);
		}
	}
