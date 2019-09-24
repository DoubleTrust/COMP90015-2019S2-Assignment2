
package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Client_Connection {

	private JFrame frame;
	private JTextField hostName;
	private JTextField portNumber;
	private JLabel hostNameLabel;
	private JLabel portLabel;
	private JButton connectButton;
	private ClientGUI clientGUI;
	private JTextField userName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_Connection window = new Client_Connection();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client_Connection() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 514);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 678, 458);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		// Initialize the prompt for input
		hostNameLabel = new JLabel("Host name");
		hostNameLabel.setBounds(133, 114, 176, 31);
		panel.add(hostNameLabel);
		hostNameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		portLabel = new JLabel("Port number");
		portLabel.setBounds(133, 169, 176, 31);
		panel.add(portLabel);
		portLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		
		// Initialize the input field for host name
		hostName = new JTextField();
		hostName.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		hostName.setBounds(340, 118, 191, 36);
		panel.add(hostName);
		hostName.setColumns(10);
		
		// Initialize the input field for port number
		portNumber = new JTextField();
		portNumber.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		portNumber.setBounds(340, 173, 191, 36);
		panel.add(portNumber);
		portNumber.setColumns(10);	
		
		// Initialize the connect button
		connectButton = new JButton("Connect");
		connectButton.setBounds(225, 320, 170, 45);
		panel.add(connectButton);
		connectButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		
		JLabel username = new JLabel("Username");
		username.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		username.setBounds(133, 224, 176, 31);
		panel.add(username);
		
		userName = new JTextField();
		userName.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		userName.setColumns(10);
		userName.setBounds(340, 228, 191, 36);
		panel.add(userName);
		
		// Add a listener to connect button
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					// Acquire use's input
					String host = hostName.getText().trim().toString();
					String port = portNumber.getText().trim().toString();
					String username = userName.getText().trim().toString();
					
					// Verify the input
					// code to be added...
					
					// Open the client GUI				
					clientGUI = new ClientGUI();
					
					if(clientGUI.initiateClient(host, port, username)) {
						JOptionPane.showMessageDialog(null, "Connection succeed.", "Information", JOptionPane.INFORMATION_MESSAGE);			
						clientGUI.frame.setVisible(true);
				
						// Dispose this frame
						frame.dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "Connection failed. Please check your typing and wait for server to set up.", "Host Not Found", JOptionPane.ERROR_MESSAGE);
						hostName.setText("");
						portNumber.setText("");
						userName.setText("");
						
					}				
					
				}catch(Exception e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(null, "Unable to connect to RMI. Please check your input.", "Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
	}
				
}