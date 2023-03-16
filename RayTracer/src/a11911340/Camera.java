package a11911340;

public class Camera {
	
	double fovx;
	double fovy;
	
	private int left = -Output.WIDTH/2;
	private int right = left * -1;
	private int top = Output.HEIGHT/2;
	private int bottom = top * -1;
	
	private MyVector vectorUP;
	private MyVector position;
	private MyVector lookat;
	
	// calculate the normal vector by subtracting the camera and the center of the image plane
	private MyVector vectorVN;
	// calculate the u vector with the cross product of the up and the normal vector (orthonormal)
	private MyVector vectorU;
	// same for v with normal vector and u vector
	private MyVector vectorV;
	
	// this is the ray
	private double distance;
	
	private MyVector VN_D_negated;
	
	
	Camera(){
		this.position = new MyVector(0, 0, -1);
		this.vectorUP = new MyVector(0, 1, 0);
		this.lookat = new MyVector(0, 0, 0);
		
		this.fovx = Math.PI/4;
		this.fovy = Output.HEIGHT/Output.WIDTH*fovx;
		
		vectorVN = lookat.subtract(position).normalize();
		vectorU = vectorUP.crossProduct(vectorVN).normalize();
		vectorV = vectorVN.crossProduct(vectorU).normalize();
		distance = top/Math.tan(fovx)/2;
		VN_D_negated = vectorVN.scalarMultiplication(distance * -1);
		
	}
	
	Camera(double fov, MyVector up, MyVector pos, MyVector look){
		if(pos == null) {
			this.position = new MyVector(0, 0, -1);
		}else {
			this.position = pos;
		}
		
		if(up == null) {
			this.vectorUP = new MyVector(0, 1, 0);
		}else {
			this.vectorUP = up;
		}
		
		if(look == null) {
			this.lookat = new MyVector(0, 0, 0);
		}else {
			this.lookat = look;
		}
		
		this.fovx = fov/2;
		this.fovy = Output.HEIGHT/Output.WIDTH*fovx;
		
		vectorVN = lookat.subtract(position).normalize();
		vectorU = vectorVN.crossProduct(vectorUP).normalize();
		vectorV = vectorU.crossProduct(vectorVN).normalize();
		distance = top/Math.tan(fovx)/2;
		VN_D_negated = vectorVN.scalarMultiplication(distance);
		
	}
	
	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public MyVector getVectorUP() {
		return vectorUP;
	}

	public void setVectorUP(MyVector vectorUP) {
		this.vectorUP = vectorUP;
	}

	public MyVector getPosition() {
		return position;
	}

	public void setPosition(MyVector position) {
		this.position = position;
	}

	public MyVector getLookat() {
		return lookat;
	}

	public void setLookat(MyVector lookat) {
		this.lookat = lookat;
	}

	public MyVector getVectorVN() {
		return vectorVN;
	}

	public void setVectorVN(MyVector vectorVN) {
		this.vectorVN = vectorVN;
	}

	public MyVector getVectorU() {
		return vectorU;
	}

	public void setVectorU(MyVector vectorU) {
		this.vectorU = vectorU;
	}

	public MyVector getVectorV() {
		return vectorV;
	}

	public void setVectorV(MyVector vectorV) {
		this.vectorV = vectorV;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public MyVector getVN_D_negated() {
		return VN_D_negated;
	}

	public void setVN_D_negated(MyVector vN_D_negated) {
		this.VN_D_negated = vN_D_negated;
	}

}
