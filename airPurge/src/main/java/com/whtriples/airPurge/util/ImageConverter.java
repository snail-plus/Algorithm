package com.whtriples.airPurge.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.io.FileUtils;

public class ImageConverter {

	private static final int WIDTH = 640;

	private static final int HEIGHT = 640;
	
	private static final double QUALITY = 0.6;
	
	private static final String FORMAT = "jpg";

//	public static ByteArrayOutputStream convertImage(InputStream is, boolean isTiff) throws Exception {
//		BufferedImage src = null;
//		if(!isTiff) {
//			src = ImageIO.read(is);
//		} else {
//		//use commons-imaging support tiff
//			src = Imaging.getBufferedImage(is);
//		}
//		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//
//		// 截取中间最大正方形
//		int srcW = src.getWidth();
//		int srcH = src.getHeight();
//		int side = Math.min(srcW, srcH);
//		int x1 = (srcW - side) / 2;
//		int y1 = (srcH - side) / 2;
//		int x2 = x1 + side;
//		int y2 = y1 + side;
//		BufferedImage square = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
//		Graphics g = square.getGraphics();
//		g.drawImage(src, 0, 0, side, side, x1, y1, x2, y2, null);
//
//		// 拉伸
//		Image resize = square.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//
//		// 背景透明
//		Graphics2D g2 = dest.createGraphics();
//		dest = g2.getDeviceConfiguration().createCompatibleImage(width, height);
//		g2.dispose();
//		g2 = dest.createGraphics();
//		
//		g2.setBackground(Color.WHITE);
//		g2.clearRect(0, 0, width, height);
//
//		// 截取圆形
//		g2.setClip(new Ellipse2D.Double(0, 0, width, height));
//
//		// 抗锯齿
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.drawImage(resize, 0, 0, null);
//		g2.dispose();
//
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageIO.write(dest, "jpg", os);
////		use commons-imaging 提升gif质量
//		//TODO use ImageMagick + PNG8 or GIF is better
////		Imaging.writeImage(dest, os, ImageFormats.JPEG, null);
//		return os;
//	}
	
	public static ByteArrayOutputStream convertImage(InputStream is, boolean isTiff) throws Exception {
		return convertImage(is, isTiff, 0, 0, true);
	}
	
	public static ByteArrayOutputStream convertImage(InputStream is, boolean isTiff, int width, int height, boolean square) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedImage src = null;
		if(!isTiff) {
			src = ImageIO.read(is);
		} else {
		//use commons-imaging support tiff
			src = Imaging.getBufferedImage(is);
		}
		if(square) {
			Thumbnails.of(src)
			.crop(Positions.CENTER)
			.size(width == 0 ? WIDTH : width, height == 0 ? HEIGHT : height)
			.outputFormat(FORMAT)
			.outputQuality(QUALITY)
			.toOutputStream(os);
		} else {
			Thumbnails.of(src)
			.size(width == 0 ? WIDTH : width, height == 0 ? HEIGHT : height)
			.outputFormat(FORMAT)
			.outputQuality(QUALITY)
			.toOutputStream(os);
		}
		
		return os;
	}

	public static void main(String[] args) throws IOException, Exception {
		FileUtils.writeByteArrayToFile(new File("D:/new.jpg"), convertImage(FileUtils.openInputStream(new File("D:/1.jpg")), false, 1280, 720, false).toByteArray());
	}

}