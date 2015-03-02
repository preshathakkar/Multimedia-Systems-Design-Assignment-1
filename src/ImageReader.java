
import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class ImageReader {

  
   public static void main(String[] args) 
   {
   	
	//System.out.println(System.getProperty("user.dir"));
	String fileName = args[0];
   	int width = 352;
	int height = 288;
       int subY = Integer.parseInt(args[1]);
       int subU = Integer.parseInt(args[2]);
       int subV = Integer.parseInt(args[3]);
       
       int quant = Integer.parseInt(args[4]);
       
       int index = height * width;
       double[] y = new double[index+1];
       double[] u = new double[index+1];
       double[] v = new double[index+1];
       
       double[] yNew = new double[index+1];
       double[] uNew = new double[index+1];
       double[] vNew = new double[index+1];
       
       int[] pixFinal = new int[index];
       
       int sub, h, w, ind, ind2, bits, i, fact, val;
	
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       
    int[] quantval;
    
    
    try {
	    File file = new File(args[0]);
	    InputStream is = new FileInputStream(file);
	    
	 
	    
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    
	    int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
//    	for(i=1; Math.pow(2,i)<quant;){
//    		i+=1;
//    	}	
//    	
//    	
//    	
//        bits = i;
//        
//        
//        val = (int) Math.pow(2, i);
//        
//        if(quant==1){
//        	bits=0;	
//        	val = 1;	
//        	}
        
        val = quant;
        
        quantval = new int[val];
        
        fact = 256 / val;
        
        //quantval[0]=0;
        
        quantval[0]=0;
        if(val > 1){
        for(i=1;i<val;i++){
        	quantval[i]=(fact * (i))-1;
        }}
        //quantval[val]=255;
            
		for(h = 0, ind = 0; h < height; h++){
	
			for(w = 0; w < width; w++){
		 
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                //System.out.println("r = " + r + "\tg = " + g+"\tb = "+b+"\n");
                
				img.setRGB(w,h,pix);
				ind++;
			}
		}
        
		
        for(h=0, ind = 0; h<height; h++){
            for(w=0; w<width; w++){
                byte a = 0;
                byte r = bytes[ind];
                byte g = bytes[ind+height*width];
                byte b = bytes[ind+height*width*2];
                
                

                double y1 = 0.299f * (r&0xff) + 0.587f * (g&0xff) + 0.114f * (b&0xff);
                double u1 = -0.147f * (r&0xff) - 0.289f * (g&0xff) + 0.436f * (b&0xff);
                double v1 = 0.615f * (r&0xff) - 0.515f * (g&0xff) - 0.100f * (b&0xff);
                
                y[ind]=y1;
                u[ind]=u1;
                v[ind]=v1;
                
                ind++;
            }
        }
        
       //System.out.println("Index is : "+index+"\n");
		
        for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subY == 0){
                		yNew[ind]=y[ind];
            		}
			ind++;
        	}
        }
   
            for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subY != 0){
            
                    		for(sub=subY-1;sub>0 && ind<index-1;sub--,ind++){
                    			int end = ind +sub;
                    			if(end >= index) end=index-1;
                        			yNew[ind]=(yNew[ind-1]+yNew[end])/2;
					ind++;
	                    	}
                	}
		ind++;
            	}
            }
   

        for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subU == 0){
                		uNew[ind]=u[ind];
            		}
			ind++;
        	}
        }
   
            for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subU != 0){
            
                    		for(sub=subU-1;sub>0 && ind<index-1;sub--,ind++){
                    			int end = ind +sub;
                    			if(end >= index) end=index-1;
                        			uNew[ind]=(uNew[ind-1]+uNew[end])/2;
					ind++;
	                    	}
                	}
		ind++;
            	}
            }
        
        
        
        for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subV == 0){
                		vNew[ind]=v[ind];
            		}
			ind++;
        	}
        }
   
            for(h = 0, ind = 0; h < height; h++){
		for(w = 0; w < width; w++){
	    		if(width%subV != 0){
            
                    		for(sub=subV-1;sub>0 && ind<index-1;sub--,ind++){
                    			int end = ind +sub;
                    			if(end >= index) end=index-1;
                        			vNew[ind]=(vNew[ind-1]+vNew[end])/2;
					ind++;
	                    	}
                	}
		ind++;
            	}
            }
        
        
/**        for(ind=0; ind<index-1;ind++){
            if(ind%subU == 0){
                uNew[ind]=u[ind];
            }
        }
        
        
            for(ind=0; ind<index-1;ind++){
                
                if(ind%subU != 0){
                    for(sub=subU-1;sub>0 && ind<index-1;sub--,ind++){
                    	int end = ind +sub;
                    	if(end >= index) end=index-1;
                    	uNew[ind]=(uNew[ind-1]+uNew[end])/2;
                        
                    }
                    
                }
            }
            
     
        
        
        
        for(ind=0; ind<index-1;ind++){
            if(ind%subV == 0){
                vNew[ind]=v[ind];
            }
        }
        
       
            for(ind=0; ind<index-1;ind++){
                
                if(ind%subV !=0){
                    for(sub=subV-1;sub>0 && ind<index-1;sub--,ind++){
                    	int end = ind +sub;
                    	if(end >= index) end=index-1;
                        vNew[ind]=(vNew[ind-1]+vNew[end])/2;
                    }
                    
                }
            }
            
    
**/        
        
            /**for(ind=1; ind<index-1;ind+=2){
                
                
                y[ind] = (y[ind-1] + y[ind+1])/2;
            }
        
        
        for(sub=1;sub<subU;sub++){
            for(ind=1; ind<index-1;ind+=2){
                u[ind] = (u[ind-1] + u[ind+1])/2;
            }
        }
        for(sub=1;sub<subV;sub++){
            for(ind=1; ind<index-1;ind+=2){
                v[ind] = (v[ind-1] + v[ind+1])/2;
            }
        }**/
        
        
        for(h=0,ind = 0; h<height; h++){
            for(w=0; w<width; w++){
                double r1 = 0.999f * yNew[ind] + 1.140f * vNew[ind];
                double g1 = 1.000f * yNew[ind] - 0.395f * uNew[ind] - 0.581f * vNew[ind];
                double b1 = 1.000f * yNew[ind] + 2.032f * uNew[ind];
                
                if(r1 > 255.0f)
                	{r1 = 255.0f;}
                if(r1 < 0.0f)
                	{r1 = 0.0f;}
                
                if(g1 > 255.0f)
                	{g1 = 255.0f;}
                if(g1 < 0.0f)
                	{g1 = 0.0f;}
                
                if(b1 > 255f)
                	{b1 = 255.0f;}
                if(b1 < 0.0f)
                	{b1 = 0.0f;}
        
        //System.out.println("r1 = " + r1 + "\tg1 = " + g1+"\tb1 = "+b1);
        
        
                int r2 = (int)Math.round(r1);
                int g2 = (int)Math.round(g1);
                int b2 = (int)Math.round(b1);
        
        //System.out.println("\tr2 = " + r2 + "\tg2 = " + g2+"\tb2 = "+b2+"\n");
               
               if(val > 1){ 
               for(i=1;i<val;i++){
            	   if(r2<quantval[i] && r2>quantval[i-1]){
            		   if((r2-quantval[i-1]) < (quantval[i]-r2)){
            			   r2 = quantval[i-1];
            		   }
            		   else {
            			   r2 = quantval[i];
            		   }
            	   }
            	   
            	   if(g2<quantval[i] && g2>quantval[i-1]){
            		   if((g2-quantval[i-1]) < (quantval[i]-g2)){
            			   g2 = quantval[i-1];
            		   }
            		   else {
            			   g2 = quantval[i];
            		   }
            	   }
            	   
            	   if(b2<quantval[i] && b2>quantval[i-1]){
            		   if((b2-quantval[i-1]) < (quantval[i]-b2)){
            			   b2 = quantval[i-1];
            		   }
            		   else {
            			   b2 = quantval[i];
            		   }
            	   }
            	   
               }}
                
               else {r2 = 0; g2=0; b2=0;}
               
                //System.out.println("r : "+r2+"\tg : "+g2+"\tb : "+b2);
                int pix = 0xff000000 | ((r2 & 0xff) << 16) | ((g2 & 0xff) << 8) | (b2 & 0xff);
                
                
                img2.setRGB(w,h,pix);
                ind++;
                
            }
        }
        
        
        
		
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // Use a panel and label to display the image
    JPanel  panel = new JPanel ();
    panel.add (new JLabel (new ImageIcon (img)));
    panel.add (new JLabel (new ImageIcon (img2)));
    
    JFrame frame = new JFrame("Display images");
    
    frame.getContentPane().add (panel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   

   }
  
}