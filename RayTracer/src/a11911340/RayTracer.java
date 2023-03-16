package a11911340;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class RayTracer {	
	
	
public void trace() {
		
		for(int y = 0; y < Output.HEIGHT; y++) { 
			for(int x = 0; x < Output.WIDTH; x++) {
				
				
				//the code from the slides didtn't work here :/
				// it makes the image turn on the x axis
				
				/*double xx = ((2 * (x + 0.5) - Output.WIDTH) / Output.WIDTH) * Math.tan(Output.camera.fovx);
				double yy = ((2 * (y + 0.5) - Output.HEIGHT) / Output.HEIGHT) * Math.tan(Output.camera.fovy);
				
				MyVector ray = new MyVector(xx, yy, 0);				
				Ray r = new Ray(Output.camera.getPosition(), ray, true);*/
				
				//code from morpheus tutorial
				/*double u = Output.camera.getLeft() + (Output.camera.getRight() - Output.camera.getLeft()) * (x+0.5)/Output.WIDTH;
				double v = Output.camera.getTop() + (Output.camera.getBottom() - Output.camera.getTop()) * (y+0.5)/Output.HEIGHT;
				
				MyVector s = Util.add(Output.camera.getVectorU().scalarMultiplication(u), Output.camera.getVectorV().scalarMultiplication(v), Output.camera.getVN_D_negated());
				MyVector dir = s.normalize();
				
				Ray r = new Ray(Output.camera.getPosition(), dir);*/
				
				//System.out.println("r1: " + r1.direction.getX() + ", " + r1.direction.getY() + ", " + r1.direction.getZ());
				//System.out.println("r: " + r.direction.getX() + ", " + r.direction.getY() + ", " + r.direction.getZ());
				
				
				
				
				/*Objects intersect = null;
				double t = Double.MAX_VALUE - 1;
				for(Objects o : Output.myObjects) {
					if(o instanceof Sphere) {
						if(((Sphere) o).checkIntersection(r)) {
							double t0 = o.intersect(r);
							if(t0 > 0 && t0 < t) {
								
								intersect = o;
								t = t0;
							}
						}	
					}
					else if(o instanceof Triangle) {
						if(((Triangle) o).checkIntersection(r)) {
							double t0 = o.intersect(r);
							if(t0 > 0 && t0 < t) {
								intersect = o;
								t = t0;
							}
						}
					}
				}*/
				
				//anti aliasing
				int resultColor = superSampling(x, y);
				//for supersampling
				
				Output.setPixelColor(x, y, resultColor);
				
				/*if(intersect != null) {
					
					
					MyVector hit1 = r.direction.scalarMultiplication(t);
					MyVector hit = Util.add(Output.camera.getPosition(), hit1);
					
					
					Output.setPixelColor(x, y, intersect.getColor(r, hit, 0));
					
					
					
				}*/
				
				
			}
		}
		
	}

	public static int superSampling(int x, int y) {
		MyVector result = new MyVector();
		int countColor = 0;
		
		for(double xx = 0.25; xx < 1; ) {
			for(double yy = 0.25; yy < 1; ) {
				
				double u = Output.camera.getLeft() + (Output.camera.getRight() - Output.camera.getLeft()) * (x+xx)/Output.WIDTH;
				double v = Output.camera.getTop() + (Output.camera.getBottom() - Output.camera.getTop()) * (y+yy)/Output.HEIGHT;
				
				MyVector s = Util.add(Output.camera.getVectorU().scalarMultiplication(u), Output.camera.getVectorV().scalarMultiplication(v), Output.camera.getVN_D_negated());
				MyVector dir = s.normalize();
				
				Ray r = new Ray(Output.camera.getPosition(), dir);
				
				
				Objects intersect = null;
				double t = Double.MAX_VALUE - 1;
				for(Objects o : Output.myObjects) {
					if(o instanceof Sphere) {
						if(((Sphere) o).checkIntersection(r)) {
							double t0 = o.intersect(r);
							if(t0 > 0 && t0 < t) {
								
								intersect = o;
								t = t0;
							}
						}	
					}
					else if(o instanceof Triangle) {
						if(((Triangle) o).checkIntersection(r)) {
							double t0 = o.intersect(r);
							if(t0 > 0 && t0 < t) {
								intersect = o;
								t = t0;
							}
						}
					}
				}
				
				if(intersect != null) {
					
					MyVector hit1 = r.direction.scalarMultiplication(t);
					MyVector hit = Util.add(Output.camera.getPosition(), hit1);
					
					//Color col = new Color(intersect.getColor(hit, 0));
					//MyVector help = new MyVector((double)col.getRed()/255, (double)col.getGreen()/255, (double)col.getBlue()/255);
					MyVector help = intersect.getColorVec(r, hit, 0);
					result.add(help);
					if( help.x != 0 && help.y != 0 && help.z != 0) {
						countColor++;
					}
				}
				yy += 0.5;
			}
			xx += 0.5;
		}
		//System.out.println("finalVector: " + result.x + ", " + result.y + ", " + result.z);
		//System.out.println("count: " + countColor);
		if(countColor > 0) {
			result.divide(countColor);
		}
		//System.out.println("Vector after: " + result.x + ", " + result.y + ", " + result.z);
		result = result.scalarMultiplication(255);
		 
		
		result.x = Math.min(255, result.x);
		result.y = Math.min(255, result.y);
		result.z = Math.min(255, result.z);
		
		result.x = Math.max(0, result.x);
		result.y = Math.max(0, result.y);
		result.z = Math.max(0, result.z);
		
		Color col = new Color((int)Math.round(result.x), (int)Math.round(result.y), (int)Math.round(result.z));
		return col.getRGB();
		
	}
			

}
