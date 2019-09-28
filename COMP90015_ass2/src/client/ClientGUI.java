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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public class ClientGUI {
	// Define GUI elements
	public JFrame frame;
	private JLabel titleOfFrame;
	private Client client;
	public static JTextArea statusArea;
	private JPanel panel;
	private JScrollPane scrollPaneForStatus;
	private JButton newWhiteBoardButton;
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
	 * Create the application 
	 */
	public ClientGUI() {
		initialize();
		
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 476, 629);
		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Add a confirm dialog when exiting
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        // Create a confirmDialog for the user
		    	int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to disconnect?", "Warning", JOptionPane.YES_NO_OPTION);
		         
		    	// If user wants to quit
		    	if(choice == JOptionPane.YES_OPTION){
		        	try {
						// Disconnect the canvas
						client.remoteInterface.disposeCanvas();	
						
						// Remove the client's username
						client.remoteInterface.RemoveClient(client.username);	
						
					} catch (RemoteException e) {
						e.printStackTrace();
					}
		        	// Dispose the frame
		        	frame.dispose();
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
		titleOfFrame = new JLabel("MANAGER GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(15, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for status area
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(26, 240, 328, 277);
		panel.add(scrollPaneForStatus);

		// Initialize the area to display connection status
		statusArea = new JTextArea();
		statusArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		statusArea.setEditable(false);
		scrollPaneForStatus.setViewportView(statusArea);
		
		JLabel userList = new JLabel("User List");
		userList.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		userList.setBounds(26, 191, 219, 34);
		panel.add(userList);

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
		
		// 'Shut Down' button
		JButton shutDownButton = new JButton("Shut Down");
		shutDownButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		shutDownButton.setBounds(26, 147, 208, 29);
		panel.add(shutDownButton);

		// Add listener for 'New Canvas' button
		newWhiteBoardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {			
				// Open the white board
				try {
					// If the white board already exists
					if(!client.remoteInterface.createWhiteBoard()) {
						JOptionPane.showMessageDialog(null, "White board aleady exists.", "Information", JOptionPane.INFORMATION_MESSAGE);		
						client.remoteInterface.openWhiteBoard();	
					}	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		// Add listener for 'Open Canvas' button
		openWhiteBoardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {			
				// Open the white board
				try {
					client.remoteInterface.openWhiteBoard();	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		// Add listener for 'Shut Down' button
		shutDownButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					// Disconnect the canvas
					client.remoteInterface.disposeCanvas();	
					
					// Remove the client's username
					client.remoteInterface.RemoveClient(client.username);	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
	        	// Dispose the frame
	        	frame.dispose();
			}
		});

	}

	/**
	 * Create the client
	 */
	public boolean initiateClient(String hostname, String port, String username) {
		client = new Client(hostname, port, username);
		
		// Build connection
		if(!client.buildConnection()) {
			return false;
		}
		else {			
			// Display username(s).... Needs while loop? 
			client.displayUserInfo();
			
			return true;
		}
	}
}
