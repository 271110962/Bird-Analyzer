package application;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.SortedSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class BirdController {

	@FXML
	private Image image;
	@FXML
	private ImageView imageView;
	@FXML
	private TextArea textarea;
	@FXML
	private Pane mypane;
	@FXML
	private  HashSet<Integer> ss,ss2;
	@FXML
	private Label bbbb, brightening;
	@FXML
	private Slider slider;
	@FXML
	private Text t,tt, ba;
	UnionFind uf;
	public int [] average;
	public boolean Flag1=false,Flag2=false,Flag3=false,Flag4 =false;
    private int averageNumber;
	@FXML
	private Rectangle r;
	@FXML
	
	public void initialize() {
	    
		slider.setMin(1); //set Minimum value of Slider
	    slider.setMax(5); //set Maximum value of Slider
	    averageNumber = 1;
       
	    //Listenr of the Slider which use to change average Number.
	    slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null) {
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(0);
				averageNumber = Integer.parseInt(df.format(newValue));
				System.out.print(averageNumber);
						brightening.setText(String.valueOf(df.format(newValue)));
		
						ss2.removeAll(ss2);
						 NoiseAndOutlier();
				}
			}
		});
	    
		bbbb.setVisible(false);
		System.out.print(mypane.getChildren().size());
	}
	
	
	/*
	 * this method is for opening the file browser.
	 */
	@FXML
    private void browseButton(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\as083\\Desktop"));
        fc.getExtensionFilters().addAll(new ExtensionFilter("JPG Files", "*.jpg"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
           // textarea.setText(selectedFile.getAbsolutePath());
           
            image = new Image(selectedFile.toURI().toString(),512,512,true,false);
            

            imageView.setImage(image);
            
            
			//imageView.setFitHeight(h);
        }
    }
	
	
    /*
     * This method is using for changing the image to black and white, and then put the different pixels into union-find,and they will be a union set.
     */
	@FXML
	private void Counting(ActionEvent event) {
		uf = new UnionFind((int)(image.getWidth()*image.getHeight()));

		PixelReader pixelReader = image.getPixelReader();
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage wbImage = new WritableImage(width,height);
		PixelWriter pixelWriter = wbImage.getPixelWriter();
		
		
		//change to black and white		
		for(int x = 0;x < width ;x++) {
			for(int y =0;y<height ; y++) {
				int pixel = pixelReader.getArgb(x, y);
	
				int alpha = ((pixel >> 24) & 0xff);
				int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff);
				int blue = (pixel & 0xff);
				int rbg = (red + green + blue) / 3;
				
				if(rbg > 127) {
					pixel = (alpha<<24)|(255<<16)|(255<<8)|255;
				}
				
				else if (rbg <=127) {
					pixel = (alpha<<24)|(0<<16)|(0<<8)|0;
				}

				pixelWriter.setArgb(x, y, pixel);
			}
		}
		//every white pixels will become -1.
		pixelReader = wbImage.getPixelReader();
	    WritableImage wbI = new WritableImage(width,height);
	    pixelWriter = wbI.getPixelWriter();
	    for (int x = 0; x < width; x++) {
	    	for (int y = 0; y < height; y++) {
	    		int pixel = pixelReader.getArgb(x, y);
	    		int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff);
				int blue = (pixel & 0xff);
				
				if(red == 255 && green == 255 && blue == 255) {
				
					
					uf.id[y*width+x]=-1;
				}
				else if(red == 0 && green == 0 && blue == 0) {

				}
				
	    	}
	    }
	    
	 //pixel which link together will be set as a union set by union-find.
     
     for(int x = 0; x < width - 1; x++) {
    	 for(int y = 0; y < height -1; y++) {
    		 if(uf.id[y*width+x] != -1) {
    			 if(x < width - 1 && uf.id[y*width+x+1]!= -1) {
    				 uf.union(y*width+x , y*width+x+1);
    			//	 System.out.println("Union H");
    			 }
    			 if(y < height - 1 && uf.id[(y+1)*width+x]!= -1) {
    				 uf.union(y*width+x, (y+1)*width+x);
    			//	 System.out.println("Union V");
    			 }
    		 }
    		 
    	 }
     }
     
     ss=new HashSet<>();//creating a hashset to store the pixels which not equals to -1
     
     for(int i=0;i<uf.id.length;i++)
		{
				//pixels[x][y]=p;
			
				//p++;
		System.out.print(uf.find(i)+" "); //pixels[x][y] + " ");
			
			if(i%width==0) System.out.print("\n");
			if(uf.find(i) != -1) {
			ss.add(uf.find(i));
			
			
			
			
			}
		}

     System.out.print("\nCount "+ss.size());//For the Hashset,we can know how many different pixels have.
     //System.out.print("\nCount "+uf.count());
     System.out.print(ss);


		
//-----------------------------------------     
     
     //buliding rectangular for every bird.
    imageView.setImage(image);
	 
	 
	 
     for(int bid : ss) {

    	 double top=image.getHeight(),left=image.getWidth(),bottom=0,right=0;
    	 for(int i=0;i<uf.id.length;i++)
 		{
    		
    		 
    		 
    		 int y=i/width; //row
    		 int x=i%width; //col
 			
    		 
    		 
    		  
 			if(uf.find(i) ==bid) {
 				if(x<left) left=x;
 				if(x>right) right=x;
 				if(y<top) top=y;
 				if(y>bottom) bottom=y;    
 			}
	 
 		}
    	 
    
    	
    	 
    	  r=new Rectangle(left,top,right-left,bottom-top);
         String amount = Integer.toString(ss.size());
         
         
    	// System.out.println("XX Bird Id: "+bid+", Bounds: "+left+", "+top+", "+bottom+", "+right);
    	  //mypane is a Pane object containing the imageview
    	 bbbb.setText("Bird Amount :" + amount);
    	 bbbb.setVisible(true);
    	 r.setStroke(Color.RED);
    	 r.setFill(Color.TRANSPARENT);
    	 mypane.getChildren().add(r);

    	
    //	 System.out.println("Bird Id: "+bid+", Bounds: "+r.getX()+", "+r.getY()+", "+r.getWidth()+", "+r.getHeight());
    	 
    	
        
     }

}
	/*
	 * This method is for counting number for each bird,and show them on the pane.

	 */
   
	@FXML
	private void birdNumber(ActionEvent e) {


		int width = (int) image.getWidth();
	    int k=1;

	     for(int bid : ss) {
	    	
	    	 double top=image.getHeight(),left=image.getWidth(),bottom=0,right=0;
	    	 for(int i=0;i<uf.id.length;i++)
	 		{
	    		 int y=i/width; //row
	    		 int x=i%width; //col
	    		 
	 			if(uf.find(i) ==bid) {
	 				if(x<left) left=x;
	 				if(x>right) right=x;
	 				if(y<top) top=y;
	 				if(y>bottom) bottom=y;	 			    
	 			}	 			
	 		}	    	 
	    	t = new Text(right+2,top, String.valueOf(k));
		    	 k++;
		    	 mypane.getChildren().add(t);
	     }
	}


	/*
	 * This method is for counting number for each bird,and show them on the pane.(after the noise and outlier)

	 */@FXML
	private void birdNumberNew(ActionEvent e) {


		int width = (int) image.getWidth();
	    int k=1;

	     for(int bid : ss2) {
	    	
	    	 double top=image.getHeight(),left=image.getWidth(),bottom=0,right=0;
	    	 for(int i=0;i<uf.id.length;i++)
	 		{
	    		 int y=i/width; //row
	    		 int x=i%width; //col
	    		 
	 			if(uf.find(i) ==bid) {
	 				if(x<left) left=x;
	 				if(x>right) right=x;
	 				if(y<top) top=y;
	 				if(y>bottom) bottom=y;	 			    
	 			}	 			
	 		}	    	 
	    	t = new Text(right+2,top, String.valueOf(k));
		    	 k++;
		    	 mypane.getChildren().add(t);
	     }
	}

	/*
	 * This method is for analysis the birds more accurate,which is countting average pixels and the pixels set smaller than the average/4, will be not count to the hashset.
	 */
	@FXML
	private void NoiseAndOutlier(ActionEvent e) 
	{   
		
		imageView.toFront();
		
		imageView.setImage(image);
		int width = (int) image.getWidth();
	    ss2 = new HashSet<>();
	    average = new int[ss.size()];
		 int c = 0;
		 int aver = 0;
		 
		 
		 for(int bid : ss)
		 {
			 average[c]=0;
			 for(int i=0;i<uf.id.length;i++)
		 		{
			 if(uf.find(i) == bid)
			 {
				 average[c]++;
				 
			 }
		 		}
			 c++;
		 }
		 
	     
	     for(int z =0; z<average.length;z++)
	    	 aver+=average[z];
	     
	     aver= aver/ss.size();
	     System.out.println("Aver="+aver);
	     
	     int z = 0;
		 for(int bid2:ss) {
	    	 if(average[z]<aver/averageNumber) {
	    		 
	    	 }
	    		 else
	    		 {
	    			 ss2.add(bid2);
	    		 }
	    	 
	    	 z++;
	     }
		 
		 System.out.println("new set:"+ ss2.size());
		 System.out.println("new Set:"+ss2);
		 
		 
		 
		 
	     for(int bid : ss2) {

	    	 double top=image.getHeight(),left=image.getWidth(),bottom=0,right=0;
	    	 for(int i=0;i<uf.id.length;i++)
	 		{
	    		
	    		 
	    		 
	    		 int y=i/width; //row
	    		 int x=i%width; //col
	 			
	    		 
	    		 
	    		  
	 			if(uf.find(i) ==bid) {
	 				if(x<left) left=x;
	 				if(x>right) right=x;
	 				if(y<top) top=y;
	 				if(y>bottom) bottom=y;    
	 			}
		 
	 		}

	    	 Rectangle r=new Rectangle(left,top,right-left,bottom-top);
	         String amount = Integer.toString(ss2.size());
	         
	         
	    	// System.out.println("XX Bird Id: "+bid+", Bounds: "+left+", "+top+", "+bottom+", "+right);
	    	  //mypane is a Pane object containing the imageview
	    	 bbbb.setText("Bird Amount :" + amount);
	    	 bbbb.setVisible(true);
	    	 r.setStroke(Color.RED);
	    	 r.setFill(Color.TRANSPARENT);
	    	 mypane.getChildren().add(r);
	         System.out.println("Bird Id: "+bid+", Bounds: "+r.getX()+", "+r.getY()+", "+r.getWidth()+", "+r.getHeight());
	    	 
	    	
	        
	     }
	     
	}
	
	
	
	
	private void NoiseAndOutlier() 
	{   
		
		imageView.toFront();
		
		imageView.setImage(image);
		int width = (int) image.getWidth();
	    ss2 = new HashSet<>();
	    average = new int[ss.size()];
		 int c = 0;
		 int aver = 0;
		 
		 
		 for(int bid : ss)
		 {
			 average[c]=0;
			 for(int i=0;i<uf.id.length;i++)
		 		{
			 if(uf.find(i) == bid)
			 {
				 average[c]++;
				 
			 }
		 		}
			 c++;
		 }
		 
	     
	     for(int z =0; z<average.length;z++)
	    	 aver+=average[z];
	     
	     aver= aver/ss.size();
	     System.out.println("Aver="+aver);
	     
	     int z = 0;
		 for(int bid2:ss) {
	    	 if(average[z]<aver/averageNumber) {
	    		 
	    	 }
	    		 else
	    		 {
	    			 ss2.add(bid2);
	    		 }
	    	 
	    	 z++;
	     }
		 
		 System.out.println("new set:"+ ss2.size());
		 System.out.println("new Set:"+ss2);
		 
		 
		 
		 
	     for(int bid : ss2) {

	    	 double top=image.getHeight(),left=image.getWidth(),bottom=0,right=0;
	    	 for(int i=0;i<uf.id.length;i++)
	 		{
	    		
	    		 
	    		 
	    		 int y=i/width; //row
	    		 int x=i%width; //col
	 			
	    		 
	    		 
	    		  
	 			if(uf.find(i) ==bid) {
	 				if(x<left) left=x;
	 				if(x>right) right=x;
	 				if(y<top) top=y;
	 				if(y>bottom) bottom=y;    
	 			}
		 
	 		}

	    	 Rectangle r=new Rectangle(left,top,right-left,bottom-top);
	         String amount = Integer.toString(ss2.size());
	         
	         
	    	// System.out.println("XX Bird Id: "+bid+", Bounds: "+left+", "+top+", "+bottom+", "+right);
	    	  //mypane is a Pane object containing the imageview
	    	 bbbb.setText("Bird Amount :" + amount);
	    	 bbbb.setVisible(true);
	    	 r.setStroke(Color.RED);
	    	 r.setFill(Color.TRANSPARENT);
	    	 mypane.getChildren().add(r);
	         System.out.println("Bird Id: "+bid+", Bounds: "+r.getX()+", "+r.getY()+", "+r.getWidth()+", "+r.getHeight());
	    	 
	     }	
	        
	     }
	     
	
	
	
	
	
	
	
	/*
	 * this method is for removing the rectangular
	 */
	@FXML
	private void removeBox(ActionEvent e)
	{
		imageView.toFront();
//		mypane.getChildren().remove(0, mypane.getChildren().size()-1);
		
		
    /*
     * this method is for analysis the shape of the bird.
     */
	}
	@FXML
	private void advancedAnalysis(ActionEvent e)
	{
		
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		
	     double top=image.getHeight(),top2 = image.getHeight(),left=image.getWidth(),left2=image.getWidth(),bottom=0,bottom2=0,right=0;
	     for(int bid : ss2) {
	    	 
	    	 for(int i=0;i<uf.id.length;i++)
	  		{
	     		 int y=i/width; //row
	     		 int x=i%width; //col
	     		 
	     		 if(uf.find(i)==bid) {
	     			if(x<left) 
	     				{ 
	     				left=x;
	     				top2 = y;
	     				
	     				}
	 				if(x>right) {
	 					right=x;
	 					bottom2 = y;
	 				}
	 				if(y<top) top=y;
	 				if(y>bottom) 
	 					{
	 					bottom=y; 
	                    left2=x; 
	 					}
	     		 }
	  		}
	     }
	     
	        System.out.println("left:"+left+"right:"+right+"top:"+top+"bottom:"+bottom+"top2:"+top2+"bottom2:"+bottom2);
	        int shape =0;
	        
	        for(int row = 0; row< height-1;row++) {
	        	Line l = new Line(right,row,right,row);
	        	mypane.getChildren().add(l);
	        	if(uf.id[(int) (row*width+right)] !=-1) {
	        		
	        		shape++;
	        	}
	       
	        }
	        if(shape == 1) {
        		Flag1 = true;
        	}
	        System.out.print("hahaha::::" +Integer.toString(shape));
	        if(Flag1==true) {
	    	 Line line = new Line(left,top2,right,bottom2);
	    	 Line line2 = new Line(right,bottom2,left2,bottom);
	         mypane.getChildren().add(line);
	         mypane.getChildren().add(line2);
	        }
	        else {
	        	Line line3 = new Line(100,100,10,10);
	        	mypane.getChildren().add(line3);
	        }
	}
	
/*
 * this method is for changing the original image to black and white image.
 */
	@FXML
	private void WhiteAndBlackConvertion(ActionEvent event) {
		PixelReader pixelReader = image.getPixelReader();
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		WritableImage wbImage = new WritableImage(width,height);
		PixelWriter pixelWriter = wbImage.getPixelWriter();
		
		
				
		for(int x = 0;x < width ;x++) {
			for(int y =0;y<height ; y++) {
				int pixel = pixelReader.getArgb(x, y);
	
				int alpha = ((pixel >> 24) & 0xff);
				int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff);
				int blue = (pixel & 0xff);
				int rbg = (red + green + blue) / 3;
				
				if(rbg > 127) {
					pixel = (alpha<<24)|(255<<16)|(255<<8)|255;
				}
				
				else if (rbg <=127) {
					pixel = (alpha<<24)|(0<<16)|(0<<8)|0;
				}

				pixelWriter.setArgb(x, y, pixel);
			}
		}
		 imageView.setImage(wbImage);
		
	}
	

	
	
}
