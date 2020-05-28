package server;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author Yangyang Long
 * @date 05/10/2019
 */
public class DrawPictureCanvas extends Canvas{

	private  Image image = null;
	/*
	 *  ���û����е�ͼƬ
	 *  image //����չʾ�Ķ���
	 */
	public void setImage(Image image){
		this.image = image;	
	}
	/*
	 * ��дpaint����
	 */
	public void paint(Graphics g){
		g.drawImage(image, 0, 0, null);
		
	}
	
	public void update(Graphics g){
		paint(g);//����paint����
	}
}
