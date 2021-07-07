package com.aviza.docscanner.helpers;

import android.graphics.Bitmap;

public class ScannerConstants {
    public static Bitmap selectedImageBitmap;
    public static String cropText = "TRIM", backText = "CLOSE",
            imageError = "No image selected, please try again.",
            cropError = "You have not selected a valid field. Please correct until the lines turn blue.";
    public static String cropColor = "#6666ff", backColor = "#ff0000", progressColor = "#331199";
    public static boolean saveStorage = false;
}
