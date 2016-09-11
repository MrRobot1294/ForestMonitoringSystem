package com.njit.monitoringsystem.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.njit.monitoringsystem.database.MySQLHandle;
import com.njit.monitoringsystem.display.Monitor;
import com.njit.monitoringsystem.display.TemAHumTab;
import com.njit.monitoringsystem.display.TxtToBmp;

public class TCPServer {
	private static TCPServer server;

	private ServerSocket serverSocket;
	private HashMap<Integer, Socket> client;
	private Monitor monitor;

	public TCPServer(int port) {
		// TODO Auto-generated constructor stub
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client = new HashMap<Integer, Socket>();
	}

	public static TCPServer getTcpServer() {
		if (server == null) {
			server = new TCPServer(8086);
		}

		return server;
	}

	public static void serverSocketClose() {
		if (server != null) {
			try {
				server.serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("·þÎñÆ÷¹Ø±Õ");
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void clientLink() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new Thread(new ClientThread(socket)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public HashMap<Integer, Socket> getClient() {
		return client;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	class ClientThread implements Runnable {
		private Socket socket;
		private int number;
		private BufferedReader reader;

		public ClientThread(Socket socket) {
			// TODO Auto-generated constructor stub
			try {
				this.socket = socket;
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String string = null;
				string = reader.readLine();
				number = Integer.parseInt(string);

				client.put(number, socket);
				monitor.getChartTab().addDetection(number);
				// monitor.getChartTab().repaint();
				monitor.getTemAHumTab().addCollectItem(number);
				// monitor.getTemAHumTab().repaint();
//				monitor.getPicTab().addDetection(number);

				while ((string = reader.readLine()) != null) {
					System.out.println(string);
					GregorianCalendar now = new GregorianCalendar();
					switch (string.charAt(0)) {
					case 'S': {
						float tem = 0;
						float hum = 0;
						tem = Float.parseFloat(string.substring(4, 9)) / 100;
						hum = Float.parseFloat(string.substring(12)) / 100;

						TemAHumTab.collectItem.get(number).changeTemHum(tem, hum);
						MySQLHandle.getMySQLHandle().updateData(number,
								String.format("%04d%02d%02d%02d%02d00", now.get(GregorianCalendar.YEAR),
										now.get(GregorianCalendar.MONTH) + 1, now.get(GregorianCalendar.DAY_OF_MONTH),
										now.get(GregorianCalendar.HOUR_OF_DAY), now.get(GregorianCalendar.MINUTE)),
								(int) (tem * 100), (int) (hum * 100));
					}
						break;

					case 'P': {
						socket.getInputStream().read(monitor.getPicTab().getPicture(), 0,
								monitor.getPicTab().getPicture().length);
						
						String name = "/pictures/" + number + "/" + number + "_"
								+ String.format("%04d/%02d/%02d_%02d:%02d:00", now.get(GregorianCalendar.YEAR),
										now.get(GregorianCalendar.MONTH) + 1, now.get(GregorianCalendar.DAY_OF_MONTH),
										now.get(GregorianCalendar.HOUR_OF_DAY), now.get(GregorianCalendar.MINUTE));

						FileOutputStream fileOutputStream = new FileOutputStream(name + ".txt");
						fileOutputStream.write(monitor.getPicTab().getPicture());
						fileOutputStream.flush();
						fileOutputStream.close();
						
						TxtToBmp txtToBmp = new TxtToBmp(name + ".txt", name + ".bmp", 240, 320);
						monitor.getPicTab().getBmpName().put(number, name);
						monitor.getPicTab().repaintPic();
					}
						break;

					default:
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				monitor.getPicTab().removeDetection(number);
				TemAHumTab.collectItem.get(number).disConnected();
				monitor.getChartTab().removeDetection(number);
				client.remove(number);
			}
		}

	}
}
