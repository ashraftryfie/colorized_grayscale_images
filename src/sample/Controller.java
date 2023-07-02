package sample;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {

    final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
    public BorderPane my;

    @FXML
    ImageView myImageView;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSizeField;

    @FXML
    private CheckBox eraser;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button backStep;

    @FXML
    private Button forwardStep;

    Image myImage1 = new Image(String.valueOf(getClass().getResource("img1.jpg")));
    BoolImage hello = new BoolImage(String.valueOf(getClass().getResource("img1.jpg")));

    double brushSize;

    public void initialize() {

        // Check if there is image
        if (myImage1 == null) openImage();

        myImageView.setImage(myImage1);

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                double dx = zoomProperty.get() * 4;
                double dy = zoomProperty.get() * 3;

                if (dx < 200) dx = 200;
                if (dx > 3200) dx = 3200;


                myImageView.setFitWidth(dx);


                myImageView.setFitHeight(dy);

                myImageView.setOnMouseDragged(e -> {
                    brushSize = Double.parseDouble(brushSizeField.getText());
                    double x = (e.getX() / myImageView.getFitWidth()) * myImageView.getImage().getWidth();
                    double y = (e.getY() / ((myImageView.getFitWidth() * myImageView.getImage().getHeight()) / myImageView.getImage().getWidth())) * myImageView.getImage().getHeight();
                    System.out.println("FROM DRAG : " + e.getX() + ":" + e.getY());

                    if (brushSize > 30) brushSize = 30;
                    if (brushSize < 1) brushSize = 1;

                    for (int i = -(int) (brushSize / 2); i <= (int) (brushSize / 2); i++) {

                        int xx = (int) x + i;
                        int yy = (int) y + i;


                        if (xx > hello.width - 0.01) {
                            xx = (int) (hello.width - 0.01);
                        } else if (xx < 0) {
                            xx = 0;
                        }

                        if (yy > hello.height - 0.01) {
                            yy = (int) (hello.height - 0.01);
                        } else if (yy < 0) {
                            yy = 0;
                        }
                        hello.dragedPixels[hello.dragedPixelsCounter] = new Position(xx, yy);
                        hello.dragedPixelsCounter++;

                    }

                });


                myImageView.setOnMouseReleased(e -> {
                    brushSize = Double.parseDouble(brushSizeField.getText());
                    double x = (e.getX() / myImageView.getFitWidth()) * myImageView.getImage().getWidth();
                    double y = (e.getY() / ((myImageView.getFitWidth() * myImageView.getImage().getHeight()) / myImageView.getImage().getWidth())) * myImageView.getImage().getHeight();
                    System.out.println("FROM DRAG Released: " + e.getX() + ":" + e.getY());


                    hello.dragedPixels[hello.dragedPixelsCounter] = new Position((int) x, (int) y);
                    hello.dragedPixelsCounter++;


                    myImageView.setImage(hello.coloring(colorPicker.getValue()));
                    System.out.println("size: " + brushSize);
                });


            }
        });

        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });

    }

    public void doBackStep() {
        System.out.println("BACK");
        if (hello.currentStep > 0) {
            hello.allowLastStep = true;
            System.out.println("BACKffffff");
            // hello.steps[hello.currentStep].color=Color.WHEAT;
            myImageView.setImage(hello.drawBackPixels(hello.currentStep));
            hello.currentStep--;
        }
    }

    public void doForwardStep() {
        System.out.println("FORWARD");

        if (hello.currentStep < hello.lastStep) {
            System.out.println("FORWARDffffff");
            // hello.steps[hello.currentStep].color=Color.WHEAT;
            hello.currentStep++;
            myImageView.setImage(hello.drawFofwardPixels(hello.currentStep));
        }
    }


    public void save(ActionEvent actionEvent) throws IOException {
        Image imageToSave = myImageView.getImage();
        java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageToSave, null);

        FileChooser imageSaver = new FileChooser();
        imageSaver.setTitle("Save Colored Image File");
        imageSaver.setInitialDirectory(new File("C:\\"));
        imageSaver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif")
        );

        File outputFile = imageSaver.showSaveDialog(null);

        if (outputFile != null) {
            String name = outputFile.getName();
            String extension = name.substring(1 + name.lastIndexOf(".")).toLowerCase();

            System.out.println("Saved Image: " + name);

            ImageIO.write(bufferedImage, extension, outputFile);
        }

    }

    public void openImage() {
        FileChooser imgOpener = new FileChooser();
        imgOpener.setTitle("Open Grayscale Image File");
        imgOpener.setInitialDirectory(new File("C:\\"));
        File openedImg = imgOpener.showOpenDialog(null);

        if (openedImg != null) {
            String imageName = openedImg.getAbsolutePath();
            // System.out.println(imageName);

            myImage1 = new Image(imageName);
            hello = new BoolImage(imageName);

            myImageView.setImage(myImage1);
        }
    }

}
