package com.attendance.fullstackbackend.controller;

import com.attendance.fullstackbackend.dto.FingerprintDataDTO;
import com.attendance.fullstackbackend.repository.FingerprintDataRepository;
import com.attendance.fullstackbackend.model.FingerprintData;
import com.attendance.fullstackbackend.repository.UserRepository;
import com.attendance.fullstackbackend.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

import java.io.*;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import SecuGen.FDxSDKPro.jni.*;
import SecuGen.FDxSDKPro.jni.JSGFPLib;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
public class SecugenController {

    private final FingerprintDataRepository fingerprintDataRepository;
    private final UserRepository userRepository;


    @Autowired
    public SecugenController(FingerprintDataRepository fingerprintDataRepository, UserRepository userRepository) {
        this.fingerprintDataRepository = fingerprintDataRepository;
        this.userRepository = userRepository;
    }

    public static long check ;
    public static long err ;
    byte[] imageBuffer1;
    byte[] imageBuffer2;
    byte[] SG400minutiaeBuffer1;
    byte[] ANSIminutiaeBuffer1;
    byte[] ISOminutiaeBuffer1;
    byte[] SG400minutiaeBuffer2;
    byte[] ANSIminutiaeBuffer2;
    byte[] ISOminutiaeBuffer2;
    FileOutputStream fout = null;
    PrintStream fp = null;
    String base64Image=null;
    String base64File=null;



    @Configuration
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/fingerprint/{userId}")
                    .allowedOrigins("http://localhost:3000") // Allow requests from frontend origin
                    .allowedMethods("Post") // Allow only PUT method
                    .allowedHeaders("*"); // Allow all headers

            registry.addMapping("/fingerprint/biometric")
                    .allowedOrigins("http://localhost:3000") // Allow requests from frontend origin
                    .allowedMethods("Get") // Allow only GET method
                    .allowedHeaders("*"); // Allow all headers
        }
    };



    @GetMapping("/fingerprint/biometric")
    public List<FingerprintDataDTO> getAllFingerprintData() {
        List<FingerprintData> fingerprintDataList = fingerprintDataRepository.findAll();
        return fingerprintDataList.stream()
                .map(this::convertFingerprintData)
                .collect(Collectors.toList());
    }

    private FingerprintDataDTO convertFingerprintData(FingerprintData fingerprintData) {
        FingerprintDataDTO dto = new FingerprintDataDTO();
        BeanUtils.copyProperties(fingerprintData, dto);

        // Set user ID if available
        if (fingerprintData.getUser() != null) {
            dto.setUserId(fingerprintData.getUser().getId());
        }

        return dto;
    }



    @CrossOrigin(origins="*", allowedHeaders="*")
    @PostMapping("/fingerprint/{userId}")
    public FingerprintData fingerprint(@PathVariable UUID userId)
    {
        JSGFPLib sgfplib = new JSGFPLib();
        if((sgfplib !=null) &&(sgfplib.jniLoadStatus!= SGFDxErrorCode.SGFDX_ERROR_JNI_DLLLOAD_FAILED))
        {
            System.out.println(sgfplib);
        }
        else{
            return new FingerprintData(false,"Device not found", "file not created".getBytes());
        }
        /**
         * INITIALIZING SECUGEN
         */
        err= sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);

        /**
         * GETING MINEX VERSION
         */
        int[] extractorVersion = new int[1];
        int[] matcherVersion = new int[1];
        /**
         * CALL MINEX VERSION GetMinexVersion()
         */
        err = sgfplib.GetMinexVersion(extractorVersion, matcherVersion);
        /**
         * EXTRACTOR VERSION , extractorVersion[0]
         * MATCHER VERSION ,   matcherVersion[0]
         */


        /**
         * OPENING SECUGEN FINGER PRINT DEVICE , OpenDevice(number PORT)
         *  AUTO DETECT PORT , OpenDevice(SGPPPortAddr.AUTO_DETECT)
         */
        //    err = sgfplib.OpenDevice(2);
        err = sgfplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
        //    System.out.println("sgpp port address sglip :" + err);

        /**
         * GET DEVICE INFO
         */
        SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
        err = sgfplib.GetDeviceInfo(deviceInfo);
        /**
         * System.out.println(err)
         * SERIAL NUMBER , new String(deviceInfo.deviceSN())
         * BRIGHTNESS , deviceInfo.brightness
         * PORT , deviceInfo.comPort
         * SPEED , deviceInfo.comSpeed
         * CONTRAST , deviceInfo.contrast
         * DEVICEID , deviceInfo.deviceID
         * FWVERSION , deviceInfo.FWVersion
         * GAIN , deviceInfo.gain
         * IMAGE DPI , deviceInfo.imageDPI
         * IMAGE HEIGHT , deviceInfo.imageHeight
         * IMAGE WIDTH , deviceInfo.imageWidth
         */

        /**
         * TURNING LED ON/OFF
         * TURN ON : sgfplib.SetLedOn(true)
         * TURN OFF : sgfplib.SetLedOn(false)
         * err = sgfplib.SetLedOn(true);
         */

        int[] quality = new int[1];
        int[] maxSize = new int[1];
        int[] size = new int[1];
        SGFingerInfo fingerInfo = new SGFingerInfo();
        fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
        fingerInfo.ImageQuality = quality[0];
        fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
        fingerInfo.ViewNumber = 1;

        /**
         * CAPTURING FINGER PRINT IMAGE
         */
        err =sgfplib.SetLedOn(true);
        imageBuffer1 = new byte[deviceInfo.imageHeight*deviceInfo.imageWidth];
        try{
            err = sgfplib.GetImage(imageBuffer1);

            /**
             * SGFDX_ERROR_NONE DENOTES SUCCESS IN CAPTURING FINGER PRINT
             */
            if (err == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                /**
                 * GETTING IMAGE QUALITY
                 */
                err = sgfplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
                /**
                 * System.out.println("GetImageQuality returned : [" + err + "]");
                 * System.out.println("Image Quality is : [" + quality[0] + "]");
                 */



                byte[][] buffer2D = new byte[deviceInfo.imageHeight][deviceInfo.imageWidth];
                for(int i=0;i<deviceInfo.imageHeight;i++) {
                    for(int j=0;j<deviceInfo.imageWidth;j++) {
                        buffer2D[i][j]=imageBuffer1[i*deviceInfo.imageWidth+j];
                    }
                }
                /**
                 * CREATE PNG IMAGE
                 * START
                 */
                // String imageName = "fiNGERPRINGT.png";
                // writeImage(buffer2D,imageName);
                /**
                 * ENDS
                 * CREATE PNG IMAGE
                 */

                /**
                 * CREATE BASE64 OF IMAGE
                 * START
                 */
                BufferedImage image = new BufferedImage(buffer2D.length, buffer2D[0].length, BufferedImage.TYPE_BYTE_GRAY);
                for (int x = 0; x < buffer2D.length; x++) {
                    for (int y = 0; y <buffer2D[0].length; y++) {
                        image.setRGB(x, y, buffer2D[x][y]);
                    }
                }

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try{
                    ImageIO.write(image, "png", bos);
                    byte[] imageBytes = bos.toByteArray();

                    base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    bos.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }

                /**
                 * ENDS
                 * CREATE BASE64 IMAGE
                 */

            }
            else
            {
                /**
                 * FAILED TO CAPTURE FINGER PRINT
                 */
                return new FingerprintData(false,"Error capturing fingerprint , try again", "file not created".getBytes());
            }
        }
        catch(Exception e)
        {
            /**
             * TURNING OFF LED
             */
            err =sgfplib.SetLedOn(false);

            /**
             * EXCEPTION OCCURED DURING CAPTURE
             */
            return new FingerprintData(false,"Error capturing fingerprint , try again", "file not created".getBytes());
        }

        /**
         * TURNING OFF LED
         */
        err =sgfplib.SetLedOn(false);


        /**
         * SET TEMPLATE FORMAT ISO19794
         */
        err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
        /**
         *  System.out.println("SetTemplateFormat returned : [" + err + "]");
         */

        /**
         * GET MAX TEMPLATE SIZE FOR ISO19794
         */
        err = sgfplib.GetMaxTemplateSize(maxSize);

        /**
         * MAX TEMPLATE SIZE = System.out.println("Max ISO19794 Template Size is : [" + maxSize[0] + "]")
         */

        /**
         * CREATE ISO19794 TEMPLATE FOR FINGER
         * Call CreateTemplate()
         * CALL GetTemplateSize() TO GET THE SIZE OF CREATED TEMPLATE
         * TEMPLATE SIZE = System.out.println("ISO19794 Template Size is : [" + size[0] + "]");
         */




        ISOminutiaeBuffer1 = new byte[maxSize[0]];
        err = sgfplib.CreateTemplate(fingerInfo, imageBuffer1, ISOminutiaeBuffer1);
        err = sgfplib.GetTemplateSize(ISOminutiaeBuffer1, size);
        try
        {
            if (err == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                /**
                 * SAVE FINGER PRINT TEMPLATE IN DISK
                 */
                fout = new FileOutputStream("fingerPrintTemplate.iso19794");
                fp = new PrintStream(fout);
                fp.write(ISOminutiaeBuffer1,0, size[0]);
                fp.close();
                fout.close();
                fp = null;
                fout = null;

                /**
                 * LOAD FINGER PRINT TEMPLATE TO ENCODE TO BASE64
                 *

                 */
                File file = new File("fingerPrintTemplate.iso19794");
                byte[] loadedFile = loadFile(file);
                byte[] encodedFile = Base64.getEncoder().encode(loadedFile);
                base64File = new String(encodedFile);

                try {
                    // Find user by ID from UserRepository
                    User user = userRepository.findById(userId).orElse(null);
                    if (user == null) {
                        return new FingerprintData(false, "User not found", "file not created".getBytes());
                    }
                    // Check if fingerprint data already exists for this user
                    if (fingerprintDataRepository.existsById(userId)) {
                        return new FingerprintData(false, "Biometric data already captured for this user", "file not created".getBytes());
                    }

                    // Initialize SGFPLib and other necessary variables here

                    // Capture fingerprint image and process it

                    // Save fingerprint data to the database
                    FingerprintData fingerprintData = new FingerprintData();
                    fingerprintData.setBiometricData(Base64.getDecoder().decode(base64File));
                    fingerprintData.setUser(user);
                    fingerprintData.setSuccess(true);
                    fingerprintData.setMessage("Initial capture" );
                    fingerprintData.setDateRegistered(Instant.ofEpochSecond(System.currentTimeMillis()));
                    fingerprintDataRepository.save(fingerprintData);

                    // Return success response
                    return new FingerprintData(true, "Biometric data captured successfully", "file created".getBytes());
                } catch (Exception e) {
                    // Handle any exceptions
                    return new FingerprintData(false, "Error capturing biometrics: " + e.getMessage(), "file not created".getBytes());
                }

//                try {
//                    // Find user by ID from UserRepository
//                    User user = userRepository.findById(userId).orElse(null);
//                    if (user == null){
//                        return new FingerprintData(false, "User not found", "file not created".getBytes());
//                    }
//
//                    // Initialize SGFPLib and other necessary variables here
//
//                    // Capture fingerprint image and process it
//
//                    // Save fingerprint data to the database
//                    FingerprintData fingerprintData = new FingerprintData();
//                    fingerprintData.setBiometricData(Base64.getDecoder().decode(base64File));
//                    fingerprintData.setUser(user);
//                    fingerprintData.setSuccess(true);
//                    fingerprintData.setMessage("Initial capture");
//                    fingerprintData.setDateRegistered(Instant.ofEpochSecond(System.currentTimeMillis()));
//                    fingerprintDataRepository.save(fingerprintData);
//
//                    // Return your response
//                    return new FingerprintData(true, base64Image, base64File.getBytes());
//                } catch (Exception e) {
//                    // Handle exceptions
//                    e.printStackTrace();
//                    return new FingerprintData(false, "Error capturing fingerprint", "file not created".getBytes());
//                }













            }
        }
        catch (IOException e)
        {
            /**
             * EXCEPTION
             */
            return new FingerprintData(false,"Error capturing fingerprint , try again", "file not created".getBytes());
        }


        /**
         * SET LED OFF BEFORE CLOSING DEVICE
         */
        err =sgfplib.SetLedOn(false);
        /**
         * CLOSING FINGER PRINT DEVICE
         */
        err = sgfplib.CloseDevice();

        /**
         * DELETE DISK'S TEMPLATE FILE
         */
        //TO DO HERE
        return new FingerprintData(true,base64Image, base64File.getBytes());






    }





    /**
     *
     * WRITING IMAGE TO THE DISK
     */

    public  void writeImage(byte[][] img , String imageName) {
        String path = imageName;
        BufferedImage image = new BufferedImage(img.length, img[0].length, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < img.length; x++) {
            for (int y = 0; y <img[0].length; y++) {
                image.setRGB(x, y, img[x][y]);
            }
        }

        File ImageFile = new File(path);
        try {
            ImageIO.write(image, "png", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * LOADING FILE TO CONVERT TO BASE64
     */

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }



}
