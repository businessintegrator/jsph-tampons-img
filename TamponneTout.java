/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.businessintegrator.tamponneur.batch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author o
 */
public class TamponneTout {

    private static void usage() {
        System.out.println("TamponneTout <repertoire origine> <repertoire destination> <fichier tampon> <numero de la premiere piece> <extension>");
        System.exit(0);

    }

    public TamponneTout() {
    }

    private BufferedImage batchFusion(Image piece, Image tampon, String numeroPiece) {
        sun.awt.image.ToolkitImage tlk = (sun.awt.image.ToolkitImage) piece;
        sun.awt.image.ToolkitImage tlk2 = (sun.awt.image.ToolkitImage) tampon;
        BufferedImage bimage1 = tlk.getBufferedImage();
        BufferedImage bimage2 = tlk2.getBufferedImage();
        //Je vais dessinner le numéro de la pièce upper lef corner
        BufferedImage image3 = addImage(bimage1, bimage2,numeroPiece);
        return image3;
    }

    private BufferedImage addImage(BufferedImage piece, BufferedImage tampon,String numeroPiece) {
        Graphics2D g2d = piece.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.drawImage(tampon, 0, 0, null);
        int width = tampon.getWidth();
        int height = tampon.getHeight();
        g2d.drawImage(tampon, 0, 0, null);
        g2d.setFont(new Font("Times", Font.BOLD, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString(numeroPiece, Math.round(width / 2), Math.round(3 * height / 4));
        g2d.dispose();
        return piece;
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            usage();
        }
        try {
            String origine = args[0];
            String destination = args[1];
            String tampon = args[2];
            String startNumero = args[3];
            String extension = args[4];
            TamponneTout tout = new TamponneTout();
            File originFile = new File(origine);
            File destinationFile = new File(destination);
            if (!originFile.exists()) {
                originFile.mkdirs();
            }
            if (!destinationFile.exists()) {
                destinationFile.mkdirs();
            }
            if (originFile.isDirectory()) {
            } else {
                usage();
            }
            if (destinationFile.isDirectory()) {
            } else {
                usage();
            }
            tout.tamponne(originFile, destinationFile, new File(tampon), Integer.parseInt(startNumero), extension);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void tamponne(File origine, File destination, File tampon, int start, String extension) throws MalformedURLException, IOException {
        
        
        File[] files = origine.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return  pathname.isFile()&& (pathname.getName().endsWith("png")||pathname.getName().endsWith("gif")||pathname.getName().endsWith("jpeg")||pathname.getName().endsWith("jpg"));
            }
        });
        ImageIcon tamponImage = new javax.swing.ImageIcon(tampon.toURI().toURL());
        System.out.println(" Nb Files : "+files.length);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.println(" "+file.getPath());
            ImageIcon pieceImage = new javax.swing.ImageIcon(file.toURI().toURL());
            String destinationOne = destination.getPath() + "/" + file.getName();
            File fDestination = new File(destinationOne);
            int sec = 0;
            while (fDestination.exists()) {
                destinationOne = destination.getPath() + "/" + start + "-" + sec + "-" + file.getName();
                sec++;
                fDestination = new File(destinationOne);
            }
            BufferedImage result = batchFusion(pieceImage.getImage(), tamponImage.getImage(), "" + start);
            ImageIO.write(result, "png", fDestination);
            System.out.println("Tamponnee " + fDestination.getPath());
            start++;
        }

    }
}
