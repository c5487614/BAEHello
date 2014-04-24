package cch.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import cch.reminder.busi.FeixinUtil;
import cch.util.proxy.HB10086Util;

public class ImageUtil {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
//		for(int i =1;i<11;i++){
//			convert2Gray(i+".jpg");
//		}
//		String file  = "F:\\test\\26.jpg";
//		file = convert2Gray(file);
		//getPixelInfo();
		//List<String> list = cutPicture(file);
		//getInfo("cut.jpg");
//		for(String str : list){
//			String value = checkImg(str);
//			System.out.println(value);
//		}
//		HB10086Util hb = HB10086Util.getInstance();
//		File file = new File("E:/10086.txt");
//		java.io.InputStream inputstream = new java.io.FileInputStream(file);
//		java.io.InputStreamReader in = new java.io.InputStreamReader(inputstream);
//		java.io.BufferedReader sr = new java.io.BufferedReader(in);
//		StringBuffer sb = new StringBuffer();
//		String temp;
//		try {
//			while((temp = sr.readLine())!=null){
//				sb.append(temp);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(sb.toString());
//		String result = hb.getSAMLart(sb.toString());
//		System.out.println(result);
		
		FeixinUtil feixin = FeixinUtil.getInstance();
		
	}
	public static RGB getRGB(int rgb){
		RGB model = new RGB();
		String strRGB = Integer.toHexString(rgb);
		model.setR(Integer.parseInt(strRGB.substring(2,4),16));
		model.setG(Integer.parseInt(strRGB.substring(4,6),16));
		model.setB(Integer.parseInt(strRGB.substring(6,8),16));
		model.setRGB(Integer.parseInt(strRGB.substring(2,8),16));
		//System.out.println(strRGB);
		return model;
	}
	public static boolean isNoisy(BufferedImage image,int i,int j){
		int rgb = image.getRGB(i, j);
		RGB color = getRGB(rgb);
		if(color.isBigger(140)){
			return true;
		}else if(color.isNoisy()){
			return true;
		}else{
			if((i>1 && i<20 && j>1 && j<60)){
			}
		}
		return false;
	}
	public static void getPixelInfo(){
		try {
			BufferedImage bufferImage = ImageIO.read(new File("F:\\test\\11.jpg"));
			int imageWidth = bufferImage.getWidth();
			int imageHeight = bufferImage.getHeight();
			Map<Integer,Integer> map = new HashMap<Integer,Integer>(); 
			int i,j; 
			for(i=0;i<imageWidth;i++){
				for(j=0;j<imageHeight;j++){
					int rgb = bufferImage.getRGB(i, j);
					RGB color = getRGB(rgb);
					int avg = (color.getR()+color.getG()+color.getB())/3;
					if(map.containsKey(avg)){
						map.put(avg, map.get(avg) + 1);
					}else{
						map.put(avg, 1);
					}
				}
			}
			for( Integer c : map.keySet() ){
				System.out.println(c + "\t"+map.get(c));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String convert2Gray(String filePath){
		try {
			BufferedImage bufferImage = ImageIO.read(new File(filePath));
			int imageWidth = bufferImage.getWidth();
			int imageHeight = bufferImage.getHeight();
			int i,j;
			BufferedImage newBufferImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_BYTE_GRAY);  
			for(i=0;i<imageWidth;i++){
				for(j=0;j<imageHeight;j++){
					int rgb = bufferImage.getRGB(i, j);
//					RGB color = getRGB(rgb);
//					//Color c = new Color(0x00,0xFF, 0x00);
//					double grayColor = 0.299 * color.getR() + 0.587 * color.getG() + 0.114 * color.getB();
//					//grayColor = rgb;
//					if(isNoisy(bufferImage,i,j)){
//						newBufferImage.setRGB(i, j,0xFFFFFF);
//					}else{
//						newBufferImage.setRGB(i, j,0x000000);
//					}
					newBufferImage.setRGB(i, j,rgb);
				}
			}
			ImageIO.write(newBufferImage, "jpg", new File("F:\\test\\" + "new1.jpg"));
			
			bufferImage = ImageIO.read(new File("F:\\test\\" + "new1.jpg"));
			newBufferImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_BYTE_BINARY);  
			for(i=0;i<imageWidth;i++){
				for(j=0;j<imageHeight;j++){
					int rgb = bufferImage.getRGB(i, j);
					RGB color = getRGB(rgb);
					//newBufferImage.setRGB(i, j,rgb);
					if(color.isBigger(180)){
						newBufferImage.setRGB(i, j,0xFFFFFF);
					}else{
						newBufferImage.setRGB(i, j,0x000000);
					}
				}
			}
			ImageIO.write(newBufferImage, "jpg", new File("F:\\test\\" + "new3.jpg"));
			return "F:\\test\\new3.jpg";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	public static List<String> cutPicture(String filePath){
		int i=0,j=0;
		try {
			BufferedImage bufferImage = ImageIO.read(new File(filePath));
			int imageWidth = bufferImage.getWidth();
			int imageHeight = bufferImage.getHeight();
			int[] array = new int[imageWidth];
			for(i=0;i<imageWidth;i++){
				int iCount = 0;
				for(j=0;j<imageHeight;j++){
					int pixel = bufferImage.getRGB(i, j);
					RGB rgb = getRGB(pixel);
					if(rgb.isBigger(150)){
						continue;
					}else{
						iCount++;
					}
				}
				array[i] = iCount;
			}
//			for(i=0;i<imageWidth;i++){
//				System.out.println(i+": " + array[i]);
//			}
			int[] arrayIndex = new int[8];
			int iStep = 0;
			for(i=0,j=0;i<imageWidth-1;i++){
				if(array[i]==0){
					if(i==0 && array[i+1]==0 ){
						continue;
					}else if(i==0 && array[i]<array[1+1]){
						arrayIndex[j++] = i;
						iStep = 0;
					}else if(i!=0 && array[i+1]==0 && iStep<10){
						iStep++;
						continue;
					}else if(i!=0 && array[i-1]!=0 && iStep>10){
						arrayIndex[j++] = i;
					}else if(i!=0 && array[i+1]==0 && iStep>=10 && j<7){
						if(j%2==1){
							arrayIndex[j++] = i;
						}
						iStep = 0;
					}else if(array[i+1]!=0 && j<7){
						if(j%2==0){
							arrayIndex[j++] = i;
						}
						iStep = 0;
					}else{
						
					}
				}else {
					if(i==0){
						arrayIndex[j++] = i;
						iStep = 0;
					}else if(array[i]<=array[i-1] && array[i]<=array[i+1] && iStep>=10 && j<7){
						arrayIndex[j++] = i;
						iStep = 0;
						if(j%2==0){
							arrayIndex[j++] = i;
						}
					}
						
				}
				iStep++;
			}
//			for(i=0;i<8;i++){
//				System.out.println(i+": " + arrayIndex[i]);
//			}
			List<String> list = new ArrayList<String>();
			for(int n=0;n<8;n+=2){
				int width = arrayIndex[n+1]-arrayIndex[n]+1;
				int height = imageHeight;
				//System.out.println(width+"," + height);
				BufferedImage bufferImageCut = new BufferedImage(width-1,height,BufferedImage.TYPE_BYTE_BINARY);
				for(i=arrayIndex[n];i<arrayIndex[n+1];i++){
					for(j=0;j<imageHeight;j++){
						int pixel = bufferImage.getRGB(i, j);
						bufferImageCut.setRGB(i-arrayIndex[n], j, pixel);
					}
				}
				ImageIO.write(bufferImageCut, "jpg", new File(filePath+n+".jpg"));
				list.add(filePath+n+".jpg");
			}
			for(String file : list){
				bufferImage = ImageIO.read(new File(file));
				int width = bufferImage.getWidth();
				int height = bufferImage.getHeight();
				int maxHeight = 0,minHeight = height;
				for(i=0;i<width;i++){
					for(j=0;j<height;j++){
						int pixel = bufferImage.getRGB(i, j);
						RGB rgb = getRGB(pixel);
						if(!rgb.isBigger(150)){
							if(minHeight>i){
								minHeight = i;
							}
						}
					}
					for(j=height-1;j>=0;j--){
						int pixel = bufferImage.getRGB(i, j);
						RGB rgb = getRGB(pixel);
						if(!rgb.isBigger(150)){
							if(maxHeight<j){
								maxHeight = j;
							}
						}
					}
				}
				BufferedImage bufferImageCut = new BufferedImage(width,maxHeight-minHeight,BufferedImage.TYPE_BYTE_BINARY);
				for(i=0;i<width;i++){
					for(j=minHeight;j<maxHeight;j++){
						int pixel = bufferImage.getRGB(i, j);
						bufferImageCut.setRGB(i, j-minHeight, pixel);
					}
				}
				ImageIO.write(bufferImageCut, "jpg", new File(file));
			}
			
			
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			System.out.println(i+"," + j);
		}
		return null;
	}
	
	public static String getInfo(String filePath){
		try {
			BufferedImage bufferImage = ImageIO.read(new File(filePath));
			int imageWidth = bufferImage.getWidth();
			int imageHeight = bufferImage.getHeight();
			int i,j;
			String sample = "";
			for(i=0;i<imageWidth;i++){
				for(j=0;j<imageHeight;j++){
					int pixel = bufferImage.getRGB(i, j);
					RGB rgb = getRGB(pixel);
					if(!rgb.isBigger(180)){
						sample += "0";
					}else{
						sample += "1";
					}
				}
			}
			System.out.println(filePath);
			System.out.println(sample);
			return sample;
//			String sample2 = "11110011111111100000000111111001110001001101111111110001011110111111010111101111111101111011111111001110011111101011110011111010011110001111100001110011110000001110110100000001111001001110001111010011100011111100111100011111";
//			int width = sample.length();
//			if(sample.length()>sample2.length()){
//				width = sample2.length();
//			}
//			int count = 0;
//			for(i=0;i<width;i++){
//				if(sample.charAt(i)==sample2.charAt(i)){
//					count++;
//				}
//			}
//			System.out.println(count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String checkImg(String filePath){
		Map<String,String> map = new HashMap<String,String>();
		map.put("6", "1111111111111111111111111100001010111111111110000010100011111111101111101000001111111011110111110011111110111101111110011111101111011111110111111001111111111101111110001110111111011111111111100000000111111111111101000001");
		map.put("8","1111111111111111111111110000110000011111111000000000000011111110000000000000011111001111000111000111110011110011111001111100111100111100011111000110001110100111111000000001010001111110000000000000111111110000110000001111");
		map.put("9", "1111111111000010111111111000000011101111111100000001110001111110011110001100011111101111100011000111111011111010110101111110111111101101011111100111010011000111111100010001110011111111100000000001111111101100000000011111");
		map.put("A", "1111111111111100000111111111111101000001111111000001110111011111100011111101111111111000011111011111111111100001111111111111111110001101111111111111111011011111111111111111110011011111111111111100000111111100001111110001");
		map.put("U", "110011111011100111111100000000000011111111000000000000001111110000000000000011111100111111001100011111101111110011100111111111111100111001111111111111001110011111111111110011100111110011111101111001111100111110101100111111000000000000011111110000000000011111111100111110110111111111101111101101111111");
		map.put("S", "111111111011011111111111000010110000111111100000001100000111111000000011100001111100111000011010011111001111000110100111110011110001101001111100111100001100011111000111100011000111111000111000000011111110000111000000111111111111111000011111111111111111011111111111111111110110111111111111111111101111");
		
		String sampple = getInfo(filePath);
		int i = 0;
		Map<String,Integer> resultMap = new HashMap<String,Integer>();
		for(String key : map.keySet()){
			String value = (String) map.get(key);
			int width = sampple.length();
			if(sampple.length()>value.length()){
				width = value.length();
			}
			int count = 0;
			for(i=0;i<width;i++){
				if(sampple.charAt(i)==value.charAt(i)){
					count++;
				}
			}
			resultMap.put(key, count);
		}
		int count = 0;
		String value = "";
		for(String key : resultMap.keySet()){
			//System.out.println(key+"," + resultMap.get(key));
			int tempCount = resultMap.get(key);
			if(tempCount>count){
				count = tempCount;
				value =  key;
			}
		}
		return value;
	}
}
