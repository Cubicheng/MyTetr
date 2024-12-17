package com.Cubicheng.MyTetr.gameWorld;

import com.Cubicheng.MyTetr.ConfigData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigVars {
    public static double ghost_piece_visibility = 0.5;
    public static long DAS = 167;
    public static long ARR = 33;
    public static long SFD_ARR = 33;

    public static void update_config_from_json(){

        ObjectMapper objectMapper = new ObjectMapper();
        ConfigData configData;
        try {
            configData = objectMapper.readValue(new File(Vars.config_file_path), ConfigData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ghost_piece_visibility = configData.getGhost_piece_visibility();
        DAS = configData.getDas();
        ARR = configData.getArr();
        SFD_ARR = configData.getSfd_ARR();
    }
}
