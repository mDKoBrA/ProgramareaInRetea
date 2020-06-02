package PR;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Image {
    private final String IMAGE_DESTINATION_FOLDER;
    private final String url;

    public Image(String image_destination_folder,String url) {
        IMAGE_DESTINATION_FOLDER = image_destination_folder;
        this.url = url;
    }

    public void getImages() {

        //library for html parsing
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10 * 1000)
                    .get();

            //ArrayList of png, jpeg, gif images.
            Elements imageElements = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

            //iterate over each image
            for (Element image : imageElements) {

                //make sure to get the absolute URL using abs: prefix
                String strImageURL = image.attr("abs:src");
                //download image one by one
                downloadImage(strImageURL);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadImage(String strImageURL){

        //get file name from image path
        String strImageName =
                strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );

       // System.out.println("Saving: " + strImageName + ", from: " + strImageURL);

        try {

            //open the stream from URL
            URL urlImage = new URL(strImageURL);
            InputStream in = urlImage.openStream();

            byte[] buffer = new byte[4096];
            int n = -1;

            OutputStream os =
                    new FileOutputStream( IMAGE_DESTINATION_FOLDER + "/" + strImageName );

            //write bytes to the output stream
            while ( (n = in.read(buffer)) != -1 ){
                os.write(buffer, 0, n);
            }

            //close the stream
            os.close();

            //System.out.println("Image saved");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
