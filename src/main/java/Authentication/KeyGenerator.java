package Authentication;

import RoomConfiguration.ReadRoomConfig;
import RoomConfiguration.RoomDataModel;

import java.security.SecureRandom;

public class KeyGenerator {

    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom secureRandom = new SecureRandom();
    ReadRoomConfig readRoomConfig = new ReadRoomConfig();

    public String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        String key = sb.toString();
        for (RoomDataModel roomDataModel : readRoomConfig.GetRoomData()){
            if (roomDataModel.getSecret().equals(key)){
                randomString(len);
            }
        }
        return key;
    }
}
