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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;


import java.awt.BorderLayout;//�߽粼��
import java.awt.BasicStroke;//��������
import java.awt.event.ActionListener;//�����¼� 
import java.awt.event.ActionEvent; //��������
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
//
//import com.mr.util.FrameGetShape;//��ȡͼ�εĽӿ�
//import com.mr.util.ShapeWindow;//ѡ��ͼ�εĽ���
//import com.mr.util.Shapes;//��ʾ����ѡ���ͼ��
import java.awt.AlphaComposite;//͸��Ч��
import java.awt.Font;//������
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;

import javax.imageio.ImageIO;
//��ť��
//��ť��
//������ʾ״̬�İ�ť
//������
//С�Ի���
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

public class DrawingBoard extends JFrame {// implements FrameGetShape{
		BufferedImage image = new BufferedImage( 570, 390, BufferedImage.TYPE_INT_BGR);//ͼƬ��С ����
		Graphics gs = image.getGraphics();//���ͼ��Ļ���ͼ��
		Graphics2D g = (Graphics2D) gs;//������ͼ��ת��ΪGraphics2D;
		DrawPictureCanvas canvas = new DrawPictureCanvas();//������������
		BufferedImage image2 = new BufferedImage( 570, 390, BufferedImage.TYPE_INT_BGR);
		
		//�����Ƶ��������
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

		Color foregroundColor = Color.BLACK;// ǰ��ɫ
		Color backgroundColor = Color.WHITE;// ����ɫ
		private String keyword = "pencil"; // ״̬
		private int pixel_size = 1; // �ߴ�
		private boolean fill = false; // �Ƿ����
		
		int hasSaved = 0;
	    String path=null;
		int type = 0;
		int type2 =0 ;
		String path2=null;
		int secondSaved=0;
		
		public  DrawingBoard() {
			setResizable(false);
			setBounds( 500, 100, 574, 460);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("DrawingBoard");
			init();
			publicInit();
			addListener();
		}
		
		public  DrawingBoard(BufferedImage image2,int hasSaved,int type2,String path) {
			setResizable(false);
			setBounds( 500, 100, 574, 460);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setTitle("画图程序 ");
			this.hasSaved=hasSaved;
			this.type=type2;
			this.path=path;
			init2(image2);
			publicInit();
			addListener();
		}
		
		private  void addListener() {
			//���� ������¼�����
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
			
			//���� ����ͷ��¼�����
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
			
			//���� ����¼�����
			canvas.addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseDragged(final MouseEvent e) //��ק���
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
			
				//���� ����ƶ��¼�����
				public void mouseMoved(final MouseEvent event){//������ƶ�ʱ
					if(keyword=="rubber")
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));					
					}
					else if(keyword=="pencil")
					{
						setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));//ʮ�ּ�
					}
					
				}
			});
			
		
		toolBar.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));//Ĭ�Ϲ��
			}
		});
		

		
		// ���½��� ��ť����
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DrawingBoard drawingBoard = new DrawingBoard();
				drawingBoard.setVisible(true);
				drawingBoard.setDefaultCloseOperation(2);
				System.out.println("new");
			}
		});
		
		// ���򿪡� ��ť����
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    JFileChooser fileOpenChooser = new JFileChooser("Open a file");
	        		int returnVal = fileOpenChooser.showOpenDialog(fileOpenChooser);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File fileToOpen = fileOpenChooser.getSelectedFile();
						String fileName = fileToOpen.getAbsolutePath().toLowerCase();
						 path2 =fileToOpen.getAbsolutePath();
						if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg" )||
								fileName.endsWith(".png")) {
							if(fileName.endsWith(".jpeg")) type2=1;
							if(fileName.endsWith(".jpg")) type2=2;
							if(fileName.endsWith(".png")) type2=3;
							image2=ImageIO.read(fileToOpen);
							DrawingBoard pic2=new DrawingBoard(image2,1,type2,path2);
							pic2.hasSaved =1;
						    pic2.setVisible(true);
						    pic2.setDefaultCloseOperation(2);
						}else {
							JOptionPane.showMessageDialog(null, "Please choose valid image!");								
						} 	
					}		
				
				   
				 }
			 catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				System.out.println("open");
			}
		});
		
		// �����桱 ��ť����
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DrawingBoard.this.hasSaved == 0) {
					 try {
						  
					        BufferedImage awtImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);						
				            Graphics g = awtImage.getGraphics();
				            canvas.printAll(g);   
					        JFileChooser fileSaveChooser = new JFileChooser("Save a file");
							FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg");						
							fileSaveChooser.setFileFilter(filter);
							fileSaveChooser.addChoosableFileFilter(new 
									FileNameExtensionFilter("PNG","png"));
							fileSaveChooser.addChoosableFileFilter(new 
									FileNameExtensionFilter("JPG","jpg"));
							int returnVal = fileSaveChooser.showSaveDialog(fileSaveChooser);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
							    File fileToSave = fileSaveChooser.getSelectedFile();
								String ends = fileSaveChooser.getFileFilter().getDescription();
								ends = ends.toLowerCase();
								if(ends.equals("jpeg")) type=1;
								if(ends.equals("jpg")) type=2;
								if(ends.equals("png")) type=3;
								String fileName = fileToSave.getAbsolutePath().toLowerCase();
								DrawingBoard.this.hasSaved = 1;
								/*
								 * If the user does not put extension at the end of the file name,
								 * automatically create one.
								 */
								if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg" )||
										fileName.endsWith(".png")) {								 
									ImageIO.write( awtImage,ends,fileToSave);
									path = fileToSave.getAbsolutePath();
								} else {
									File newFile =new File(fileToSave.getAbsolutePath() + "." + ends);
									ImageIO.write( awtImage,ends,newFile);
									path = newFile.getAbsolutePath();
								}
							}
					          
					    } 
					    catch(IOException e1){
							System.out.println("Problems reading.");
						}
				}
		// if this file has been saved once. execute the following code
		      if(DrawingBoard.this.hasSaved ==1 ) {
			        BufferedImage awtImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);						
		            Graphics g = awtImage.getGraphics();
		            canvas.printAll(g);
		    	       if(type == 1)
						try {
						
							ImageIO.write(awtImage,"jpeg",new File(path));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	        if(type == 2)
						try {
							
							ImageIO.write(awtImage,"jpg",new File(path));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	       if(type == 3)
						try {
							 
							ImageIO.write(awtImage,"png",new File(path));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
				}
		      }		
				System.out.println("save");
			}
		});
		
		// �����Ϊ�� ��ť����
		btnSaveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point p = new Point(0, 0);
			    SwingUtilities.convertPointToScreen(p, canvas);    

			    /*
			     * Try to screenShot the current image on the canvas and save it as jpg or png.
			     * Catch possible extensions such as AWTException or file exceptions.
			     */
			    try {
			        BufferedImage awtImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);						
			        Graphics g = awtImage.getGraphics();
			        canvas.printAll(g);
			        
			        JFileChooser fileSaveChooser = new JFileChooser("Save a file");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg");						
					fileSaveChooser.setFileFilter(filter);
					fileSaveChooser.addChoosableFileFilter(new 
							FileNameExtensionFilter("PNG","png"));
					fileSaveChooser.addChoosableFileFilter(new 
							FileNameExtensionFilter("JPG","jpg"));

					int returnVal = fileSaveChooser.showSaveDialog(fileSaveChooser);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileSaveChooser.getSelectedFile();
						String ends = fileSaveChooser.getFileFilter().getDescription();
						ends = ends.toLowerCase();
						String fileName = fileToSave.getAbsolutePath().toLowerCase();
						
						/*
						 * If the user does not put extension at the end of the file name,
						 * automatically create one.
						 */
						if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg" )||
								fileName.endsWith(".png")) {								 
							ImageIO.write( awtImage,ends,fileToSave);
						} else {
							File newFile =new File(fileToSave.getAbsolutePath() + "." + ends);
							ImageIO.write( awtImage,ends,newFile);
						}
					}
			        
			    } 
			    catch(IOException e1){
					System.out.println("Problems reading.");
				}
				System.out.println("save as");
			}
		});
		
		// ��Ǧ�ʡ� ��ť����
		btnDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "pencil";
				textPane.setText(keyword);
			}
		});
		
		// ����Ƥ�� ��ť����
		btnErase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "rubber";
				textPane.setText(keyword);
			}
		});
		
		// ����״�� ��ť����
		btnShapes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapesMenu.show(btnShapes,0+btnShapes.getWidth(),0);
				textPane.setText(keyword);
			}
		});
		
		// ����״-ֱ�ߡ� ��ť����
		itemLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "line";
				textPane.setText(keyword);
			}
		});
		
		// ����״-��Բ�� ��ť����
		itemOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "oval";
				textPane.setText(keyword);
			}
		});
		
		// ����״-Բ�Ρ� ��ť����
		itemCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "circle";
				textPane.setText(keyword);
			}
		});
		
		// ����״-���Ρ� ��ť����
		itemRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "rectangle";
				textPane.setText(keyword);
			}
		});
		
		// ����״-�����Ρ� ��ť����
		itemSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				keyword = "square";
				textPane.setText(keyword);
			}
		});
		
		// ������� ��ť����
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, 570, 390);
				g.setColor(foregroundColor);
				canvas.repaint();
			}
		});
		
		// ����䡱 ��ť����
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
		
		
		// ��������ɫ�� ��ť����
		btnBc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JColorChooser();
				Color bgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
				if(bgColor != null){
					backgroundColor = bgColor;//���ѡ����ɫ�ǿվͰ�����ֵ��������ɫ
				}
				//�Ѱ�ťҲ����Ϊ������ɫ
				btnBc.setBackground(backgroundColor);
			}
		});
		
		// ��ǰ����ɫ�� ��ť����
		btnFc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JColorChooser();
				Color fgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
				if(fgColor != null){
					foregroundColor = fgColor;//���ѡ����ɫ�ǿվͰ�����ֵ��������ɫ
				}
				//�Ѱ�ťҲ����Ϊ������ɫ
				btnFc.setBackground(foregroundColor);
			}
		});
		
		// ���ߴ֡� ��ť����
		btnPixelSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				pixelsizeMenu.show(btnPixelSize,0+btnPixelSize.getWidth(),0);
			}
		});
		
		}
		
		public void publicInit()
		{
			
			// ���˵�
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setOrientation(SwingConstants.VERTICAL);
			toolBar.setBackground(Color.WHITE);
			getContentPane().add(toolBar, BorderLayout.WEST);
			toolBar.addSeparator();
			
			// ���½��� ��ť
			btnNew = new JButton();
			btnNew.setBackground(Color.WHITE);
			btnNew.setToolTipText("New file");
			btnNew.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnNew_16.png")));
			toolBar.add(btnNew);
			// ���򿪡� ��ť
			btnOpen = new JButton();
			btnOpen.setBackground(Color.WHITE);
			btnOpen.setToolTipText("Open file");
			btnOpen.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnOpen_16.png")));
			toolBar.add(btnOpen);
			// �����桱 ��ť
			btnSave = new JButton();
			btnSave.setBackground(Color.WHITE);
			btnSave.setToolTipText("Save file");
			btnSave.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSave_16.png")));
			toolBar.add(btnSave);
			// �����Ϊ�� ��ť
			btnSaveas = new JButton();
			btnSaveas.setBackground(Color.WHITE);
			btnSaveas.setToolTipText("Save file as");
			btnSaveas.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSaveas_16.png")));
			toolBar.add(btnSaveas);
			// �ֽ�
			toolBar.addSeparator();
			// ��Ǧ�ʡ� ��ť�����ɻ滭��
			btnDraw = new JButton();
			btnDraw.setBackground(Color.WHITE);
			btnDraw.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnDraw_16.png")));
			btnDraw.setToolTipText("Pencil");
			toolBar.add(btnDraw);
			// ����Ƥ�� ��ť
			btnErase = new JButton();
			btnErase.setBackground(Color.WHITE);
			btnErase.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnEraser_16.png")));
			btnErase.setToolTipText("Rubber");
			toolBar.add(btnErase);
			
			// ����״�� �˵�
			shapesMenu = new JPopupMenu();
			shapesMenu.setBackground(Color.WHITE);
			// ��ֱ�ߡ� ��ť
			itemLine = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/line_16.png")));
			itemLine.setToolTipText("Line");
			itemLine.setBackground(Color.WHITE);
			shapesMenu.add(itemLine);
			// ��Բ�Ρ� ��ť
			itemCircle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/circle_16.png")));
			itemCircle.setToolTipText("Circle");
			itemCircle.setBackground(Color.WHITE);
			shapesMenu.add(itemCircle);
			// ����Բ�� ��ť
			itemOval = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/oval_16.png")));
			itemOval.setToolTipText("Oval");
			itemOval.setBackground(Color.WHITE);
			shapesMenu.add(itemOval);
			// �������Ρ� ��ť
			itemSquare = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/square_16.png")));
			itemSquare.setToolTipText("Square");
			itemSquare.setBackground(Color.WHITE);
			shapesMenu.add(itemSquare);
			// �����Ρ� ��ť
			itemRectangle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/rectangle_16.png")));
			itemRectangle.setToolTipText("Rectangle");
			itemRectangle.setBackground(Color.WHITE);
			shapesMenu.add(itemRectangle);
			// ����״�� ��ť
			btnShapes = new JButton();
			btnShapes.setBackground(Color.WHITE);
			btnShapes.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnShapes_16.png")));
			btnShapes.setToolTipText("Shapes");
			toolBar.add(btnShapes);
			toolBar.addSeparator();
			// ���ߴ֡� �˵�
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

			// ������� ��ť
			btnClear = new JButton();
			btnClear.setToolTipText("Clear");
			btnClear.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/clear_16.png")));
			btnClear.setBackground(Color.WHITE);
			toolBar.add(btnClear);
			// ����䡱 ��ť
			btnFill = new JButton();
			btnFill.setBackground(Color.WHITE);
			btnFill.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/notfill_16.png")));
			toolBar.add(btnFill);
			// ���ߴ֡� ��ť
			btnPixelSize = new JButton();
			btnPixelSize.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
			btnPixelSize.setToolTipText("Pixel size");
			btnPixelSize.setText("  1px");
			btnPixelSize.setBackground(Color.WHITE);
			toolBar.add(btnPixelSize);
			// �ֽ�
			toolBar.addSeparator();
			// ��������ɫ�� ��ť
			btnBc = new JButton();
			btnBc.setBackground(backgroundColor);
			btnBc.setToolTipText("Background Color");
			toolBar.add(btnBc);
			// ��ǰ����ɫ�� ��ť
			btnFc = new JButton();
			btnFc.setBackground(foregroundColor);
			btnBc.setToolTipText("Foreground Color");
			toolBar.add(btnFc);
			
			textPane = new JTextPane();
			getContentPane().add(textPane, BorderLayout.SOUTH);
			textPane.setEditable(false);
			textPane.setText("pencil");
		}
		
		public void init() {
			g.setColor(backgroundColor);//���û�����ɫ
			g.fillRect(0, 0, 570, 390);//�û������
			g.setColor(foregroundColor);//���û�����ɫ
			canvas.setImage(image);//���û�����ɫ
			Container s = getContentPane();
			s.add(canvas);
		}
		
		public void init2(BufferedImage image2) {
			gs = image2.getGraphics();//获得图像的绘制图像
			g = (Graphics2D) gs;
			g.setColor(backgroundColor);//设置画笔颜色
//			g.fillRect(0, 0, 570, 390);//用画笔填充
			g.setColor(foregroundColor);//设置画笔颜色
			canvas.setImage(image2);//设置画布
			Container s = getContentPane();
			s.add(canvas);
		}
		
		public static void main(String[] args) {
			new DrawingBoard().setVisible(true);
		}
	}
