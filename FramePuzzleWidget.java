package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FramePuzzleWidget extends JPanel implements MouseListener,KeyListener{
	
	
	private PictureView picture_view;
	private Picture picture;
	private ObservablePictureImpl obsPicture;
	private SubPictureImpl[][] panels = new SubPictureImpl[5][5];
	private int xStep,yStep;
	private int xIndex, yIndex;
	
	
	public FramePuzzleWidget(Picture picture){
		
		this.picture = picture;
		this.obsPicture = new ObservablePictureImpl(new PictureImpl(picture.getWidth(),picture.getHeight()));
		xStep = picture.getWidth()/5;
		yStep = picture.getHeight()/5;
		
		
		setLayout(new BorderLayout());
		
		setupPixels(picture,obsPicture);
		
		picture_view = new PictureView(obsPicture.createObservable());
		picture_view.addMouseListener(this);;
		picture_view.addKeyListener(this);
		add(picture_view, BorderLayout.CENTER);		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() > xStep * xIndex && e.getX() < xStep*xIndex + xStep || e.getY() > yStep * yIndex && e.getY() < yStep * yIndex + yStep) {
			swapPanels(findXPanel(e.getX()), findYPanel(e.getY()), xIndex, yIndex);
			xIndex = findXPanel(e.getX());
			yIndex = findYPanel(e.getY());
		}
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

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			movePanelUp();
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			movePanelRight();
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			movePanelDown();
		
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			movePanelLeft();
		
		}
	}
	
	public void setupPixels(Picture picture, ObservablePictureImpl obsPic) {
		for(int y = 0; y < picture.getHeight(); y++) {
			for(int x = 0; x< picture.getWidth(); x++) {
				if(x<xStep*4 || y<yStep*4) {
					obsPic.setPixel(x, y, picture.getPixel(x, y));
				}else {
					obsPic.setPixel(x, y, new ColorPixel(1,1,1));
				}
			}
		}
		
		//setup the subPictures
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j <5; j++) {
				panels[i][j] = (SubPictureImpl) obsPic.extract(new Coordinate(xStep * i, yStep * j), new Coordinate(xStep * (i+1)-1, yStep * (j+1)-1));
			}
		}
		
		xIndex = 4;
		yIndex = 4;
		
	}
	
	public int findXPanel(int xClicked) {
		if(xClicked < xStep) {
			return 0;
		}
		else if(xClicked < xStep*2) {
			return 1;
		}
		else if(xClicked < xStep*3) {
			return 2;
		}
		else if(xClicked < xStep*4) {
			return 3;
		}
		else {
			return 4;
		}
	}
	
	public int findYPanel(int yClicked) {
		if(yClicked < yStep) {
			return 0;
		}
		else if(yClicked < yStep*2) {
			return 1;
		}
		else if(yClicked < yStep*3) {
			return 2;
		}
		else if(yClicked < yStep*4) {
			return 3;
		}
		else {
			return 4;
		}
	}
	
	public void swapPanels(int xHost, int yHost, int xWhite, int yWhite) {
			
		
		for(int y = yStep*yWhite; y < yStep*(yWhite+1); y++) {
			for(int x = xStep*xWhite; x< xStep*(xWhite+1); x++) {
				obsPicture.setPixel(x,y,panels[xHost][yHost].getPixel(x-(xStep*xWhite), y-(yStep*yWhite)));

			}
		}
		
		for(int y = yStep*yHost; y < yStep*(yHost+1); y++) {
			for(int x = xStep*xHost; x< xStep*(xHost+1); x++) {
				obsPicture.setPixel(x,y,new ColorPixel(1,1,1));
			}
		}
		
		
	}
	
	public void movePanelUp() {
		if(yIndex == 0) {
			System.out.println("You can not move it up!");
		}else {
			swapPanels(xIndex, yIndex-1, xIndex, yIndex);
			yIndex -= 1;
		}
	}
	
	public void movePanelRight() {
		if(xIndex == 4) {
			System.out.println("You can not move it right!");
		}else {
			swapPanels(xIndex+1, yIndex, xIndex, yIndex);
			xIndex += 1;
		}
	}
	
	public void movePanelDown() {
		if(yIndex == 4) {
			System.out.println("You can not move it down!");
		}else {
			swapPanels(xIndex, yIndex+1, xIndex, yIndex);
			yIndex += 1;
		}
	}
	
	public void movePanelLeft() {
		if(xIndex == 0) {
			System.out.println("You can not move it left!");
		}else {
			swapPanels(xIndex-1, yIndex, xIndex, yIndex);
			xIndex -= 1;
		}
	}
}
