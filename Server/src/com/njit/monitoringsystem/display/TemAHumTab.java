package com.njit.monitoringsystem.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.njit.monitoringsystem.server.TCPServer;

public class TemAHumTab extends JPanel {
	private JPanel tAHDisplay;
	private JButton collect;

	public static HashMap<Integer, Item> collectItem = new HashMap<Integer, Item>();

	public TemAHumTab() {
		super();
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		tAHDisplay = new JPanel(new GridLayout(0, 3));
		tAHDisplay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JScrollPane jScrollPane = new JScrollPane(tAHDisplay);
		add(jScrollPane, BorderLayout.CENTER);

		JPanel controlJPanel = new JPanel();
		collect = new JButton("采集");
		collect.addActionListener(new JButtonListener());
		controlJPanel.add(collect);
		add(controlJPanel, BorderLayout.SOUTH);

		// Item item1 = new Item(1);
		// Item item2 = new Item(2);
	}
	
	public void addCollectItem(int number) {
		new Item(number);
	}

	public class Item {
		private int num;
		private JCheckBox collection;
		private JLabel tem;
		private JLabel hum;

		public Item(int num) {
			// TODO Auto-generated constructor stub
			this.num = num;
			collection = new JCheckBox("监测点" + num);
			collection.setFont(new Font("黑体", Font.PLAIN, 60));
			collection.setForeground(Color.black);

			tem = new JLabel("温度:0.00\u2103 ");
			tem.setFont(new Font("黑体", Font.PLAIN, 60));
			tem.setForeground(Color.black);
			hum = new JLabel("湿度:0.00%");
			hum.setFont(new Font("黑体", Font.PLAIN, 60));
			hum.setForeground(Color.black);
			tAHDisplay.add(collection);
			tAHDisplay.add(tem);
			tAHDisplay.add(hum);

			TemAHumTab.collectItem.put(num, this);
		}

		public void changeTemHum(float tem, float hum) {
			this.tem.setText("温度:" + tem + "\u2103 ");
			this.hum.setText("湿度:" + hum + "%");

			if (tem >= Monitor.warningTem) {
				this.tem.setForeground(Color.RED);
			} else {
				this.tem.setForeground(Color.BLACK);
			}

			if (hum >= Monitor.warningHum) {
				this.hum.setForeground(Color.RED);
			} else {
				this.hum.setForeground(Color.BLACK);
			}
		}

		public boolean collectionIsSelected() {
			return collection.isSelected();
		}

		public void disConnected() {
			TemAHumTab.collectItem.remove(num);
			tAHDisplay.remove(collection);
			tAHDisplay.remove(tem);
			tAHDisplay.remove(hum);
		}
	}

	class JButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Iterator<Entry<Integer, Item>> iterator = TemAHumTab.collectItem.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Item> entry = (Entry<Integer, Item>) iterator.next();
				if (entry.getValue().collectionIsSelected()) {
					try {
						PrintWriter writer = new PrintWriter(new OutputStreamWriter(
								TCPServer.getTcpServer().getClient().get(entry.getKey()).getOutputStream()));
						writer.print("L");
						writer.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
