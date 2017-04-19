package Classes;

/**
 * Created by mahmut on 13.04.2017.
 */
public class Variables {
    public static String realQuerry
            = "SELECT f.name, um.score , f.content, f.idFile " +
            "FROM files f, User_Melody um, User u " +
            "WHERE f.idfile = um.idMelody and u.idUser=um.idUser and u.trustable=1";
    public static String optimumQuerry = "SELECT f.name, um.score , f.content, f.idFile " +
            "FROM files f, User_Melody um WHERE f.idfile = um.idMelody";
    public static String fakeQuerry = "SELECT f.name, (FLOOR( 1 + RAND( ) *5 )) as score , f.content , f.idFile" +
            "FROM files f";
    public static String selected = optimumQuerry;
    public static int inputLenght = 35; //it will be duplicated by 2. 1 for pitch - one for duration

}
