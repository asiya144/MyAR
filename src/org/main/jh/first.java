package org.main.jh;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.jogamp.opengl.GL;

import jp.nyatla.nyartoolkit.core.raster.rgb.INyARRgbRaster;
import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.j2se.NyARBufferedImageRaster;
import jp.nyatla.nyartoolkit.jogl2.sketch.GlSketch;
import jp.nyatla.nyartoolkit.jogl2.utils.NyARGlMarkerSystem;
import jp.nyatla.nyartoolkit.jogl2.utils.NyARGlRender;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystemConfig;
import jp.nyatla.nyartoolkit.markersystem.NyARSensor;


public class first extends GlSketch {

	private Webcam camera;
	private NyARGlMarkerSystem markerSystem;
	private NyARGlRender render;
	private NyARSensor sensor;
	
	
	private final static String ARCODE_FILE = "Data/neco2.pat";
	private int id;
	
	NyARBufferedImageRaster img;

	
	@Override
	public void setup(GL i_gl) throws Exception {
		// TODO Auto-generated method stub
		this.size(640,480);
		NyARMarkerSystemConfig config = new NyARMarkerSystemConfig(640,480);
		this.camera = Webcam.getDefault();
		this.camera.setViewSize(new  Dimension(640,480));
		this.markerSystem = new NyARGlMarkerSystem(config);
		this.render = new NyARGlRender(this.markerSystem);
		this.sensor = new NyARSensor(config.getScreenSize());
		this.id = this.markerSystem.addARMarker(ARCODE_FILE, 32, 50, 32);
		i_gl.glEnable(GL.GL_DEPTH_TEST);
		img = new  NyARBufferedImageRaster(ImageIO.read(new File("Data/neco2.jpg")));
		this.camera.open();
	}

	@Override
	public void draw(GL i_gl) throws Exception {
		// TODO Auto-generated method stub
		synchronized(this.camera) {
			try {
				this.sensor.update(new NyARBufferedImageRaster(this.camera.getImage()));
				this.render.drawBackground(i_gl,  this.sensor.getSourceImage());
				this.render.loadARProjectionMatrix(i_gl);
				
				this.markerSystem.update(this.sensor);
				if(this.markerSystem.isExist(this.id)){
					this.markerSystem.loadTransformMatrix(i_gl, this.id);
					this.render.drawImage2d(i_gl, 0, 0,img);
					
					//this.render.colorCube(i_gl, 40, 0, 0, 20);
				}
				//this.render.drawBackground(i_gl, img);
				
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
