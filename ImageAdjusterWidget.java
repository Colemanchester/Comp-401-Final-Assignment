package a8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterWidget extends JPanel implements ChangeListener{
	private PictureView picture_view;
	private JSlider blurSlider, brightSlider, saturationSlider;
	private Picture picture;
	private ObservablePictureImpl obsPicture;
	private ObservablePictureImpl previousPicture;

		public  ImageAdjusterWidget(Picture picture){
						
			this.picture = picture;
			this.obsPicture = new ObservablePictureImpl(new PictureImpl(picture.getWidth(),picture.getHeight()));
			this.previousPicture = new ObservablePictureImpl(new PictureImpl(picture.getWidth(),picture.getHeight()));

			
			//reset all the original to dereference from the original picture :/
			makeClone(obsPicture, picture);
			 
			picture_view = new PictureView(obsPicture.createObservable());

			setLayout(new BorderLayout());
			
			add(picture_view, BorderLayout.CENTER);
			
			
			JPanel slider_panel = new JPanel();
			slider_panel.setLayout(new GridLayout(3,1));

			blurSlider = new JSlider(0, 5, 0);
			brightSlider = new JSlider(-100, 100, 0);
			saturationSlider = new JSlider(-100, 100, 0);

			blurSlider.setPreferredSize(new Dimension(500, 20));
			brightSlider.setPreferredSize(new Dimension(500, 20));
			saturationSlider.setPreferredSize(new Dimension(500, 20));
			
			JPanel blur = new JPanel();
			blur.setLayout(new FlowLayout());
			blur.add(new JLabel("Blur: "));
			blur.add(blurSlider);
			
			JPanel bright = new JPanel();
			bright.setLayout(new FlowLayout());
			bright.add(new JLabel("Brightness: "));
			bright.add(brightSlider);
			
			JPanel saturate = new JPanel();
			saturate.setLayout(new FlowLayout());
			saturate.add(new JLabel("Saturation: "));
			saturate.add(saturationSlider);


			slider_panel.add(blur);
			slider_panel.add(bright);
			slider_panel.add(saturate);
			
			add(slider_panel, BorderLayout.SOUTH);
		
			
			blurSlider.addChangeListener(this);
			brightSlider.addChangeListener(this);
			saturationSlider.addChangeListener(this);
			
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			
			if (!brightSlider.getValueIsAdjusting() && !saturationSlider.getValueIsAdjusting() && !blurSlider.getValueIsAdjusting()) {
				
				makeClone(obsPicture, picture);
				
				obsPicture = blur(obsPicture, blurSlider.getValue());
				makeClone(previousPicture, obsPicture);
				obsPicture = saturate(obsPicture, saturationSlider.getValue());
				makeClone(previousPicture, obsPicture);
				obsPicture = brighten(obsPicture, brightSlider.getValue());
				
			}
			
						
		}
		
		public ObservablePictureImpl brighten(ObservablePictureImpl obsPic, double weight) {
			if(weight < 0) {
				weight /= -100;
				for(int y = 0; y < obsPic.getHeight(); y++) {
					for(int x = 0; x< obsPic.getWidth(); x++) {
						obsPic.setPixel(x,y, previousPicture.getPixel(x, y).darken(weight));
					}
				}
			} else {
				weight /= 100;
				for(int y = 0; y < obsPic.getHeight(); y++) {
					for(int x = 0; x< obsPic.getWidth(); x++) {
						obsPic.setPixel(x,y, previousPicture.getPixel(x, y).lighten(weight));
					}
				}
			}
			
			return obsPic;
		}

		public ObservablePictureImpl saturate(ObservablePictureImpl obsPic, double f) {
			
			double b = 0;
			double a = 0;
			
			if(f < 0) {
				for(int y = 0; y < obsPic.getHeight(); y++) {
					for(int x = 0; x< obsPic.getWidth(); x++) {
						b = previousPicture.getPixel(x, y).getIntensity();
						obsPic.setPixel(x,y, new ColorPixel(previousPicture.getPixel(x, y).getRed() * (1.0 + (f / 100.0) ) - (b * f / 100.0), previousPicture.getPixel(x, y).getGreen() * (1.0 + (f / 100.0) ) - (b * f / 100.0), previousPicture.getPixel(x, y).getBlue() * (1.0 + (f / 100.0) ) - (b * f / 100.0)));
					}
				}
			} else {
				for(int y = 0; y < obsPic.getHeight(); y++) {
					for(int x = 0; x< obsPic.getWidth(); x++) {
						a = Math.max(previousPicture.getPixel(x, y).getGreen(),Math.max(previousPicture.getPixel(x, y).getRed(), previousPicture.getPixel(x, y).getBlue()));
						obsPic.setPixel(x,y, new ColorPixel( previousPicture.getPixel(x, y).getRed() * ((a + ((1.0 - a) * (f / 100.0))) / a), previousPicture.getPixel(x, y).getGreen() * ((a + ((1.0 - a) * (f / 100.0))) / a), previousPicture.getPixel(x, y).getBlue() * ((a + ((1.0 - a) * (f / 100.0))) / a)));
					}
				}
			}
			
			return obsPic;
		}
		
		public void makeClone(ObservablePictureImpl hostPic, Picture clonePic) {
			for(int y = 0; y < picture.getHeight(); y++) {
				for(int x = 0; x< picture.getWidth(); x++) {
					hostPic.setPixel(x, y, clonePic.getPixel(x, y));
				}
			}
		}

	public ObservablePictureImpl blur(ObservablePictureImpl obsPic, int f) {
		double redTotal = 0;
		double greenTotal = 0;
		double blueTotal = 0;
		int factor =  f;
		SubPictureImpl block;
		int averageDivider;

		
		if(f == 0) {
			return obsPic;
		}
		for (int y = factor; y < obsPic.getHeight()-factor; y++) {
			for (int x = factor; x < obsPic.getWidth()-factor; x++) {

				block = (SubPictureImpl) obsPic.extract(new Coordinate(x - factor, y - factor),
						new Coordinate(x + factor, y + factor));
				
				redTotal = 0;
				greenTotal = 0;
				blueTotal = 0;
				averageDivider = 0;
							
				for (int j = 0; j < block.getHeight(); j++) {
					for (int i = 0; i < block.getWidth(); i++) {
						
						redTotal += block.getPixel(i,j).getRed();
						greenTotal += block.getPixel(i,j).getGreen();
						blueTotal += block.getPixel(i,j).getBlue();
						averageDivider++;

					}
				}
			
				obsPic.setPixel(x, y, new ColorPixel(redTotal/(averageDivider),greenTotal/(averageDivider), blueTotal/(averageDivider)));
			}
		}

		return obsPic;
	}
		
}
		

