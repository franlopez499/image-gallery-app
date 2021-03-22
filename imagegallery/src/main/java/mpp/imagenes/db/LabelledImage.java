package mpp.imagenes.db;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;

public class LabelledImage {

	@Nonnull
	private final File fileName;
	@Nonnull
	private final Set<String> labels = new LinkedHashSet<>();
	
	public LabelledImage(@Nonnull File fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return this.fileName;
	}

	public void addLabel(@Nonnull String value) {
		labels.add(value);
	}
	
	@Nonnull
	public Set<String> getLabels() {
		return new LinkedHashSet<>(labels);
	}

	@Nonnull
	public String getHumanLabels() {
		return String.join(", ", labels);
	}

	public void setHumanLabels(String text) {
		labels.clear();
		String[] parts = text.split(",");
		for (String string : parts) {
			labels.add(string.trim());
		}
	}
}
