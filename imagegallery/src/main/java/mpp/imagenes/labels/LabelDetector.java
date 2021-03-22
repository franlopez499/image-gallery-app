package mpp.imagenes.labels;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface LabelDetector {

	@Nonnull
	public List<Label> label(@Nonnull File file) throws IOException;
	
	public static class Label {
		private String value;
		private double confidence;
		
		public Label(@Nonnull String value, @Nonnegative double confidence) {
			this.value = value;
			this.confidence = confidence;
		}
		
		public String getValue() {
			return value;
		}
		
		public double getConfidence() {
			return confidence;
		}
	}
}
