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
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public class ClientGUI {
	// Define GUI elements
	private JFrame frame;
	private JLabel titleOfFrame;
	public static JTextArea dicArea;
	public static JTextArea statusArea;
	private JPanel panel;
	private JScrollPane scrollPaneForStatus;
	private JButton disconnectButton;
	private JButton openCanvasButton;

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
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Initialize Client and connect using RMI
		Client client = new Client();
		client.buildConnection();
		
		// Initialize the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 836, 624);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Initialize the panel
		panel = new JPanel();
		panel.setBounds(0, 0, 813, 571);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Initialize the title of the frame
		titleOfFrame = new JLabel("Client GUI DEMO");
		titleOfFrame.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		titleOfFrame.setBounds(15, 15, 219, 34);
		panel.add(titleOfFrame);

		// Initialize the scroll bar for status area
		scrollPaneForStatus = new JScrollPane();
		scrollPaneForStatus.setBounds(10, 121, 747, 175);
		panel.add(scrollPaneForStatus);

		// Initialize the area to display connection status
		statusArea = new JTextArea();
		statusArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		statusArea.setEditable(false);
		scrollPaneForStatus.setViewportView(statusArea);

		// 'Open canvas' button
		openCanvasButton = new JButton("Open Canvas");
		openCanvasButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		openCanvasButton.setBounds(25, 64, 179, 29);
		panel.add(openCanvasButton);
		
		// 'Disconnect' button
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		disconnectButton.setBounds(250, 64, 179, 29);
		panel.add(disconnectButton);
		
		// (Test) title of displaying current amount of the client
		JLabel clientAmountText = new JLabel("Current client amount:");
		clientAmountText.setBounds(15, 324, 219, 21);
		panel.add(clientAmountText);	
		JLabel clientAmount = new JLabel("0");
		clientAmount.setBounds(250, 324, 81, 21);
		panel.add(clientAmount);

		// Add listener for 'Open Canvas' button
		openCanvasButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {			
				// Open the canvas
				try {
					client.remoteInterface.openCanvas();	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		// Add listener for 'Disconnect' button
		disconnectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Disconnect the canvas
				try {
					client.remoteInterface.disposeCanvas();	
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}

			}
		});

	}
}
