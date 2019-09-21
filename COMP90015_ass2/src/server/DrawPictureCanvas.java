package server;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class DrawPictureCanvas extends Canvas{

	private  Image image = null;
	/*
	 *  设置画�?�中的图片
	 *  image //画�?�展示的对象
	 */
	public void setImage(Image image){
		this.image = image;	
	}
	/*
	 * �?写paint方法
	 */
	public void paint(Graphics g){
		g.drawImage(image, 0, 0, null);
		
	}
	
	public void update(Graphics g){
		paint(g);//调用paint方法
	}
}