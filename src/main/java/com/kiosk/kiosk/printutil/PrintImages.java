package com.kiosk.kiosk.printutil;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Collection;
     
 
@SuppressWarnings("rawtypes")
     
public class PrintImages implements Printable 
{
	private static float  gcWidht = 2.2047243f;
	private static float gcHeight = 3.444882f; 

	
    protected ArrayList imageList = new ArrayList<Object>();
    
    
 
    public PrintImages() 
    {
 
    }
         
    @SuppressWarnings("unchecked")
    public void print(Collection images, String printerName, String requestNumber) 
    {
        this.imageList.clear();
        this.imageList.addAll(images);
        printData(printerName, requestNumber);
    }
 
    protected void printData(String printerName, String requestNumber) 
    {
        String printer = printerName;           
             
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService service = null;
 
             
        for (int i = 0; i < printService.length; i++)
        {
            String p_name = printService[i].getName();
            if (p_name.equals(printer))
            {
                service = printService[i];
            }
        }           
             
 
        try
        {
            DocPrintJob pj = service.createPrintJob();
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new JobName(requestNumber, null));
            //aset.add(MediaSizeName.NA_LETTER);
            aset.add(Sides.DUPLEX);
    			aset.add(OrientationRequested.LANDSCAPE);
            aset.add(setMediaSizeNameForCard(service));
            
            DocAttributeSet docAttr = new HashDocAttributeSet();
            docAttr.add(Sides.DUPLEX);
            
            Doc doc = new SimpleDoc(this, flavor, null);
            pj.print(doc, aset);
        } 
        catch (PrintException pe) 
        {
            System.err.println(pe);
        }
    }
 
    public int print(Graphics g, PageFormat f, int pageIndex) 
    {
        if (pageIndex >= imageList.size()) 
        {
            return Printable.NO_SUCH_PAGE;
        }
        RenderedImage image = (RenderedImage) imageList.get(pageIndex);
 
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(f.getImageableX(), f.getImageableY());
 
            int imgWidth = (int)image.getWidth();
            int imgHeight = (int)image.getHeight();
            double xRatio = (double) f.getImageableWidth() / (double) imgWidth;
            double yRatio = (double) f.getImageableHeight() / (double) imgHeight;
 
            g2.scale(xRatio, yRatio);
 
            AffineTransform at = AffineTransform.getTranslateInstance(f.getImageableX(), f.getImageableY());
            g2.drawRenderedImage(image, at);
                 
            return Printable.PAGE_EXISTS;
            } 
        else
        {
            return Printable.NO_SUCH_PAGE;
        }
    }
    
	private MediaSizeName setMediaSizeNameForCard(PrintService service)
	{
		MediaSizeName msn = null;
	
		Media[] res = (Media[]) service.getSupportedAttributeValues((Class<? extends Attribute>) Media.class, null, null);
		for (Media media : res) {
		    if (media instanceof MediaSizeName) {
		        msn = (MediaSizeName) media;
		        MediaSize ms = MediaSize.getMediaSizeForName(msn);
		        float width = ms.getX(MediaSize.INCH);
		        float height = ms.getY(MediaSize.INCH);
		        System.out.println(media + ": width = " + width + "; height = " + height);
		        
		        if ( width == gcWidht && height == gcHeight )
		        {
		        		System.out.println("Found Media Name for GC: " + msn);

		        }

		    }
		}
		return msn;
	}
}
