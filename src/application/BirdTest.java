package application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

class BirdTest {

	private Image image;
	private ImageView imageView;
	@BeforeEach
	void setUp() throws Exception {
		File f;
		try {
			f=new File("C:\\Users\\as083\\Desktop\\bird3.jpg");
			if(f!=null)
				{
				image=new Image(new FileInputStream(f));

				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	private void WhiteAndBlackConvertion() {
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
