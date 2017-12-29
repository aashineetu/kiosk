package com.kiosk.kiosk.printutil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class PrintUtil {
	
	public static  BufferedImage readImageWithColorSpacePreservation(byte[] imageData) {
		  try (ImageInputStream inputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData))) {
		      // Get the reader
		      Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);

		      if (!readers.hasNext()) {
		          throw new IllegalArgumentException("No reader found");
		      }

		      ImageReader reader = readers.next();
		      try {
		          reader.setInput(inputStream);
		          Iterator<ImageTypeSpecifier> types = reader.getImageTypes(0);

		          // determine if CMYK decoding is possible
		          ImageTypeSpecifier cmykType = null;
		          while (types.hasNext()) {
		              ImageTypeSpecifier type = types.next();

		              int csType = type.getColorModel().getColorSpace().getType();
		              if (csType == ColorSpace.TYPE_CMYK) {
		                  cmykType = type;
		                  break;
		              }
		          }

		          // set reader to preserve CMYK color space if found
		          ImageReadParam param = reader.getDefaultReadParam();
		          if (cmykType != null) {
		              param.setDestinationType(cmykType);
		          }

		          // Finally read the image, using settings from param
		          return reader.read(0, param);
		      }
		      finally {
		          reader.dispose();
		      }
		  } catch (IOException e) {
		      e.printStackTrace();
		  }
		  return null;
		}

}
