package mpp.workers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import mpp.imagenes.ImageManager;
import mpp.imagenes.db.ImageDB;

public class LoadImagesBatchWorker extends SwingWorker<Integer, Integer> {

	private ImageManager view;
	private ImageDB db;
	private int width;
	private int height;
	private List<? extends File> files;
	public LoadImagesBatchWorker(ImageManager view, ImageDB db, List<? extends File> files, int width, int height) {
		this.view = view;
		 this.db = db;
		 this.files = files;
		 this.width = width;
		 this.height = height;
	}
	@Override
	protected Integer doInBackground() throws Exception {
		int size = files.size();
		int numFilesProcessed = 0;
		for (File file : files) {
			if(isCancelled()) {
				view.setInfo("Carga de imágenes cancelada", 100);
				return null;
			}
			Image img = loadScaled(file, width, height);
			// reportar!
			if (img == null)
				continue;
			db.addImage(file);
			++numFilesProcessed;
			double num = (double)numFilesProcessed / (double)size * 100.0f;
			publish((int)num);
			
			view.addThumbnail(img, file);
			
		}
		return 1;
		
	}
	@Override
	protected void process(List<Integer> chunks) {
		view.setInfo("", chunks.get(chunks.size()-1));
		
    }
	@Override
	protected void done() {
		try {
			Integer resultado = get();
			if(resultado != null) {
				view.setInfo("Carga de imágenes completada", 100);
				
			}
		} catch (InterruptedException | ExecutionException e) {
			return;
			//e.printStackTrace();
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
