package com.njit.monitoringsystem.display;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.njit.monitoringsystem.database.MySQLHandle;
import com.njit.monitoringsystem.server.TCPServer;

public class Monitor extends JFrame {

	public static float warningTem = 35;
	public static float warningHum = 20;

	private JMenuItem time10;
	private JMenuItem time15;
	private JMenuItem time20;
	private JMenuItem time25;

	private JMenuItem tem35;
	private JMenuItem tem40;
	private JMenuItem tem45;

	private JMenuItem hum1;
	private JMenuItem hum2;
	private JMenuItem hum3;

	private JTabbedPane jTabbedPane;
	private ChartTab chartTab;
	private PicTab picTab;
	private TemAHumTab temAHumTab;

	public Monitor(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub

		setLocation(100, 100);
		setResizable(false);
		setSize(1300, 700);

		chartTab = new ChartTab();

		picTab = new PicTab();
		temAHumTab = new TemAHumTab();

		jTabbedPane = new JTabbedPane();
		jTabbedPane.addTab("折线图", null, chartTab, "折线图");
		jTabbedPane.addTab("图像", null, picTab, "图像");
		jTabbedPane.addTab("实时温湿度", null, temAHumTab, "实时温湿度");
		add(jTabbedPane, BorderLayout.CENTER);

		SetListener setListener = new SetListener();

		JMenuBar jMenuBar = new JMenuBar();
		setJMenuBar(jMenuBar);
		JMenu jMenu = new JMenu("设置");
		jMenuBar.add(jMenu);
		JMenu timeJMenu = new JMenu("时间间隔");
		jMenu.add(timeJMenu);
		time10 = new JMenuItem("10分钟");
		time15 = new JMenuItem("15分钟");
		time20 = new JMenuItem("20分钟");
		time25 = new JMenuItem("25分钟");
		time10.addActionListener(setListener);
		time15.addActionListener(setListener);
		time20.addActionListener(setListener);
		time25.addActionListener(setListener);
		timeJMenu.add(time10);
		timeJMenu.add(time15);
		timeJMenu.add(time20);
		timeJMenu.add(time25);

		JMenu temJMenu = new JMenu("警戒温度");
		jMenu.add(temJMenu);
		tem35 = new JMenuItem("35\u2103");
		tem40 = new JMenuItem("40\u2103");
		tem45 = new JMenuItem("45\u2103");
		tem35.addActionListener(setListener);
		tem40.addActionListener(setListener);
		tem45.addActionListener(setListener);
		temJMenu.add(tem35);
		temJMenu.add(tem40);
		temJMenu.add(tem45);

		JMenu humJMenu = new JMenu("警戒湿度");
		jMenu.add(humJMenu);
		hum1 = new JMenuItem("1");
		hum2 = new JMenuItem("2");
		hum3 = new JMenuItem("3");
		hum1.addActionListener(setListener);
		hum2.addActionListener(setListener);
		hum3.addActionListener(setListener);
		humJMenu.add(hum1);
		humJMenu.add(hum2);
		humJMenu.add(hum3);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosed(e);
				MySQLHandle.closeConnection();
				// TCPServer.serverSocketClose();
			}
		});

		setVisible(true);
	}

	public ChartTab getChartTab() {
		return chartTab;
	}

	public TemAHumTab getTemAHumTab() {
		return temAHumTab;
	}

	public PicTab getPicTab() {
		return picTab;
	}

	public static void main(String[] args) {
		Monitor monitor = new Monitor("森林检测系统");

		TCPServer.getTcpServer().setMonitor(monitor);
		TCPServer.getTcpServer().clientLink();
	}

	class SetListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Iterator<Entry<Integer, Socket>> iterator = TCPServer.getTcpServer().getClient().entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<java.lang.Integer, java.net.Socket> entry = (Entry<java.lang.Integer, java.net.Socket>) iterator
						.next();
				PrintWriter pWriter;
				try {
					pWriter = new PrintWriter(entry.getValue().getOutputStream());

					String string = null;

					if (e.getSource() == time10) {
						string = "A";
					} else if (e.getSource() == time15) {
						string = "B";
					} else if (e.getSource() == time20) {
						string = "C";
					} else if (e.getSource() == time25) {
						string = "D";
					} else if (e.getSource() == tem35) {
						string = "E";
					} else if (e.getSource() == tem40) {
						string = "F";
					} else if (e.getSource() == tem45) {
						string = "G";
					} else if (e.getSource() == hum1) {
						string = "H";
					} else if (e.getSource() == hum2) {
						string = "J";
					} else if (e.getSource() == hum3) {
						string = "K";
					}
					pWriter.print(string);
					pWriter.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

	}
}
