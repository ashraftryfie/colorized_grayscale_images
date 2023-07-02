package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Queue;

public class BoolImage {

    public boolean[][] pixels;
    public Queue<Pixel> myQue = new LinkedList<Pixel>();
    public int width;
    public int height;
    public Image startImage;
    // public PixelReader pixelReader ;//= image.getPixelReader();
    public WritableImage writableImage;// = new WritableImage(w, h);
    public PixelWriter pixelWriter;// = writableImage.getPixelWriter();

    //Draged array of Pixels
    public Position[] dragedPixels;
    public int dragedPixelsCounter;

    //Alfa Work
    public Alpha[] steps;
    public int currentStep;
    public int lastStep;
    public boolean allowLastStep;

    //alip
    double[] currentYuv = {0.0, 0.0, 0.0};
    double[] newYuv = {0.0, 0.0, 0.0};

    public int c = 0;

    public BoolImage(String imgSrc) {
        this.startImage = new Image(imgSrc);

        this.width = (int) startImage.getWidth();
        this.height = (int) startImage.getHeight();
        writableImage = new WritableImage(this.width, this.height);
        PixelReader pixelReader = startImage.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                Color currentColor = pixelReader.getColor(x, y);


                pixelWriter.setColor(x, y, currentColor);
            }
        }


        this.pixels = new boolean[this.width][this.height];
        this.dragedPixels = new Position[60000];
        this.dragedPixelsCounter = 0;

        this.steps = new Alpha[200];
        this.currentStep = 0;
        this.lastStep = 0;
        this.allowLastStep = false;

        this.steps[currentStep] = new Alpha(this.width, this.height, Color.WHEAT);
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.steps[currentStep].pixels[i][j] = true;
            }
        }


    }

    public Image coloring(Color color) {

        this.currentStep++;
        this.lastStep = this.currentStep;
        this.steps[this.currentStep] = new Alpha(this.width, this.height, color);


        this.myQue.clear();
        fillArrayOfPixels(this.currentStep);
        // fillPixelsQueue(xP, yP, color, this.currentStep);
        // System.out.println(c);
        c = 0;
        this.dragedPixels = new Position[60000];
        this.dragedPixelsCounter = 0;
        return drawPixels(this.currentStep);

    }


    public Image drawPixels(int stepIndex) {

        Color color = this.steps[stepIndex].color;
        PixelWriter pixelWriter = this.writableImage.getPixelWriter();
        PixelReader pixelReader = this.writableImage.getPixelReader();
        PixelReader pixelReader_1 = this.startImage.getPixelReader();


        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                Color currentColor = pixelReader.getColor(x, y);
                Color currentColor_1 = pixelReader_1.getColor(x, y);


                Double currentRed = currentColor_1.getRed();
                Double currentGreen = currentColor_1.getGreen();
                Double currentBlur = currentColor_1.getBlue();


                Double newRed = color.getRed();
                Double newGreen = color.getGreen();
                Double newBlur = color.getBlue();

                Double Red = currentRed * newRed;
                Double Green = currentGreen * newGreen;
                Double Blue = currentBlur * newBlur;


                if (this.steps[stepIndex].pixels[x][y]) currentColor = new Color(Red, Green, Blue, 1);

                pixelWriter.setColor(x, y, currentColor);
            }
        }


        return writableImage;
    }


    public Image drawBackPixels(int stepIndex) {


        PixelWriter pixelWriter = writableImage.getPixelWriter();
        PixelReader pixelReader = this.writableImage.getPixelReader();
        PixelReader pixelReader_1 = this.startImage.getPixelReader();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                Color currentColor = pixelReader.getColor(x, y);


                if (this.steps[stepIndex].pixels[x][y]) currentColor = pixelReader_1.getColor(x, y);

                pixelWriter.setColor(x, y, currentColor);
            }
        }


        return writableImage;
    }


    public Image drawFofwardPixels(int stepIndex) {
        // int w = (int) image.getWidth();
        // int h = (int) image.getHeight();
        Color color = this.steps[stepIndex].color;
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        PixelReader pixelReader = this.writableImage.getPixelReader();
        PixelReader pixelReader_1 = this.startImage.getPixelReader();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                Color currentColor = pixelReader.getColor(x, y);
                Color currentColor_1 = pixelReader_1.getColor(x, y);

                // List<Pixel> pixs = new ArrayList<Pixel>();
                // pixs.add(pixel);

                Double currentRed = currentColor_1.getRed();
                Double currentGreen = currentColor_1.getGreen();
                Double currentBlur = currentColor_1.getBlue();


                Double newRed = color.getRed();
                Double newGreen = color.getGreen();
                Double newBlur = color.getBlue();

                Double Red = currentRed * newRed;
                Double Green = currentGreen * newGreen;
                Double Blue = currentBlur * newBlur;


                if (this.steps[stepIndex].pixels[x][y]) currentColor = new Color(Red, Green, Blue, 1);

                pixelWriter.setColor(x, y, currentColor);
            }
        }


        return writableImage;
    }


    public void fillPixelsQueue(int xPP, int yPP, Color color, int stepIndex) {

        this.myQue.add(new Pixel(xPP, yPP));


        // if(c>100000)return;

        // this.pixels[xPP][yPP]=true;

        while (this.myQue.size() > 0) {
            int xP = (int) this.myQue.peek().x;
            int yP = (int) this.myQue.peek().y;
            this.myQue.remove();
            if (this.steps[stepIndex].pixels[xP][yP] == true) {
                continue;
            } else {
                this.steps[stepIndex].pixels[xP][yP] = true;
            }

            for (int i = -1; i < 2; i += 1) {
                int x = xP + i;

                if (x >= 0 && x < this.width)
                    for (int j = -1; j < 2; j += 1) {

                        // int x = xP +i;
                        int y = yP + j;
                        if (y >= 0 && y < this.height) {

                            // if(i==j)continue;
                            if (this.steps[stepIndex].pixels[x][y]) {
                                continue;
                            }


                            PixelReader pixelReader = startImage.getPixelReader();

                            Color currentColor = pixelReader.getColor(xP, yP);
                            Color chaildColor = pixelReader.getColor(x, y);

                            Double currentRed = currentColor.getRed();
                            Double currentGreen = currentColor.getGreen();
                            Double currentBlur = currentColor.getBlue();


                            Double newRed = chaildColor.getRed();
                            Double newGreen = chaildColor.getGreen();
                            Double newBlur = chaildColor.getBlue();


                            double[] currentYuvIm = BoolImage.rgb2yuv(currentRed, currentGreen, currentBlur, this.currentYuv);

                            double[] vewYuvIm = BoolImage.rgb2yuv(newRed, newGreen, newBlur, this.newYuv);

                            double delta = Math.abs(vewYuvIm[0] - currentYuvIm[0]);


                            if (vewYuvIm[0] > 1 - 0.0001) {
                                System.out.print("ffffffff");
                                continue;
                            }
//                        System.out.println("currentYuvIm[0] " + currentYuvIm[0]);
                            if (delta < 0.03) {
                                c++;
                                this.myQue.add(new Pixel(x, y));
                            }


                        }
                    }

            }
        }
    }


    public void fillArrayOfPixels(int stepIndex) {
        PixelReader pixelReader = this.writableImage.getPixelReader();

        for (int i = 0; i < dragedPixelsCounter; i++) {
            Color currentColor = pixelReader.getColor(this.dragedPixels[i].x, this.dragedPixels[i].y);

            if (currentColor.getRed() == currentColor.getBlue() && currentColor.getRed() == currentColor.getGreen()) {
                this.myQue.clear();
                fillPixelsQueue(this.dragedPixels[i].x, this.dragedPixels[i].y, this.steps[stepIndex].color, stepIndex);
            }
        }
    }

    public static double[] rgb2yuv(double r, double g, double b, double[] yuv) {

        yuv[0] = .299f * r + .587f * g + .114f * b;
        yuv[1] = -.14713f * r + -.28886f * g + .436f * b;
        yuv[2] = .615f * r + -.51499f * g + -.10001f * b;


        return yuv;
    }


}