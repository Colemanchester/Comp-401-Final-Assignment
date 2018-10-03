package a8;

public class BrightnessDecorator implements Picture{

	Picture p;
	
	public BrightnessDecorator(Picture picture) {
		this.p = picture;
	}
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return p.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return p.getHeight();
	}

	@Override
	public Pixel getPixel(int x, int y) {
		// TODO Auto-generated method stub
		return p.getPixel(x, y);
	}

	@Override
	public Pixel getPixel(Coordinate c) {
		// TODO Auto-generated method stub
		return p.getPixel(c);
	}

	@Override
	public void setPixel(int x, int y, Pixel p) {
		// TODO Auto-generated method stub
		this.p.setPixel(x, y,p);
	}

	@Override
	public void setPixel(Coordinate c, Pixel p) {
		// TODO Auto-generated method stub
		this.p.setPixel(c, p);
	}

	@Override
	public SubPicture extract(int xoff, int yoff, int width, int height) {
		// TODO Auto-generated method stub
		return p.extract(xoff, yoff, width, height);
	}

	@Override
	public SubPicture extract(Coordinate a, Coordinate b) {
		// TODO Auto-generated method stub
		return p.extract(a, b);
	}

	@Override
	public SubPicture extract(Region r) {
		// TODO Auto-generated method stub
		return p.extract(r);
	}

	@Override
	public ObservablePicture createObservable() {
		// TODO Auto-generated method stub
		return p.createObservable();
	}

	
	public void brightness(double weight) {
		for(int y= 0; y < getHeight(); y++) {
			for(int x = 0; x < getWidth(); x++) {
				if(weight <= 0) {
					p.setPixel(x, y, new ColorPixel(p.getPixel(x, y).getRed() * (1-weight) , p.getPixel(x, y).getGreen()* (1-weight), p.getPixel(x, y).getBlue()* (1-weight)));
				}
				else {
					p.setPixel(x,y, new ColorPixel(p.getPixel(x, y).getRed() * (1-weight) + 1 * weight, p.getPixel(x, y).getGreen() * (1-weight) + 1 * weight, p.getPixel(x, y).getBlue() * (1-weight) + 1 * weight));
				}
			}
		}
	}

}
