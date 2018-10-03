package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorWidget extends JPanel implements MouseListener{	
	
private PictureView picture_view;
private JLabel x, y, red, blue, green, brightness;
private Picture picture;
	
	public PixelInspectorWidget(Picture picture){
		this.picture = picture;
		
		setLayout(new BorderLayout());
		
		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);
		
		JPanel pixelInfo = new JPanel();
		
		pixelInfo.setLayout(new GridLayout(0,1));
		
		x = new JLabel("X: ");
		pixelInfo.add(x);
		
		y = new JLabel("Y: ");
		pixelInfo.add(y);
		
		red = new JLabel("Red: ");
		pixelInfo.add(red);
		
		green = new JLabel("Green: ");
		pixelInfo.add(green);
		
		blue = new JLabel("Blue: ");
		pixelInfo.add(blue);
		
		brightness = new JLabel("Brightness: ");
		pixelInfo.add(brightness);
		
		add(pixelInfo, BorderLayout.WEST);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		x.setText("X: " + e.getX());
		y.setText("Y: " + e.getY());
		red.setText("Red: " + Math.round(picture.getPixel(e.getX(), e.getY()).getRed() * 100.0)/100.0);
		green.setText("Green: " + Math.round(picture.getPixel(e.getX(), e.getY()).getGreen()*100.0)/100.0);
		blue.setText("Blue: " + Math.round(picture.getPixel(e.getX(), e.getY()).getBlue()*100.0)/100.0);
		brightness.setText("Brightness: " + Math.round(picture.getPixel(e.getX(), e.getY()).getIntensity()*100.0)/100.0);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
