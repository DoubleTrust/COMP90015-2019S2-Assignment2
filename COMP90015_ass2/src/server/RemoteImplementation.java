package server;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import server.DrawingBoardMonitor;
import remote.RemoteInterface;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 19/10/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	// Define the an arrayList to store client username, an arrayList to store clients' white boards and a white board for manager only
	private ArrayList<String> clientInfo;
	private DrawingBoardMonitor BoardMonitor;
	private String kickUsername;
	
	//Define the require and return allow to the client
	public static int require=0;
	
	public int getRequire() throws RemoteException{
		return require;
	}
	public void setRequire(int require)throws RemoteException {
		this.require = require;	
	}
	
	public static int allow=0;
	
	public int getAllow() throws RemoteException{
		return allow;
	}
	public void setAllow(int allow) throws RemoteException {
		this.allow = allow;
	}
	
	// ----------------------------------------------------------------
	private ArrayList<String>  dialogueList;
	// ----------------------------------------------------------------
	
	/** 
	 * Default constructor 
	 */
	protected RemoteImplementation() throws RemoteException {
		this.clientInfo = new ArrayList<String>();
		this.dialogueList = new ArrayList<String>();
	}
	
	/** 
	 * Implementation of recording the client's username
	 */
	@Override
	public void uploadUserInfo(String username) throws RemoteException {
		this.clientInfo.add(username);
		
		System.out.println("Current users:");
		for(String name:this.clientInfo) {
            System.out.print(name+ " ");
        }
		 System.out.println();
	}
	
	
	// ------------------------------------------------------------------------
	/**
	 * Implementation of recording the dialogue
	 */
	@Override
	public void updateDialogue(String dialogue, String username) throws RemoteException {
		this.dialogueList.add("["+username+"]: "+dialogue);
	}
	// ------------------------------------------------------------------------
	
	
	/** 
	 * Implementation of removing the client's username and corresponding white board
	 */
	@Override
	public void RemoveUser(String username) throws RemoteException {
		this.clientInfo.remove(username);
		
		System.out.println("Current users:");
		for(String name:this.clientInfo) {
            System.out.print(name+ " ");
        }
		 System.out.println();
		
	}
	
	/** 
	 * Implementation of removing all clients' info
	 */
	@Override
	public void removeAllInfo() throws RemoteException {
		this.clientInfo.clear();
		this.BoardMonitor = null;
		System.out.println("All users have left the system.");
	}
	
	/** 
	 * Implementation of setting kick username
	 */
	@Override
	public void setKickUsername(String kickname) throws RemoteException {
		this.kickUsername = kickname;
	}
	
	/** 
	 * Implementation of kicking the user
	 */
	@Override
	public String KickUser() throws RemoteException {
		// If no user is selected or user list cannot find the name
		if(this.kickUsername.length() == 0 || this.clientInfo.contains(this.kickUsername) == false) {
			return "NOTSELECTED";
		}
		else if(this.clientInfo.contains(this.kickUsername) == false){
			return "NOTINTHELIST";
		}
		else {
			// Remove the user from user list 
			this.clientInfo.remove(this.kickUsername);
			
			System.out.println("Current users:");
			for(String name:this.clientInfo) {
	            System.out.print(name+ " ");
	        }
			 System.out.println();
			
			return "";
		}
	}
	
	/** 
	 * Implementation of returning the client info
	 */
	@Override
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	// ------------------------------------------------------------------------
	/** 
	 * Implementation of returning the dialogue
	 */
	@Override
	public ArrayList<String> getDialogue() {
		return this.dialogueList;
	}
	// ------------------------------------------------------------------------
	
	/** 
	 * Implementation of creating the manager's white board 
	 */
	@Override
	public void createWhiteBoard() throws RemoteException{

		try {
			// Determine whether the canvas has been created before
			if(this.BoardMonitor == null) {
				// Create a new white board
				this.BoardMonitor = new DrawingBoardMonitor();

				// Set the visibility and title
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);				
			}
			else {
				// Dispose the previous one
				this.BoardMonitor.dispose();
				
				// Create a new white board
				this.BoardMonitor = new DrawingBoardMonitor();

				// Set the visibility and title
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);	
			}	
	
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception.");
			//return null;
		}
	}
	
	
	
	/** 
	 * Implementation of getting the status of the white board  
	 */
	public byte[] getBoardStatus() throws RemoteException{	
		try {
			// Convert the buffered image to bytes and return
			BufferedImage outputImg = this.BoardMonitor.image;
			byte[] imageBytes = null;			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(outputImg, "jpg", bos);
			bos.flush();
			imageBytes = bos.toByteArray();
			bos.close();
			
			return imageBytes;
			
			
		} catch (IOException e) {
			System.out.println("IOException caught");
			e.printStackTrace();
			return null;
		}
	}
	
	/** Implementation of setting the status of the white board 
	 * 
	 */
	public void updateBoardStatus(byte[] imageBytes) throws RemoteException{	
		try {
			// Convert to buffer image
			InputStream in = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(in);
			in.close();			
		
			this.BoardMonitor.setCanvas(image);
			
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	} 
	
	/** 
	 * Implementation of returning whether the manager tries to open a white board   
	 */
	public boolean canSynchronize() throws RemoteException{
		return this.BoardMonitor.synchronize;
	}
	
	/** 
	 * Implementation of changing the synchronization status when the manager tries to open a white board
	 */
	public void changeSynchronization(boolean bool) throws RemoteException{
		this.BoardMonitor.synchronize = bool;
	}
	
	/** 
	 * interface to clear the content of a white board
	 */
	public boolean clearContent() throws RemoteException{
		return this.BoardMonitor.clearContent();
	}
	
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
	
	/** 
	 * Implementation of joining the white board (client only)
	 */
	@Override
	public byte[] joinWhiteBoard() throws RemoteException{
		try {
			if(this.BoardMonitor == null) {
				return null;
			}
			else {
				
				// Convert the buffered image of monitor board to bytes and return
				byte[] imageBytes = null;			
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(this.BoardMonitor.image, "jpg", bos);
				bos.flush();
				imageBytes = bos.toByteArray();
				bos.close();
				
				return imageBytes;
			}
		} catch (Exception e) {
			System.out.println("Exception caught.");
			e.printStackTrace();
			return null;
		}	
	}
	
	/** 
	 * Implementation of closing manager's white board 
	 */
	@Override
	public void closeManagerBoard() throws RemoteException{
		if(this.BoardMonitor != null) {
			this.BoardMonitor.dispose();
		}		
	}
	
	/**
	 * allow or not
	 */
	public void AllowClient() throws RemoteException{
		require=1;
	}
	/**
	 * return the result of allow or not
	 */
	public int returnAllow() throws RemoteException{
		while(getRequire()==1){
	       System.out.println("");
		}
		return getAllow();
	}
	/** 
	 * Implementation of opening the white board (manager only) 
	 */
	@Override
	public void openWhiteBoard(byte[] imageBytes) throws RemoteException{
		try {
			// Convert the byte to buffer image
			InputStream in = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(in);
			in.close();	
			
			if(this.BoardMonitor != null) {
				this.BoardMonitor.synchronize = false;
				this.BoardMonitor.openTriger = true;
				
				// Update the server's board monitor
				this.BoardMonitor.setCanvas(image);
	
	
				this.BoardMonitor.openTriger = false;
				this.BoardMonitor.synchronize = true;
			}
			else {
				this.BoardMonitor = new DrawingBoardMonitor(image);
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);
				
				this.BoardMonitor.openTriger = false;
				this.BoardMonitor.synchronize = true;
			}
		
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}