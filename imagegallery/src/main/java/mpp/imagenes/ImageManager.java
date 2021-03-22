package mpp.imagenes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import mpp.imagenes.db.LabelledImage;

public class ImageManager {

	private static final int HEIGHT = 50;
	private static final int WIDTH = 50;
	private JFrame frame;
	private JPanel figureListPanel;
	private JLabel pictureLabel;
	
	private Controller controller = new Controller(this);
	private JLabel lblTask;
	private JProgressBar progressBar;
	private JPanel infoPanel;
	private JTextField imageLabels;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageManager window = new ImageManager();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ImageManager() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnLoad = new JButton("Cargar");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadImage();
			}
		});
		toolBar.add(btnLoad);
		
		JButton btnLoadBatch = new JButton("Cargar directorio");
		btnLoadBatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadImageBatch();
			}
		});
		toolBar.add(btnLoadBatch);

		JButton btnAutoEtiquetado = new JButton("Auto-etiquetado");
		btnAutoEtiquetado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAutoLabelling();
			}
		});
		toolBar.add(btnAutoEtiquetado);

		
		JButton btnAplicarFiltro = new JButton("Aplicar filtro");
		btnAplicarFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTransform();
			}
		});
		toolBar.add(btnAplicarFiltro);
		
		
		JPanel picturePane = new JPanel();
		JScrollPane pictureScrollPane = new JScrollPane(picturePane);
		picturePane.setLayout(new BorderLayout(0, 0));
		pictureLabel = new JLabel();
		picturePane.add(pictureLabel, BorderLayout.CENTER);
		imageLabels = new JTextField();
		imageLabels.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n')
					saveLabels();
			}
		});
		
		picturePane.add(imageLabels, BorderLayout.NORTH);
		
		
		figureListPanel = new JPanel();
		
	
		figureListPanel.setLayout(new GridLayout(0, 1, 0, 0));	
		
		JScrollPane figureScroll = new JScrollPane(figureListPanel);
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				figureScroll, pictureScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		
		
		Dimension minimumSize = new Dimension(100, 50);
		figureListPanel.setMinimumSize(minimumSize);
		
		infoPanel = new JPanel();
		frame.getContentPane().add(infoPanel, BorderLayout.SOUTH);
		infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		lblTask = new JLabel("");
		infoPanel.add(lblTask);
		
		progressBar = new JProgressBar();
		infoPanel.add(progressBar);
		
		JButton btnStopTask = new JButton("Detener");
		btnStopTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopTasks();
			}
		});
		infoPanel.add(btnStopTask);
		picturePane.setMinimumSize(minimumSize);
	}

	protected void saveLabels() {
		LabelledImage currentImage = controller.getCurrentImage();
		if (currentImage != null) {
			currentImage.setHumanLabels(imageLabels.getText());
		}
	}

	protected void doAutoLabelling() {
		controller.autoLabel();
	}

	
	protected void doTransform() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File folder = fc.getSelectedFile();
			controller.transformAll(folder);
		}
	}

	protected void loadImageBatch() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File folder = fc.getSelectedFile();
			try {
				Stream<Path> files = Files.walk(folder.toPath());
				List<File> fileList = files
						.map(Path::toFile)
						.filter(f_ -> f_.isFile())
						.collect(Collectors.toList());
				files.close();
				controller.loadScaledImages(fileList, WIDTH, HEIGHT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	protected void loadImage() {
		final JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			try {
				controller.loadScaledImage(f, WIDTH, HEIGHT);
			} catch (IOException e) {
			}
		}
	}
	
	protected void stopTasks() {
		controller.stopBackgroundTasks();
	}
	
	private void addToPanel(ImageIcon icon, File file) {
	    JLabel picLabel = new JLabel(icon);
	    figureListPanel.add(picLabel);
	    picLabel.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		File loadedFile = file;
	    		
				try {
		    		BufferedImage img = ImageIO.read(loadedFile);
		    		LabelledImage image = controller.setCurrentImage(file);
					if (image == null)
						return;
		    		imageLabels.setText(image.getHumanLabels());
		    		pictureLabel.setIcon(new ImageIcon(img));
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}	    		
	    	}
	    });
	}
	
	public void addThumbnail(Image image, File f) {
		addThumbnail(new ImageIcon(image), f);
	}
	
	public void addThumbnail(ImageIcon image, File f) {
		addToPanel(image, f);
		figureListPanel.revalidate();
	}

	public void setInfo(String taskDescription, int progress) {
		lblTask.setText(taskDescription);
		if (progress >= 0 && progress <= 100)
			progressBar.setValue(progress);
	}
}
