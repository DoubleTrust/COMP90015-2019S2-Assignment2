package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import client.ManagerGUI.dialogueListener;
import javax.swing.JList;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 19/10/2019
 */
public class ClientGUI {
	
	// Define GUI elements
	public JFrame frame;
	private JLabel titleOfFrame;
	private Client client;
	private JTextArea statusArea;
	private JPanel panel;
	private JScrollPane scrollPaneForStatus;
	private JButton joinWhiteBoardButton;
	private JButton clearBoardContentButton;
	private JLabel userListTitle;
	private JList<String> userList;
	// ---------------------------------
	private JTextPane chatPane;
	// ---------------------------------
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create an empty constructor here for 'Client_Connection' to determine who is the manager and who are clients
	 */
	public ClientGUI() {
		// For coding only
		//initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 547, 629);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Client Whiteboard ");
		
		// Add a confirm dialog when closing the client program
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        // Create a confirmDialog for the client
		    	int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to disconnect?", "Warning", JOptionPane.YES_NO_OPTION);
		         
		    	// If the client wants to close all connections
		    	if(choice == JOptionPane.YES_OPTION){
		        	try {						
						// Remove the client's username and close the board
						client.remoteInterface.RemoveClient(client.username);	
						
						// Shut down the client application
						System.exit(0);
						
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
						JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
					}
		        	
		        	// Disconnect
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
		panel.setBounds(0, 0, 533, 571);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Initialize the title of the frame
		titleOfFrame = new JLabel("Client GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(26, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for user list
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(26, 240, 208, 289);
		panel.add(scrollPaneForStatus);
		
		// User list
		userList = new JList();
		userList.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		scrollPaneForStatus.setViewportView(userList);

		// Initialize the area to display connection status
		/*statusArea = new JTextArea();
		statusArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		statusArea.setText("");
		statusArea.setEditable(false);
		scrollPaneForStatus.setViewportView(statusArea);*/
		
		userListTitle = new JLabel("User List");
		userListTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		userListTitle.setBounds(26, 191, 219, 34);
		panel.add(userListTitle);

		// 'Clear Board' button
		clearBoardContentButton = new JButton("Clear Board");
		clearBoardContentButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		clearBoardContentButton.setBounds(26, 100, 208, 29);
		panel.add(clearBoardContentButton);
			
		// 'Join WhiteBoard' button
		joinWhiteBoardButton = new JButton("Join WhiteBoard");
		joinWhiteBoardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		joinWhiteBoardButton.setBounds(25, 56, 209, 29);
		panel.add(joinWhiteBoardButton);		

		// -----------------------------------------------------
		// Chat room
		chatPane = new JTextPane();
		chatPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		chatPane.setBounds(260, 26, 252, 460);
		chatPane.setEditable(false);
		panel.add(chatPane);
		
		// Enter dialogue textField
		JTextField textField = new JTextField();
		textField.setBounds(260, 497, 181, 32);
		panel.add(textField);
		textField.setColumns(10);
		// -----------------------------------------------------
		
		
		// 'send' button
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(446, 496, 66, 23);
		panel.add(btnSend);

		// Add listener for 'Join WhiteBoard' button
		joinWhiteBoardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				try {
		
					if(client.whiteBoard == null) {
						// Convert the received board image and create the board
						InputStream in = new ByteArrayInputStream(client.remoteInterface.joinWhiteBoard());
						BufferedImage image = ImageIO.read(in);						
						client.whiteBoard = new DrawingBoard(image);
						
						// Set the visibility and title
						client.whiteBoard.setVisible(true);
						client.whiteBoard.setTitle("Client board");
						
						// A listener to keep updating and receiving the white board status in the server side
						Thread updateCanvasListener = new Thread() {
							@Override
							public void run() {
								try {
									while(true) {
										if(client.remoteInterface.canSynchronize() == true) {
											BufferedImage outputImg = setTransparency(client.whiteBoard.image);
											
											// Send the image
											byte[] imageBytes = null;			
											ByteArrayOutputStream bos = new ByteArrayOutputStream();
											ImageIO.write(outputImg, "jpg", bos);
											bos.flush();
											imageBytes = bos.toByteArray();
											bos.close();
											
											client.remoteInterface.updateBoardStatus(imageBytes);	
										}
										else {
											// Clear remote and local content
											client.remoteInterface.clearContent();
											client.whiteBoard.clearContent();
										}

										
									} 
								} catch (NullPointerException e) {
									System.out.println("NullPointerException.");
									//e.printStackTrace();
								} catch (RemoteException e) {
									System.out.println("RemoteException caught.");
									e.printStackTrace();
								} catch (IOException e) {
									System.out.println("IOException caught.");
									e.printStackTrace();
								} 
							}
						
						};
						updateCanvasListener.start();
						
						// A listener to keep receiving the white board status in the server side
						Thread getCanvasListener = new Thread() {
							@Override
							public void run() {
								try {
									while(true) {
										if(client.remoteInterface.canSynchronize() == true) {
											// Receive the image
											InputStream in = new ByteArrayInputStream(client.remoteInterface.getBoardStatus());
											BufferedImage image = ImageIO.read(in);		
											in.close();
	
											client.whiteBoard.setCanvas(image);
										}else {
											// Clear remote and local content
											client.remoteInterface.clearContent();
											client.whiteBoard.clearContent();
										}
									
									} 
								} catch (NullPointerException e) {
									System.out.println("Exception caught.");
									//e.printStackTrace();
								} catch (RemoteException e) {
									System.out.println("RemoteException caught.");
									e.printStackTrace();
								} catch (IOException e) {
									System.out.println("IOException caught.");
									e.printStackTrace();
								} 
							}
						};
						getCanvasListener.start();
						
						
					}
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					JOptionPane.showConfirmDialog(frame, "Manager closed the white board or connection error.", "Error", JOptionPane.YES_NO_OPTION);
				} catch (IOException e) {
					System.out.println("Exception caught.");
					e.printStackTrace();
				}
			}
		});
		
		
		// Add listener for 'Clear Board' button 
		clearBoardContentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
//				try {
//
//					
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
			}
		});
		
		
		// -------------------------------------------------------------
		// Add listener to 'send' button
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					String dialogue = textField.getText();
					client.remoteInterface.updateDialogue(dialogue,client.username);
					textField.setText("");
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
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
			return true;
		}
	}
	
	/**
	 * Check the amount of users
	 */
	public int getUserAmount() {
		try {
			return client.remoteInterface.getUserInfo().size();
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		} catch (NullPointerException e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
			return -1;
		}
	}
	
	/**
	 * Upload user info
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
	/**
	 * client sends the require to the manager and return the result
	 */
	public int AllowOrNot() {
		int allow=2;
		try {
			client.remoteInterface.AllowClient();
			allow=client.remoteInterface.returnAllow();
			client.remoteInterface.setAllow(0);
			return allow;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allow;
	}
	/**
	 * Construct a listener for user list
	 */
	public void createUserListListener() {
		Thread userListUpdate = new Thread(new userListListener());
		userListUpdate.start();
		
		// ---------------------------------------------------------
		// Display dialogue
		Thread dialogueUpdate = new Thread(new dialogueListener());
		dialogueUpdate.start();
		// ---------------------------------------------------------
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
					
					// Get the user info to be displayed
					String[] userinfo = client.getDisplayUserInfo();
					String[] user_info = client.getUserInfo();
					if(user_info.length == 0) {
						// throw the exception
						
						// Show kick message
						JOptionPane.showMessageDialog(frame, "Manager has closed the server.");
						
						// Disconnect and close white board
			        	frame.dispose();
			        	client.disconnect();
			        	//client.remoteInterface.
			        	
			        	// Shut Down
			        	System.exit(0);
			        	
			        	//break;
					}
					else if(userinfo[0] == "Being kicked.") {
						// throw the exception
						
						// Show kick message
						JOptionPane.showMessageDialog(frame, "You have been kicked from the manager.");
						
						// Disconnect and close white board
			        	frame.dispose();
			        	client.disconnect();
			        	//client.remoteInterface.
			        	
			        	// Shut Down
			        	System.exit(0);
			        	
			        	//break;
					}
					else {
						// Get the displayed user list
						ListModel<String> originalUserList = userList.getModel();
						
						
						if(originalUserList.getSize() != userinfo.length) {	
							// Judge whether the client has been kicked
							/*boolean isKicked = true;
							
							for(String name: userinfo) {
								System.out.println(name);
								if(name.equals(client.username + " (you)")) {
									isKicked = false;
								}
							}					
							if(isKicked) {
								// Show kick message
								JOptionPane.showConfirmDialog(frame, "You have been kicked from the manager.", "Error", JOptionPane.YES_NO_OPTION);
								
								// Disconnect
					        	frame.dispose();
					        	client.disconnect();
					        	//System.exit(0);	
							}
							else {*/
								// Update the user list
								userList.setListData(userinfo);								
							//}
					
						}
					}

					
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Connection to the server has been lost.");
				
			} catch (InterruptedException e) {
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

//					// Compare whether the dialogueList number changed
//					ListModel<String> originalUserList = userList.getModel();
					
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
		int color_range = 210;
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
