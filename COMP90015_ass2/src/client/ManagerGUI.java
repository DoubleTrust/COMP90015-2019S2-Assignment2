package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextField;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 19/10/2019
 */
public class ManagerGUI {
	
	// Define GUI elements
	public JFrame frame;
	private JLabel titleOfFrame;
	private Client client;
	private JButton kick;
	private JLabel userListTitle;
	private JPanel panel;
	private JScrollPane scrollPaneForStatus;
	private JButton newWhiteBoardButton;
	private JButton openWhiteBoardButton;
	private JButton clearBoardContentButton;
	private JList<String> userList;
	private JTextField textField;
	BufferedImage getIma;
	// ---------------------------------
	private JTextPane chatPane;
	// ---------------------------------
	private Thread uploadImg;
	private Thread downloadImg;
	



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ManagerGUI window = new ManagerGUI();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application 
	 */
	public ManagerGUI() {
		initialize();
		
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 604, 670);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Manager Board ");
		
		// Add a confirm dialog when exiting
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        // Create a confirmDialog for the user
		    	int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to disconnect?", "Warning", JOptionPane.YES_NO_OPTION);
		         
		    	// If manager wants to close the program
		    	if(choice == JOptionPane.YES_OPTION){
		        	try {
						// Disconnect the canvas
						client.remoteInterface.closeManagerBoard();	
						
						// Remove the clients' info
						client.remoteInterface.removeAllInfo();	
						
						// Shut down the manager application
						System.exit(0);
						
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
						JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
					}
		        	
		        	// Dispose the frame
		        	frame.dispose();
		        	client.disconnect();
		        	//System.exit(0);
	        	}
		    	else {
		    		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    	}
		    }
		});		

		// Initialize the panel
		panel = new JPanel();
		panel.setBounds(0, 0, 582, 614);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Initialize the title of the frame
		titleOfFrame = new JLabel("MANAGER GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(15, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for user list 
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(26, 240, 219, 289);
		panel.add(scrollPaneForStatus);
		
		// User list
		userList = new JList<String>();
		userList.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		scrollPaneForStatus.setViewportView(userList);
		
		// Initialize the user list to display user information
		userListTitle = new JLabel("User List");
		userListTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		userListTitle.setBounds(26, 191, 87, 34);
		panel.add(userListTitle);

		// 'Create WhiteBoard' button
		openWhiteBoardButton = new JButton("Open WhiteBoard");
		openWhiteBoardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		openWhiteBoardButton.setBounds(26, 100, 208, 29);
		panel.add(openWhiteBoardButton);
			
		// 'Open WhiteBoard' button
		newWhiteBoardButton = new JButton("New WhiteBoard");
		newWhiteBoardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		newWhiteBoardButton.setBounds(25, 56, 209, 29);
		panel.add(newWhiteBoardButton);
		
		// 'Clear Board' button
		clearBoardContentButton = new JButton("Clear Board");
		clearBoardContentButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		clearBoardContentButton.setBounds(26, 147, 208, 29);
		panel.add(clearBoardContentButton);
		
		// 'kick' button
		kick = new JButton("Kick");
		kick.setBounds(111, 197, 123, 29);
		kick.setEnabled(false);
		panel.add(kick);
		
		// -----------------------------------------------------
		// Chat room
		chatPane = new JTextPane();
		chatPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		chatPane.setBounds(260, 26, 252, 460);
		chatPane.setEditable(false);
		panel.add(chatPane);
		
		// Enter dialogue textField
		textField = new JTextField();
		textField.setBounds(260, 497, 181, 32);
		panel.add(textField);
		textField.setColumns(10);
		// -----------------------------------------------------
		
		// 'send' button
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(446, 496, 69, 33);
		panel.add(btnSend);
	
		
		// Select user listener
		userList.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent arg0) {	
            	try {
            		// Get the selected index of the username
                	int selectedIndex = userList.getSelectedIndex();
                	
                	// Get the usernames
                	ListModel<String> usernames = userList.getModel();
                	
                	// Get the specific username
                	String kickName = usernames.getElementAt(selectedIndex);
                	if(!kickName.equals(client.username + " (you)")) {
                		client.remoteInterface.setKickUsername(usernames.getElementAt(selectedIndex));

                		// Enable 'kick' button
                    	kick.setEnabled(true);
                	}
                	else {
                		// Disable 'kick' button
                    	kick.setEnabled(false);
                	}
                	
            	} catch (RemoteException e) {
            		e.printStackTrace();
            	}
            }
        });
		
		// Add listener for 'kick' button
		kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Kick the user
				try {
					String reply = client.remoteInterface.KickUser();
					if (reply == "NOTINTHELIST") {
						JOptionPane.showConfirmDialog(frame, "Please check the user list (user info not in RMI).", "Error", JOptionPane.YES_NO_OPTION);
					}
					else {
						// Reset the kick name
						client.remoteInterface.setKickUsername("");					
					}				
					
					// Reset the button
					kick.setEnabled(false);	
					
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Add listener for 'New WhiteBoard' button
		newWhiteBoardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(client.whiteBoard != null) {
					JOptionPane.showMessageDialog(frame, "You have already created one.");
				}
				else {
					try {
						// Create a new white board for the server
						client.remoteInterface.createWhiteBoard();
						
						// Create a local one anyway
						if(client.whiteBoard != null) {
							client.whiteBoard.dispose();
						}
						client.whiteBoard = new DrawingBoard();	
						
						// Add a listener for closing
						client.whiteBoard.addWindowListener(new java.awt.event.WindowAdapter() {
						    @Override
						    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
						        // Create a confirmDialog for the user
						    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want so save before closing?", "Notice", JOptionPane.YES_NO_OPTION);
						         
						    	// If user wants to close the window
						    	if(choice == JOptionPane.YES_OPTION){
						    		// Call 'SaveAs' function
						    		client.whiteBoard.saveAs();
						    		
						        	// Dispose the frame and clear remote content
						    		client.whiteBoard.dispose();
						    		client.whiteBoard = null;
						    		try {
										client.remoteInterface.clearContent();
									} catch (RemoteException e) {
									}
					        	}
						    	else if(choice == JOptionPane.NO_OPTION){
						        	// Dispose the frame directly
						    		client.whiteBoard.dispose();
						    		client.whiteBoard = null;
						    		try {
										client.remoteInterface.clearContent();
									} catch (RemoteException e) {
									}
						    	}
						    	else{
						    		client.whiteBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						    	}
						    }
						});	
					
						// Set the visibility and title
						client.whiteBoard.setActive(true);
						client.whiteBoard.setVisible(true);
						client.whiteBoard.setTitle("Manager board");
						
						// A listener to keep uploading its img to the white board in the server side
						uploadImg = new Thread(new uploadIMG());
						uploadImg.start();
						
						// A listener to keep downloading the white board img in the server side
						downloadImg = new Thread(new downloadIMG());
						downloadImg.start();
						
						
					} catch (NullPointerException e) {
						e.printStackTrace();
						JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
				
		// Add listener for 'Open WhiteBoard' button
		openWhiteBoardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				boolean remotePermission = false;
				// Open the white board
				try {
					client.remoteInterface.changeSynchronization(false);
					
					// Create an invisible white board if user chooses to open a white board firstly
					if(client.whiteBoard == null || client.whiteBoard.getActive() == false) {
						client.whiteBoard = new DrawingBoard();
						client.whiteBoard.setVisible(false);	
						client.whiteBoard = client.whiteBoard.openWhiteBoard();
						if(client.whiteBoard != null) {
							client.whiteBoard.hasSaved = 1;
							client.whiteBoard.setVisible(true);
							client.whiteBoard.setActive(true);
							client.whiteBoard.setDefaultCloseOperation(2);
						}
						remotePermission = true;
					}
					else if(client.whiteBoard.getActive() != false)  {					    		
						// Dispose the previous canvas directly
			    		client.whiteBoard.dispose();
			    		
			    		// Open another one
			    		client.whiteBoard = client.whiteBoard.openWhiteBoard();
			    		if(client.whiteBoard != null) {
			    			client.whiteBoard.hasSaved = 1;
			    			client.whiteBoard.setVisible(true);
			    			client.whiteBoard.setActive(true);
			    			client.whiteBoard.setDefaultCloseOperation(2);
			    		}
			    		remotePermission = true;			
					}
					
					// Add a listener for closing
					client.whiteBoard.addWindowListener(new java.awt.event.WindowAdapter() {
					    @Override
					    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					        // Create a confirmDialog for the user
					    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want so save before closing?", "Notice", JOptionPane.YES_NO_OPTION);
					         
					    	// If user wants to close the window
					    	if(choice == JOptionPane.YES_OPTION){
					    		// Call 'SaveAs' function
					    		client.whiteBoard.saveAs();
					    		
					        	// Dispose the frame and clear remote content
					    		client.whiteBoard.dispose();
					    		client.whiteBoard = null;
					    		try {
									client.remoteInterface.clearContent();
								} catch (RemoteException e) {
								}
				        	}
					    	else if(choice == JOptionPane.NO_OPTION){
					        	// Dispose the frame directly
					    		client.whiteBoard.dispose();
					    		client.whiteBoard = null;
					    		try {
									client.remoteInterface.clearContent();
								} catch (RemoteException e) {
								}
					    	}
					    	else{
					    		client.whiteBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					    	}
					    }
					});	
					
					if(remotePermission  == true) {
						// Send the buffer image of opened white board
						byte[] imageBytes = null;			
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(client.whiteBoard.image, "jpg", bos);
						bos.flush();
						imageBytes = bos.toByteArray();
						bos.close();
						
						client.remoteInterface.openWhiteBoard(imageBytes);	
						
						client.remoteInterface.changeSynchronization(true);
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					//JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
					
					// Create an invisible white board if user chooses to open a white board firstly
					client.whiteBoard = new DrawingBoard();
					client.whiteBoard.setVisible(false);	
					client.whiteBoard = client.whiteBoard.openWhiteBoard();
					if(client.whiteBoard != null) {
						client.whiteBoard.hasSaved = 1;
						client.whiteBoard.setVisible(true);
						client.whiteBoard.setActive(true);
						client.whiteBoard.setDefaultCloseOperation(2);
					}
					remotePermission = true;
			
					if(remotePermission  == true) {
						// Send the buffer image of opened white board
						byte[] imageBytes = null;			
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						try {
							ImageIO.write(client.whiteBoard.image, "jpg", bos);
							bos.flush();
							imageBytes = bos.toByteArray();
							bos.close();
							
							client.remoteInterface.openWhiteBoard(imageBytes);
						
							client.remoteInterface.changeSynchronization(true);
							
						}catch(Exception e1) {
							e1.printStackTrace();
						}
					}
					
					// Add a listener for closing
					client.whiteBoard.addWindowListener(new java.awt.event.WindowAdapter() {
					    @Override
					    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					        // Create a confirmDialog for the user
					    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want so save before closing?", "Notice", JOptionPane.YES_NO_OPTION);
					         
					    	// If user wants to close the window
					    	if(choice == JOptionPane.YES_OPTION){
					    		// Call 'SaveAs' function
					    		client.whiteBoard.saveAs();
					    		
					        	// Dispose the frame and clear remote content
					    		client.whiteBoard.dispose();
					    		client.whiteBoard = null;
					    		try {
									client.remoteInterface.clearContent();
								} catch (RemoteException e) {
								}
				        	}
					    	else if(choice == JOptionPane.NO_OPTION){
					        	// Dispose the frame directly
					    		client.whiteBoard.dispose();
					    		client.whiteBoard = null;
					    		try {
									client.remoteInterface.clearContent();
								} catch (RemoteException e) {
								}
					    	}
					    	else{
					    		client.whiteBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					    	}
					    }
					});	
					
					// Set listeners
					// A listener to keep uploading its img to the white board in the server side
					uploadImg = new Thread(new uploadIMG());
					uploadImg.start();
					
					// A listener to keep downloading the white board img in the server side
					downloadImg = new Thread(new downloadIMG());
					downloadImg.start();
					
				} catch (IOException e) {
					System.out.println("IOException caught.");
					e.printStackTrace();
				}
			}
		});
		
		// Add listener for 'Clear WhiteBoard' button
		clearBoardContentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Stop synchronization
					client.remoteInterface.changeSynchronization(false);
					
					client.remoteInterface.clearContent();
					client.whiteBoard.clearContent();
					
					// Create the JOptionPane to display waiting message
			    	JOptionPane jop = new JOptionPane("White Board is being cleared...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);			    
			    	JDialog dialog = jop.createDialog(null, "Message");				
					
			    	// Create a thread to display JDialog
					new Thread(new Runnable() {
					    @Override
					    public void run() {
					    	try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
							// Check if the content is cleared
							while (uploadImg.getState() != Thread.State.TIMED_WAITING && downloadImg.getState() != Thread.State.TIMED_WAITING) {
								// Do nothing
							}
							dialog.dispose();
					    }

					}).start();
					
					dialog.setVisible(true);
					
					// Restore the synchronization
					client.remoteInterface.changeSynchronization(true);
					
				}catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		// -------------------------------------------------------------
		// Add listener to 'send' button
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String dialogue = textField.getText();
					client.remoteInterface.updateDialogue(dialogue,client.username);
					textField.setText("");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
				}
			}
		});
		// -------------------------------------------------------------
		

	}

	/**
	 * Initialize the client
	 */
	public boolean initiateClient(String hostname, String port, String username) {
		client = new Client(hostname, port, username);
		
		// Build connection
		if(!client.buildConnection()) {
			return false;
		}
		else {			
			// Display username(s)
			Thread userListUpdate = new Thread(new userListListener());
			userListUpdate.start();
			
			// ---------------------------------------------------------
			// Display dialogue
			Thread dialogueUpdate = new Thread(new dialogueListener());
			dialogueUpdate.start();
			// ---------------------------------------------------------
			
			return true;
		}
	}
	
	/*
	 *  Listener of the user list (keep updating forever) 
	 */
	class userListListener implements Runnable{		
		@Override
		public void run() {
			try {
				while(true) {
					// Reduce the memory load
					Thread.sleep(500);
					try {
							//if a client requires to connetct
							if(client.remoteInterface.getRequire()==1) {
								int choice2=JOptionPane.showConfirmDialog(frame, "Are you allowed this user to connect?", "Warning", JOptionPane.YES_NO_OPTION);
							//if the manager allow the connection
								if(choice2 == JOptionPane.YES_OPTION){
									    client.remoteInterface.setAllow(1);
							    		client.remoteInterface.setRequire(0);
								}
								// if the manager refuses the connection.
								else {
								    client.remoteInterface.setAllow(0);
									client.remoteInterface.setRequire(0);
									
								}
							}
						} catch (HeadlessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					// Get the user info
					String[] userinfo = client.getDisplayUserInfo();					

					// Compare whether the user number changed
					ListModel<String> originalUserList = userList.getModel();
					
					if(originalUserList.getSize() != userinfo.length) {
						userList.setListData(userinfo);	
					}

				}
			} catch(NullPointerException e) {
				System.out.println("NullPointerException caught");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
				e.printStackTrace();
			} 
		}				
	}	
	
	// -------------------------------------------------------------------
	/*
	 *  Listener of the dialogue (keep updating forever) 
	 */
	class dialogueListener implements Runnable{		
		@Override
		public void run() {
			try {
				while(true) {
					// Reduce the memory load
					Thread.sleep(500);
					
					// Get the user info
					ArrayList<String> dialogue = client.getDialogue();					
					
					String mergedDialogue = "";
					// Merge dialogue:
					for(int i=0;i<dialogue.size();i++) {
						mergedDialogue = mergedDialogue + dialogue.get(i) + "\n";
					}
					
					if(chatPane.getText().length() != dialogue.size()) {
						chatPane.setText(mergedDialogue);
					}

				}
			} catch(NullPointerException e) {
				System.out.println("NullPointerException caught");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
				e.printStackTrace();
			} 
		}				
	}
	// -------------------------------------------------------------------
	
	/*
	 *  Upload image thread
	 */
	class uploadIMG implements Runnable{
		@Override
		public void run() {
			try {
				while(true) {	
					if(client.remoteInterface.canSynchronize() == true) {
						  if(client.whiteBoard.mouseIsPressed == true) {
								// Send the image
							BufferedImage outputImg = client.whiteBoard.image;
								byte[] imageBytes = null;			
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								ImageIO.write(outputImg, "jpg", bos);
								bos.flush();
								imageBytes = bos.toByteArray();
								bos.close();
								
								client.remoteInterface.updateBoardStatus(imageBytes);	
						  }
					}else {
						// Clear remote and local content
						client.remoteInterface.clearContent();
						client.whiteBoard.clearContent();
						
						// Sleep until the board could be synchronized
						while(client.remoteInterface.canSynchronize() != true) {
							Thread.sleep(100);
						}
					}
					
				} 
			} catch (NullPointerException e) {
				System.out.println("Exception caught.");
				e.printStackTrace();
			} catch (RemoteException e) {
				System.out.println("RemoteException caught.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException caught.");
				e.printStackTrace();
			} catch (InterruptedException e) {
			} 
		}
	}
	
	/*
	 * Download image thread
	 */
	class downloadIMG implements Runnable{
		@Override
		public void run() { 
			try {
				while(true) {
					if(client.remoteInterface.canSynchronize() == true ) {
						// Check whether the user utilizes pencil or eraser										
						if(client.whiteBoard.penEraOperation == false) {
							// Sleep in case of being overlapped
							Thread.sleep(2500);		
							
							InputStream in = new ByteArrayInputStream(client.remoteInterface.getBoardStatus());
							getIma = ImageIO.read(in);		
							in.close();	
							client.whiteBoard.setCanvas(getIma);	
						}
						else {
							if(client.whiteBoard.mouseIsPressed == false) {
								InputStream in = new ByteArrayInputStream(client.remoteInterface.getBoardStatus());
								getIma = ImageIO.read(in);		
								in.close();	
								client.whiteBoard.setCanvas(getIma);	
							}
						}

					}else {
						// Clear remote and local content
						client.remoteInterface.clearContent();
						client.whiteBoard.clearContent();
						
						// Sleep until the board could be synchronized
						while(client.remoteInterface.canSynchronize() != true) {
							Thread.sleep(100);
						}
					}
	
				} 
			} catch (NullPointerException e) {
				System.out.println("Exception caught.");
				e.printStackTrace();
				return;
			} catch (RemoteException e) {
				System.out.println("RemoteException caught.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException caught.");
				e.printStackTrace();
			} catch (InterruptedException e) {
			} 
		}
	}
	
	/**
	 * Upload manager info
	 */
	public void uploadInfo() {
		try {
			client.remoteInterface.uploadUserInfo(client.username);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
		}	
	}
	
	
	
	/*
	 *  Change the transparency of buffer image
	 */
	private BufferedImage setTransparency(BufferedImage image) {
		// 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();

        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片

        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb)) {
                    alpha = 0;
                } else {
                    // 设置为不透明
                    alpha = 255;
                }
                // #AARRGGBB 最前两位为透明度
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage, 0, 0, null);
        
        g2D.dispose();
		
		return bufferedImage;
	}
	
	/*
	 *  Identify which color should be set to transparent
	 */

	private boolean colorInRange(int color) {
		int color_range = 254;
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red >= color_range && green >= color_range && blue >= color_range) {
            return true;
        }
        ;
        return false;
	}
	
}
