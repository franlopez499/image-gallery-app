package mpp.imagenes.labels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockLabelDetector implements LabelDetector {

	private static final String[] DEFAULT_LABELS = {
		"Paris", "New York", "Cuenca", "Galicia", "Roma", "Madrid", "Murcia", "Alcantarilla", "Londres",
		"juego", "niño", "fútbol", "tenis", "cesped", "habitación", "armario", "árbol", "higuera", 
		"conejito", "playa",
		"limón", "naranja", "sandía", "melón", "aguacate", "puerro", "cebolla"
	};
	
	@Override
	public List<Label> label(File file) {
		Random random = new Random();
		List<Label> labels = new ArrayList<Label>();
		
		TRY:
		while (labels.size() < 10) {
			int idx = random.nextInt(DEFAULT_LABELS.length);
			String v = DEFAULT_LABELS[idx];
			
			for (Label label : labels) {
				if (label.getValue().equals(v))
					continue TRY;
			}
			
			labels.add(new Label(v, 100 - labels.size()));
		}
		
		// Simulate this takes time
		try {
			Thread.sleep(random.nextInt(2000) + random.nextInt(500));
		} catch (InterruptedException e) { }
		
		return labels;
	}

}
