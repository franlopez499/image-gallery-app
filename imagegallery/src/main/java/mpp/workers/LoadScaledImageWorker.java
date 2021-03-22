package mpp.workers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import mpp.imagenes.ImageManager;
import mpp.imagenes.db.ImageDB;

public class LoadScaledImageWorker extends SwingWorker<Image, Void> {
	
	private ImageManager view;
	private ImageDB db;
	private File f;
	private int width;
	private int height;
	public LoadScaledImageWorker(ImageManager view, ImageDB db, File f, int width, int height) {
		 this.view = view;
		 this.db = db;
		 this.f = f;
		 this.width = width;
		 this.height = height;
	}
	@Override
	protected Image doInBackground() throws Exception {
		Image image = loadScaled(f, width, height);
		if (image == null) {
			// reportar
			return null;
		}		
		
		return image;
		
	}
	@Override
	protected void done() {
		try {
			Image resultado = get();
			if(resultado != null) {
				db.addImage(f);
				view.addThumbnail(resultado, f);
				view.setInfo("Imagen "+f+" cargada\n", 100);
			}
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
		}
	}
	protected Image loadScaled(@Nonnull File f, @Nonnegative int w, @Nonnegative int h) throws IOException {
		System.out.println("Processing: " + f);
		BufferedImage img = ImageIO.read(f);
		if (img == null)
			return null;
		return img.getScaledInstance(w, h, Image.SCALE_FAST);
	}
}