package a11911340;

import java.awt.Color;

public class Ray {
	public MyVector position;
	public MyVector direction;

	public Ray(MyVector pos, MyVector dir){
		
		this.direction = dir;
		this.position = pos;
		
	}

	// second Constructor with a dummy variable
	public Ray(MyVector start, MyVector end, boolean secondConstructor){
		
		this.direction = end.subtract(start).normalize();
		this.position = start;
		
	}

	public int castRay(int depth){
		
		if(depth > Output.maxBounces){			
			return Color.BLACK.getRGB();
		}
		
		Objects intersect = null;
		double t = Double.MAX_VALUE - 1;
		for (Objects o : Output.myObjects){		
			
			if(o instanceof Sphere) {
				if(((Sphere) o).checkIntersection(this)) {
					double t0 = o.intersect(this);
					if(t0 > 0 && t0 < t) {
						
						intersect = o;
						t = t0;
					}
				}	
			}
			else if(o instanceof Triangle) {
				if(((Triangle) o).checkIntersection(this)) {
					double t0 = o.intersect(this);
					if(t0 > 0 && t0 < t) {
						intersect = o;
						t = t0;
					}
				}
			}
			
			/*double t2 = o.intersect(this);
			if (t2 > 0 && t2 < t){
				
				intersect = o;
				t = t2;
			}*/
		}

		if (intersect != null){
			return intersect.getColor(this, this.getPosition(t), depth);
			
		}else{
			return Color.BLACK.getRGB();
		}
	}
	

	// get the position of the intersection
	private MyVector getPosition(double t)
	{
		return Util.add(this.position, this.direction.scalarMultiplication(t));
	}
	
	public boolean checkShadow(double limit) {
		
		double t = Double.MAX_VALUE - 1;
		for (Objects o : Output.myObjects)
		{
			if(o.checkIntersection(this)) {
				double t2 = o.intersect(this);
				
				if (t2 > 0 && t2 < t && t2 < limit)
				{
					
					return true;
				}
			}
		}
		
		
		return false;
	}
}
