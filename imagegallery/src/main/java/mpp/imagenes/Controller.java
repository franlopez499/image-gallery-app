package mpp.imagenes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import mpp.imagenes.db.ImageDB;
import mpp.imagenes.db.LabelledImage;
import mpp.imagenes.labels.LabelDetector;
import mpp.imagenes.labels.LabelDetector.Label;
import mpp.imagenes.labels.MockLabelDetector;
import mpp.workers.LoadImagesBatchWorker;
import mpp.workers.LoadScaledImageWorker;



public class Controller {

	@Nonnull
	private final ImageDB db;
	@Nonnull
	private final ImageManager view;
	@Nonnull
	private final LabelDetector labelDetector = new MockLabelDetector();
	@CheckForNull
	private LabelledImage currentImage;
	private ExecutorService service;
	private LoadImagesBatchWorker loadImagesBatchWorker;
	private ArrayList<Future<?>> futures = new ArrayList<Future<?>>();
	public Controller(@Nonnull ImageManager view) {
		this.view = view;
		this.db = new ImageDB();

	}

	public LabelledImage setCurrentImage(File file) {
		LabelledImage img = db.getImage(file);
		if (img == null)
			return null;
		this.currentImage = img;
		return img;
	}

	public LabelledImage getCurrentImage() {
		synchronized (this.currentImage) {
			return currentImage;
		}
	}

	public void loadScaledImage(@Nonnull File f, @Nonnegative int width, @Nonnegative int height) throws IOException {
		LoadScaledImageWorker loadScaledImageWorker = new LoadScaledImageWorker(this.view, this.db, f, width,height);

		loadScaledImageWorker.execute();

	}

	public void loadScaledImages(@Nonnull List<? extends File> files, @Nonnegative int width, @Nonnegative int height)
			throws IOException {
		loadImagesBatchWorker = new LoadImagesBatchWorker(this.view, this.db, files, width, height);
		loadImagesBatchWorker.execute();

	}

	
	public void transformAll(@Nonnull File folder) {
		
		service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (LabelledImage labelledImage : db.getImages()) {

			Future<?> f = service.submit(() -> {
		        try {
		        	transformFile(folder, labelledImage);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    });
			futures.add(f);
		}
	}
private void transformFile(File folder, LabelledImage labelledImage) {

		File f = labelledImage.getFile();
		try {
			BufferedImage image = ImageIO.read(f);
			float[] blurMatrix = { 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
					1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f };
			BufferedImageOp blurFilter = new ConvolveOp(new Kernel(3, 3, blurMatrix), ConvolveOp.EDGE_NO_OP, null);

			BufferedImage filtered = blurFilter.filter(image, null);

			File outputFileName = Paths.get(folder.getPath(), f.getName()).toFile();
			ImageIO.write(filtered, "jpg", outputFileName);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void stopBackgroundTasks() {
		
		try {
			for(Future<?> f : futures) {
				f.cancel(true);
			}
			service.shutdownNow();


			view.setInfo("Transformación de imágenes cancelada", -1);
			
		} catch (Exception e) {
		
		}
		try {

			loadImagesBatchWorker.cancel(true);

		} catch (Exception e) {

		}

	}

	public void autoLabel() {
		service = Executors.newSingleThreadExecutor();

		for (LabelledImage img : db.getImages()) {

				service.submit(() -> {

					try {

						List<Label> labels = labelDetector.label(img.getFile());
						for (Label label : labels) {
							synchronized (img) {
								img.addLabel(label.getValue());
							}

						}
					} catch (IOException e) {
						
						e.printStackTrace();
					}

			    });

		}
	}



}
