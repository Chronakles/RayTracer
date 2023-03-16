package a11911340;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Triangle implements Objects {

	private ArrayList<MyVector> points;
	private MyVector normal;
	private ArrayList<MyVector> texCoordinates;
	private MyVector color;
	private BufferedImage texture;
	private double ka;
	private double kd;
	private double ks;
	private double exp;

	
	
	Triangle(ArrayList<MyVector> p, MyVector n, ArrayList<MyVector> tc, MyVector col, double KA, double KD, double KS, double EXP) {
		this.points = p;
		this.normal = n;
		this.texCoordinates = tc;
		this.color = col;
		this.ka = KA;
		this.kd = KD;
		this.ks = KS;
		this.exp = EXP;
		texture = null;
	}
	
	Triangle(ArrayList<MyVector> p, MyVector n, ArrayList<MyVector> tc, MyVector col, double KA, double KD, double KS, double EXP, BufferedImage img) {
		this.points = p;
		this.normal = n;
		this.texCoordinates = tc;
		this.color = new MyVector();
		this.ka = KA;
		this.kd = KD;
		this.ks = KS;
		this.exp = EXP;
		texture = img;
	}
	
	@Override
	public double intersect(Ray ray) {
		/*Double D = this.normal.product(this.points.get(0));
		Double t = ( -this.getNormal().product(ray.position) + D ) / ( this.getNormal().product(ray.direction) );*/
		
		MyVector v0v1 = this.points.get(1).subtract(this.points.get(0));
		MyVector v0v2 = this.points.get(2).subtract(this.points.get(0));
		
		MyVector pvec = ray.direction.crossProduct(v0v2);
		double det = v0v1.product(pvec);
		
		double invDet = 1/det;
		
		MyVector tvec = ray.position.subtract(this.points.get(0));		
		MyVector qvec = tvec.crossProduct(v0v1);
		
		double t = v0v2.product(qvec) * invDet;
		
		return t;
	}
	
	public boolean checkIntersection(Ray ray) {
		
		//Check if the Ray is parallel to plane
		/*Double parallel = this.normal.product(ray.direction);
		if(Util.abs(parallel) < 0.00001)	return false;
		
		//Check if the Triangle is in front of the camera
		Double D = this.normal.product(this.points.get(0));
		Double t = ( -this.getNormal().product(ray.position) + D ) / ( this.getNormal().product(ray.direction) );
		if(t < 0) return false;
		
		//Check if the Ray is inside of the Triangle
		MyVector edge0 = this.points.get(1).subtract(this.points.get(0));
		MyVector edge1 = this.points.get(2).subtract(this.points.get(1));
		MyVector edge2 = this.points.get(0).subtract(this.points.get(2));
		
		MyVector Phit = Util.add(ray.position, ray.direction.scalarMultiplication(t));
		
		MyVector C0 = Phit.subtract(this.points.get(0));
		MyVector C1 = Phit.subtract(this.points.get(1));
		MyVector C2 = Phit.subtract(this.points.get(2));
		
		if( this.normal.product(edge0.crossProduct(C0)) > 0 &&
			this.normal.product(edge1.crossProduct(C1)) > 0 &&
			this.normal.product(edge2.crossProduct(C2)) > 0) return true;
			
		return false;*/
		
		MyVector v0v1 = this.points.get(1).subtract(this.points.get(0));
		MyVector v0v2 = this.points.get(2).subtract(this.points.get(0));
		
		MyVector pvec = ray.direction.crossProduct(v0v2);
		double det = v0v1.product(pvec);
		
		if(Util.abs(det) < 0.00001) {
			//Output.false1 += 1;
			return false;
		}
		
		double invDet = 1/det;
		
		MyVector tvec = ray.position.subtract(this.points.get(0));
		double u = tvec.product(pvec) * invDet;
		if(u < 0 || u > 1) {
			//Output.false2 += 1;
			return false;
		}
		
		MyVector qvec = tvec.crossProduct(v0v1);
		double v = ray.direction.product(qvec) * invDet;
		if(v < 0 || u + v > 1) {
			//Output.false3 += 1;
			return false;
		}
		
		double t = v0v2.product(qvec) * invDet;
		
		if(t <= 0) return false;
		
		return true;
	}

	@Override
	public int getColor(Ray ray, MyVector hitPoint, int depth) {

		MyVector newColor = new MyVector();
		
		if(this.texture == null) {
			//calculate ambient part
			MyVector intensityAmbient = this.color.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
			newColor.add(this.color.multiply(intensityAmbient));	
		
		
			for(int i = 0; i < Output.lightSource.size(); i++) {
				if(Output.lightSource.get(i) instanceof PointLight) {
					//ambientes licht
					PointLight light = (PointLight) Output.lightSource.get(i);
					
					double limit = (light.getPosition().subtract(hitPoint)).length();
					MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
					hitPoint.move(l);
					
					Ray shadow = new Ray(hitPoint, l);
					
					boolean shadowed = shadow.checkShadow(limit);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//gib diffuses licht
						double nl = Math.max((this.normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							//calculate specular part
							MyVector v = (ray.position.subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(v), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exp)*this.ks);
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
						}
					}
				}
				
			}
			
		
			
			
			newColor = newColor.scalarMultiplication(255);
			
			newColor.x = Math.min(255, newColor.x);
			newColor.y = Math.min(255, newColor.y);
			newColor.z = Math.min(255, newColor.z);
			
			newColor.x = Math.max(0, newColor.x);
			newColor.y = Math.max(0, newColor.y);
			newColor.z = Math.max(0, newColor.z);
			
			Color col = new Color((int)Math.round(newColor.x), (int)Math.round(newColor.y), (int)Math.round(newColor.z));
			return col.getRGB();
			
			
		}else { //if we have a texture use this
			MyVector v0v1 = this.points.get(1).subtract(this.points.get(0));
			MyVector v0v2 = this.points.get(2).subtract(this.points.get(0));
		
			MyVector pvec = ray.direction.crossProduct(v0v2);
			double det = v0v1.product(pvec);
			
			double invDet = 1/det;
			
			MyVector tvec = ray.position.subtract(this.points.get(0));		
			MyVector qvec = tvec.crossProduct(v0v1);
			
			double u = tvec.product(pvec) * invDet;
			double v = ray.direction.product(qvec) * invDet;		
			
			double srcX = (1-u-v)*this.texCoordinates.get(0).getX() + u * this.texCoordinates.get(1).getX() + v * this.texCoordinates.get(2).getX();
					
			double srcY = (1-u-v)*this.texCoordinates.get(0).getY() + u * this.texCoordinates.get(1).getY() + v * this.texCoordinates.get(2).getY();
			
			int myX = (int) Math.round(srcX * (this.texture.getWidth()-1));
			int myY = (int) Math.round(srcY * (this.texture.getHeight()-1));
			
			
			int result = this.texture.getRGB(myX, myY);
			
			Color help = new Color(result);
			MyVector texel = new MyVector((double)help.getRed()/255, (double)help.getGreen()/255, (double) help.getBlue()/255);
			
			MyVector intensityAmbient = texel.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
			newColor.add(texel.multiply(intensityAmbient));
			
			for(int i = 0; i < Output.lightSource.size(); i++) {
				if(Output.lightSource.get(i) instanceof PointLight) {
					//ambientes licht
					PointLight light = (PointLight) Output.lightSource.get(i);
					
					double limit = (light.getPosition().subtract(hitPoint)).length();
					MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
					hitPoint.move(l);
					
					Ray shadow = new Ray(hitPoint, l);
					
					boolean shadowed = shadow.checkShadow(limit);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//gib diffuses licht
						double nl = Math.max((this.normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = texel.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							//calculate specular part
							MyVector view = (ray.position.subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(view), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exp)*this.ks);
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
						}
					}
				}
				
			}
			
		
			
			
			newColor = newColor.scalarMultiplication(255);
			
			newColor.x = Math.min(255, newColor.x);
			newColor.y = Math.min(255, newColor.y);
			newColor.z = Math.min(255, newColor.z);
			
			newColor.x = Math.max(0, newColor.x);
			newColor.y = Math.max(0, newColor.y);
			newColor.z = Math.max(0, newColor.z);
			
			Color col = new Color((int)Math.round(newColor.x), (int)Math.round(newColor.y), (int)Math.round(newColor.z));
			return col.getRGB();
			
		}
	}

	public MyVector getNormal() {
		return this.normal;
	}

	public MyVector getColor() {
		return this.color;
	}
	
	
	public MyVector getColorVec(Ray ray, MyVector hitPoint, int depth) {

		MyVector newColor = new MyVector();
				
		if(this.texture == null) {
			
			//calculate ambient part
			MyVector intensityAmbient = this.color.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
			newColor.add(this.color.multiply(intensityAmbient));
		
			for(int i = 0; i < Output.lightSource.size(); i++) {
				if(Output.lightSource.get(i) instanceof PointLight) {
					//ambientes licht
					PointLight light = (PointLight) Output.lightSource.get(i);
					
					double limit = (light.getPosition().subtract(hitPoint)).length();
					MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
					hitPoint.move(l);
					
					Ray shadow = new Ray(hitPoint, l);
					
					boolean shadowed = shadow.checkShadow(limit);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//gib diffuses licht
						double nl = Math.max((this.normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							//calculate specular part
							MyVector v = (ray.position.subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(v), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exp)*this.ks);
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
						}
					}
				}
				
			}
			
			//System.out.println("Vector Triangle: " + newColor.x + ", " + newColor.y + ", " + newColor.z);
			
			return newColor;
		
		}else {
			MyVector v0v1 = this.points.get(1).subtract(this.points.get(0));
			MyVector v0v2 = this.points.get(2).subtract(this.points.get(0));
		
			MyVector pvec = ray.direction.crossProduct(v0v2);
			double det = v0v1.product(pvec);
			
			double invDet = 1/det;
			
			MyVector tvec = ray.position.subtract(this.points.get(0));		
			MyVector qvec = tvec.crossProduct(v0v1);
			
			double u = tvec.product(pvec) * invDet;
			double v = ray.direction.product(qvec) * invDet;		
			
			double srcX = (1-u-v)*this.texCoordinates.get(0).getX() + u * this.texCoordinates.get(1).getX() + v * this.texCoordinates.get(2).getX();
					
			double srcY = (1-u-v)*this.texCoordinates.get(0).getY() + u * this.texCoordinates.get(1).getY() + v * this.texCoordinates.get(2).getY();
			
			int myX = (int) Math.round(srcX * (this.texture.getWidth()-1));
			int myY = (int) Math.round(srcY * (this.texture.getHeight()-1));
			
			
			int result = this.texture.getRGB(myX, myY);
			
			
			Color help = new Color(result);
			MyVector texel = new MyVector((double)help.getRed()/255, (double)help.getGreen()/255, (double) help.getBlue()/255);
			
			//System.out.println(help.getRed() + ", " + help.getGreen() + ", " + help.getBlue());
			
			MyVector intensityAmbient = texel.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
			newColor.add(texel.multiply(intensityAmbient));
			
			for(int i = 0; i < Output.lightSource.size(); i++) {
				if(Output.lightSource.get(i) instanceof PointLight) {
					//ambientes licht
					PointLight light = (PointLight) Output.lightSource.get(i);
					
					double limit = (light.getPosition().subtract(hitPoint)).length();
					MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
					hitPoint.move(l);
					
					Ray shadow = new Ray(hitPoint, l);
					
					boolean shadowed = shadow.checkShadow(limit);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//gib diffuses licht
						double nl = Math.max((this.normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = texel.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							//calculate specular part
							MyVector view = (ray.position.subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(view), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exp)*this.ks);
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
							//System.out.println("diffuse: " + diffuse.x + ", " + diffuse.y + ", " + diffuse.z);
							//System.out.println("specular: " + specular.x + ", " + specular.y + ", " + specular.z);
						}
					}
				}
				
			}
			
			return newColor;
		}
		
	}
	
	
}
