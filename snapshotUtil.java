package snapshot;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;


public class snapshotUtil {
	
	  public static String dir ="";
	  
	  public static void main(String[] args) throws IOException, AWTException{
		  
		 
		    int cacheTime = Integer.parseInt(propertiesFactory.getInstance().getConfig("time"));
		  
		    Timer timer = new Timer();
		  
		    timer.schedule(new TimerTask() {
		      public void run() {
		    	  try {
		    		System.out.println("Task is run ");  
					snapshotUtil.snapshot();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		    }, 1000,cacheTime);
		  
	  }
	
	public static void snapshot() throws IOException, AWTException {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice[] gs = ge.getScreenDevices();
    	int i = 0;
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH：mm：ss");//设置日期格式
    	
    	SimpleDateFormat dfdir = new SimpleDateFormat("yyyyMMdd");//设置日期格式
    	
    	SimpleDateFormat dfdirH = new SimpleDateFormat("HH");//设置日期格式
    	
    	//String dir = "F:\\\\snapshotd\\\\";
    	
        String dir = propertiesFactory.getInstance().getConfig("path");
        
        dir += "/snapshot/";
        
    	if(dir.equals("")) {
    		dir = "./snapshot/";
    	}
        
    	
    	String path = dir+dfdir.format(new Date())+"/"+dfdirH.format(new Date());
    	if(!new File(path).exists())   {
    	    new File(path).mkdirs();
    	}
    	
    	String RPATH = "";
    	
    	RPATH = path+"/snapshot-"+df.format(new Date())+".png";
    	
    	for(GraphicsDevice curGs : gs){
            i++;
    		GraphicsConfiguration[] gc = curGs.getConfigurations();
    		Robot robot = null;
    		for(GraphicsConfiguration curGc : gc)
    		{
    			Rectangle bounds = curGc.getBounds();
    			ColorModel cm = curGc.getColorModel();
    			robot= new Robot();  
  	            BufferedImage img=robot.createScreenCapture(bounds);  
  	            if(img!=null&&img.getWidth()>1){  
  	              File imgfile = new File(RPATH);  
  	            	 if (!imgfile.exists()) {
  	                  //  BufferedImage imageFirsts = ImageIO.read(imgfile);
  	                   ImageIO.write(img, "png", imgfile);// 写图片  
  	            	 }else {
  	            		 BufferedImage image = ImageIO.read(imgfile);
  	            		 ImageIO.write(xPic(image , img), "png", imgfile);
  	            	 }
  	               // ImageIO.write(img, "png", new File(path+"/snapshot["+i+"]-"+df.format(new Date())+".png"));
  	            }  
    			System.out.println("" + bounds.getX() + "," + bounds.getY() + " " + bounds.getWidth() + "x" + bounds.getHeight() + " " + cm);
    		}
    	}
		
	}
	
	
	   
    public static BufferedImage xPic(BufferedImage imageFirst ,BufferedImage imageSecond){//横向处理图片  
        try {  
            
            int width = imageFirst.getWidth();// 图片宽度  
            int height = imageFirst.getHeight();// 图片高度  
            int[] imageArrayFirst = new int[width * height];// 从图片中读取RGB  
            imageArrayFirst = imageFirst.getRGB(0, 0, width, height, imageArrayFirst, 0, width);  
  
            int width2 = imageSecond.getWidth();// 图片宽度  
            int height2 = imageSecond.getHeight();// 图片高度  
            
            int[] imageArraySecond = new int[width2 * height2];  
            imageArraySecond = imageSecond.getRGB(0, 0, width2, height2, imageArraySecond, 0, width2);  
            
            // 生成新图片   
            BufferedImage imageResult = new BufferedImage(width + imageSecond.getWidth() , height,BufferedImage.TYPE_INT_RGB); 
            
            imageResult.setRGB(0, 0, width, height, imageArrayFirst, 0, width);// 设置左半部分的RGB  
            
            imageResult.setRGB(width, 0, width2, height2, imageArraySecond, 0, width2);// 设置右半部分的RGB  
            
            return imageResult;
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return imageSecond;  
    }  
}
