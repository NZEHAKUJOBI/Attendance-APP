package com.attendance.fullstackbackend.controller;

import com.attendance.fullstackbackend.dto.FingerprintDataDTO;
import com.attendance.fullstackbackend.model.FingerprintData;
import com.attendance.fullstackbackend.model.VerifyFinger;
import com.attendance.fullstackbackend.repository.FingerprintDataRepository;
import com.attendance.fullstackbackend.repository.StoredTemplate;
import com.attendance.fullstackbackend.repository.UserRepository;
import com.attendance.fullstackbackend.repository.VerifyFingerDataRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.attendance.fullstackbackend.controller.SecugenManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// import java.awt.image.BufferedImage;

import java.io.*;
// import java.util.Scanner;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// import javax.imageio.ImageIO;
import SecuGen.FDxSDKPro.jni.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@RestController
public class VerifyFingerprintController {

    // public static JSGFPLib sgfplib = new JSGFPLib();
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

    private final FingerprintDataRepository fingerprintDataRepository;
    private final UserRepository userRepository;
    private final VerifyFingerDataRepository verifyFingerDataRepository;
    public static String MATCHED_PERSON_UUID;
    private final SecugenManager secugenManager;

    private static String BIOMETRIC_DATA;


    @Autowired
    public VerifyFingerprintController(FingerprintDataRepository fingerprintDataRepository, UserRepository userRepository, SecugenManager secugenManager, VerifyFingerDataRepository verifyFingerDataRepository) {
        this.fingerprintDataRepository = fingerprintDataRepository;
        this.userRepository = userRepository;
        this.secugenManager = secugenManager;
        this.verifyFingerDataRepository = verifyFingerDataRepository;


    }


    @GetMapping("/fingerprint/list")
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



    // @CrossOrigin(origins="http://localhost:3000")
    @CrossOrigin(origins="*")
    @PostMapping("/verify")
    @ResponseBody
    public VerifyFinger verifyprint()
    {
        JSGFPLib sgfplib = new JSGFPLib();
        if((sgfplib !=null) &&(sgfplib.jniLoadStatus!= SGFDxErrorCode.SGFDX_ERROR_JNI_DLLLOAD_FAILED))
        {
            // System.out.println(sgfplib);
        }
        else{
            return new VerifyFinger(false,"Fingerprint device not found");
        }
        err= sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);

        int[] extractorVersion = new int[1];
        int[] matcherVersion = new int[1];
        err = sgfplib.GetMinexVersion(extractorVersion, matcherVersion);
        err = sgfplib.OpenDevice(2);
        SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
        err = sgfplib.GetDeviceInfo(deviceInfo);
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
            }
            else
            {
                /**
                 * TURNING OFF LED
                 */
                err =sgfplib.SetLedOn(false);

                // System.out.println("ERROR: Fingerprint image capture failed for sample1.");
                return new VerifyFinger(false,"Error capturing finger! try again");
            }
        }
        catch(Exception e)
        {
            /**
             * TURNING OFF LED
             */
            err =sgfplib.SetLedOn(false);
            // System.out.println("Exception reading keyboard : " + e);
            return new VerifyFinger(false,"Error capturing finger! try again");

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



        /**
         * CREATE TEMPLATE FROM MINUTAE DATA FROM POST REQUEST AND LOAD IT TO BUFFER
         *
         */
        ISOminutiaeBuffer2 = new byte[maxSize[0]];

        try{

            /**
             * DECODING BASE64 ENCODED MINUTAE DATA
             */
//            byte[] decodeDMinutae = Base64.getDecoder().decode();

            /**
             * LOADING DECODED MINUTAE TO BUFFER
             */
//            ISOminutiaeBuffer2=decodeDMinutae;
        }
        catch(Exception e)
        {
            return new VerifyFinger(false,"Error verifying fingerprint! try again");
        }

        boolean[] matched = new boolean[1];
        int[] score = new int[1];

        /**
         * MATCHING ISO TEMPLATES
         */
        matched[0] = false;
        score[0] = 0;
        err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);

        err = sgfplib.MatchTemplate(ISOminutiaeBuffer1, ISOminutiaeBuffer2, SGFDxSecurityLevel.SL_NORMAL, matched);
        err = sgfplib.GetMatchingScore(ISOminutiaeBuffer1, ISOminutiaeBuffer2,score);

        return new VerifyFinger(matched[0], "Match score : "+score[0]);
    }
    public Boolean getMatch(Set<StoredTemplate> storedTemplates, byte[] scannedTemplate) {
        Boolean matched = Boolean.FALSE;
        MATCHED_PERSON_UUID = null;
        for (StoredTemplate biometric : storedTemplates) {
            if(null != biometric.getUser()) {
                MATCHED_PERSON_UUID = biometric.getUser();
                //LOG.info("person biometric is {}", MATCHED_PERSON_UUID);
            }
            if (biometric.getBiometricData() != null && biometric.getBiometricData().length != 0) {
                matched = secugenManager.matchTemplate(biometric.getBiometricData(), scannedTemplate);
                BIOMETRIC_DATA = BIOMETRIC_DATA;
            } else if (biometric.getBiometricData() != null && biometric.getBiometricData().length != 0) {
                matched = secugenManager.matchTemplate(biometric.getBiometricData(), scannedTemplate);
                BIOMETRIC_DATA = BIOMETRIC_DATA;
            }

            if(matched)break;
        }
        return matched;
    }

    private Boolean setMatch(byte[] capturedFinger, byte[] dbPrint, String personUuid){
        MATCHED_PERSON_UUID = personUuid;
        return secugenManager.matchTemplate(capturedFinger, dbPrint);
    }



    @Configuration
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/verify")
                    .allowedOrigins("http://localhost:3000") // Allow requests from frontend origin
                    .allowedMethods("Post") // Allow only PUT method
                    .allowedHeaders("*"); // Allow all headers

            registry.addMapping("/fingerprint/list")
                    .allowedOrigins("http://localhost:3000") // Allow requests from frontend origin
                    .allowedMethods("Get") // Allow only GET method
                    .allowedHeaders("*"); // Allow all headers
        }
    };


}
