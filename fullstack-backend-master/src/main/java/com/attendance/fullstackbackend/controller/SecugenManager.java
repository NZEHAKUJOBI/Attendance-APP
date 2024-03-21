package com.attendance.fullstackbackend.controller;
import SecuGen.FDxSDKPro.jni.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static SecuGen.FDxSDKPro.jni.SGPPPortAddr.USB_AUTO_DETECT;


@Service
public class SecugenManager {
    public static final int QUALITY = 61;
    public static final int AGE = 6;
    private JSGFPLib sgfplib;
    private SGDeviceInfoParam deviceInfo;
    private Long error;
    private long iCount = 0L;
    private int sgFingerPositionNumber = SGFingerPosition.SG_FINGPOS_UK;


    private static Logger logger = LoggerFactory.getLogger(SecugenManager.class);

    /**
     * @param sgFDxDeviceName
     * @return
     */
    public Long boot(long sgFDxDeviceName) {
        if (this.sgfplib != null) {
            this.sgfplib.CloseDevice();
            this.sgfplib.Close();
            this.sgfplib = null;
        }

        this.sgfplib = new JSGFPLib();

        //Init
        error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        //Open Device
        error = this.openDeviceWithUsbAutoDetect();
        //Get Device Information
        deviceInfo = new SGDeviceInfoParam();
        error = sgfplib.GetDeviceInfo(deviceInfo);
        return error;
    }

    public Long boot() {
        return this.boot(SGFDxDeviceName.SG_DEV_AUTO);
    }



    public Long openDeviceWithUsbAutoDetect() {
        return this.sgfplib.OpenDevice(USB_AUTO_DETECT);
    }

    public Long closeDevice() {
        return this.sgfplib.Close();
    }

    public void setLedOn(boolean ledStatus) {
        this.sgfplib.SetLedOn(ledStatus);
    }

    public Boolean matchTemplate(byte[] template1, byte[] template2) {
        boolean[] matched = new boolean[1];
        try {
            long sl = SGFDxSecurityLevel.SL_NORMAL;
            if ((template1.length - template2.length) > 200) {
                return false;
            }
            error = this.sgfplib.MatchTemplate(template1, template2, sl, matched);
            //System.out.println("ERROR RATE: "+error +" " +" MATCHED: " + matched[0]);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return matched[0];
    }

    /**
     * @param template1
     * @param template2
     * @return
     */
    public HashMap<Integer, Boolean> identifyTemplate(byte[] template1, byte[] template2) {
        boolean[] matched = new boolean[1];
        int[] score = new int[1];
        HashMap<Integer, Boolean> matcher = new HashMap<>();
        try {
            long sl = SGFDxSecurityLevel.SL_NORMAL;
            /*if ((template1.length - template2.length) > 200) {
                return false;
            }*/
            error = this.sgfplib.MatchTemplate(template1, template2, sl, matched);
            error = this.sgfplib.GetMatchingScore(template1, template2, score);
            matcher.put(score[0], Boolean.valueOf(String.valueOf(matched)));
            return matcher;
            //System.out.println("ERROR RATE: "+error +" " +" MATCHED: " + matched[0]);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return matcher;
    }
}
