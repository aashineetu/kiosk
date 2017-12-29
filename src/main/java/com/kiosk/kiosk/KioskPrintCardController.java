package com.kiosk.kiosk;

import com.kiosk.kiosk.printutil.PrintImages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


@RestController
public class KioskPrintCardController {

    @Value("${printer.name}")
    private String printerName;
    @RequestMapping(method = RequestMethod.POST, value = "/kiosk/printCard",
            consumes = "text/plain", produces = "application/json")

    public ResponseEntity<Boolean> vinCheckerWithRequestParam(
            @RequestParam("operationName") String fileNameToPrint) {
        {
            ArrayList<Object> images = new ArrayList<Object>();
            String pngFiles = fileNameToPrint;

            boolean imagePrinted = true;
            try {

                    //FileInputStream fis = new FileInputStream((pngFiles[j]));
                    //BufferedImage ri = ImageIO.read(fis);
                    BufferedImage ri = ImageIO.read(new File(fileNameToPrint));
                    //BufferedImage ri = JPEGCodec.createJPEGDecoder(inputStream).decodeAsBufferedImage();
                    images.add(ri);
                    //fis.close();
                    ri.flush();
                    ri = null;
                PrintImages printImages = new PrintImages();
                printerName = "EPSON8B673D (WF-2760 Series)";
                //String printerName = "CX-D80 U1";
                String requestNumber = "14";
                printImages.print(images, printerName, requestNumber);
            } catch (Exception e) {
                imagePrinted= false;
                e.printStackTrace();
            }

            ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(Boolean.valueOf(imagePrinted),HttpStatus.OK);
            return responseEntity;
        }
    }
}
