package Classes;

/**
 * Created by mahmut on 13.04.2017.
 */
public class Variables {
    public static String realQuerry
            = "SELECT f.name, um.score , f.content, f.idFile " +
            "FROM files f, User_Melody um, User u " +
            "WHERE f.idfile = um.idMelody and u.idUser=um.idUser and u.trustable=1 and um.score = 5";
    public static String optimumQuerry = "SELECT f.name, avg(um.score) , f.content, f.idFile FROM files f, User_Melody um " +
            "WHERE f.idfile = um.idMelody group by f.idFile order by rand()";
    public static String fakeQuerry = "SELECT f.name, (FLOOR( 1 + RAND( ) *5 )) as score , f.content , f.idFile" +
            "FROM files f";
    public static String selected = optimumQuerry;

    public static int inputLenght = 50; //it will be duplicated by 2. 1 for pitch - one for duration
    public static int geneticPoolLenght = 1000;
    public static int geneticIterationCount = 100;
    public static int testLenght = 100;
    public static double crossOverProb = 0.8;
    public static double mutationProb = 0.3;
}
