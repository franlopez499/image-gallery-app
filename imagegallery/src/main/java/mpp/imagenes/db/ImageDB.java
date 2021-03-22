package mpp.imagenes.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public class ImageDB {

	@Nonnull
	private final List<LabelledImage> images = new ArrayList<>();

	public void addImage(@Nonnull File f) {
		images.add(new LabelledImage(f));
	}
	
	@Nonnull
	public List<? extends LabelledImage> getImages() {
		return images;
	}

	@CheckForNull
	public LabelledImage getImage(@Nonnull File file) {
		for (LabelledImage labelledImage : images) {
			if (labelledImage.getFile().equals(file))
				return labelledImage;
		}
		return null;
	}
	
}
