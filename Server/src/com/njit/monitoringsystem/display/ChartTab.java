package com.njit.monitoringsystem.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChartTab extends JPanel {
	private JPanel selectPanel;
	private Chart chart;
	private JPanel controlPanel;

	private HashMap<Integer, JCheckBox> detection;
	private Color[] colorText = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.PINK, Color.MAGENTA, Color.BLACK };

	private String[] betweenText = { "10min", "15min", "20min", "25min", "1hour", "1day" };

	private JComboBox<String> year;
	private JComboBox<String> month;
	private JComboBox<String> date;
	private JComboBox<String> hour;
	private JComboBox<String> min;
	private JComboBox<String> between;
	
	private JCheckBox temJCheckBox;
	private JCheckBox humJCheckBox;

	private JButton show;

//	class colorJLabel extends JLabel
	
	public ChartTab() {
		super();
		
		checkBoxListener cBoxListener = new checkBoxListener();
		
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		selectPanel = new JPanel(new GridLayout(20, 1));
		selectPanel.setSize(300, 500);
		add(selectPanel, BorderLayout.WEST);

		chart = new Chart();
		add(chart, BorderLayout.CENTER);

		controlPanel = new JPanel(new GridLayout(1, 13));
		controlPanel.setSize(900, 200);
		add(controlPanel, BorderLayout.SOUTH);

		detection = new HashMap<Integer, JCheckBox>();

		year = new JComboBox<String>();
		year.setEditable(true);
		year.setMaximumRowCount(6);
		year.addActionListener(cBoxListener);
		for (int i = 0; i < 20; i++) {
			year.addItem("" + (2016 + i));
		}
		controlPanel.add(year);
		controlPanel.add(new JLabel("年"));

		month = new JComboBox<String>();
		month.setEditable(true);
		month.setMaximumRowCount(6);
		month.addActionListener(cBoxListener);
		for (int i = 0; i < 12; i++) {
			month.addItem("" + (1 + i));
		}
		controlPanel.add(month);
		controlPanel.add(new JLabel("月"));

		date = new JComboBox<String>();
		date.setEditable(true);
		date.setMaximumRowCount(6);
		date.addActionListener(cBoxListener);
		for (int i = 0; i < 31; i++) {
			date.addItem("" + (1 + i));
		}
		controlPanel.add(date);
		controlPanel.add(new JLabel("日"));

		hour = new JComboBox<String>();
		hour.setEditable(true);
		hour.setMaximumRowCount(6);
		hour.addActionListener(cBoxListener);
		for (int i = 0; i < 24; i++) {
			hour.addItem("" + (0 + i));
		}
		controlPanel.add(hour);
		controlPanel.add(new JLabel("时"));

		min = new JComboBox<String>();
		min.setEditable(true);
		min.setMaximumRowCount(6);
		min.addActionListener(cBoxListener);
		for (int i = 0; i < 60; i++) {
			min.addItem("" + (0 + i));
		}
		controlPanel.add(min);
		controlPanel.add(new JLabel("分"));

		between = new JComboBox<String>();
		between.setEditable(true);
		between.setMaximumRowCount(6);
		between.addActionListener(cBoxListener);
		for (int i = 0; i < 6; i++) {
			between.addItem(betweenText[i]);
		}
		controlPanel.add(between);
		controlPanel.add(new JLabel("间隔"));

		temJCheckBox = new JCheckBox("温度", false);
		humJCheckBox = new JCheckBox("湿度", false);
		controlPanel.add(temJCheckBox);
		controlPanel.add(humJCheckBox);
		
		show = new JButton("show");
		show.addActionListener(new JButtonListener());
		controlPanel.add(show);
	}

	public JCheckBox getTemJCheckBox() {
		return temJCheckBox;
	}

	public JCheckBox getHumJCheckBox() {
		return humJCheckBox;
	}

	public HashMap<Integer, JCheckBox> getDetection() {
		return detection;	
	}
	
	public Color[] getColorText() {
		return colorText;
	}
	
	public void addDetection(int number) {
		JCheckBox jCheckBox = new JCheckBox("监测点" + number, false);
		jCheckBox.setBackground(colorText[number - 1]);
		detection.put(number, jCheckBox);
		selectPanel.add(jCheckBox);
	}
	
	public void removeDetection(int number) {
		selectPanel.remove(detection.get(number));
		detection.remove(number);
	}
	
	public ChartTab getThis() {
		return this;
	}
	
	class JButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			GregorianCalendar times = new GregorianCalendar(Integer.parseInt((String) year.getSelectedItem()),
					Integer.parseInt((String) month.getSelectedItem()) - 1,
					Integer.parseInt((String) date.getSelectedItem()),
					Integer.parseInt((String) hour.getSelectedItem()),
					Integer.parseInt((String) min.getSelectedItem()));              //输入值会与日历不符，例（2月31）
			times.add(Calendar.DAY_OF_MONTH, 1);                                    //日历天数加一减一，消除与日期不符的日期
			times.add(Calendar.DAY_OF_MONTH, -1);
			chart.update(chart.getGraphics());                                      //一开始用chart的repaint()方法，但repaint()方法在监听器执行完后才执行(不知道为什么)，但调用update()方法就可以。
			chart.timeDisplay(getThis(), times, (String)between.getSelectedItem());
		}
	}
	
	class checkBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			chart.repaint();
		}	
	}
}
