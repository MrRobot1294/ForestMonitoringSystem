package com.njit.monitoringsystem.display;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JCheckBox;

import com.njit.monitoringsystem.database.MySQLHandle;

public class Chart extends Canvas {

	public Chart() {
		super();
		// TODO Auto-generated constructor stub
		// setSize(600, 500);
		setBackground(Color.WHITE);
	}

	public void timeDisplay(ChartTab chartTab, GregorianCalendar times, String between) {
		Graphics g = getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		Color lastColor = g.getColor();
		g2.setStroke(new BasicStroke(3.0f));
		Stroke lastStroke = g2.getStroke();
		// g2.setColor(Color.RED);
		ResultSet resultSet;
		int[][] temp = new int[30][2];
		int tem = 0;
		int hum = 0;
		for (int i = 0; i < 30; i++) {
			temp[i][0] = 2016;
			temp[i][1] = 2016;
		}
		for (int i = 0; i < 16; i++) {
			g.drawString(times.get(Calendar.YEAR) + "/" + (times.get(Calendar.MONTH) + 1) + "/"
					+ times.get(Calendar.DAY_OF_MONTH), 25 + i * 70, 515);
			g.drawString(String.format("%02d:%02d:00", times.get(Calendar.HOUR_OF_DAY), times.get(Calendar.MINUTE)),
					25 + i * 70, 528);
			String down = String.format("%d%02d%02d%02d%02d00", times.get(Calendar.YEAR), times.get(Calendar.MONTH) + 1,
					times.get(Calendar.DAY_OF_MONTH), times.get(Calendar.HOUR_OF_DAY), times.get(Calendar.MINUTE));

			switch (between) {
			case "10min":
				times.add(Calendar.MINUTE, 10);
				break;

			case "15min":
				times.add(Calendar.MINUTE, 15);
				break;

			case "20min":
				times.add(Calendar.MINUTE, 20);
				break;

			case "25min":
				times.add(Calendar.MINUTE, 25);
				break;

			case "1hour":
				times.add(Calendar.HOUR_OF_DAY, 1);
				break;

			case "1day":
				times.add(Calendar.DAY_OF_MONTH, 1);
				break;

			default:
				break;
			}

			String up = String.format("%d%02d%02d%02d%02d00", times.get(Calendar.YEAR), times.get(Calendar.MONTH) + 1,
					times.get(Calendar.DAY_OF_MONTH), times.get(Calendar.HOUR_OF_DAY), times.get(Calendar.MINUTE));
			Iterator<Entry<Integer, JCheckBox>> iterator = chartTab.getDetection().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, JCheckBox> entry = iterator.next();
				int j = entry.getKey();
				if (entry.getValue().isSelected()) {
					resultSet = MySQLHandle.getMySQLHandle().selectData(j, down, up);
					try {
						if (resultSet.next()) {
							g2.setColor(chartTab.getColorText()[j - 1]);
							tem = resultSet.getInt(3);
							if (chartTab.getTemJCheckBox().isSelected()) {
								g.drawOval(i * 70 + 50 - 1, (int) ((60 - (float) tem / 100) * 5) + 75 - 1, 3, 3);
							}
							hum = resultSet.getInt(4);
							if (chartTab.getHumJCheckBox().isSelected()) {
								g.drawOval(i * 70 + 50 - 1, (int) ((60 - (float) hum / 100) * 5) + 75 - 1, 3, 3);
							}
							if (temp[j][0] != 2016) {
								if (chartTab.getTemJCheckBox().isSelected()) {
									g2.drawLine((i - 1) * 70 + 50, (int) ((60 - (float) temp[j][0] / 100) * 5) + 75,
											i * 70 + 50, (int) ((60 - (float) tem / 100) * 5) + 75);
								}
								if (chartTab.getHumJCheckBox().isSelected()) {
									Stroke stroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
											0, new float[] { 12, 12 }, 0);
									g2.setStroke(stroke);
									g2.drawLine((i - 1) * 70 + 50, (int) ((60 - (float) temp[j][1] / 100) * 5) + 75,
											i * 70 + 50, (int) ((60 - (float) hum / 100) * 5) + 75);
									g2.setStroke(lastStroke);
								}
							}
							temp[j][0] = tem;
							temp[j][1] = hum;
							g.setColor(lastColor);
						} else {
							temp[j][0] = 2016;
							temp[j][1] = 2016;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Color lastColor = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine(50, 50, 50, 500); // ◊Û ˙÷·
		g.drawLine(1100, 50, 1100, 500); // ”“ ˙÷·
		g.drawLine(50, 500, 1100, 500); // x÷·
		g.setColor(Color.LIGHT_GRAY);
		for (int i = 75; i < 500; i += 25) {
			g.drawLine(50, i, 1100, i);
		}
		for (int i = 120; i < 1100; i += 70) {
			g.drawLine(i, 50, i, 500);
		}
		g.setColor(Color.BLACK);
		for (int i = 75, j = 60; i <= 500; i += 25, j -= 5) {
			g.drawString(j + "\u2103", 20, i);
		}
		for (int i = 75, j = 85; i <= 500; i += 25, j -= 5) {
			g.drawString(j + "%", 1105, i);
		}
		g.drawString("(Œ¬∂»)", 20, 50);
		g.drawString("( ™∂»)", 1105, 50);
		g.drawString("( ±º‰)", 1130, 515);
		for (int i = 120; i < 1100; i += 70) {
			g.drawLine(i, 495, i, 500);
		}
		g.setColor(lastColor);
	}
}
