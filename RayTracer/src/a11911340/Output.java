package a11911340;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Output extends JPanel {
	
	public static int WIDTH = 200;
	public static int HEIGHT = 200;
	public static int maxBounces = 8;
	public static Color background;
	
	public static ArrayList<Objects> myObjects = new ArrayList<>();
	public static ArrayList<Light> lightSource = new ArrayList<>();
	public static Camera camera;
	public static MyVector AMBIENT;
		
	private BufferedImage canvas;
	
	private static Output instance = null;
	
	public static String filename;
	
	public static ArrayList<MyVector> meshVertex = new ArrayList<MyVector>();;
	public static ArrayList<MyVector> meshTexCo = new ArrayList<MyVector>();;
	public static ArrayList<MyVector> meshNormals = new ArrayList<MyVector>();;
	public static ArrayList<OBJIndex> meshIndices = new ArrayList<OBJIndex>();;
	
	public static MyVector wallColor = new MyVector();
	public static double wallKa;
	public static double wallKd;
	public static double wallKs;
	public static double wallExp;
	public static double wallrefl;
	public static double wallrefr;
	public static double walltrans;
	
	public static String meshTextureName = "";
	
	public static BufferedImage texture = null;
	
	public Output(int width, int height) {
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < canvas.getHeight(); i++) {
			for(int j = 0; j < canvas.getHeight(); j++) {
				canvas.setRGB(j, i, background.getRGB());
			}
		}
		
		//for a window not to create the image
		this.setPreferredSize(new Dimension(width, height));
		//g2d = canvas.createGraphics();
		
		//RayTracer.trace(Main.camera, canvas, g2d);
		
	}
	
	public BufferedImage getCanvas() {
		return this.canvas;
	}
	
	public void setCanvas(int width, int height) {
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static void setPixel(int x, int y, int z, Graphics2D g2d, int posx, int posy) {
		
		g2d.setColor(new Color(x, y, z));
		g2d.fillRect(posx, posy, 1, 1);
	}
	
	public static void setPixelColor(int x, int y, int rgb) {
		Output inst = getOutput();
		if(x > WIDTH - 1 || y > HEIGHT -1 || x < 0 || y < 0)
		{
			return;
		}
		inst.canvas.setRGB(x, y, rgb);
		inst.repaint();
	}
	
	public static void setBackground(int x, int y, int z, Graphics2D g2d) {
		int maxx = x;
		int maxy = y;
		if(x >= 255) {
			maxx = 255;
		}
		if(y >= 255){
			maxy = 255;
		};
		g2d.setColor(new Color(maxx, maxy, 0));
		g2d.fillRect(x, y, 1, 1);
	}
	
	
	public static Output getOutput() {
		if(Output.instance == null) {
			//Main.image = new MyVector[Main.width * Main.height];
			Output.instance = new Output(WIDTH, HEIGHT);
		}
		return Output.instance;
		//return new Output(WIDTH, HEIGHT);
	}
	
	//macht das unser canvas in dem JPanel window dargestellt wird
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(canvas,  null,  null);
		
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 1; i++) {
			File input = new File("example7.xml");
			new XMLParser().parseFile(input);
			if(i >= 1) {
				MyVector up = new MyVector(0,1,0);
				MyVector transform = Output.camera.getPosition();
				MyVector looking = Output.camera.getLookat();
				for(int j = 0; j < i; j++) {
					looking.add(new MyVector(0.3, 0, 0));
					transform.add(new MyVector(-1, 0, 0));
				}
				Output.camera = new Camera(45, up, transform, looking);
				
			}
			ObjParser parsed = new ObjParser(filename);	
			
			//((Sphere)myObjects.get(0)).setPosition(new MyVector(0, 1, -4));
			
			try {
				if(meshTextureName != "") {
					Output.texture = ImageIO.read(new File(meshTextureName));
				}
			} catch( IOException e ) {
				
			}
			if(Output.texture == null) {
				makeTriangles();
			}else {
				makeTriangles(texture);
				
			}
			
			Output panel = getOutput();
			new RayTracer().trace();
		
			
			JFrame frame = new JFrame("RayTracing");
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			try {
				if(i == 0) {
					panel.createOutput(panel.canvas, "png", "MyOutput1.png");
				}else if(i == 1) {
					panel.createOutput(panel.canvas, "png", "MyOutput2.png");
				}else {
					panel.createOutput(panel.canvas, "png", "MyOutput3.png");
				}
			}catch(IOException e) {
				System.out.println(e);
			}
			
			Output.instance = null;
			Output.myObjects = new ArrayList<>();
			Output.lightSource = new ArrayList<>();
		}
	}
	
	public void createOutput(BufferedImage image, String format, String fileName) throws IOException{
		try {
			ImageIO.write(image, format, new File(fileName));
		}catch(IOException e) {
			e.printStackTrace(); 
		}
	}
	
	
	public static void makeTriangles() {
		
		ArrayList<MyVector> vertices = new ArrayList<MyVector>();
		MyVector norm = new MyVector(0, 1, 0);
		ArrayList<MyVector> texco = new ArrayList<MyVector>();
			
		for(int i = 1; i <= meshIndices.size(); i++) {
			
			
			vertices.add(meshVertex.get(meshIndices.get(i-1).getVertexIndex()));
			texco.add(meshTexCo.get(meshIndices.get(i-1).getTexCoIndex()));
			norm = meshNormals.get(meshIndices.get(i-1).getNormalIndex());
			
			if(i%3 == 0) {
				myObjects.add(new Triangle(vertices, norm, texco, wallColor, wallKa, wallKd, wallKs, wallExp));
				
				vertices = new ArrayList<MyVector>();
				texco = new ArrayList<MyVector>();
			}
			
		}
		
	}
	
	
	public static void makeTriangles(BufferedImage img) {
		
		ArrayList<MyVector> vertices = new ArrayList<MyVector>();
		MyVector norm = new MyVector(0, 1, 0);
		ArrayList<MyVector> texco = new ArrayList<MyVector>();
			
		for(int i = 1; i <= meshIndices.size(); i++) {
			
			
			vertices.add(meshVertex.get(meshIndices.get(i-1).getVertexIndex()));
			texco.add(meshTexCo.get(meshIndices.get(i-1).getTexCoIndex()));
			norm = meshNormals.get(meshIndices.get(i-1).getNormalIndex());
			
			if(i%3 == 0) {
				myObjects.add(new Triangle(vertices, norm, texco, wallColor, wallKa, wallKd, wallKs, wallExp, img));
				
				vertices = new ArrayList<MyVector>();
				texco = new ArrayList<MyVector>();
			}
			
		}
		
	}
	
}

