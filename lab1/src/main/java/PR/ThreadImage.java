package PR;

public class ThreadImage extends Thread {
    private Image image;

    ThreadImage(Image image) {
        this.image = image;
    }

    public void run() {
        image.getImages();
    }
}
