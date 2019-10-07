package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 07/10/2019
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
	private JButton openWhiteBoardButton;

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

	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 476, 629);
		frame.getContentPane().setLayout(null);
		
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
						
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
						JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
					}
		        	
		        	// Dispose the frame
		        	frame.dispose();
		        	client.disconnect();
		        	System.exit(0);	        	
	        	}
		    	else {
		    		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    	}
		    }
		});		

		// Initialize the panel
		panel = new JPanel();
		panel.setBounds(0, 0, 454, 571);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Initialize the title of the frame
		titleOfFrame = new JLabel("Client GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(26, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for status area
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(26, 240, 328, 277);
		panel.add(scrollPaneForStatus);

		// Initialize the area to display connection status
		statusArea = new JTextArea();
		statusArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		statusArea.setText("");
		statusArea.setEditable(false);
		scrollPaneForStatus.setViewportView(statusArea);
		
		JLabel userList = new JLabel("User List");
		userList.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		userList.setBounds(26, 191, 219, 34);
		panel.add(userList);

		// 'Open WhiteBoard' button
		openWhiteBoardButton = new JButton("Open WhiteBoard");
		openWhiteBoardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		openWhiteBoardButton.setBounds(26, 100, 208, 29);
		panel.add(openWhiteBoardButton);
	
		
		// 'Join WhiteBoard' button
		joinWhiteBoardButton = new JButton("Join WhiteBoard");
		joinWhiteBoardButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		joinWhiteBoardButton.setBounds(25, 56, 209, 29);
		panel.add(joinWhiteBoardButton);

		// Add listener for 'Join WhiteBoard' button
		joinWhiteBoardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {			
				try {
					// If the client has already joined 
					if(client.remoteInterface.joinWhiteBoard(client.username) == false) {
						JOptionPane.showMessageDialog(null, "You have already joined.", "Information", JOptionPane.INFORMATION_MESSAGE);			
					}	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
				}
			}
		});
		
		
		// Add listener for 'Open WhiteBoard' button (no idea whether it is necessary to add this function)
//		openWhiteBoardButton.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {			
//				// Open the white board
//				try {
//					client.remoteInterface.openWhiteBoard(client.username);	
//					
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//			}
//		});

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
	 * Construct a listener for user list
	 */
	public void createUserListListener() {
		Thread userListUpdate = new Thread(new userListListener());
		userListUpdate.start();
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
					
					String userList = client.displayUserInfo();
					statusArea.setText(userList);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog(frame, "Connection to the server has been lost.", "Error", JOptionPane.YES_NO_OPTION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
}
