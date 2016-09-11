package com.njit.monitoringsystem.display;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TxtToBmp {
	private File txtfile, bmpfile;
	private DataOutputStream dos;
	private FileOutputStream outputStream;
	private DataInputStream dis;
	private FileInputStream inputStream;

	private short read_data;
	private int file_size;
	private byte[] ReadBuf = new byte[2];
	private byte[] WriteBuf = new byte[3];
	private byte[] header = { 0x42, 0x4d, 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0,
			40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 24, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public TxtToBmp(String txtName, String bmpName, int height, int width)
			throws IOException {
		txtfile = new File(txtName);
		bmpfile = new File(bmpName);
		/* 宽*高 +补充的字节 + 头部信息 */
		file_size = (width * height * 3 + height * (width % 4) + 54);
		/* 文件大小 4个字节 */
		header[2] = (byte) (file_size & 0x000000ff);
		header[3] = (byte) ((file_size >> 8) & 0x000000ff);
		header[4] = (byte) ((file_size >> 16) & 0x000000ff);
		header[5] = (byte) ((file_size >> 24) & 0x000000ff);

		/* 位图宽 4个字节 */
		header[18] = (byte) (width & 0x000000ff);
		header[19] = (byte) ((width >> 8) & 0x000000ff);
		header[20] = (byte) ((width >> 16) & 0x000000ff);
		header[21] = (byte) ((width >> 24) & 0x000000ff);

		/* 位图高 4个字节 */
		header[22] = (byte) (height & 0x000000ff);
		header[23] = (byte) ((height >> 8) & 0x000000ff);
		header[24] = (byte) ((height >> 16) & 0x000000ff);
		header[25] = (byte) ((height >> 24) & 0x000000ff);
		if (!txtfile.exists()) {
			txtfile.createNewFile();
		}
		if (!bmpfile.exists()) {
			bmpfile.createNewFile();
		}
		outputStream = new FileOutputStream(bmpfile);
		dos = new DataOutputStream(outputStream);
		inputStream = new FileInputStream(txtfile);
		dis = new DataInputStream(inputStream);
		dos.write(header, 0, 54);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				dis.read(ReadBuf, 0, 2);
				read_data = 0;
				read_data = (short) (read_data | ((short)(ReadBuf[0]) & 0x00ff));
				read_data = (short) (read_data << 8);
				read_data = (short) (read_data | ((short)(ReadBuf[1]) & 0x00ff));
				WriteBuf[2] = (byte) ((read_data >>> 11) << 3);
				WriteBuf[1] = (byte) (((read_data & 0x7ff) >>> 5) << 2);
				WriteBuf[0] = (byte) (((read_data & 0x1f) << 3));
				dos.write(WriteBuf, 0, 3);
			}
		}
		outputStream.close();
		dos.close();
		inputStream.close();
		dis.close();
	}
}
