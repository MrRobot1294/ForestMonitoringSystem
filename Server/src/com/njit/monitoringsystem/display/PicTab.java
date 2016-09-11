package com.njit.monitoringsystem.display;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.njit.monitoringsystem.server.TCPServer;

public class PicTab extends JPanel {
	private Picture pictureCanvas;

	HashMap<Integer, String> bmpName;

	private byte[] picture;

	public PicTab() {
		super();
		// TODO Auto-generated constructor stub
		bmpName = new HashMap<Integer, String>();
//		bmpName.put(1, System.getProperty("user.dir") + "/pictures/1/Photo");
//		bmpName.put(2, System.getProperty("user.dir") + "/pictures/2/Photo");
//		bmpName.put(4, System.getProperty("user.dir") + "/pictures/4/Photo");

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pictureCanvas = new Picture();
		add(pictureCanvas, BorderLayout.CENTER);

		picture = new byte[153600];
	}

	public Picture getPictureCanvas() {
		return pictureCanvas;
	}

	public void setPictureCanvas(Picture pictureCanvas) {
		this.pictureCanvas = pictureCanvas;
	}

	public HashMap<Integer, String> getBmpName() {
		return bmpName;
	}

	public void setBmpName(HashMap<Integer, String> bmpName) {
		this.bmpName = bmpName;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public void repaintPic() {
		pictureCanvas.repaint();
	}

	public class Picture extends Canvas {

		public Picture() {
			super();
			// TODO Auto-generated constructor stub
			setBackground(Color.WHITE);
		}

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);

			Iterator<Entry<Integer, String>> iterator = bmpName.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<java.lang.Integer, java.lang.String> entry = (Entry<java.lang.Integer, java.lang.String>) iterator
						.next();
				int number = entry.getKey();
				String name = entry.getValue() + ".bmp";

				Image image;
				try {
					image = ImageIO.read(new File(name));
					g.drawImage(image, 340 * ((number - 1) % 3), 260 * ((number - 1) / 3), 340 * ((number - 1) % 3) + 320,
							260 * ((number - 1) / 3) + 240, 0, 0, 320, 240, null);
					g.drawString("¼à²âµã" + number, 340 * ((number - 1) % 3) + 120, 260 * ((number - 1) / 3) + 255);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
