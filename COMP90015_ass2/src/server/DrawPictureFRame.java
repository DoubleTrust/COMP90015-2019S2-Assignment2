package server;

import javax.swing.JFrame;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;


import java.awt.BorderLayout;//边界布局

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;//按钮组
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;//按钮类
import javax.swing.JToggleButton;//�?�以显示状�?的按钮
import javax.swing.JToolBar;//工具�?
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BasicStroke;//基本画笔
import java.awt.event.ActionListener;//动作事件 
import java.awt.event.ActionEvent; //动作监�?�

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.AlphaComposite;//�?明效果
import java.awt.Font;//字体类
import javax.swing.JOptionPane;//�?对�?框

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;

public class DrawPictureFRame extends JFrame{// implements FrameGetShape{
	BufferedImage image = new BufferedImage( 570, 390, BufferedImage.TYPE_INT_BGR);//图片大�? 类型
	Graphics gs = image.getGraphics();//获得图�?的绘制图�?
	Graphics2D g = (Graphics2D) gs;//将绘制图�?转�?�为Graphics2D;
	DrawPictureCanvas canvas = new DrawPictureCanvas();//创建画布对象
	Color forecolor = Color.BLACK;
	Color backgroundColor = Color.WHITE;//背景色
	
	//鼠标绘制点横纵�??标
	int x = -1;
	int y = -1;
	boolean rubber = false;//橡皮
	private JMenuItem strokeMenuItem1;
	private JMenuItem strokeMenuItem2;
	private JMenuItem strokeMenuItem3;
	private JMenuItem clearMenuItem;
	private JMenuItem foregroundItem;
	private JMenuItem backgroundItem;
	private JMenuItem eraseMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
    int hasSaved = 0;
    String path=null;
	int type = 0;
	String path2=null;
	int secondSaved=0;
	
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		 DrawPictureFRame pic1= new DrawPictureFRame();
		 pic1.setVisible(true);		
	}*/
	/*
	 * 构造方法
	 */
	public  DrawPictureFRame() {
		setResizable(false);
		setBounds( 500, 100, 574, 460);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("画图程�? ");
		init();
		addListener();
	}
	
	private  void addListener() {
		//画�?�添加鼠标移动事件监�?�
		canvas.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(final MouseEvent e){//拖拽鼠标
				if(x > 0 && y > 0){
					if(rubber){
						g.setColor(backgroundColor);
						g.fillRect(x, y, 10, 10);
					}else{
						g.drawLine(x, y, e.getX(), e.getY());
					}
				}
				x = e.getX();
				y = e.getY();
				canvas.repaint();
			}
		
			public void mouseMoved(final MouseEvent event){//当鼠标移动时
				if(rubber){
					Toolkit toolkit = Toolkit.getDefaultToolkit();//获得系统默认工具包
					
					Image image = toolkit.createImage("src/img/icom/鼠标橡皮.png");//利用工具包获�?�图�?
					
					Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), "c");
					setCursor(cursor);					
				}else{
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));//�??字架
				}
				
			}
		});

	
	canvas.addMouseListener(new MouseAdapter(){
		public void mouseReleased(final MouseEvent arg0){
			 x = -1;
			 y = -1;
		}
	});

	/*exitMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			//System.exit(0);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
	});*/
	clearMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			g.setColor(backgroundColor);
			g.fillRect(0, 0, 570, 390);
			g.setColor(forecolor);
			canvas.repaint();
		}
	});
	
	foregroundItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			Color fColor = new JColorChooser().showDialog(DrawPictureFRame.this, "选择颜色", Color.CYAN);
			if(fColor != null){
				forecolor = fColor;
			}
			
			g.setColor(forecolor);
		}
	});
		
	backgroundItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			Color bgColor = new JColorChooser().showDialog(DrawPictureFRame.this,"选择颜色", Color.CYAN);
			if(bgColor != null){
				backgroundColor = bgColor;//如果选择颜色�?�空就把它赋值给背景颜色
			}
			
			//设置背景
			g.setColor(backgroundColor);//画笔�?�为背景色
			g.fillRect(0, 0, 570, 390);//画满画布
			g.setColor(forecolor);//画笔设置为�?景色
			canvas.repaint();	
		}
	});	
	strokeMenuItem1.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			//声明画笔属性 ：粗 细（�?��?�?素） 末端无修饰 折线处呈尖角
			BasicStroke bStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
			g.setStroke(bStroke);	
		}
	});
	strokeMenuItem2.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			//声明画笔属性 ：粗 细（�?��?�?素） 末端无修饰 折线处呈尖角
			BasicStroke bStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
			g.setStroke(bStroke);
			
		}
	});
	strokeMenuItem3.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			//声明画笔属性 ：粗 细（�?��?�?素） 末端无修饰 折线处呈尖角
			BasicStroke bStroke = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
			g.setStroke(bStroke);
		
		}
			});
	eraseMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
			if((!rubber)){
				eraseMenuItem.setText("画图");
				rubber = true;
			}else{
				eraseMenuItem.setText("橡皮");
				rubber = false;
				g.setColor(forecolor);
			}
		}
	});
	saveAsMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
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
			
		}
	});
	
	saveMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (hasSaved == 0) {
				 try {
					    secondSaved=1;
					    hasSaved=1;
//				        BufferedImage awtImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);						
//				        Graphics g = awtImage.getGraphics();
//				        canvas.printAll(g);
				        
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
							path2 = fileToSave.getAbsolutePath();
							String fileName = fileToSave.getAbsolutePath().toLowerCase();
							
							/*
							 * If the user does not put extension at the end of the file name,
							 * automatically create one.
							 */
							if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg" )||
									fileName.endsWith(".png")) {								 
								ImageIO.write( image,ends,fileToSave);
							} else {
								File newFile =new File(fileToSave.getAbsolutePath() + "." + ends);
								ImageIO.write( image,ends,newFile);
							}
						}
				          
				    } 
				    catch(IOException e1){
						System.out.println("Problems reading.");
					}
			}
	// if this file has been saved once. execute the following code
	      if(hasSaved ==1 ) {
	    	  if(secondSaved==0) {
	    	       if(type == 1)
					try {
						ImageIO.write(image,"jpeg",new File(path));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	       if(type == 2)
					try {
						ImageIO.write(image,"jpg",new File(path));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	    	       if(type == 3)
					try {
						ImageIO.write(image,"png",new File(path));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}
	    	  if(secondSaved==1) {
	    	       if(type == 1)
					try {
						ImageIO.write(image,"jpeg",new File(path2));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	       if(type == 2)
					try {
						ImageIO.write(image,"jpg",new File(path2));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	    	       if(type == 3)
					try {
						ImageIO.write(image,"png",new File(path2));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}

	      }
		}
	});
	
	newMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new DrawPictureFRame().setVisible(true);
		}
	});
	
	openMenuItem.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			// TODO Auto-generated method stub
		        try {
		        		       		
					    DrawPictureFRame pic2=new DrawPictureFRame();
					    JFileChooser fileOpenChooser = new JFileChooser("Open a file");
		        		int returnVal = fileOpenChooser.showOpenDialog(fileOpenChooser);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File fileToOpen = fileOpenChooser.getSelectedFile();
							String fileName = fileToOpen.getAbsolutePath().toLowerCase();
							 path =fileToOpen.getAbsolutePath();
							if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg" )||
									fileName.endsWith(".png")) {
								if(fileName.endsWith(".jpeg")) type=1;
								if(fileName.endsWith(".jpg")) type=2;
								if(fileName.endsWith(".png")) type=3;
								image=ImageIO.read(fileToOpen);
							}else {
								JOptionPane.showMessageDialog(null, "Please choose valid image!");								
							} 	
						}		 
					    hasSaved =1;
					    gs = image.getGraphics();
					    g = (Graphics2D) gs;
					    canvas.setImage(image);
	  			        Container s = getContentPane();
						s.add(canvas);
					    pic2.setVisible(true);
					 }
				 catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	});
	}
	
	
	
	public void init(){
		g.setColor(backgroundColor);//设置画笔颜色
		g.fillRect(0, 0, 570, 390);//用画笔填充
		g.setColor(forecolor);//设置画笔颜色
		canvas.setImage(image);//设置画布
		Container s = getContentPane();
		s.add(canvas);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);//窗体载入�?��?�（set）
		
		JMenu systemMenu = new JMenu("系统");
		menuBar.add(systemMenu);
		newMenuItem = new JMenuItem("新建");
		systemMenu.add(newMenuItem);
		systemMenu.addSeparator();
		openMenuItem = new JMenuItem("打开");
		systemMenu.add(openMenuItem);
		systemMenu.addSeparator();
		saveMenuItem = new JMenuItem("�?存");
		systemMenu.add(saveMenuItem);
		systemMenu.addSeparator();
		saveAsMenuItem = new JMenuItem("�?�存为");
		systemMenu.add(saveAsMenuItem);
		systemMenu.addSeparator();
		exitMenuItem = new JMenuItem("退出");
		systemMenu.add(exitMenuItem);
		
		JMenu menu = new JMenu("线型");
		menuBar.add(menu);
		strokeMenuItem1 = new JMenuItem("细线");
		menu.add(strokeMenuItem1);
		menu.addSeparator();
		strokeMenuItem2 = new JMenuItem("粗线");
		menu.add(strokeMenuItem2);
		menu.addSeparator();
		strokeMenuItem3 = new JMenuItem("较粗");
		menu.add(strokeMenuItem3);
	
		JMenu menu2 = new JMenu("颜色");
		menuBar.add(menu2);
		foregroundItem = new JMenuItem("�?景颜色");
		menu2.add(foregroundItem);
		menu2.addSeparator();
		backgroundItem = new JMenuItem("背景颜色");
		menu2.add(backgroundItem);
		
		JMenu menu3 = new JMenu("编辑");
		menuBar.add(menu3);
		clearMenuItem = new JMenuItem("清除");
		menu3.add(clearMenuItem);
		menu3.addSeparator();
		eraseMenuItem = new JMenuItem("橡皮");
		menu3.add(eraseMenuItem);
	}
	
}