package server;

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

import whiteboard.DrawingBoard;

import java.awt.*;

public class DrawingBoard extends JFrame {// implements FrameGetShape
	// Canvas configuration
	int canvasWidth = 1024;
	int canvasHeight = 768;
	BufferedImage image = new BufferedImage( canvasWidth, canvasHeight, BufferedImage.TYPE_INT_BGR);// Size and type of the image
	Graphics gs = image.getGraphics();
	Graphics2D g = (Graphics2D) gs;
	DrawPictureCanvas canvas = new DrawPictureCanvas();
	BufferedImage image2 = new BufferedImage( canvasWidth, canvasHeight, BufferedImage.TYPE_INT_BGR);
	
	
	// Mouse action coordinates initialization
	int x1;
	int y1;
	int x2;
	int y2;
	int x = -1;
	int y = -1;
	
	// Components
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
	private JButton btnText;
	
	private JButton btnPixelSize;
	private JPopupMenu pixelsizeMenu;
	private JButton btnBc;
	private JButton btnFc;
	private JTextPane textPane;

	// Initial parameters
	Color foregroundColor = Color.BLACK;
	Color backgroundColor = Color.WHITE;
	private String keyword = "pencil"; 
	private int pixel_size = 1; 
	private boolean fill = false;
	private String inputString;
	
	int hasSaved = 0;
    String path=null;
	int type = 0;
	int type2 =0 ;
	String path2=null;
	int secondSaved=0;
	
	// Board config
	int boardWidth = 1029;
	int boardHeight = 757;
	
	public DrawingBoard() {
		
		setResizable(false);
		setBounds( 500, 100, boardWidth, boardHeight);
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        // Create a confirmDialog for the user
		    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Are you sure you want to close the window?\n You can open it again through 'Open WhiteBoard' button", "Warning", JOptionPane.YES_NO_OPTION);
		         
		    	// If user wants to close the window
		    	if(choice == JOptionPane.YES_OPTION){
		        	// Dispose the frame
		    		setVisible(false);
	        	}
		    	else {
		    		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    	}
		    }
		});	
		
		setTitle("DrawingBoard");
		init();
		publicInit();
		addListener();
	}
	
	public DrawingBoard(BufferedImage image2,int hasSaved,int type2,String path) {
		setResizable(false);
		setBounds( 500, 100, boardWidth, boardHeight);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("WhiteBoard");
		this.hasSaved=hasSaved;
		this.type=type2;
		this.path=path;
		init2(image2);
		publicInit();
		addListener();
	}
	
	private  void addListener() {
		// Canvas-Mouse clicked action listener
		canvas.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				int xc = e.getX();
				int yc = e.getY();
				if(keyword=="text")
				{
					inputString = JOptionPane.showInputDialog(null,null,"Input text",JOptionPane.PLAIN_MESSAGE);
					if(inputString!=null)
					{
						System.out.println(xc+" "+yc);
						g.setFont(new Font("TimesRoman", Font.PLAIN, 5*pixel_size));
						g.setColor(foregroundColor);
						g.drawString(inputString, xc, yc);
					}
				}
				canvas.repaint();
			}
		});
		
		// Canvas-Mouse pressed action listener
		// Record the coordinates when the mouse is pressed. 
		canvas.addMouseListener(new MouseAdapter(){
			public void mousePressed(final MouseEvent e)
			{
				x1 = e.getX();
				y1 = e.getY();
			}
		});
		
		// Canvas-Mouse release action listener
		// When the mouse is released, draw corresponding objects.
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
		
		// Canvas-Mouse action listener
		canvas.addMouseMotionListener(new MouseMotionAdapter()
		{
			// Drag the mouse to draw specific objects.
			public void mouseDragged(final MouseEvent e)
			{
				if(x > 0 && y > 0){
					if(keyword=="rubber")
					{
						g.setColor(backgroundColor);
						g.fillRect(x, y, pixel_size, pixel_size);
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
		
			// Set the cursor when the mouse is moving on the canvas.
			public void mouseMoved(final MouseEvent event){
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		
	
	toolBar.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	});
	

	
	// "New" button listener
	btnNew.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			DrawingBoard drawingBoard = new DrawingBoard();
			drawingBoard.setVisible(true);
			drawingBoard.setDefaultCloseOperation(2);
			System.out.println("new");
		}
	});
	
	// "Open" button listener
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
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			System.out.println("open");
		}
	});
	
	// "Save" button listener
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
	
	// "Saveas" button listener
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
	
	// "Pencil" button listener
	btnDraw.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "pencil";
			textPane.setText(keyword);
		}
	});
	
	// "Rubber" button listener
	btnErase.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "rubber";
			textPane.setText(keyword);
		}
	});
	
	// "New" button listener
	btnShapes.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			shapesMenu.show(btnShapes,0+btnShapes.getWidth(),0);
			textPane.setText(keyword);
		}
	});
	
	// "Line" button listener
	itemLine.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "line";
			textPane.setText(keyword);
		}
	});
	
	// "Oval" button listener
	itemOval.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "oval";
			textPane.setText(keyword);
		}
	});
	
	// "Circle" button listener
	itemCircle.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "circle";
			textPane.setText(keyword);
		}
	});
	
	// "Rectangle" button listener
	itemRectangle.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "rectangle";
			textPane.setText(keyword);
		}
	});
	
	// "Square" button listener
	itemSquare.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "square";
			textPane.setText(keyword);
		}
	});
	
	// "Text" button listener
	btnText.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			keyword = "text";
			textPane.setText(keyword);
		}
	});
	
	/// "Clear" button listener
	btnClear.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, 1024, 768);
			g.setColor(foregroundColor);
			canvas.repaint();
		}
	});
	
	// "Fill in color" button listener
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
	
	
	// "Background Color" button listener
	btnBc.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			new JColorChooser();
			Color bgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
			if(bgColor != null){
				backgroundColor = bgColor;// if the color selected is not null
			}
			// set this color as the background of the button
			btnBc.setBackground(backgroundColor);
		}
	});
	
	// "Foreground Color" button listener
	btnFc.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			new JColorChooser();
			Color fgColor = JColorChooser.showDialog(DrawingBoard.this,"Color", Color.CYAN);
			if(fgColor != null){
				foregroundColor = fgColor; // if the color selected is not null
			}
			// set this color as the background of the button
			btnFc.setBackground(foregroundColor);
		}
	});
	
	// "Pixel Size" button listener
	btnPixelSize.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) 
		{
			pixelsizeMenu.show(btnPixelSize,0+btnPixelSize.getWidth(),0);
		}
	});
	
	}
	

	public void publicInit()
	{
		
		// Left Tool Bar
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		toolBar.setBackground(Color.WHITE);
		getContentPane().add(toolBar, BorderLayout.WEST);
		toolBar.addSeparator();
		
		// New
		btnNew = new JButton();
		btnNew.setBackground(Color.WHITE);
		btnNew.setToolTipText("New file");
		btnNew.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnNew_16.png")));
		toolBar.add(btnNew);
		// Open
		btnOpen = new JButton();
		btnOpen.setBackground(Color.WHITE);
		btnOpen.setToolTipText("Open file");
		btnOpen.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnOpen_16.png")));
		toolBar.add(btnOpen);
		// Save
		btnSave = new JButton();
		btnSave.setBackground(Color.WHITE);
		btnSave.setToolTipText("Save file");
		btnSave.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSave_16.png")));
		toolBar.add(btnSave);
		// Save as
		btnSaveas = new JButton();
		btnSaveas.setBackground(Color.WHITE);
		btnSaveas.setToolTipText("Save file as");
		btnSaveas.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnSaveas_16.png")));
		toolBar.add(btnSaveas);
		// Separator
		toolBar.addSeparator();
		// Pencil(free drawing)
		btnDraw = new JButton();
		btnDraw.setBackground(Color.WHITE);
		btnDraw.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnDraw_16.png")));
		btnDraw.setToolTipText("Pencil");
		toolBar.add(btnDraw);
		// Rubber
		btnErase = new JButton();
		btnErase.setBackground(Color.WHITE);
		btnErase.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnEraser_16.png")));
		btnErase.setToolTipText("Rubber");
		toolBar.add(btnErase);
		
		// Popup Menu for Shapes
		shapesMenu = new JPopupMenu();
		shapesMenu.setBackground(Color.WHITE);
		// item line
		itemLine = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/line_16.png")));
		itemLine.setToolTipText("Line");
		itemLine.setBackground(Color.WHITE);
		shapesMenu.add(itemLine);
		// item circle
		itemCircle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/circle_16.png")));
		itemCircle.setToolTipText("Circle");
		itemCircle.setBackground(Color.WHITE);
		shapesMenu.add(itemCircle);
		// item oval
		itemOval = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/oval_16.png")));
		itemOval.setToolTipText("Oval");
		itemOval.setBackground(Color.WHITE);
		shapesMenu.add(itemOval);
		// item square
		itemSquare = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/square_16.png")));
		itemSquare.setToolTipText("Square");
		itemSquare.setBackground(Color.WHITE);
		shapesMenu.add(itemSquare);
		// item rectangle
		itemRectangle = new JMenuItem(new ImageIcon(DrawingBoard.class.getResource("/img/rectangle_16.png")));
		itemRectangle.setToolTipText("Rectangle");
		itemRectangle.setBackground(Color.WHITE);
		shapesMenu.add(itemRectangle);
		// Shapes
		btnShapes = new JButton();
		btnShapes.setBackground(Color.WHITE);
		btnShapes.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/btnShapes_16.png")));
		btnShapes.setToolTipText("Shapes");
		toolBar.add(btnShapes);
		// Text
		btnText = new JButton("");
		btnText.setBackground(Color.WHITE);
		btnText.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/text_16.png")));
		toolBar.add(btnText);
		toolBar.addSeparator();
		
		// PopMenu of Pixel size
		pixelsizeMenu = new JPopupMenu();
		pixelsizeMenu.setBackground(Color.WHITE);
		String[] strlistPixelsize = { "   3", "   5", "   7", "   9", "  12", "  14"};
		int[] intarrayPixelsize = { 3, 5, 7, 9, 12, 14 };
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

		// Clear
		btnClear = new JButton();
		btnClear.setToolTipText("Clear");
		btnClear.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/clear_16.png")));
		btnClear.setBackground(Color.WHITE);
		toolBar.add(btnClear);
		// Fill in Color
		btnFill = new JButton();
		btnFill.setBackground(Color.WHITE);
		btnFill.setToolTipText("Fill in color");
		btnFill.setIcon(new ImageIcon(DrawingBoard.class.getResource("/img/notfill_16.png")));
		toolBar.add(btnFill);
		// Pixel Size
		btnPixelSize = new JButton();
		btnPixelSize.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
		btnPixelSize.setToolTipText("Line size");
		btnPixelSize.setText("   3");
		btnPixelSize.setBackground(Color.WHITE);
		toolBar.add(btnPixelSize);
		// Separater
		toolBar.addSeparator();
		// BackgroundColor
		btnBc = new JButton("     ");
		btnBc.setBackground(backgroundColor);
		btnBc.setToolTipText("Background Color");
		toolBar.add(btnBc);
		// Forground Color
		btnFc = new JButton("     ");
		btnFc.setBackground(foregroundColor);
		btnFc.setToolTipText("Foreground Color");
		toolBar.add(btnFc);

		// Condition description text panel
		textPane = new JTextPane();
		getContentPane().add(textPane, BorderLayout.SOUTH);
		textPane.setEditable(false);
		textPane.setText("pencil");
	}
	
	// Initalize canvas
	public void init() {
		g.setColor(backgroundColor);// set the color for drawing
		g.fillRect(0, 0, canvasWidth, canvasHeight);//set background
		g.setColor(foregroundColor);//set the color for drawing
		canvas.setImage(image);//set background color of canvas
		Container s = getContentPane();
		s.add(canvas);
	}
	
	public void init2(BufferedImage image2) {
		gs = image2.getGraphics();
		g = (Graphics2D) gs;
		g.setColor(backgroundColor);
		g.setColor(foregroundColor);
		canvas.setImage(image2);
		Container s = getContentPane();
		s.add(canvas);
	}
	
	/*public static void main(String[] args) {
		new DrawingBoard().setVisible(true);
	}*/
}
