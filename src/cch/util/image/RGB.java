package cch.util.image;

public class RGB {
	private int R;
	private int G;
	private int B;
	private int RGB;
	public int getRGB() {
		return RGB;
	}
	public void setRGB(int rGB) {
		RGB = rGB;
	}
	public int getR() {
		return R;
	}
	public void setR(int r) {
		R = r;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G = g;
	}
	public int getB() {
		return B;
	}
	public void setB(int b) {
		B = b;
	}
	
	public boolean isBigger(int compared){
		if(this.R>compared&&this.G>compared){
			return true;
		}else if(this.R>compared&&this.B>compared){
			return true;
		}else if(this.G>compared&&this.B>compared){
			return true;
		}
		return false;
	}
	public boolean isNoisy(){
		//if(this.R>)
		return false;
		
	}

}
