package a11911340;

public interface Objects {

	public double intersect(Ray ray);

	public int getColor(Ray ray, MyVector position, int depth);
	
	public MyVector getColorVec(Ray ray, MyVector position, int depth);

	public boolean checkIntersection(Ray ray);
	
}
